//
//  STIndexViewController.m
//  ElectricianRun
//  首页
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STIndexViewController.h"

#import "ETFoursquareImages.h"
#import "NSString+Utils.h"

#define IMAGEHEIGHT 160

@interface STIndexViewController ()

@end

@implementation STIndexViewController{
    HttpRequest *_hRequest;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    ETFoursquareImages *foursquareImages = [[ETFoursquareImages alloc] initWithFrame:CGRectMake(0, 0, 320, self.view.frame.size.height-0)];
//    [foursquareImages setImagesHeight:IMAGEHEIGHT];
//    
//    NSArray *images  = [NSArray arrayWithObjects:
//                        [UIImage imageNamed:@"horses"],
//                        [UIImage imageNamed:@"surfer"],
//                        [UIImage imageNamed:@"bridge"], nil];
//    
//    [foursquareImages setImages:images];
//    [foursquareImages setBackgroundColor:[UIColor redColor]];
//    
//    [self.view addSubview:foursquareImages];
//    
//    foursquareImages.scrollView.contentSize = CGSizeMake(320, IMAGEHEIGHT);
//    
//    [foursquareImages.pageControl setCurrentPageIndicatorTintColor:[UIColor colorWithRed:(28/255.f) green:(189/255.f) blue:(141/255.f) alpha:1.0]];
    
}

- (IBAction)onClick:(id)sender {
    
    NSString *URL=@"checkMobileValid.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"2" forKey:@"Type"];
    [p setObject:@"2" forKey:@"IsEncode"];
    
    _hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [_hRequest setIsShowMessage:YES];
    [_hRequest start:URL params:p];
    
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    NSLog(@"json:%@",[response resultJSON]);
}

- (void)requestFailed:(int)repCode didFailWithError:(NSError *)error{
    NSLog(@"requestFailed");
}

@end
