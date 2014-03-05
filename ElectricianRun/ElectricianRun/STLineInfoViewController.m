//
//  STLineInfoViewController.m
//  ElectricianRun
//  线路最新信息
//  Created by Start on 2/21/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STLineInfoViewController.h"

#import "NSString+Utils.h"

@interface STLineInfoViewController ()

@end

@implementation STLineInfoViewController{
    UILabel *lblV1;
    UILabel *lblV2;
    UILabel *lblV3;
    UILabel *lblV4;
    UILabel *lblV5;
    UILabel *lblV6;
    UILabel *lblV7;
    UILabel *lblV8;
    UILabel *lblV9;
    UILabel *lblV10;
    UILabel *lblV11;
    UILabel *lblV12;
    UILabel *lblV13;
    UILabel *lblV14;
    UILabel *lblV15;
    UILabel *lblV16;
    UILabel *lblV17;
    UILabel *lblV18;
    UILabel *lblV19;
    UILabel *lblV20;
    UILabel *lblV21;
    UILabel *lblV22;
    UILabel *lblV23;
}

- (id)initWithSerialNo:(NSString*)sno channelNo:(NSString*)cno {
    self = [super init];
    if (self) {
        self.title=@"线路最新信息";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        self.serialNo=sno;
        self.channelNo=cno;
        
        [self reloadDataSource];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(0, 64, 320, 50)];
    [lbl setTextAlignment:NSTextAlignmentCenter];
    [lbl setFont:[UIFont systemFontOfSize:16.0]];
    [lbl setText:@"0301 1601 0008 | 33"];
    [lbl setTextColor:[UIColor blackColor]];
    [self.view addSubview:lbl];
    
    UIView *view=[[UIView alloc]initWithFrame:CGRectMake(0, 114, 320, 400)];
    [self.view addSubview:view];
    
    UIScrollView *control=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, 320, 400)];
    control.contentSize = CGSizeMake(320,580);
    [control setScrollEnabled:YES];
    [view addSubview:control];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 5, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"线路名称:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV1=[[UILabel alloc]initWithFrame:CGRectMake(155, 5, 120, 20)];
    [lblV1 setTextAlignment:NSTextAlignmentLeft];
    [lblV1 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV1 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV1];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 30, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"线路编号:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV2=[[UILabel alloc]initWithFrame:CGRectMake(155, 30, 120, 20)];
    [lblV2 setTextAlignment:NSTextAlignmentLeft];
    [lblV2 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV2 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV2];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 55, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"开关编号:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV3=[[UILabel alloc]initWithFrame:CGRectMake(155, 55, 120, 20)];
    [lblV3 setTextAlignment:NSTextAlignmentLeft];
    [lblV3 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV3 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV3];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 80, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"负荷等级:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV4=[[UILabel alloc]initWithFrame:CGRectMake(155, 80, 120, 20)];
    [lblV4 setTextAlignment:NSTextAlignmentLeft];
    [lblV4 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV4 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV4];
    
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 105, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"额定电流:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV5=[[UILabel alloc]initWithFrame:CGRectMake(155, 105, 120, 20)];
    [lblV5 setTextAlignment:NSTextAlignmentLeft];
    [lblV5 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV5 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV5];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 130, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"变比:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV6=[[UILabel alloc]initWithFrame:CGRectMake(155, 130, 120, 20)];
    [lblV6 setTextAlignment:NSTextAlignmentLeft];
    [lblV6 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV6 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV6];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 155, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"运行状态:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV7=[[UILabel alloc]initWithFrame:CGRectMake(155, 155, 120, 20)];
    [lblV7 setTextAlignment:NSTextAlignmentLeft];
    [lblV7 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV7 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV7];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 180, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"上报时间:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV8=[[UILabel alloc]initWithFrame:CGRectMake(155, 180, 120, 20)];
    [lblV8 setTextAlignment:NSTextAlignmentLeft];
    [lblV8 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV8 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV8];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 205, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"组合有功总电能:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV9=[[UILabel alloc]initWithFrame:CGRectMake(155, 205, 120, 20)];
    [lblV9 setTextAlignment:NSTextAlignmentLeft];
    [lblV9 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV9 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV9];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 230, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"日耗量:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV10=[[UILabel alloc]initWithFrame:CGRectMake(155, 230, 120, 20)];
    [lblV10 setTextAlignment:NSTextAlignmentLeft];
    [lblV10 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV10 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV10];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 255, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"A相电压:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV11=[[UILabel alloc]initWithFrame:CGRectMake(155, 255, 120, 20)];
    [lblV11 setTextAlignment:NSTextAlignmentLeft];
    [lblV11 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV11 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV11];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 280, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"B相电压:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV12=[[UILabel alloc]initWithFrame:CGRectMake(155, 280, 120, 20)];
    [lblV12 setTextAlignment:NSTextAlignmentLeft];
    [lblV12 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV12 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV12];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 305, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"C相电压:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV13=[[UILabel alloc]initWithFrame:CGRectMake(155, 305, 120, 20)];
    [lblV13 setTextAlignment:NSTextAlignmentLeft];
    [lblV13 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV13 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV13];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 330, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"电流A相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV14=[[UILabel alloc]initWithFrame:CGRectMake(155, 330, 120, 20)];
    [lblV14 setTextAlignment:NSTextAlignmentLeft];
    [lblV14 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV14 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV14];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 355, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"电流B相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV15=[[UILabel alloc]initWithFrame:CGRectMake(155, 355, 120, 20)];
    [lblV15 setTextAlignment:NSTextAlignmentLeft];
    [lblV15 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV15 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV15];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 380, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"电流C相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV16=[[UILabel alloc]initWithFrame:CGRectMake(155, 380, 120, 20)];
    [lblV16 setTextAlignment:NSTextAlignmentLeft];
    [lblV16 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV16 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV16];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 405, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"功率A相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV17=[[UILabel alloc]initWithFrame:CGRectMake(155, 405, 120, 20)];
    [lblV17 setTextAlignment:NSTextAlignmentLeft];
    [lblV17 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV17 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV17];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 430, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"功率B相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV18=[[UILabel alloc]initWithFrame:CGRectMake(155, 430, 120, 20)];
    [lblV18 setTextAlignment:NSTextAlignmentLeft];
    [lblV18 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV18 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV18];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 455, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"功率C相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV19=[[UILabel alloc]initWithFrame:CGRectMake(155, 455, 120, 20)];
    [lblV19 setTextAlignment:NSTextAlignmentLeft];
    [lblV19 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV19 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV19];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 480, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"功率因数A相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV20=[[UILabel alloc]initWithFrame:CGRectMake(155, 480, 120, 20)];
    [lblV20 setTextAlignment:NSTextAlignmentLeft];
    [lblV20 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV20 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV20];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 505, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"功率因数B相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV21=[[UILabel alloc]initWithFrame:CGRectMake(155, 505, 120, 20)];
    [lblV21 setTextAlignment:NSTextAlignmentLeft];
    [lblV21 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV21 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV21];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 530, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"功率因数C相:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV22=[[UILabel alloc]initWithFrame:CGRectMake(155, 530, 120, 20)];
    [lblV22 setTextAlignment:NSTextAlignmentLeft];
    [lblV22 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV22 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV22];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(50, 555, 100, 20)];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [lbl setFont:[UIFont systemFontOfSize:12.0]];
    [lbl setText:@"电网频率:"];
    [lbl setTextColor:[UIColor blackColor]];
    [control addSubview:lbl];
    
    lblV23=[[UILabel alloc]initWithFrame:CGRectMake(155, 555, 120, 20)];
    [lblV23 setTextAlignment:NSTextAlignmentLeft];
    [lblV23 setFont:[UIFont systemFontOfSize:12.0]];
    [lblV23 setTextColor:[UIColor blackColor]];
    [control addSubview:lblV23];
    
}

