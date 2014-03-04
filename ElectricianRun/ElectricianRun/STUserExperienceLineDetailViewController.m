//
//  STUserExperienceLineDetailViewController.m
//  ElectricianRun
//  进出线电费柱状图
//  Created by Start on 2/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceLineDetailViewController.h"
#import "STChartElectricCurrentLineViewController.h"
#import "STChartBurdenLineViewController.h"
#import "STChartElectricityViewController.h"
#import "STChartElectricityValViewController.h"
#import "STChartElectricityPieViewController.h"
#import "STChartElectricityValPieViewController.h"

//保存每条进出线开关的状态
extern bool finalB[8];
//保存最近12个每条进出线的电流IA、IB、IC的值
extern double allPhaseCurrentList[12][2][4][3];
//保存最近12个每条进出线的负荷数
double allTotalBurden[12][2][4];
//保存最近12个每条进出线的电量
double allTotalElectricity[12][2][4];

@interface STUserExperienceLineDetailViewController () <UIActionSheetDelegate>

@end

@implementation STUserExperienceLineDetailViewController {
    long _currentIndex;
    
    UILabel *lblLineName;
    UILabel *lblIA;
    UILabel *lblIB;
    UILabel *lblIC;
    UILabel *lblTotalBurden;
    UILabel *lblNum;
    UILabel *lblElectricity;
    UILabel *lblSwitchStatus;
    NSTimer *timer;
    NSTimer *timerElectricity;
    
}

