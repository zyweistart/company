//
//  STProjectSiteViewController.m
//  ElectricianRun
//  工程建站
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STProjectSiteViewController.h"
#import "STScanningViewController.h"

@interface STProjectSiteViewController ()

@end

@implementation STProjectSiteViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	UIButton * scanButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [scanButton setTitle:@"扫描" forState:UIControlStateNormal];
    scanButton.frame = CGRectMake(100, 100, 120, 40);
    [scanButton addTarget:self action:@selector(setupCamera) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:scanButton];
    
}
-(void)setupCamera
{
    STScanningViewController * rt = [[STScanningViewController alloc]init];
    [self presentViewController:rt animated:YES completion:^{
        
    }];
}

@end
