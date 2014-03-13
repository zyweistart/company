//
//  STChartElectricCurrentLineViewController.m
//  ElectricianRun
//  进出线电流曲线图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartElectricCurrentLineViewController.h"
//保存最近12个每条进出线的电流IA、IB、IC的值
double allPhaseCurrentList[12][2][4][3];

@interface STChartElectricCurrentLineViewController ()

@end

@implementation STChartElectricCurrentLineViewController

- (id)initWithIndex:(NSUInteger)index
{
    self = [super init];
    if (self) {
        self.title=@"进出线电流曲线图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
		
		_lineChartView = [[PCLineChartView alloc] initWithFrame:CGRectMake(10,64,300,300)];
		[_lineChartView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
		_lineChartView.minValue = 0;
        _lineChartView.interval=500;
		_lineChartView.maxValue = 3000;
        _lineChartView.xLabelFont=[UIFont systemFontOfSize:10];
        _lineChartView.yLabelFont=[UIFont systemFontOfSize:10];
        _lineChartView.valueLabelFont=[UIFont systemFontOfSize:10];
        _lineChartView.legendFont=[UIFont systemFontOfSize:10];
		[self.view addSubview:_lineChartView];
        
        NSMutableArray *components = [NSMutableArray array];
        PCLineChartViewComponent *componentia = [[PCLineChartViewComponent alloc] init];
        [componentia setTitle:@"IA"];
        [componentia setPoints:[[NSMutableArray alloc]initWithObjects:
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[11][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[10][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[9][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[8][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[7][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[6][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[5][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[4][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[3][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[2][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[1][index/4][index%4][0]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[0][index/4][index%4][0]], nil]];
        [componentia setShouldLabelValues:NO];
        [componentia setColour:PCColorYellow];
        [components addObject:componentia];
        
        PCLineChartViewComponent *componentib = [[PCLineChartViewComponent alloc] init];
        [componentib setTitle:@"IB"];
        [componentib setPoints:[[NSMutableArray alloc]initWithObjects:
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[11][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[10][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[9][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[8][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[7][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[6][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[5][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[4][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[3][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[2][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[1][index/4][index%4][1]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[0][index/4][index%4][1]], nil]];
        [componentib setShouldLabelValues:NO];
        [componentib setColour:PCColorGreen];
        [components addObject:componentib];
        
        PCLineChartViewComponent *componentic= [[PCLineChartViewComponent alloc] init];
        [componentic setTitle:@"IC"];
        [componentic setPoints:[[NSMutableArray alloc]initWithObjects:
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[11][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[10][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[9][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[8][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[7][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[6][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[5][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[4][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[3][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[2][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[1][index/4][index%4][2]],
                                [NSString stringWithFormat:@"%.2f",allPhaseCurrentList[0][index/4][index%4][2]], nil]];
        [componentic setShouldLabelValues:NO];
        [componentic setColour:PCColorBlue];
        [components addObject:componentic];
        
//        for(int i=0;i<12;i++){
//            NSLog(@"%f",allPhaseCurrentList[i][index/4][index%4][0]);
//        }
        
		[_lineChartView setComponents:components];
		[_lineChartView setXLabels:[[NSMutableArray alloc]initWithObjects:@"1",@"2",@"3",@"4",@"5",@"6",@"7",@"8",@"9",@"10",@"11",@"12", nil]];
        
    }
    return self;
}

@end
