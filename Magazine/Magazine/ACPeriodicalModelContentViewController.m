#import "ACPeriodicalModelContentViewController.h"
#import "ACPeriodicalDataContentViewController.h"

@interface ACPeriodicalModelContentViewController()

@property (strong, nonatomic) NSArray *pageData;

@end

@implementation ACPeriodicalModelContentViewController

- (id)initWithData:(NSArray *)data
{
    self = [super init];
    if (self) {
        self.pageData=data;
    }
    return self;
}

- (ACPeriodicalDataContentViewController *)viewControllerAtIndex:(NSUInteger)index
{
    if (([self.pageData count] == 0) || (index >= [self.pageData count])) {
        return nil;
    }
    ACPeriodicalDataContentViewController *periodicalDataContentViewController=[[ACPeriodicalDataContentViewController alloc]initWithData:self.pageData[index]];
    [periodicalDataContentViewController loadData];
    return periodicalDataContentViewController;
}

- (NSUInteger)indexOfViewController:(ACPeriodicalDataContentViewController *)viewController
{
    return [self.pageData indexOfObject:viewController.data];
}

#pragma mark - Page View Controller Data Source

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerBeforeViewController:(UIViewController *)viewController
{
    NSUInteger index = [self indexOfViewController:(ACPeriodicalDataContentViewController *)viewController];
    if ((index == 0) || (index == NSNotFound)) {
        [Common alert:@"当前为第一页"];
        return nil;
    }
    index--;
    return [self viewControllerAtIndex:index];
}

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerAfterViewController:(UIViewController *)viewController
{
    NSUInteger index = [self indexOfViewController:(ACPeriodicalDataContentViewController *)viewController];
    if (index == NSNotFound) {
        return nil;
    }
    index++;
    if (index == [self.pageData count]) {
        [Common alert:@"已经到达最后一页"];
        return nil;
    }
    return [self viewControllerAtIndex:index];
}

@end