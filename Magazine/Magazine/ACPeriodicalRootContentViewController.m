#import "ACPeriodicalRootContentViewController.h"
#import "ACPeriodicalDataContentViewController.h"

@interface ACPeriodicalRootContentViewController ()

@property NSInteger index;
@property (strong,nonatomic)NSArray *itemDataArray;
@property (strong,nonatomic) Book *book;
@property (strong, nonatomic) UIPageViewController *pageViewController;

@end

@implementation ACPeriodicalRootContentViewController

- (id)initWithData:(NSArray *)data Index:(NSInteger)index book:(Book *)book
{
    self=[super init];
    if(self){
        self.index=index;
        self.itemDataArray=data;
        self.book=book;
        self.pageViewController = [[UIPageViewController alloc] initWithTransitionStyle:UIPageViewControllerTransitionStylePageCurl navigationOrientation:UIPageViewControllerNavigationOrientationHorizontal options:nil];
        self.pageViewController.delegate = self;
        self.pageViewController.dataSource = self;
        
        ACPeriodicalDataContentViewController *startingViewController = [self viewControllerAtIndex:self.index];
        NSArray *viewControllers = @[startingViewController];
        [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:nil];
        
        [self addChildViewController:self.pageViewController];
        [self.view addSubview:self.pageViewController.view];
        
        self.pageViewController.view.frame = self.view.bounds;
        [self.pageViewController didMoveToParentViewController:self];
        
        self.view.gestureRecognizers = self.pageViewController.gestureRecognizers;
    }
    return self;
}

- (ACPeriodicalDataContentViewController *)viewControllerAtIndex:(NSUInteger)index
{
    if (([self.itemDataArray count] == 0) || (index >= [self.itemDataArray count])) {
        return nil;
    }
    ACPeriodicalDataContentViewController *periodicalDataContentViewController=[[ACPeriodicalDataContentViewController alloc]initWithData:self.itemDataArray[index]];
    [periodicalDataContentViewController setIndex:index];
    [periodicalDataContentViewController setBook:self.book];
    [periodicalDataContentViewController loadData];
    return periodicalDataContentViewController;
}

- (NSUInteger)indexOfViewController:(ACPeriodicalDataContentViewController *)viewController
{
    return viewController.index;
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
    if (index == [self.itemDataArray count]) {
        [Common alert:@"已经到达最后一页"];
        return nil;
    }
    return [self viewControllerAtIndex:index];
}

#pragma mark - UIPageViewController delegate methods

- (UIPageViewControllerSpineLocation)pageViewController:(UIPageViewController *)pageViewController spineLocationForInterfaceOrientation:(UIInterfaceOrientation)orientation
{
    UIViewController *currentViewController = self.pageViewController.viewControllers[0];
    NSArray *viewControllers = @[currentViewController];
    [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:nil];
    self.pageViewController.doubleSided = NO;
    return UIPageViewControllerSpineLocationMin;
}

@end