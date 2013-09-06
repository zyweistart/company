#import "MKStoreManager.h"
#import "SFHFKeychainUtils.h"
#import "MKSKSubscriptionProduct.h"
#import "MKSKProduct.h"

@interface MKStoreManager () //private methods and properties

@property (nonatomic, copy) void (^onTransactionCancelled)();
@property (nonatomic, copy) void (^onTransactionCompleted)(NSString *productId);

@property (nonatomic, copy) void (^onRestoreFailed)(NSError* error);
@property (nonatomic, copy) void (^onRestoreCompleted)();

@property (nonatomic, strong) NSMutableArray *purchasableObjects;
@property (nonatomic, strong) NSMutableDictionary *subscriptionProducts;

@property (nonatomic, strong) MKStoreObserver *storeObserver;
@property (nonatomic, assign, getter=isProductsAvailable) BOOL isProductsAvailable;

- (void) requestProductData;
- (void) startVerifyingSubscriptionReceipts;
-(void) rememberPurchaseOfProduct:(NSString*) productIdentifier;
-(void) addToQueue:(NSString*) productId;
@end

@implementation MKStoreManager

@synthesize purchasableObjects = _purchasableObjects;
@synthesize storeObserver = _storeObserver;
@synthesize subscriptionProducts;

@synthesize isProductsAvailable;

@synthesize onTransactionCancelled;
@synthesize onTransactionCompleted;
@synthesize onRestoreFailed;
@synthesize onRestoreCompleted;

static MKStoreManager* _sharedStoreManager;

- (void)dealloc {
    
    _purchasableObjects = nil;
    _storeObserver = nil;
    onTransactionCancelled = nil;
    onTransactionCompleted = nil;
    onRestoreFailed = nil;
    onRestoreCompleted = nil;    
}

+ (void) dealloc
{
	_sharedStoreManager = nil;
	[super dealloc];
}

+(void) setObject:(id) object forKey:(NSString*) key
{
    NSString *objectString = nil;
    if([object isKindOfClass:[NSData class]])
    {
        objectString = [[NSString alloc] initWithData:object encoding:NSUTF8StringEncoding];
    }
    if([object isKindOfClass:[NSNumber class]])
    {       
        objectString = [(NSNumber*)object stringValue];
    }
    NSError *error = nil;
    [SFHFKeychainUtils storeUsername:key 
                         andPassword:objectString
                      forServiceName:@"MKStoreKit"
                      updateExisting:YES 
                               error:&error];
    
    if(error)
        NSLog(@"%@", [error localizedDescription]);
}

+(id) objectForKey:(NSString*) key
{
    NSError *error = nil;
    NSObject *object = [SFHFKeychainUtils getPasswordForUsername:key 
                                                  andServiceName:@"MKStoreKit" 
                                                           error:&error];
    if(error)
        NSLog(@"%@", [error localizedDescription]);
    
    return object;
}

+(NSNumber*) numberForKey:(NSString*) key
{
    return [NSNumber numberWithInt:[[MKStoreManager objectForKey:key] intValue]];
}

+(NSData*) dataForKey:(NSString*) key
{
    NSString *str = [MKStoreManager objectForKey:key];
    return [str dataUsingEncoding:NSUTF8StringEncoding];
}

#pragma mark Singleton Methods

+ (MKStoreManager*)sharedManager
{
	@synchronized(self) {
		
        if (_sharedStoreManager == nil) {
            
#if TARGET_IPHONE_SIMULATOR
			NSLog(@"You are running in Simulator MKStoreKit runs only on devices");
#else
            _sharedStoreManager = [[self alloc] init];					
			_sharedStoreManager.purchasableObjects = [[NSMutableArray alloc] init];
			[_sharedStoreManager requestProductData];						
			_sharedStoreManager.storeObserver = [[MKStoreObserver alloc] init];
			[[SKPaymentQueue defaultQueue] addTransactionObserver:_sharedStoreManager.storeObserver];            
            [_sharedStoreManager startVerifyingSubscriptionReceipts];
#endif
        }
    }
    return _sharedStoreManager;
}

+ (id)allocWithZone:(NSZone *)zone

{	
    @synchronized(self) {
		
        if (_sharedStoreManager == nil) {
			
            _sharedStoreManager = [super allocWithZone:zone];			
            return _sharedStoreManager;  // assignment and return on first allocation
        }
    }
	
    return nil; //on subsequent allocation attempts return nil	
}


- (id)copyWithZone:(NSZone *)zone
{
    return self;	
}

#pragma mark Internal MKStoreKit functions

-(NSDictionary*) storeKitItems
{
    return [NSDictionary dictionaryWithContentsOfFile:
            [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:
             @"MKStoreKitConfigs.plist"]];
}

