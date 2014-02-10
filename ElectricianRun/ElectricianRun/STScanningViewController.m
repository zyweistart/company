//
//  STScanningViewController.m
//  ElectricianRun
//  扫描操作
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STScanningViewController.h"

@interface STScanningViewController ()

@end

@implementation STScanningViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"扫描操作";
        
        self.navigationItem.title=@"用户注册";
    }
    return self;
}

- (void)viewDidLoad
{
    //隐藏顶部bar栏
    self.navigationController.navigationBarHidden=NO;
    
    UIButton *btnF5=[[UIButton alloc]initWithFrame:CGRectMake(162.7,95, 73.85, 80)];
    btnF5.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF5 setTitle:@"扫描操作" forState:UIControlStateNormal];
    [btnF5 setBackgroundColor:[UIColor redColor]];
    [self.view addSubview:btnF5];
    
    [super viewDidLoad];
}

@end
