#import "ACPeriodicalRootContentViewController.h"
#import "ACPeriodicalDataContentViewController.h"
#import "ACPeriodicalModelContentViewController.h"

@interface ACPeriodicalRootContentViewController ()

@property NSInteger index;
@property (strong,nonatomic)NSArray *data;

@property (readonly, strong, nonatomic) ACPeriodicalModelContentViewController *modelController;

@end

@implementation ACPeriodicalRootContentViewController

@synthesize modelController = _modelController;

- (id)initWithData:(NSArray *)data Index:(NSInteger)index
{
    self=[super init];
    if(self){
        self.data=data;
        self.index=index;
        self.pageViewController = [[UIPageViewController alloc] initWithTransitionStyle:UIPageViewControllerTransitionStylePageCurl navigationOrientation:UIPageViewControllerNavigationOrientationHorizontal options:nil];
        self.pageViewController.delegate = self;
        
        ACPeriodicalDataContentViewController *startingViewController = [self.modelController viewControllerAtIndex:self.index];
        NSArray *viewControllers = @[startingViewController];
        [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:NO completion:nil];
        
        self.pageViewController.dataSource = self.modelController;
        
        [self addChildViewController:self.pageViewController];
        [self.view addSubview:self.pageViewController.view];
        
        CGRect pageViewRect = self.view.bounds;
        self.pageViewController.view.frame = pageViewRect;
        
        [self.pageViewController didMoveToParentViewController:self];
        
        self.view.gestureRecognizers = self.pageViewController.gestureRecognizers;
    }
    return self;
}

- (ACPeriodicalModelContentViewController *)modelController
{
    if (!_modelController) {
        _modelController = [[ACPeriodicalModelContentViewController alloc] initWithData:self.data];
    }
    return _modelController;
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