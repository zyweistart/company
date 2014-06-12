#import "BaseViewController.h"

@interface ACPeriodicalRootContentViewController : BaseViewController<UIPageViewControllerDelegate,UIPageViewControllerDataSource>

- (id)initWithData:(NSArray *)data Index:(NSInteger)index;

@end