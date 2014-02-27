//
//  STChartElectricityViewController.m
//  ElectricianRun
//  进出线电耗量柱状图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartElectricityViewController.h"

//保存最近12个每条进出线的负荷数
extern double allTotalBurden[12][2][4];

@interface STChartElectricityViewController ()

@end

@implementation STChartElectricityViewController


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"进出线电耗量柱状图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

@end