- (void)reloadDataSource{
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:@"1" forKey:@"OpWap"];
    [p setObject:@"1" forKey:@"OpType"];
    [p setObject:self.serialNo forKey:@"SerialNo"];
    [p setObject:self.channelNo forKey:@"Channel"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URLAppScanNumber params:p];
    
}


- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode {
    NSMutableArray *dataArray=[[NSMutableArray alloc]initWithArray:[[response resultJSON] objectForKey:@"Rows"]];
    for(NSDictionary *dic in dataArray) {
        [lblV1 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"METER_NAME"]]];
        [lblV2 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"METER_NO"]]];
        [lblV3 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"SWITCH_NO"]]];
        [lblV4 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"LOAD_GRADE"]]];
        [lblV5 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"FIX_VALUE"]]];
        [lblV6 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"RATIO"]]];
        [lblV7 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"STATUS2"]]];
        [lblV8 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"REPORT_DATE"]]];
        [lblV9 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"TOTAL_POWER"]]];
        [lblV10 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"all_hour"]]];
        [lblV11 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"V_A"]]];
        [lblV12 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"V_B"]]];
        [lblV13 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"V_C"]]];
        [lblV14 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"I_A"]]];
        [lblV15 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"I_B"]]];
        [lblV16 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"I_C"]]];
        [lblV17 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"P_A"]]];
        [lblV18 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"P_B"]]];
        [lblV19 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"P_C"]]];
        [lblV20 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"F_A"]]];
        [lblV21 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"F_B"]]];
        [lblV22 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"F_C"]]];
        [lblV23 setText:[NSString stringWithFormat:@"%@",[dic objectForKey:@"HZ"]]];
        break;
    }
    
}

@end