- (id)initWithIndex:(long)index
{
    self = [super init];
    if (self) {
        self.title=@"线路详细信息";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"图表"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(chartSwitch:)];
        
        _currentIndex=index;
        
        UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 80, 320, 340)];
        
        UIButton *btnHistory1=[[UIButton alloc]initWithFrame:CGRectMake(20, 5, 80, 30)];
        btnHistory1.titleLabel.font=[UIFont systemFontOfSize:12];
        [btnHistory1 setTitle:@"电压报警体验" forState:UIControlStateNormal];
        [btnHistory1 setBackgroundColor:[UIColor blueColor]];
        [btnHistory1 addTarget:self action:@selector(alarme1:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory1];
        
        UIButton *btnHistory2=[[UIButton alloc]initWithFrame:CGRectMake(120, 5, 80, 30)];
        btnHistory2.titleLabel.font=[UIFont systemFontOfSize:12];
        [btnHistory2 setTitle:@"电流报警体验" forState:UIControlStateNormal];
        [btnHistory2 setBackgroundColor:[UIColor blueColor]];
        [btnHistory2 addTarget:self action:@selector(alarme2:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory2];
        
        UIButton *btnHistory3=[[UIButton alloc]initWithFrame:CGRectMake(220, 5, 80, 30)];
        btnHistory3.titleLabel.font=[UIFont systemFontOfSize:12];
        [btnHistory3 setTitle:@"开关状态报警体验" forState:UIControlStateNormal];
        [btnHistory3 setBackgroundColor:[UIColor blueColor]];
        [btnHistory3 addTarget:self action:@selector(alarme3:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory3];
        
        UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(75, 40, 80, 20)];
        [lbl1 setTextAlignment:NSTextAlignmentRight];
        [lbl1 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl1 setText:@"线路名称:"];
        [lbl1 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl1];
        
        lblLineName=[[UILabel alloc]initWithFrame:CGRectMake(160, 40, 120, 20)];
        [lblLineName setTextAlignment:NSTextAlignmentLeft];
        [lblLineName setFont:[UIFont systemFontOfSize:12.0]];
        [lblLineName setText:@""];
        [lblLineName setTextColor:[UIColor blackColor]];
        [control addSubview:lblLineName];
        
        UILabel *lbl2=[[UILabel alloc]initWithFrame:CGRectMake(75, 65, 80, 20)];
        [lbl2 setTextAlignment:NSTextAlignmentRight];
        [lbl2 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl2 setText:@"A相电压:"];
        [lbl2 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl2];
        
        UILabel *lblV2=[[UILabel alloc]initWithFrame:CGRectMake(160, 65, 120, 20)];
        [lblV2 setTextAlignment:NSTextAlignmentLeft];
        [lblV2 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV2 setText:@"220V"];
        [lblV2 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV2];
        
        UILabel *lbl3=[[UILabel alloc]initWithFrame:CGRectMake(75, 90, 80, 20)];
        [lbl3 setTextAlignment:NSTextAlignmentRight];
        [lbl3 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl3 setText:@"B相电压:"];
        [lbl3 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl3];
        
        UILabel *lblV3=[[UILabel alloc]initWithFrame:CGRectMake(160, 90, 120, 20)];
        [lblV3 setTextAlignment:NSTextAlignmentLeft];
        [lblV3 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV3 setText:@"220V"];
        [lblV3 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV3];
        
        UILabel *lbl4=[[UILabel alloc]initWithFrame:CGRectMake(75, 115, 80, 20)];
        [lbl4 setTextAlignment:NSTextAlignmentRight];
        [lbl4 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl4 setText:@"C相电压:"];
        [lbl4 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl4];
        
        UILabel *lblV4=[[UILabel alloc]initWithFrame:CGRectMake(160, 115, 120, 20)];
        [lblV4 setTextAlignment:NSTextAlignmentLeft];
        [lblV4 setFont:[UIFont systemFontOfSize:12.0]];
        [lblV4 setText:@"220V"];
        [lblV4 setTextColor:[UIColor blackColor]];
        [control addSubview:lblV4];
        
        UILabel *lbl5=[[UILabel alloc]initWithFrame:CGRectMake(75, 140, 80, 20)];
        [lbl5 setTextAlignment:NSTextAlignmentRight];
        [lbl5 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl5 setText:@"A相电流:"];
        [lbl5 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl5];
        
        lblIA=[[UILabel alloc]initWithFrame:CGRectMake(160, 140, 120, 20)];
        [lblIA setTextAlignment:NSTextAlignmentLeft];
        [lblIA setFont:[UIFont systemFontOfSize:12.0]];
        [lblIA setText:@""];
        [lblIA setTextColor:[UIColor blackColor]];
        [control addSubview:lblIA];
        
        UILabel *lbl6=[[UILabel alloc]initWithFrame:CGRectMake(75, 165, 80, 20)];
        [lbl6 setTextAlignment:NSTextAlignmentRight];
        [lbl6 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl6 setText:@"B相电流:"];
        [lbl6 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl6];
        
        lblIB=[[UILabel alloc]initWithFrame:CGRectMake(160, 165, 120, 20)];
        [lblIB setTextAlignment:NSTextAlignmentLeft];
        [lblIB setFont:[UIFont systemFontOfSize:12.0]];
        [lblIB setText:@""];
        [lblIB setTextColor:[UIColor blackColor]];
        [control addSubview:lblIB];
        
        UILabel *lbl7=[[UILabel alloc]initWithFrame:CGRectMake(75, 190, 80, 20)];
        [lbl7 setTextAlignment:NSTextAlignmentRight];
        [lbl7 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl7 setText:@"C相电流:"];
        [lbl7 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl7];
        
        lblIC=[[UILabel alloc]initWithFrame:CGRectMake(160, 190, 120, 20)];
        [lblIC setTextAlignment:NSTextAlignmentLeft];
        [lblIC setFont:[UIFont systemFontOfSize:12.0]];
        [lblIC setText:@""];
        [lblIC setTextColor:[UIColor blackColor]];
        [control addSubview:lblIC];
        
        UILabel *lbl8=[[UILabel alloc]initWithFrame:CGRectMake(75, 215, 80, 20)];
        [lbl8 setTextAlignment:NSTextAlignmentRight];
        [lbl8 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl8 setText:@"总有功功率:"];
        [lbl8 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl8];
        
        lblTotalBurden=[[UILabel alloc]initWithFrame:CGRectMake(160, 215, 120, 20)];
        [lblTotalBurden setTextAlignment:NSTextAlignmentLeft];
        [lblTotalBurden setFont:[UIFont systemFontOfSize:12.0]];
        [lblTotalBurden setText:@""];
        [lblTotalBurden setTextColor:[UIColor blackColor]];
        [control addSubview:lblTotalBurden];
        
        UILabel *lbl9=[[UILabel alloc]initWithFrame:CGRectMake(75, 240, 80, 20)];
        [lbl9 setTextAlignment:NSTextAlignmentRight];
        [lbl9 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl9 setText:@"功率因数:"];
        [lbl9 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl9];
        
        double r1=(double)(arc4random() % 100)/100;
        
        lblNum=[[UILabel alloc]initWithFrame:CGRectMake(160, 240, 120, 20)];
        [lblNum setTextAlignment:NSTextAlignmentLeft];
        [lblNum setFont:[UIFont systemFontOfSize:12.0]];
        [lblNum setText:[NSString stringWithFormat:@"%.2f",r1]];
        [lblNum setTextColor:[UIColor blackColor]];
        [control addSubview:lblNum];
        
        UILabel *lbl10=[[UILabel alloc]initWithFrame:CGRectMake(75, 265, 80, 20)];
        [lbl10 setTextAlignment:NSTextAlignmentRight];
        [lbl10 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl10 setText:@"电量值:"];
        [lbl10 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl10];
        
        lblElectricity=[[UILabel alloc]initWithFrame:CGRectMake(160, 265, 120, 20)];
        [lblElectricity setTextAlignment:NSTextAlignmentLeft];
        [lblElectricity setFont:[UIFont systemFontOfSize:12.0]];
        [lblElectricity setText:@""];
        [lblElectricity setTextColor:[UIColor blackColor]];
        [control addSubview:lblElectricity];
        
        UILabel *lbl11=[[UILabel alloc]initWithFrame:CGRectMake(75, 290, 80, 20)];
        [lbl11 setTextAlignment:NSTextAlignmentRight];
        [lbl11 setFont:[UIFont systemFontOfSize:12.0]];
        [lbl11 setText:@"开关状态:"];
        [lbl11 setTextColor:[UIColor blackColor]];
        [control addSubview:lbl11];
        
        lblSwitchStatus=[[UILabel alloc]initWithFrame:CGRectMake(160, 290, 120, 20)];
        [lblSwitchStatus setTextAlignment:NSTextAlignmentLeft];
        [lblSwitchStatus setFont:[UIFont systemFontOfSize:12.0]];
        [lblSwitchStatus setText:@""];
        [lblSwitchStatus setTextColor:[UIColor blackColor]];
        [control addSubview:lblSwitchStatus];
        
        [self.view addSubview:control];
        
        [self startBusinessCal];
        [self totalElectricity];
        
        timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(startBusinessCal) userInfo:nil repeats:YES];
        timerElectricity = [NSTimer scheduledTimerWithTimeInterval:60 target:self selector:@selector(totalElectricity) userInfo:nil repeats:YES];
        
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if(finalB[_currentIndex]){
        [lblSwitchStatus setText:@"合"];
    }else{
        [lblSwitchStatus setText:@"关"];
    }
}

