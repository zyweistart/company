//
//  STIndexViewController.m
//  ElectricianRun
//  首页
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STIndexViewController.h"

#import "ETFoursquareImages.h"

#define IMAGEHEIGHT 160

@interface STIndexViewController ()

@end

@implementation STIndexViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    ETFoursquareImages *foursquareImages = [[ETFoursquareImages alloc] initWithFrame:CGRectMake(0, 0, 320, self.view.frame.size.height-0)];
    [foursquareImages setImagesHeight:IMAGEHEIGHT];
    
    NSArray *images  = [NSArray arrayWithObjects:
                        [UIImage imageNamed:@"horses"],
                        [UIImage imageNamed:@"surfer"],
                        [UIImage imageNamed:@"bridge"], nil];
    
    [foursquareImages setImages:images];
    
    [self.view addSubview:foursquareImages];
    
    foursquareImages.scrollView.contentSize = CGSizeMake(320, IMAGEHEIGHT);
    
    [foursquareImages.pageControl setCurrentPageIndicatorTintColor:[UIColor colorWithRed:(28/255.f) green:(189/255.f) blue:(141/255.f) alpha:1.0]];
    
}

@end
