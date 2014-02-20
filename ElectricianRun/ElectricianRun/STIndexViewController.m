//
//  STIndexViewController.m
//  ElectricianRun
//  首页
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STIndexViewController.h"
#import "STUserExperienceAlarmViewController.h"
#import "STUserExperienceViewController.h"
#import "STProjectSiteViewController.h"
#import "STScanningOperationViewController.h"
#import "STCalculateViewController.h"
#import "STMyeViewController.h"

#import "STDataMonitoringViewController.h"
#import "STAlarmManagerViewController.h"
#import "STTaskManagerViewController.h"
#import "STTaskAuditViewController.h"

#import "ETFoursquareImages.h"
#import "NSString+Utils.h"

#define IMAGEHEIGHT 140

@interface STIndexViewController () <UITabBarControllerDelegate>

@end

@implementation STIndexViewController{
    HttpRequest *_hRequest;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    ETFoursquareImages *foursquareImages = [[ETFoursquareImages alloc] initWithFrame:CGRectMake(0, 0, 320, self.view.frame.size.height-0)];
    [foursquareImages setImagesHeight:IMAGEHEIGHT];
    
    NSArray *images  = [NSArray arrayWithObjects:
                        [UIImage imageNamed:@"horses"],
                        [UIImage imageNamed:@"surfer"],
                        [UIImage imageNamed:@"bridge"], nil];
    
    [foursquareImages setImages:images];
    [foursquareImages setBackgroundColor:[UIColor redColor]];
    
    [self.view addSubview:foursquareImages];
    
    //用户体验
    UIButton *btnF1=[[UIButton alloc]initWithFrame:CGRectMake(5,10+IMAGEHEIGHT, 152.5, 80)];
    btnF1.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF1 setTitle:@"用户体验" forState:UIControlStateNormal];
    [btnF1 setBackgroundColor:[UIColor purpleColor]];
    [btnF1 addTarget:self action:@selector(onClickUserExperience:) forControlEvents:UIControlEventTouchUpInside];
    [foursquareImages.scrollView addSubview:btnF1];
    //我管辖的变电站
    UIButton *btnF2=[[UIButton alloc]initWithFrame:CGRectMake(162.5,10+IMAGEHEIGHT, 152.5, 80)];
    btnF2.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF2 setTitle:@"我管辖的变电站" forState:UIControlStateNormal];
    [btnF2 setBackgroundColor:[UIColor blueColor]];
    [btnF2 addTarget:self action:@selector(onClickJurisdiction:) forControlEvents:UIControlEventTouchUpInside];
    [foursquareImages.scrollView addSubview:btnF2];
    //调试工具
    UIButton *btnF3=[[UIButton alloc]initWithFrame:CGRectMake(5,95+IMAGEHEIGHT, 73.85, 80)];
    btnF3.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF3 setTitle:@"调试工具" forState:UIControlStateNormal];
    [btnF3 setBackgroundColor:[UIColor greenColor]];
    [btnF3 addTarget:self action:@selector(onClickDebug:) forControlEvents:UIControlEventTouchUpInside];
    [foursquareImages.scrollView addSubview:btnF3];
    //工程建站
    UIButton *btnF4=[[UIButton alloc]initWithFrame:CGRectMake(83.85,95+IMAGEHEIGHT, 73.85, 80)];
    btnF4.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF4 setTitle:@"工程建站" forState:UIControlStateNormal];
    [btnF4 setBackgroundColor:[UIColor orangeColor]];
    [btnF4 addTarget:self action:@selector(onClickSite:) forControlEvents:UIControlEventTouchUpInside];
    [foursquareImages.scrollView addSubview:btnF4];
    //扫描操作
    UIButton *btnF5=[[UIButton alloc]initWithFrame:CGRectMake(162.7,95+IMAGEHEIGHT, 73.85, 80)];
    btnF5.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF5 setTitle:@"扫描操作" forState:UIControlStateNormal];
    [btnF5 setBackgroundColor:[UIColor redColor]];
    [btnF5 addTarget:self action:@selector(onClickOperating:) forControlEvents:UIControlEventTouchUpInside];
    [foursquareImages.scrollView addSubview:btnF5];
    //在线计算
    UIButton *btnF6=[[UIButton alloc]initWithFrame:CGRectMake(241.55,95+IMAGEHEIGHT, 73.85, 80)];
    btnF6.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF6 setTitle:@"在线计算" forState:UIControlStateNormal];
    [btnF6 setBackgroundColor:[UIColor blueColor]];
    [btnF6 addTarget:self action:@selector(onClickCalculation:) forControlEvents:UIControlEventTouchUpInside];
    [foursquareImages.scrollView addSubview:btnF6];
    
    foursquareImages.scrollView.contentSize = CGSizeMake(320, 95+80+IMAGEHEIGHT);
    
    [foursquareImages.pageControl setCurrentPageIndicatorTintColor:[UIColor colorWithRed:(28/255.f) green:(189/255.f) blue:(141/255.f) alpha:1.0]];
    
}

