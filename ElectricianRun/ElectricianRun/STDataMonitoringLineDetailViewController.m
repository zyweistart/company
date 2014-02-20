//
//  STDataMonitoringLineDetailViewController.m
//  ElectricianRun
//  数据监测-线路-详细
//  Created by Start on 1/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STDataMonitoringLineDetailViewController.h"
#import "STDataMonitoringLineDetailListViewController.h"

@interface STDataMonitoringLineDetailViewController ()

@end

@implementation STDataMonitoringLineDetailViewController

- (id)initWithData:(NSDictionary *)data
{
    self = [super init];
    if (self) {
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.data=data;
        
        UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 80, 320, 340)];
        
        UIButton *btnHistory1=[[UIButton alloc]initWithFrame:CGRectMake(80, 5, 80, 30)];
        [btnHistory1 setTitle:@"历史耗量" forState:UIControlStateNormal];
        [btnHistory1 setBackgroundColor:[UIColor blueColor]];
        [btnHistory1 addTarget:self action:@selector(history1:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory1];
        
        UIButton *btnHistory2=[[UIButton alloc]initWithFrame:CGRectMake(160, 5, 80, 30)];
        [btnHistory2 setTitle:@"历史数据" forState:UIControlStateNormal];
        [btnHistory2 setBackgroundColor:[UIColor blueColor]];
        [btnHistory2 addTarget:self action:@selector(history2:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory2];
        
        UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(75, 40, 80, 20)];
        [lbl1 setTextAlignment:NSTextAlignmentRight];
        [lbl1 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl1 setText:@"线路名称:"];
        [lbl1 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl1];
        
        UILabel *lblV1=[[UILabel alloc]initWithFrame:CGRectMake(160, 40, 120, 20)];
        [lblV1 setTextAlignment:NSTextAlignmentLeft];
        [lblV1 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV1 setText:[self.data objectForKey:@"METER_NAME"]];
        [lblV1 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV1];
        
        UILabel *lbl2=[[UILabel alloc]initWithFrame:CGRectMake(75, 65, 80, 20)];
        [lbl2 setTextAlignment:NSTextAlignmentRight];
        [lbl2 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl2 setText:@"上线时间:"];
        [lbl2 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl2];
        
        UILabel *lblV2=[[UILabel alloc]initWithFrame:CGRectMake(160, 65, 120, 20)];
        [lblV2 setTextAlignment:NSTextAlignmentLeft];
        [lblV2 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV2 setText:[self.data objectForKey:@"REPORT_DATE"]];
        [lblV2 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV2];
        
        UILabel *lbl3=[[UILabel alloc]initWithFrame:CGRectMake(75, 90, 80, 20)];
        [lbl3 setTextAlignment:NSTextAlignmentRight];
        [lbl3 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl3 setText:@"开关状态:"];
        [lbl3 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl3];
        
        UILabel *lblV3=[[UILabel alloc]initWithFrame:CGRectMake(160, 90, 120, 20)];
        [lblV3 setTextAlignment:NSTextAlignmentLeft];
        [lblV3 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV3 setText:[self.data objectForKey:@"I_A"]];
        [lblV3 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV3];
        
        UILabel *lbl4=[[UILabel alloc]initWithFrame:CGRectMake(75, 115, 80, 20)];
        [lbl4 setTextAlignment:NSTextAlignmentRight];
        [lbl4 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl4 setText:@"日耗量:"];
        [lbl4 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl4];
        
        UILabel *lblV4=[[UILabel alloc]initWithFrame:CGRectMake(160, 115, 120, 20)];
        [lblV4 setTextAlignment:NSTextAlignmentLeft];
        [lblV4 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV4 setText:[self.data objectForKey:@"I_A"]];
        [lblV4 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV4];
        
        UILabel *lbl5=[[UILabel alloc]initWithFrame:CGRectMake(75, 140, 80, 20)];
        [lbl5 setTextAlignment:NSTextAlignmentRight];
        [lbl5 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl5 setText:@"A相电压:"];
        [lbl5 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl5];
        
        UILabel *lblV5=[[UILabel alloc]initWithFrame:CGRectMake(160, 140, 120, 20)];
        [lblV5 setTextAlignment:NSTextAlignmentLeft];
        [lblV5 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV5 setText:[self.data objectForKey:@"V_A"]];
        [lblV5 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV5];
        
        UILabel *lbl6=[[UILabel alloc]initWithFrame:CGRectMake(75, 165, 80, 20)];
        [lbl6 setTextAlignment:NSTextAlignmentRight];
        [lbl6 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl6 setText:@"B相电压:"];
        [lbl6 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl6];
        
        UILabel *lblV6=[[UILabel alloc]initWithFrame:CGRectMake(160, 165, 120, 20)];
        [lblV6 setTextAlignment:NSTextAlignmentLeft];
        [lblV6 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV6 setText:[self.data objectForKey:@"V_B"]];
        [lblV6 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV6];
        
        UILabel *lbl7=[[UILabel alloc]initWithFrame:CGRectMake(75, 190, 80, 20)];
        [lbl7 setTextAlignment:NSTextAlignmentRight];
        [lbl7 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl7 setText:@"C相电压:"];
        [lbl7 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl7];
        
        UILabel *lblV7=[[UILabel alloc]initWithFrame:CGRectMake(160, 190, 120, 20)];
        [lblV7 setTextAlignment:NSTextAlignmentLeft];
        [lblV7 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV7 setText:[self.data objectForKey:@"V_C"]];
        [lblV7 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV7];
        
        UILabel *lbl8=[[UILabel alloc]initWithFrame:CGRectMake(75, 215, 80, 20)];
        [lbl8 setTextAlignment:NSTextAlignmentRight];
        [lbl8 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl8 setText:@"A相电流:"];
        [lbl8 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl8];
        
        UILabel *lblV8=[[UILabel alloc]initWithFrame:CGRectMake(160, 215, 120, 20)];
        [lblV8 setTextAlignment:NSTextAlignmentLeft];
        [lblV8 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV8 setText:[self.data objectForKey:@"I_A"]];
        [lblV8 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV8];
        
        UILabel *lbl9=[[UILabel alloc]initWithFrame:CGRectMake(75, 240, 80, 20)];
        [lbl9 setTextAlignment:NSTextAlignmentRight];
        [lbl9 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl9 setText:@"B相电流:"];
        [lbl9 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl9];
        
        UILabel *lblV9=[[UILabel alloc]initWithFrame:CGRectMake(160, 240, 120, 20)];
        [lblV9 setTextAlignment:NSTextAlignmentLeft];
        [lblV9 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV9 setText:[self.data objectForKey:@"I_B"]];
        [lblV9 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV9];
        
        UILabel *lbl10=[[UILabel alloc]initWithFrame:CGRectMake(75, 265, 80, 20)];
        [lbl10 setTextAlignment:NSTextAlignmentRight];
        [lbl10 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl10 setText:@"C相电流:"];
        [lbl10 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl10];
        
        UILabel *lblV10=[[UILabel alloc]initWithFrame:CGRectMake(160, 265, 120, 20)];
        [lblV10 setTextAlignment:NSTextAlignmentLeft];
        [lblV10 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV10 setText:[self.data objectForKey:@"I_C"]];
        [lblV10 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV10];
        
        UILabel *lbl11=[[UILabel alloc]initWithFrame:CGRectMake(75, 290, 80, 20)];
        [lbl11 setTextAlignment:NSTextAlignmentRight];
        [lbl11 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl11 setText:@"总有功功率:"];
        [lbl11 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl11];
        
        UILabel *lblV11=[[UILabel alloc]initWithFrame:CGRectMake(160, 290, 120, 20)];
        [lblV11 setTextAlignment:NSTextAlignmentLeft];
        [lblV11 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV11 setText:[self.data objectForKey:@"P_POWER"]];
        [lblV11 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV11];
        
        UILabel *lbl12=[[UILabel alloc]initWithFrame:CGRectMake(75, 315, 80, 20)];
        [lbl12 setTextAlignment:NSTextAlignmentRight];
        [lbl12 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl12 setText:@"总功率因数:"];
        [lbl12 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl12];
        
        UILabel *lblV12=[[UILabel alloc]initWithFrame:CGRectMake(160, 315, 120, 20)];
        [lblV12 setTextAlignment:NSTextAlignmentLeft];
        [lblV12 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV12 setText:[self.data objectForKey:@"FACTOR"]];
        [lblV12 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV12];
        
        [self.view addSubview:control];
    }
    return self;
}

- (void)history1:(id)sender {
    STDataMonitoringLineDetailListViewController *dataMonitoringLineDetailListViewController=[[STDataMonitoringLineDetailListViewController alloc]init];
    [self.navigationController pushViewController:dataMonitoringLineDetailListViewController animated:YES];
}

- (void)history2:(id)sender {
    STDataMonitoringLineDetailListViewController *dataMonitoringLineDetailListViewController=[[STDataMonitoringLineDetailListViewController alloc]init];
    [self.navigationController pushViewController:dataMonitoringLineDetailListViewController animated:YES];
}

@end
