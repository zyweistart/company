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
        [self.view setBackgroundColor:[UIColor whiteColor]];
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
    }
    return self;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIView *view=[[UIView alloc]initWithFrame:CGRectMake(0, 64, 320, 300)];
    [self.view addSubview:view];
    
    UIButton *btn1=[[UIButton alloc]initWithFrame:CGRectMake(100, 100, 150, 30)];
    [btn1 setTitle:@"报警体验" forState:UIControlStateNormal];
    [btn1 setBackgroundColor:[UIColor blueColor]];
    [btn1 addTarget:self action:@selector(alarm:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:btn1];
    
    UIButton *btn2=[[UIButton alloc]initWithFrame:CGRectMake(100, 150, 150, 30)];
    [btn2 setTitle:@"商业用户" forState:UIControlStateNormal];
    [btn2 setBackgroundColor:[UIColor blueColor]];
    [btn2 addTarget:self action:@selector(business:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:btn2];
    
    UIButton *btn3=[[UIButton alloc]initWithFrame:CGRectMake(100, 200, 150, 30)];
    [btn3 setTitle:@"大工业用户" forState:UIControlStateNormal];
    [btn3 setBackgroundColor:[UIColor blueColor]];
    [btn3 addTarget:self action:@selector(industrial:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:btn3];
    
}

- (void)alarm:(id)sender
{
    UIStoryboard *storyboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    STUserExperienceAlarmViewController *stuea=[storyboard instantiateViewControllerWithIdentifier:@"STUserExperienceAlarmViewController"];
    UINavigationController *experienceAlarmViewControllerNav = [[UINavigationController alloc] initWithRootViewController:stuea];
    [self presentViewController:experienceAlarmViewControllerNav animated:YES completion:nil];
}

- (void)business:(id)sender
{
    UIStoryboard *storyboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    STUserExperienceViewController *stuea=[storyboard instantiateViewControllerWithIdentifier:@"STUserExperienceViewController"];
    UINavigationController *userExperienceViewController = [[UINavigationController alloc] initWithRootViewController:stuea];
    [self presentViewController:userExperienceViewController animated:YES completion:nil];
}

- (void)industrial:(id)sender
{
    UIStoryboard *storyboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    STUserExperienceViewController *stuea=[storyboard instantiateViewControllerWithIdentifier:@"STUserExperienceViewController"];
    UINavigationController *userExperienceViewController = [[UINavigationController alloc] initWithRootViewController:stuea];
    [self presentViewController:userExperienceViewController animated:YES completion:nil];
}

@end