//用户体验
- (void)onClickUserExperience:(id)sender {
    
    UIStoryboard *storyboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    STUserExperienceAlarmViewController *stuea=[storyboard instantiateViewControllerWithIdentifier:@"STUserExperienceAlarmViewController"];
    UINavigationController *experienceAlarmViewControllerNav = [[UINavigationController alloc] initWithRootViewController:stuea];
    [self presentViewController:experienceAlarmViewControllerNav animated:YES completion:nil];
}

//我管辖的变电站
- (void)onClickJurisdiction:(id)sender {
    //数据监测
     UINavigationController *dtaMonitoringViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STDataMonitoringViewController alloc]init]];
//    dtaMonitoringViewControllerNav.navigationBarHidden=YES;
    dtaMonitoringViewControllerNav.tabBarItem.title=@"数据监测";
    //报警管理
    UINavigationController *alarmManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STAlarmManagerViewController alloc]init]];
    alarmManagerViewControllerNav.tabBarItem.title=@"报警管理";
    //任务管理
    UINavigationController *taskManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STTaskManagerViewController alloc]init]];
    taskManagerViewControllerNav.title=@"任务管理";
    taskManagerViewControllerNav.tabBarItem.title=@"任务管理";
    //任务稽核
    UINavigationController *taskAuditViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STTaskAuditViewController alloc]init]];
    taskAuditViewControllerNav.title=@"任务稽核";
    taskAuditViewControllerNav.tabBarItem.title=@"任务稽核";
    
    UITabBarController *_tabBarController = [[UITabBarController alloc] init];
    [_tabBarController.view setBackgroundColor:[UIColor whiteColor]];
    _tabBarController.delegate = self;
    _tabBarController.viewControllers = [NSArray arrayWithObjects:
                                             dtaMonitoringViewControllerNav,
                                             alarmManagerViewControllerNav,
                                             taskManagerViewControllerNav,
                                             taskAuditViewControllerNav,nil];
    [self presentViewController:_tabBarController animated:YES completion:nil];
}

//调试工具
- (void)onClickDebug:(id)sender {
    UINavigationController *myeViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STMyeViewController alloc]init]];
    [self presentViewController:myeViewControllerNav animated:YES completion:nil];
}

//工程建站
- (void)onClickSite:(id)sender {
    UINavigationController *projectSiteViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STProjectSiteViewController alloc]init]];
    [self presentViewController:projectSiteViewControllerNav animated:YES completion:nil];
}

//扫描操作
- (void)onClickOperating:(id)sender {
    UINavigationController *scanningOperationViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STScanningOperationViewController alloc]init]];
    [self presentViewController:scanningOperationViewControllerNav animated:YES completion:nil];
}

//在线计算
- (void)onClickCalculation:(id)sender {
    UINavigationController *calculateViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STCalculateViewController alloc]init]];
    [self presentViewController:calculateViewControllerNav animated:YES completion:nil];
}

@end
