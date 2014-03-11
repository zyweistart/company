//
//  STChartElectricityValViewController.m
//  ElectricianRun
//
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartElectricityValViewController.h"
//保存最近12个每条进出线的负荷数
extern double allTotalElectricityVal[12][2][4];
@interface STChartElectricityValViewController ()

@end

@implementation STChartElectricityValViewController{
    NSTimer *timer;
}


- (id)initWithIndex:(NSUInteger)index
{
    self = [super init];
    if (self) {
        self.title=@"进出线电费柱状图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        _currentIndex=index;
        CGRect chartFrame				= CGRectMake(0.0,
                                                     0.0,
                                                     300.0,
                                                     300.0);
        _chart							= [[SimpleBarChart alloc] initWithFrame:chartFrame];
        _chart.center					= CGPointMake(self.view.frame.size.width / 2.0, self.view.frame.size.height / 2.0);
        _chart.delegate					= self;
        _chart.dataSource				= self;
        _chart.barShadowOffset			= CGSizeMake(2.0, 1.0);
        _chart.animationDuration		= 1.0;
        _chart.barShadowColor			= [UIColor grayColor];
        _chart.barShadowAlpha			= 0.5;
        _chart.barShadowRadius			= 1.0;
        _chart.barWidth					= 18.0;
        _chart.xLabelType				= SimpleBarChartXLabelTypeVerticle;
        _chart.incrementValue			= 10;
        _chart.barTextType				= SimpleBarChartBarTextTypeTop;
        _chart.barTextColor				= [UIColor whiteColor];
        _chart.gridColor				= [UIColor grayColor];
        
        [self.view addSubview:_chart];
        [self changeValue];
        timer = [NSTimer scheduledTimerWithTimeInterval:60 target:self selector:@selector(changeValue) userInfo:nil repeats:YES];
    }
    return self;
}

- (void)changeValue
{
    _values=@[
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[11][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[10][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[9][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[8][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[7][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[6][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[5][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[4][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[3][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[2][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[1][_currentIndex/4][_currentIndex%4]],
              [NSString stringWithFormat:@"%.2f",allTotalElectricityVal[0][_currentIndex/4][_currentIndex%4]]
              ];
    [_chart reloadData];
}

#pragma mark SimpleBarChartDataSource

- (NSUInteger)numberOfBarsInBarChart:(SimpleBarChart *)barChart
{
	return _values.count;
}

- (CGFloat)barChart:(SimpleBarChart *)barChart valueForBarAtIndex:(NSUInteger)index
{
	return [[_values objectAtIndex:index] floatValue];
}

- (NSString *)barChart:(SimpleBarChart *)barChart textForBarAtIndex:(NSUInteger)index
{
	return [_values objectAtIndex:index];
}

- (NSString *)barChart:(SimpleBarChart *)barChart xLabelForBarAtIndex:(NSUInteger)index
{
	return [_values objectAtIndex:index];
}

- (UIColor *)barChart:(SimpleBarChart *)barChart colorForBarAtIndex:(NSUInteger)index
{
	return [UIColor blueColor];
}

@end
