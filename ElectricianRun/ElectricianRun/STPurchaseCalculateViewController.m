//
//  STPurchaseCalculateViewController.m
//  ElectricianRun
//
//  Created by Start on 3/4/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STPurchaseCalculateViewController.h"

@interface STPurchaseCalculateViewController ()

@end

@implementation STPurchaseCalculateViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        self.title=@"在线报价单";
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                                initWithTitle:@"购买"
                                                style:UIBarButtonItemStyleBordered
                                                target:self
                                                action:@selector(buy:)];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)buy:(id)sender
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http:/www.baidu.com"]];
}

@end
