//
//  STChartElectricityValPieViewController.m
//  ElectricianRun
//  进出线尖峰谷进线电费饼图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartElectricityValPieViewController.h"

#import "Example2PieView.h"
#import "MyPieElement.h"
#import "PieLayer.h"

@interface STChartElectricityValPieViewController ()

@end

@implementation STChartElectricityValPieViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"进出线尖峰谷进线电费饼图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}


- (void)buildUI
{
    
    float a=4000;
    float b=720;
    float c=7200;
    
    if(_index==1||_index==5){
        a=a*0.2;
        b=b*0.2;
        c=c*0.2;
    }else if(_index==2||_index==6){
        a=a*0.3;
        b=b*0.3;
        c=c*0.3;
    }else if(_index==3||_index==7){
        a=a*0.5;
        b=b*0.5;
        c=c*0.5;
    }
    
    Example2PieView *pieView=[[Example2PieView alloc]initWithFrame:CGRectMake(10 ,80, 300, 300)];
    [pieView setBackgroundColor:[UIColor whiteColor]];
    [self.view addSubview:pieView];
    
    MyPieElement* elem = [MyPieElement pieElementWithValue:(a) color:[UIColor colorWithRed:(17/255.0) green:(69/255.0) blue:(202/255.0) alpha:1]];
    elem.title = @"谷电量";
    [pieView.layer addValues:@[elem] animated:NO];
    
    MyPieElement* elem1 = [MyPieElement pieElementWithValue:(b) color:[UIColor colorWithRed:(42/255.0) green:(180/255.0) blue:(13/255.0) alpha:1]];
    elem1.title = @"尖电量";
    [pieView.layer addValues:@[elem1] animated:NO];
    
    MyPieElement* elem2 = [MyPieElement pieElementWithValue:(c) color:[UIColor colorWithRed:(241/255.0) green:(2/255.0) blue:(2/255.0) alpha:1]];
    elem2.title = @"峰电量";
    [pieView.layer addValues:@[elem2] animated:NO];
    
    pieView.layer.transformTitleBlock = ^(PieElement* elem){
        return [(MyPieElement*)elem title];
    };
    pieView.layer.showTitles = ShowTitlesAlways;
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 350, 200, 20)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:[NSString stringWithFormat:@"谷电量：%.2f",a]];
    [lbl setTextColor:[UIColor colorWithRed:(17/255.0) green:(69/255.0) blue:(202/255.0) alpha:1]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentLeft];
    [self.view addSubview:lbl];
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 375, 200, 20)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:[NSString stringWithFormat:@"尖电量：%.2f",b]];
    [lbl setTextColor:[UIColor colorWithRed:(42/255.0) green:(180/255.0) blue:(13/255.0) alpha:1]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentLeft];
    [self.view addSubview:lbl];
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 400, 200, 20)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:[NSString stringWithFormat:@"峰电量：%.2f",c]];
    [lbl setTextColor:[UIColor colorWithRed:(241/255.0) green:(2/255.0) blue:(2/255.0) alpha:1]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentLeft];
    [self.view addSubview:lbl];
    
}

@end
