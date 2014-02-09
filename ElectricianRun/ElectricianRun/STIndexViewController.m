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
#import "HttpRequest.h"

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
//    [p setObject:@"imei" forKey:@"zhangyy"];
//    [p setObject:@"authentication" forKey:[@"8888AA" md5]];
//    [p setObject:@"Type" forKey:@"2"];
//    [p setObject:@"IsEncode" forKey:@"2"];

    NSMutableString *urlString=[[NSMutableString alloc]initWithString:@"http://122.224.247.221:7003/WEB/mobile/"];
    [urlString appendString:URL];
    
    if(p!=nil&&[p count]>0){
        [urlString appendString:@"?"];
        
        for(NSString *key in p){
            [urlString appendFormat:@"%@=%@&",key,[p objectForKey:key]];
        }
        
    }
    NSLog(@"%@",urlString);
    
//    _hRequest=[[HttpRequest alloc]init];
//    [_hRequest start:URL params:p];
    
}
@end
