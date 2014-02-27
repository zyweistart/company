//
//  STChartElectricityPieViewController.m
//  ElectricianRun
//  进出线尖峰谷进线电量饼图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartElectricityPieViewController.h"

@interface STChartElectricityPieViewController ()

@end

@implementation STChartElectricityPieViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"进出线尖峰谷进线电量饼图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

@end
