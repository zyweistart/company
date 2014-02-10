//
//  STIndexViewController.m
//  ElectricianRun
//  首页
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STIndexViewController.h"
#import "NavigationControllerDelegate.h"
#import "STScanningViewController.h"

#import "ETFoursquareImages.h"
#import "NSString+Utils.h"

#define IMAGEHEIGHT 200

@interface STIndexViewController ()

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

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:YES];
    //隐藏顶部bar栏
    self.navigationController.navigationBarHidden=YES;
}

//用户体验
- (void)onClickUserExperience:(id)sender {
    NSLog(@"用户体验");
}
//我管辖的变电站
- (void)onClickJurisdiction:(id)sender {
    NSLog(@"我管辖的变电站");
}
//调试工具
- (void)onClickDebug:(id)sender {
    NSLog(@"调试工具");
}
//工程建站
- (void)onClickSite:(id)sender {
    NSLog(@"工程建站");
}
//扫描操作
- (void)onClickOperating:(id)sender {
    STScanningViewController *scanningViewController=[[STScanningViewController alloc]init];
//    [self.navigationController pushViewController:scanningViewController animated:YES];
    
    UIViewController *view=[[UIViewController alloc]init];
    [self.navigationController pushViewController:view animated:YES];
}
//在线计算
- (void)onClickCalculation:(id)sender {
    NSLog(@"在线计算");
}

//- (IBAction)onClick:(id)sender {
//    
//    NSString *URL=@"checkMobileValid.aspx";
//    
//    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
//    [p setObject:@"zhangyy" forKey:@"imei"];
//    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
//    [p setObject:@"2" forKey:@"Type"];
//    [p setObject:@"2" forKey:@"IsEncode"];
//    
//    _hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
//    [_hRequest setIsShowMessage:YES];
//    [_hRequest start:URL params:p];
//    
//}
//
//- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
//    NSLog(@"json:%@",[response resultJSON]);
//}
//
//- (void)requestFailed:(int)repCode didFailWithError:(NSError *)error{
//    NSLog(@"requestFailed");
//}

@end
