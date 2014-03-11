//
//  STChartElectricityPieViewController.m
//  ElectricianRun
//  进出线尖峰谷进线电量饼图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartElectricityPieViewController.h"

#import "Example2PieView.h"
#import "MyPieElement.h"
#import "PieLayer.h"

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

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    float a=4000;
    float b=720;
    float c=7200;
    
    Example2PieView *pieView=[[Example2PieView alloc]initWithFrame:CGRectMake(10 ,80, 300, 300)];
    [pieView setBackgroundColor:[UIColor whiteColor]];
    [self.view addSubview:pieView];
    
    MyPieElement* elem = [MyPieElement pieElementWithValue:(a) color:[UIColor greenColor]];
    elem.title = @"谷电量";
    [pieView.layer addValues:@[elem] animated:NO];
    
    MyPieElement* elem1 = [MyPieElement pieElementWithValue:(b) color:[UIColor blueColor]];
    elem1.title = @"尖电量";
    [pieView.layer addValues:@[elem1] animated:NO];
    
    MyPieElement* elem2 = [MyPieElement pieElementWithValue:(c) color:[UIColor redColor]];
    elem2.title = @"峰电量";
    [pieView.layer addValues:@[elem2] animated:NO];
    
    pieView.layer.transformTitleBlock = ^(PieElement* elem){
        return [(MyPieElement*)elem title];
    };
    pieView.layer.showTitles = ShowTitlesAlways;
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 350, 200, 20)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:[NSString stringWithFormat:@"谷电量：%.2f",a]];
    [lbl setTextColor:[UIColor greenColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentLeft];
    [self.view addSubview:lbl];
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 375, 200, 20)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:[NSString stringWithFormat:@"尖电量：%.2f",b]];
    [lbl setTextColor:[UIColor blueColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentLeft];
    [self.view addSubview:lbl];
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 400, 200, 20)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:[NSString stringWithFormat:@"峰电量：%.2f",c]];
    [lbl setTextColor:[UIColor redColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentLeft];
    [self.view addSubview:lbl];
    
}


@end
