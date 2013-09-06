#import "MKSKProduct.h"

static void (^onReviewRequestVerificationSucceeded)();
static void (^onReviewRequestVerificationFailed)();
static NSURLConnection *sConnection;
static NSMutableData *sDataFromConnection;

@implementation MKSKProduct
@synthesize onReceiptVerificationFailed;
@synthesize onReceiptVerificationSucceeded;
@synthesize receipt;
@synthesize productId;
@synthesize theConnection;
@synthesize dataFromConnection;

-(id) initWithProductId:(NSString*) aProductId receiptData:(NSData*) aReceipt
{
    if((self = [super init]))
    {
        self.productId = aProductId;
        self.receipt = aReceipt;
    }
    return self;
}


#pragma mark -
#pragma mark In-App purchases promo codes support
// This function is only used if you want to enable in-app purchases for free for reviewers
// Read my blog post http://mk.sg/31

+(void) verifyProductForReviewAccess:(NSString*) productId
                          onComplete:(void (^)(NSNumber*)) completionBlock
                             onError:(void (^)(NSError*)) errorBlock
{
    if(REVIEW_ALLOWED)
    {
        onReviewRequestVerificationSucceeded = [completionBlock copy];
        
        onReviewRequestVerificationFailed = [errorBlock copy];
        
        UIDevice *dev = [UIDevice currentDevice];
        NSString *uniqueID;
        if ([dev respondsToSelector:@selector(uniqueIdentifier)])
            uniqueID = [dev valueForKey:@"uniqueIdentifier"];
        else {
            NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
            id uuid = [defaults objectForKey:@"uniqueID"];
            if (uuid)
                uniqueID = (NSString *)uuid;
            else {
                CFStringRef cfUuid = CFUUIDCreateString(NULL, CFUUIDCreate(NULL));
                uniqueID = (__bridge NSString *)cfUuid;
                CFRelease(cfUuid);
                [defaults setObject:uniqueID forKey:@"uniqueID"];
            }
        }
        // check udid and featureid with developer's server
		
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@/%@", OWN_SERVER, @"featureCheck.php"]];
        
        NSMutableURLRequest *theRequest = [NSMutableURLRequest requestWithURL:url 
                                                                  cachePolicy:NSURLRequestReloadIgnoringCacheData 
                                                              timeoutInterval:60];
        
        [theRequest setHTTPMethod:@"POST"];		
        [theRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
        
        NSString *postData = [NSString stringWithFormat:@"productid=%@&udid=%@", productId, uniqueID];
        
        NSString *length = [NSString stringWithFormat:@"%d", [postData length]];	
        [theRequest setValue:length forHTTPHeaderField:@"Content-Length"];	
        
        [theRequest setHTTPBody:[postData dataUsingEncoding:NSASCIIStringEncoding]];
        
        sConnection = [NSURLConnection connectionWithRequest:theRequest delegate:self];    
        [sConnection start];	
    }
    else
    {
        completionBlock([NSNumber numberWithBool:NO]);
    }
}

- (void) verifyReceiptOnComplete:(void (^)(void)) completionBlock
                         onError:(void (^)(NSError*)) errorBlock
{
    self.onReceiptVerificationSucceeded = completionBlock;
    self.onReceiptVerificationFailed = errorBlock;
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@/%@", OWN_SERVER, @"verifyProduct.php"]];
	
	NSMutableURLRequest *theRequest = [NSMutableURLRequest requestWithURL:url 
                                                              cachePolicy:NSURLRequestReloadIgnoringCacheData 
                                                          timeoutInterval:60];
	
	[theRequest setHTTPMethod:@"POST"];		
	[theRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
	
	NSString *receiptDataString = [[NSString alloc] initWithData:self.receipt 
                                                        encoding:NSASCIIStringEncoding];
    
	NSString *postData = [NSString stringWithFormat:@"receiptdata=%@", receiptDataString];
	
	NSString *length = [NSString stringWithFormat:@"%d", [postData length]];	
	[theRequest setValue:length forHTTPHeaderField:@"Content-Length"];	
	
	[theRequest setHTTPBody:[postData dataUsingEncoding:NSASCIIStringEncoding]];
	
    self.theConnection = [NSURLConnection connectionWithRequest:theRequest delegate:self];    
    [self.theConnection start];	
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
    NSString *responseString = [[NSString alloc] initWithData:self.dataFromConnection 
                                                     encoding:NSASCIIStringEncoding];
	
    self.dataFromConnection = nil;
    
	if([responseString isEqualToString:@"YES"])		
	{
        if(self.onReceiptVerificationSucceeded)
        {
            self.onReceiptVerificationSucceeded();
            self.onReceiptVerificationSucceeded = nil;
        }
	}
    else
    {
        if(self.onReceiptVerificationFailed)
        {
            self.onReceiptVerificationFailed(nil);
            self.onReceiptVerificationFailed = nil;
        }
    }
	
    
}


- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{
    
    self.dataFromConnection = nil;
    if(self.onReceiptVerificationFailed)
    {
        self.onReceiptVerificationFailed(nil);
        self.onReceiptVerificationFailed = nil;
    }
}



+ (void)connection:(NSURLConnection *)connection
didReceiveResponse:(NSURLResponse *)response
{	
    sDataFromConnection = [[NSMutableData alloc] init];
}

+ (void)connection:(NSURLConnection *)connection
    didReceiveData:(NSData *)data
{
	[sDataFromConnection appendData:data];
}

+ (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    NSString *responseString = [[NSString alloc] initWithData:sDataFromConnection 
                                                     encoding:NSASCIIStringEncoding];
	
    sDataFromConnection = nil;
    
	if([responseString isEqualToString:@"YES"])		
	{
        if(onReviewRequestVerificationSucceeded)
        {
            onReviewRequestVerificationSucceeded();
            onReviewRequestVerificationFailed = nil;
        }
	}
    else
    {
        if(onReviewRequestVerificationFailed)
            onReviewRequestVerificationFailed(nil);
        
        onReviewRequestVerificationFailed = nil;
    }
	
    
}

+ (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{
    sDataFromConnection = nil;
    
    if(onReviewRequestVerificationFailed)
    {
        onReviewRequestVerificationFailed(nil);    
        onReviewRequestVerificationFailed = nil;
    }
}
@end
