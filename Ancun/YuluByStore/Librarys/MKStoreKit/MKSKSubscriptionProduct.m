#import "MKSKSubscriptionProduct.h"
#import "NSData+Base64.h"

@implementation MKSKSubscriptionProduct
@synthesize onSubscriptionVerificationFailed;
@synthesize onSubscriptionVerificationCompleted;
@synthesize receipt;
@synthesize subscriptionDays;
@synthesize theConnection;
@synthesize dataFromConnection;
@synthesize productId;
@synthesize verifiedReceiptDictionary;

-(id) initWithProductId:(NSString*) aProductId subscriptionDays:(int) days
{
    if((self = [super init]))
    {
        self.productId = aProductId;
        self.subscriptionDays = days;
    }
    
    return self;
}

- (void) verifyReceiptOnComplete:(void (^)(NSNumber*)) completionBlock
                         onError:(void (^)(NSError*)) errorBlock
{        
    self.onSubscriptionVerificationCompleted = completionBlock;
    self.onSubscriptionVerificationFailed = errorBlock;
    
    NSURL *url = [NSURL URLWithString:kReceiptValidationURL];
	
	NSMutableURLRequest *theRequest = [NSMutableURLRequest requestWithURL:url 
                                                              cachePolicy:NSURLRequestReloadIgnoringCacheData 
                                                          timeoutInterval:60];
	
	[theRequest setHTTPMethod:@"POST"];		
	[theRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
	
    NSString *receiptString = [NSString stringWithFormat:@"{\"receipt-data\":\"%@\" \"password\":\"%@\"}", [self.receipt base64EncodedString], kSharedSecret];        
    
	NSString *length = [NSString stringWithFormat:@"%d", [receiptString length]];	
	[theRequest setValue:length forHTTPHeaderField:@"Content-Length"];	
	
	[theRequest setHTTPBody:[receiptString dataUsingEncoding:NSUTF8StringEncoding]];
	
    self.theConnection = [NSURLConnection connectionWithRequest:theRequest delegate:self];    
    [self.theConnection start];    
}

-(BOOL) isSubscriptionActive
{
    NSString *purchasedDateString = [[self.verifiedReceiptDictionary objectForKey:@"receipt"] objectForKey:@"purchase_date"];
    if(!purchasedDateString) return NO;
    
    NSDateFormatter *df = [[NSDateFormatter alloc] init];

    //2011-07-03 05:31:55 Etc/GMT
    purchasedDateString = [purchasedDateString stringByReplacingOccurrencesOfString:@" Etc/GMT" withString:@""];    
    NSLocale *POSIXLocale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US_POSIX"];
    [df setLocale:POSIXLocale];
    [df setTimeZone:[NSTimeZone timeZoneForSecondsFromGMT:0]];    
    [df setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    
    NSDate *purchasedDate = [df dateFromString: purchasedDateString];
    
    int numberOfDays = [purchasedDate timeIntervalSinceNow] / (-86400.0);    
    return (self.subscriptionDays > numberOfDays);
}


#pragma mark -
#pragma mark NSURLConnection delegate

- (void)connection:(NSURLConnection *)connection
didReceiveResponse:(NSURLResponse *)response
{	
    self.dataFromConnection = [NSMutableData data];
}

- (void)connection:(NSURLConnection *)connection
    didReceiveData:(NSData *)data
{
	[self.dataFromConnection appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    self.verifiedReceiptDictionary = [[self.dataFromConnection copy] objectFromJSONData];                                              
    if(self.onSubscriptionVerificationCompleted)
    {
        self.onSubscriptionVerificationCompleted([NSNumber numberWithBool:[self isSubscriptionActive]]);
        self.dataFromConnection = nil;
    }
    
    self.onSubscriptionVerificationCompleted = nil;
}

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{
    self.dataFromConnection = nil;
    if(self.onSubscriptionVerificationFailed)
        self.onSubscriptionVerificationFailed(error);
    
    self.onSubscriptionVerificationFailed = nil;
}

@end
