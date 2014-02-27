//
//  STChartBurdenLineViewController.m
//  ElectricianRun
//  进出线负荷曲线图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartBurdenLineViewController.h"

@interface STChartBurdenLineViewController ()

@end

@implementation STChartBurdenLineViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"进出线负荷曲线图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

@end
