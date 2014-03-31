#import <UIKit/UIKit.h>
#import "BaseRefreshTableViewController.h"

@interface ACOldAccountViewController :BaseRefreshTableViewController<UIScrollViewDelegate,UIActionSheetDelegate>

@property (strong,nonatomic) HttpRequest *loadHttp;

@end

