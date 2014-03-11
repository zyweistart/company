//
//  STChartElectricityViewController.h
//  ElectricianRun
//
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SimpleBarChart.h"

@interface STChartElectricityViewController : UIViewController<SimpleBarChartDataSource, SimpleBarChartDelegate>
{
	NSArray *_values;
    
	SimpleBarChart *_chart;
}

@property NSUInteger currentIndex;

- (id)initWithIndex:(NSUInteger)index;

@end