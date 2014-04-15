//
//  BaseUIActivityViewController.m
//  Ancun
//
//  Created by Start on 4/15/14.
//
//

#import "BaseUIActivityViewController.h"

@interface BaseUIActivityViewController ()

@end

@implementation BaseUIActivityViewController

- (id)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

- (void)presentViewController:(UIViewController *)viewControllerToPresent animated: (BOOL)flag completion:(void (^)(void))completion
{
//    [[[Config Instance] allViewControllerHierarchys] addObject:viewControllerToPresent];
    [super presentViewController:viewControllerToPresent animated:flag completion:completion];
}

- (void)dismissViewControllerAnimated: (BOOL)flag completion: (void (^)(void))completion
{
    [[[Config Instance] allViewControllerHierarchys] removeLastObject];
    [super dismissViewControllerAnimated:flag completion:completion];
}

- (void)presentModalViewController:(UIViewController *)modalViewController animated:(BOOL)animated
{
//    [[[Config Instance] allViewControllerHierarchys] addObject:modalViewController];
    [super presentModalViewController:modalViewController animated:animated];
}

- (void)dismissModalViewControllerAnimated:(BOOL)animated
{
    [[[Config Instance] allViewControllerHierarchys] removeLastObject];
    [super dismissModalViewControllerAnimated:animated];
}

@end