- (void) restorePreviousTransactionsOnComplete:(void (^)(void)) completionBlock
                                       onError:(void (^)(NSError*)) errorBlock
{
    self.onRestoreCompleted = completionBlock;
    self.onRestoreFailed = errorBlock;
    
	[[SKPaymentQueue defaultQueue] restoreCompletedTransactions];
}

-(void) restoreCompleted
{
    if(self.onRestoreCompleted)
        self.onRestoreCompleted();
    self.onRestoreCompleted = nil;
}

-(void) restoreFailedWithError:(NSError*) error
{
    if(self.onRestoreFailed)
        self.onRestoreFailed(error);
    self.onRestoreFailed = nil;
}

-(void) requestProductData
{
    NSMutableArray *productsArray = [NSMutableArray array];
    NSArray *consumables = [[[self storeKitItems] objectForKey:@"Consumables"] allKeys];
    NSArray *nonConsumables = [[self storeKitItems] objectForKey:@"Non-Consumables"];
    NSArray *subscriptions = [[[self storeKitItems] objectForKey:@"Subscriptions"] allKeys];
    
    [productsArray addObjectsFromArray:consumables];
    [productsArray addObjectsFromArray:nonConsumables];
    [productsArray addObjectsFromArray:subscriptions];
    
	SKProductsRequest *request= [[SKProductsRequest alloc] initWithProductIdentifiers:[NSSet setWithArray:productsArray]];
	request.delegate = self;
	[request start];
}
- (BOOL) removeAllKeychainData {
    NSMutableArray *productsArray = [NSMutableArray array];
    NSArray *consumables = [[[self storeKitItems] objectForKey:@"Consumables"] allKeys];
    NSArray *nonConsumables = [[self storeKitItems] objectForKey:@"Non-Consumables"];
    NSArray *subscriptions = [[[self storeKitItems] objectForKey:@"Subscriptions"] allKeys];
    
    [productsArray addObjectsFromArray:consumables];
    [productsArray addObjectsFromArray:nonConsumables];
    [productsArray addObjectsFromArray:subscriptions];
    
    int itemCount = productsArray.count;
    NSError *error;
    
    //loop through all the saved keychain data and remove it    
    for (int i = 0; i < itemCount; i++ ) {
        [SFHFKeychainUtils deleteItemForUsername:[productsArray objectAtIndex:i] andServiceName:@"MKStoreKit" error:&error];
    }
    if (!error) {
        return YES; 
    }
    else {
        return NO;
    }
}

- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response
{
	[self.purchasableObjects addObjectsFromArray:response.products];
	
#ifndef NDEBUG	
	for(int i=0;i<[self.purchasableObjects count];i++)
	{		
		SKProduct *product = [self.purchasableObjects objectAtIndex:i];
		NSLog(@"Feature: %@, Cost: %f, ID: %@",[product localizedTitle],
			  [[product price] doubleValue], [product productIdentifier]);
	}
	
	for(NSString *invalidProduct in response.invalidProductIdentifiers)
		NSLog(@"Problem in iTunes connect configuration for product: %@", invalidProduct);
#endif
		
	isProductsAvailable = YES;    
    [[NSNotificationCenter defaultCenter] postNotificationName:kProductFetchedNotification 
                                                        object:[NSNumber numberWithBool:isProductsAvailable]];
}

- (void)request:(SKRequest *)request didFailWithError:(NSError *)error
{
	isProductsAvailable = NO;	
    [[NSNotificationCenter defaultCenter] postNotificationName:kProductFetchedNotification 
                                                        object:[NSNumber numberWithBool:isProductsAvailable]];
}

// call this function to check if the user has already purchased your feature
+ (BOOL) isFeaturePurchased:(NSString*) featureId
{    
    return [[MKStoreManager numberForKey:featureId] boolValue];
}

- (BOOL) isSubscriptionActive:(NSString*) featureId
{    
    MKSKSubscriptionProduct *subscriptionProduct = [self.subscriptionProducts objectForKey:featureId];
    return [subscriptionProduct isSubscriptionActive];
}

// Call this function to populate your UI
// this function automatically formats the currency based on the user's locale

