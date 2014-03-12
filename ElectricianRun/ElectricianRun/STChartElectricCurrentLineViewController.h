//
//  STChartElectricCurrentLineViewController.h
//  ElectricianRun
//
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PCLineChartView.h"
@interface STChartElectricCurrentLineViewController : UIViewController

@property (nonatomic, strong) PCLineChartView *lineChartView;

- (id)initWithIndex:(NSUInteger)index;
@end
