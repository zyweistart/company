//
//  STUserExperienceSelectViewController.m
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceSelectViewController.h"
#import "STUserExperienceAlarmViewController.h"
#import "STUserExperienceViewController.h"

@interface STUserExperienceSelectViewController ()

@end

@implementation STUserExperienceSelectViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"用户体验";
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    float height=self.view.frame.size.height-64;
    self.automaticallyAdjustsScrollViewInsets=NO;
    UIScrollView *control=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 64, 320, height)];
    control.contentSize = CGSizeMake(640,height);
    [control setScrollEnabled:YES];
    UIView *firstView=[[UIView alloc]initWithFrame:CGRectMake(0, 0, 320, height)];
    UIButton *btnAlarm=[[UIButton alloc]initWithFrame:CGRectMake(100, 100, 150, 30)];
    [btnAlarm setTitle:@"报警体验" forState:UIControlStateNormal];
    [btnAlarm setBackgroundColor:[UIColor blueColor]];
    [btnAlarm addTarget:self action:@selector(alarm:) forControlEvents:UIControlEventTouchUpInside];
    [firstView addSubview:btnAlarm];
    [control addSubview:firstView];
    
    UIView *secondView=[[UIView alloc]initWithFrame:CGRectMake(320, 0, 320, height)];
    UIButton *btnBusiness=[[UIButton alloc]initWithFrame:CGRectMake(100, 150, 150, 30)];
    [btnBusiness setTitle:@"商业用户" forState:UIControlStateNormal];
    [btnBusiness setBackgroundColor:[UIColor blueColor]];
    [btnBusiness addTarget:self action:@selector(business:) forControlEvents:UIControlEventTouchUpInside];
    [secondView addSubview:btnBusiness];

    UIButton *btnIndustrial=[[UIButton alloc]initWithFrame:CGRectMake(100, 200, 150, 30)];
    [btnIndustrial setTitle:@"大工业用户" forState:UIControlStateNormal];
    [btnIndustrial setBackgroundColor:[UIColor blueColor]];
    [btnIndustrial addTarget:self action:@selector(industrial:) forControlEvents:UIControlEventTouchUpInside];
    [secondView addSubview:btnIndustrial];
    [control addSubview:secondView];
    
    [self.view addSubview:control];
}

- (void)alarm:(id)sender
{
    UINavigationController *userExperienceAlarmViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceAlarmViewController alloc]init]];
    [self presentViewController:userExperienceAlarmViewControllerNav animated:YES completion:nil];
}

- (void)business:(id)sender
{
    UINavigationController *userExperienceViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceViewController alloc]initWithUserType:2]];
    [self presentViewController:userExperienceViewControllerNav animated:YES completion:nil];
}

- (void)industrial:(id)sender
{
    UINavigationController *userExperienceViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceViewController alloc]initWithUserType:1]];
    [self presentViewController:userExperienceViewControllerNav animated:YES completion:nil];
}

@end
