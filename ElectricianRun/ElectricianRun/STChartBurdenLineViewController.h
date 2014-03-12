//
//  STChartBurdenLineViewController.h
//  ElectricianRun
//
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BEMSimpleLineGraphView.h"

@interface STChartBurdenLineViewController : UIViewController<BEMSimpleLineGraphDelegate>
@property (strong,nonatomic) UILabel *lblCurrentValue;
@property (strong, nonatomic) BEMSimpleLineGraphView *myGraph;

@property (strong, nonatomic) NSMutableArray *ArrayOfValues;
@property (strong, nonatomic) NSMutableArray *ArrayOfDates;


- (id)initWithIndex:(int)index;

@end