- (void)startBusinessCal
{
    [lblIA setText:[NSString stringWithFormat:@"%.2f",allPhaseCurrentList[11][_currentIndex/4][_currentIndex%4][0]]];
    [lblIB setText:[NSString stringWithFormat:@"%.2f",allPhaseCurrentList[11][_currentIndex/4][_currentIndex%4][1]]];
    [lblIC setText:[NSString stringWithFormat:@"%.2f",allPhaseCurrentList[11][_currentIndex/4][_currentIndex%4][2]]];
    [lblTotalBurden setText:[NSString stringWithFormat:@"%.2f",allTotalBurden[11][_currentIndex/4][_currentIndex%4]/1000]];
    
}

- (void)totalElectricity
{
    [lblElectricity setText:[NSString stringWithFormat:@"%.2f",allTotalElectricity[11][_currentIndex/4][_currentIndex%4]]];
}

- (void)chartSwitch:(id)sender
{
    UIActionSheet *sheet = [[UIActionSheet alloc]
                            initWithTitle:nil
                            delegate:self
                            cancelButtonTitle:@"取消"
                            destructiveButtonTitle:nil
                            otherButtonTitles:
                            @"进出线电流曲线图",
                            @"进出线负荷曲线图",
                            @"进出线电耗量柱状图",
                            @"进出线电费柱状图",
                            @"进出线尖峰谷进线电量饼图",
                            @"进出线尖峰谷进线电费饼图",nil];
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex==0){
        STChartElectricCurrentLineViewController *chartElectricCurrentLineViewController=[[STChartElectricCurrentLineViewController alloc]init];
        [self.navigationController pushViewController:chartElectricCurrentLineViewController animated:YES];
    }else if(buttonIndex==1){
        STChartBurdenLineViewController *chartBurdenLineViewController=[[STChartBurdenLineViewController alloc]init];
        [self.navigationController pushViewController:chartBurdenLineViewController animated:YES];
    }else if(buttonIndex==2){
        STChartElectricityViewController *chartElectricityViewController=[[STChartElectricityViewController alloc]init];
        [self.navigationController pushViewController:chartElectricityViewController animated:YES];
    }else if(buttonIndex==3){
        STChartElectricityValViewController *chartElectricityValViewController=[[STChartElectricityValViewController alloc]init];
        [self.navigationController pushViewController:chartElectricityValViewController animated:YES];
    }else if(buttonIndex==4){
        STChartElectricityPieViewController *chartElectricityPieViewController=[[STChartElectricityPieViewController alloc]init];
        [self.navigationController pushViewController:chartElectricityPieViewController animated:YES];
    }else if(buttonIndex==5){
        STChartElectricityValPieViewController *chartElectricityValPieViewController=[[STChartElectricityValPieViewController alloc]init];
        [self.navigationController pushViewController:chartElectricityValPieViewController animated:YES];
    }
}

- (void)alarme1:(id)sender
{
    UIAlertView *alert = [[UIAlertView alloc]
                       initWithTitle:@"电压设置报警体验阈值"
                       message:nil
                       delegate:nil
                       cancelButtonTitle:@"确定"
                       otherButtonTitles:nil,nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    //设置输入框的键盘类型
    UITextField *tf = [alert textFieldAtIndex:0];
    tf.keyboardType = UIKeyboardTypeNumberPad;
    [alert show];
}

- (void)alarme2:(id)sender
{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"电流设置报警体验阈值"
                          message:@"提示：输入阈值范围在0～1443.0之间"
                          delegate:nil
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:nil,nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    //设置输入框的键盘类型
    UITextField *tf = [alert textFieldAtIndex:0];
    tf.keyboardType = UIKeyboardTypeNumberPad;
    [alert show];
}

- (void)alarme3:(id)sender
{
    [Common alert:@"开关状态报警已开启，请回主接线上进行开着操作"];
}

@end
