#import "BaseViewController.h"

@interface ACPeriodicalRootContentViewController : BaseViewController<UIPageViewControllerDelegate>

- (id)initWithData:(NSArray *)data Index:(NSInteger)index;

@property (strong, nonatomic) UIPageViewController *pageViewController;

@end