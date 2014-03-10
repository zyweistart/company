//
//  STGPSSearchViewController.m
//  ElectricianRun
//
//  Created by Start on 3/9/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STGPSSearchViewController.h"
#import "DatePickerView.h"

@interface STGPSSearchViewController () <DatePickerViewDelegate,UITextFieldDelegate>

@end

@implementation STGPSSearchViewController {
    DatePickerView *datePicker;
    UITextField *txtValueView1;
    UITextField *txtValueView2;
    UITextField *txtValueView3;
    UITextField *txtValueView4;
}

- (id)initWitData:(NSMutableDictionary*)data
{
    self = [super init];
    if (self) {
        self.title=@"GPS轨迹查看";
        self.searchData=data;
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 300)];
    [self.view addSubview:control];
    
    datePicker = [[DatePickerView alloc] init];
    [datePicker setDelegate:self];
    
    ///////////////////////
    NSDateFormatter *dateFormatter =[[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSDate *startDate=[NSDate date];
    NSString *startDateStr = [dateFormatter stringFromDate:startDate];
    NSTimeInterval secondsPerDay = 86400*30;
    NSDate *endDate = [startDate dateByAddingTimeInterval:-secondsPerDay];
    NSString *endDateStr = [dateFormatter stringFromDate:endDate];
    
    UIControl *view2=[[UIControl alloc]initWithFrame:CGRectMake(0, 80, 320, 210)];
    [view2 setHidden:YES];
    [view2 addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [control addSubview:view2];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"手机号码"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [view2 addSubview:lbl];
    
    txtValueView2=[[UITextField alloc]initWithFrame:CGRectMake(105, 50, 150, 30)];
    [txtValueView2 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueView2 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueView2 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueView2 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueView2 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [view2 addSubview:txtValueView2];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 90, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"开始时间"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [view2 addSubview:lbl];
    
    txtValueView3=[[UITextField alloc]initWithFrame:CGRectMake(105, 90, 150, 30)];
    [txtValueView3 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueView3 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueView3 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueView3 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueView3 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueView3 setDelegate:self];
    [txtValueView3 setText:endDateStr];
    [txtValueView3 setInputView:datePicker];
    [view2 addSubview:txtValueView3];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 130, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"结束时间"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [view2 addSubview:lbl];
    
    txtValueView4=[[UITextField alloc]initWithFrame:CGRectMake(105, 130, 150, 30)];
    [txtValueView4 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueView4 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueView4 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueView4 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueView4 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueView4 setDelegate:self];
    [txtValueView4 setText:startDateStr];
    [txtValueView4 setInputView:datePicker];
    [view2 addSubview:txtValueView4];
    
    //查询
    UIButton *btnSearch=[[UIButton alloc]initWithFrame:CGRectMake(110, 170, 100, 30)];
    [btnSearch setTitle:@"查询" forState:UIControlStateNormal];
    btnSearch.titleLabel.font=[UIFont systemFontOfSize: 12.0];
    [btnSearch  setBackgroundColor:[UIColor colorWithRed:(55/255.0) green:(55/255.0) blue:(139/255.0) alpha:1]];
    [btnSearch addTarget:self action:@selector(search:) forControlEvents:UIControlEventTouchUpInside];
    [view2 addSubview:btnSearch];
    [self.view addSubview:view2];
}

- (void)backgroundDoneEditing:(id)sender
{
    
}

- (void)search:(id)sender
{
    NSLog(@"搜索");
}

@end