- (NSMutableArray*) purchasableObjectsDescription
{
	NSMutableArray *productDescriptions = [[NSMutableArray alloc] initWithCapacity:[self.purchasableObjects count]];
	for(int i=0;i<[self.purchasableObjects count];i++)
	{
		SKProduct *product = [self.purchasableObjects objectAtIndex:i];
		
		NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
		[numberFormatter setFormatterBehavior:NSNumberFormatterBehavior10_4];
		[numberFormatter setNumberStyle:NSNumberFormatterCurrencyStyle];
		[numberFormatter setLocale:product.priceLocale];
		NSString *formattedString = [numberFormatter stringFromNumber:product.price];
		
		// you might probably need to change this line to suit your UI needs
		NSString *description = [NSString stringWithFormat:@"%@ (%@)",[product localizedTitle], formattedString];
		
#ifndef NDEBUG
		NSLog(@"Product %d - %@", i, description);
#endif
		[productDescriptions addObject: description];
	}
	
	return productDescriptions;
}

/*Call this function to get a dictionary with all prices of all your product identifers 

For example, 
 
NSDictionary *prices = [[MKStoreManager sharedManager] pricesDictionary];

NSString *upgradePrice = [prices objectForKey:@"com.mycompany.upgrade"]

*/
- (NSMutableDictionary *)pricesDictionary {
    NSMutableDictionary *priceDict = [NSMutableDictionary dictionary];
	for(int i=0;i<[self.purchasableObjects count];i++)
	{
		SKProduct *product = [self.purchasableObjects objectAtIndex:i];
		
		NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
		[numberFormatter setFormatterBehavior:NSNumberFormatterBehavior10_4];
		[numberFormatter setNumberStyle:NSNumberFormatterCurrencyStyle];
		[numberFormatter setLocale:product.priceLocale];
		NSString *formattedString = [numberFormatter stringFromNumber:product.price];
        
        NSString *priceString = [NSString stringWithFormat:@"%@", formattedString];
        [priceDict setObject:priceString forKey:product.productIdentifier]; 
        
    }
    return priceDict;
}

- (void) buyFeature:(NSString*) featureId
         onComplete:(void (^)(NSString*)) completionBlock         
        onCancelled:(void (^)(void)) cancelBlock
{
    self.onTransactionCompleted = completionBlock;
    self.onTransactionCancelled = cancelBlock;
    
    [MKSKProduct verifyProductForReviewAccess:featureId                                                              
                                   onComplete:^(NSNumber * isAllowed)
     {
         if([isAllowed boolValue])
         {
             UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Review request approved", @"")
                                                             message:NSLocalizedString(@"You can use this feature for reviewing the app.", @"")
                                                            delegate:self 
                                                   cancelButtonTitle:NSLocalizedString(@"Dismiss", @"")
                                                   otherButtonTitles:nil];
             [alert show];
             
             if(self.onTransactionCompleted)
                 self.onTransactionCompleted(featureId);                                         
         }
         else
         {
             [self addToQueue:featureId];
         }
         
     }                                                                   
                                      onError:^(NSError* error)
     {
         NSLog(@"Review request cannot be checked now: %@", [error description]);
         [self addToQueue:featureId];
     }];    
}

-(void) addToQueue:(NSString*) productId
{
    if ([SKPaymentQueue canMakePayments])
	{
        NSArray *allIds = [self.purchasableObjects valueForKey:@"productIdentifier"];
        int index = [allIds indexOfObject:productId];
        
        if(index == NSNotFound) return;
        
        SKProduct *thisProduct = [self.purchasableObjects objectAtIndex:index];
		SKPayment *payment = [SKPayment paymentWithProduct:thisProduct];
		[[SKPaymentQueue defaultQueue] addPayment:payment];
	}
	else
	{
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"In-App Purchasing disabled", @"")
														message:NSLocalizedString(@"Check your parental control settings and try again later", @"")
													   delegate:self 
											  cancelButtonTitle:NSLocalizedString(@"Dismiss", @"")
											  otherButtonTitles: nil];
		[alert show];
	}
}

- (BOOL) canConsumeProduct:(NSString*) productIdentifier
{
	int count = [[MKStoreManager numberForKey:productIdentifier] intValue];
	
	return (count > 0);
	
}

- (BOOL) canConsumeProduct:(NSString*) productIdentifier quantity:(int) quantity
{
	int count = [[MKStoreManager numberForKey:productIdentifier] intValue];
	return (count >= quantity);
}

- (BOOL) consumeProduct:(NSString*) productIdentifier quantity:(int) quantity
{
	int count = [[MKStoreManager numberForKey:productIdentifier] intValue];
	if(count < quantity)
	{
		return NO;
	}
	else 
	{
		count -= quantity;
        [MKStoreManager setObject:[NSNumber numberWithInt:count] forKey:productIdentifier];
		return YES;
	}	
}

