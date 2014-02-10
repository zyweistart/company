//
//  STMyeViewController.m
//  ElectricianRun
//  我的e电工
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STMyeViewController.h"

#import "NavigationControllerDelegate.h"

@interface STMyeViewController ()

@end

@implementation STMyeViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    return self;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIButton *btnF1=[[UIButton alloc]initWithFrame:CGRectMake(5,10, 152.5, 80)];
    btnF1.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF1 setTitle:@"用户体验" forState:UIControlStateNormal];
    [btnF1 setBackgroundColor:[UIColor purpleColor]];
    [btnF1 addTarget:self action:@selector(onClickOperating:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnF1];
    
}

- (void)onClickOperating:(id)sender {
    
    UIViewController *view=[[UIViewController alloc]init];
    [self.navigationController pushViewController:view animated:YES];
}

@end
