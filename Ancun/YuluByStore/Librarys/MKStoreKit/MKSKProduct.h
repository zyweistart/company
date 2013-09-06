#import <Foundation/Foundation.h>
#import "MKStoreKitConfigs.h"

@interface MKSKProduct : NSObject 

@property (nonatomic, copy) void (^onReceiptVerificationSucceeded)();
@property (nonatomic, copy) void (^onReceiptVerificationFailed)();

@property (nonatomic, strong) NSData *receipt;

@property (nonatomic, strong) NSString *productId;
@property (nonatomic, strong) NSURLConnection *theConnection;
@property (nonatomic, strong) NSMutableData *dataFromConnection;

- (void) verifyReceiptOnComplete:(void (^)(void)) completionBlock
                         onError:(void (^)(NSError*)) errorBlock;

-(id) initWithProductId:(NSString*) aProductId receiptData:(NSData*) aReceipt;

+(void) verifyProductForReviewAccess:(NSString*) productId
                          onComplete:(void (^)(NSNumber*)) completionBlock
                             onError:(void (^)(NSError*)) errorBlock;
@end