- (void) startVerifyingSubscriptionReceipts
{
    NSDictionary *subscriptions = [[self storeKitItems] objectForKey:@"Subscriptions"];
    
    self.subscriptionProducts = [NSMutableDictionary dictionary];
    for(NSString *productId in [subscriptions allKeys])
    {
        MKSKSubscriptionProduct *product = [[MKSKSubscriptionProduct alloc] initWithProductId:productId subscriptionDays:[[subscriptions objectForKey:productId] intValue]];        
        product.receipt = [MKStoreManager dataForKey:productId]; // cached receipt
        
        if(product.receipt)
        {
            [product verifyReceiptOnComplete:^(NSNumber* isActive)
             {
                 if([isActive boolValue] == NO)
                 {
                     [[NSNotificationCenter defaultCenter] postNotificationName:kSubscriptionsInvalidNotification 
                                                                         object:product.productId];
                     
                     NSLog(@"Subscription: %@ is inactive", product.productId);
                 }
                 else
                 {
                     NSLog(@"Subscription: %@ is active", product.productId);                     
                 }
             }
                                     onError:^(NSError* error)
             {
                 NSLog(@"Unable to check for subscription validity right now");                                      
             }]; 
        }
        
        [self.subscriptionProducts setObject:product forKey:productId];
    }
}

#pragma mark In-App purchases callbacks
// In most cases you don't have to touch these methods
-(void) provideContent: (NSString*) productIdentifier 
            forReceipt:(NSData*) receiptData
{
    MKSKSubscriptionProduct *subscriptionProduct = [self.subscriptionProducts objectForKey:productIdentifier];
    if(subscriptionProduct)
    {                
        subscriptionProduct.receipt = receiptData;
        [subscriptionProduct verifyReceiptOnComplete:^(NSNumber* isActive)
         {
             [[NSNotificationCenter defaultCenter] postNotificationName:kSubscriptionsPurchasedNotification 
                                                                 object:productIdentifier];

             [MKStoreManager setObject:receiptData forKey:productIdentifier];             
         }
                                             onError:^(NSError* error)
         {
             NSLog(@"%@", [error description]);
         }];
    }        
    else
    {
        if(OWN_SERVER && SERVER_PRODUCT_MODEL)
        {
            // ping server and get response before serializing the product
            // this is a blocking call to post receipt data to your server
            // it should normally take a couple of seconds on a good 3G connection
            MKSKProduct *thisProduct = [[MKSKProduct alloc] initWithProductId:productIdentifier receiptData:receiptData];
            
            [thisProduct verifyReceiptOnComplete:^
             {
                 [self rememberPurchaseOfProduct:productIdentifier];
             }
                                         onError:^(NSError* error)
             {
                 if(self.onTransactionCancelled)
                 {
                     self.onTransactionCancelled(productIdentifier);
                 }
                 else
                 {
                     NSLog(@"The receipt could not be verified");
                 }
             }];            
        }
        else
        {
            [self rememberPurchaseOfProduct:productIdentifier];
            if(self.onTransactionCompleted)
                self.onTransactionCompleted(productIdentifier);
        }                
    }
}


-(void) rememberPurchaseOfProduct:(NSString*) productIdentifier
{
    NSDictionary *allConsumables = [[self storeKitItems] objectForKey:@"Consumables"];
    if([[allConsumables allKeys] containsObject:productIdentifier])
    {
        NSDictionary *thisConsumableDict = [allConsumables objectForKey:productIdentifier];
        int quantityPurchased = [[thisConsumableDict objectForKey:@"Count"] intValue];
        NSString* productPurchased = [thisConsumableDict objectForKey:@"Name"];
        
        int oldCount = [[MKStoreManager numberForKey:productPurchased] intValue];
        int newCount = oldCount + quantityPurchased;	
        
        [MKStoreManager setObject:[NSNumber numberWithInt:newCount] forKey:productIdentifier];        
    }
    else
    {
        [MKStoreManager setObject:[NSNumber numberWithBool:YES] forKey:productIdentifier];	
    }
}

- (void) transactionCanceled: (SKPaymentTransaction *)transaction
{
    
#ifndef NDEBUG
	NSLog(@"User cancelled transaction: %@", [transaction description]);
    NSLog(@"error: %@", transaction.error);
#endif
    
    if(self.onTransactionCancelled)
        self.onTransactionCancelled();
}

- (void) failedTransaction: (SKPaymentTransaction *)transaction
{
    
#ifndef NDEBUG
    NSLog(@"Failed transaction: %@", [transaction description]);
    NSLog(@"error: %@", transaction.error);    
#endif
	
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:[transaction.error localizedFailureReason] 
													message:[transaction.error localizedRecoverySuggestion]
												   delegate:self 
										  cancelButtonTitle:NSLocalizedString(@"Dismiss", @"")
										  otherButtonTitles: nil];
	[alert show];
    
    if(self.onTransactionCancelled)
        self.onTransactionCancelled();
}

@end
