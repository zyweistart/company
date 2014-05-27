#import "ResultDelegate.h"

@interface Config : NSObject

//是否已登陆
@property Boolean isLogin;
//是否需要计算缓存空间大小
@property Boolean isCalculateTotal;

//唯一缓存键名称
@property (strong,nonatomic) NSString *cacheKey;
//用户信息
@property (strong,nonatomic) NSMutableDictionary *userInfo;
//登陆代理
@property (strong,nonatomic) NSObject<ResultDelegate> *loginResultDelegate;

+ (Config *)Instance;

+ (void)resetConfig;

@end