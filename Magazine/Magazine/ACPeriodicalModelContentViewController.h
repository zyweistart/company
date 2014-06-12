#import <UIKit/UIKit.h>

@class ACPeriodicalDataContentViewController;

@interface ACPeriodicalModelContentViewController : NSObject <UIPageViewControllerDataSource>

- (id)initWithData:(NSArray *)data;

- (ACPeriodicalDataContentViewController *)viewControllerAtIndex:(NSUInteger)index;

@end