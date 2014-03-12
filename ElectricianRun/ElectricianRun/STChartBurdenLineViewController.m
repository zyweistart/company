//
//  STChartBurdenLineViewController.m
//  ElectricianRun
//  进出线负荷曲线图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartBurdenLineViewController.h"
//保存最近12个每条进出线的负荷数
extern double allTotalBurden[12][2][4];

@interface STChartBurdenLineViewController ()

@end

@implementation STChartBurdenLineViewController {
    NSTimer *timer;
    int currentIndex;
}

#pragma mark - View Lifecycle
- (id)initWithIndex:(int)index
{
    self=[super init];
    if(self){
        self.title=@"进出线负荷曲线图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        currentIndex=index;
        
        self.ArrayOfValues=[[NSMutableArray alloc]init];
        self.ArrayOfDates=[[NSMutableArray alloc]init];
        
        self.myGraph = [[BEMSimpleLineGraphView alloc] initWithFrame:CGRectMake(10, 60, 300, 300)];
        self.myGraph.delegate = self;
        [self.view addSubview:self.myGraph];
        
        self.myGraph.enableTouchReport=YES;
        self.myGraph.colorTop = [UIColor whiteColor];
        self.myGraph.colorBottom = [UIColor whiteColor];
        self.myGraph.colorLine = [UIColor blueColor];
        self.myGraph.colorXaxisLabel = [UIColor blueColor];
        self.myGraph.widthLine = 3.0;
        
        self.lblCurrentValue=[[UILabel alloc]initWithFrame:CGRectMake(10, 370, 300, 30)];
        self.lblCurrentValue.font=[UIFont systemFontOfSize:15.0];
        [self.lblCurrentValue setTextColor:[UIColor blackColor]];
        [self.lblCurrentValue setBackgroundColor:[UIColor clearColor]];
        [self.lblCurrentValue setTextAlignment:NSTextAlignmentLeft];
        [self.view addSubview:self.lblCurrentValue];
        
        [self refreshValue];
        
        [self.lblCurrentValue setText:[NSString stringWithFormat:@"当前负荷值:%.2f",[[self.ArrayOfValues objectAtIndex:0] floatValue]]];
        
        timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(refreshValue) userInfo:nil repeats:YES];
    }
    return self;
}

#pragma mark - Graph Actions

- (void)refreshValue {
    [self.ArrayOfValues removeAllObjects];
    [self.ArrayOfDates removeAllObjects];
    
    for(int i=11;i>=0;i--){
        float f=allTotalBurden[i][currentIndex/4][currentIndex%4]/1000;
        [self.ArrayOfValues addObject:[NSString stringWithFormat:@"%.2f",f]];
        [self.ArrayOfDates addObject:[NSString stringWithFormat:@"%d",i]];
    }
    
    [self.myGraph reloadGraph];
}

#pragma mark - SimpleLineGraph Data Source

- (int)numberOfPointsInGraph {
    return (int)[self.ArrayOfValues count];
}

- (float)valueForIndex:(NSInteger)index {
    return [[self.ArrayOfValues objectAtIndex:index] floatValue];
}

#pragma mark - SimpleLineGraph Delegate

- (int)numberOfGapsBetweenLabels {
    return 0;
}

- (NSString *)labelOnXAxisForIndex:(NSInteger)index {
    return [NSString stringWithFormat:@"%d",index+1];
}

- (void)didTouchGraphWithClosestIndex:(int)index {
    [self.lblCurrentValue setText:[NSString stringWithFormat:@"当前负荷值:%.2f",[[self.ArrayOfValues objectAtIndex:index] floatValue]]];
}

@end
