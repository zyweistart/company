//
//  STGuideViewController.m
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STGuideViewController.h"
#import "STMainTabBarController.h"
#import "STIndexViewController.h"
#import "STStudyViewController.h"
#import "STMyeViewController.h"


@interface STGuideViewController ()

@end

@implementation STGuideViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIButton *btnF2=[[UIButton alloc]initWithFrame:CGRectMake(60,100, 152.5, 80)];
    btnF2.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF2 setTitle:@"进入主界面" forState:UIControlStateNormal];
    [btnF2 setBackgroundColor:[UIColor blueColor]];
    [btnF2 addTarget:self action:@selector(onClickIn:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnF2];
    
}

- (void)onClickIn:(id)sender {
    //首页
    UINavigationController *indexViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STIndexViewController alloc]init]];
    indexViewControllerNav.navigationBarHidden=YES;
    indexViewControllerNav.tabBarItem.title=@"首页";
    //我要学习
    UINavigationController *studyViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STStudyViewController alloc]init]];
    studyViewControllerNav.tabBarItem.title=@"我要学习";
    //我的E电工
    UINavigationController *myeViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STMyeViewController alloc]init]];
    myeViewControllerNav.tabBarItem.title=@"我的E电工";
    
    STMainTabBarController *_mainTabBarController = [[STMainTabBarController alloc] init];
//    _mainTabBarController.delegate = self;
    _mainTabBarController.viewControllers = [NSArray arrayWithObjects:
                                         indexViewControllerNav,
                                         studyViewControllerNav,
                                         myeViewControllerNav,
                                         nil];
    [self presentViewController:_mainTabBarController animated:YES completion:nil];
}
@end
