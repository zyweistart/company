#import <Foundation/Foundation.h>
#import <StoreKit/StoreKit.h>
#import "MKStoreManager.h"

@interface MKSKSubscriptionProduct : NSObject

@property (nonatomic, copy) void (^onSubscriptionVerificationFailed)();
@property (nonatomic, copy) void (^onSubscriptionVerificationCompleted)(NSNumber* isActive);
@property (nonatomic, strong) NSData *receipt;
@property (nonatomic, strong) NSDictionary *verifiedReceiptDictionary;
@property (nonatomic, assign) int subscriptionDays; 
@property (nonatomic, strong) NSString *productId;
@property (nonatomic, strong) NSURLConnection *theConnection;
@property (nonatomic, strong) NSMutableData *dataFromConnection;


- (void) verifyReceiptOnComplete:(void (^)(NSNumber*)) completionBlock
                         onError:(void (^)(NSError*)) errorBlock;

-(BOOL) isSubscriptionActive;
-(id) initWithProductId:(NSString*) productId subscriptionDays:(int) days;
@end
