#import <Foundation/Foundation.h>
#import <StoreKit/StoreKit.h>
#import "MKStoreObserver.h"
#import "MKStoreKitConfigs.h"
#import "JSONKit.h"

#define kReceiptStringKey @"MK_STOREKIT_RECEIPTS_STRING"

#ifndef NDEBUG
#define kReceiptValidationURL @"https://sandbox.itunes.apple.com/verifyReceipt"
#else
#define kReceiptValidationURL @"https://buy.itunes.apple.com/verifyReceipt"
#endif

#define kProductFetchedNotification @"MKStoreKitProductsFetched"
#define kSubscriptionsPurchasedNotification @"MKStoreKitSubscriptionsPurchased"
#define kSubscriptionsInvalidNotification @"MKStoreKitSubscriptionsInvalid"

@interface MKStoreManager : NSObject<SKProductsRequestDelegate>

// These are the methods you will be using in your app
+ (MKStoreManager*)sharedManager;

// this is a static method, since it doesn't require the store manager to be initialized prior to calling
+ (BOOL) isFeaturePurchased:(NSString*) featureId; 
//returns a dictionary with all prices for identifiers
- (NSMutableDictionary *)pricesDictionary;
- (NSMutableArray*) purchasableObjectsDescription;

// use this method to invoke a purchase
- (void) buyFeature:(NSString*) featureId
         onComplete:(void (^)(NSString*)) completionBlock         
        onCancelled:(void (^)(void)) cancelBlock;

// use this method to restore a purchase
- (void) restorePreviousTransactionsOnComplete:(void (^)(void)) completionBlock
                                       onError:(void (^)(NSError*)) errorBlock;

- (BOOL) canConsumeProduct:(NSString*) productName quantity:(int) quantity;
- (BOOL) consumeProduct:(NSString*) productName quantity:(int) quantity;
- (BOOL) isSubscriptionActive:(NSString*) featureId;
//for testing proposes you can use this method to remove all the saved keychain data (saved purchases, etc.)
- (BOOL) removeAllKeychainData;

+(void) setObject:(id) object forKey:(NSString*) key;
+(NSNumber*) numberForKey:(NSString*) key;

-(void) restoreCompleted;
-(void) restoreFailedWithError:(NSError*) error;
@end
