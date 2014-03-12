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
#import <AVFoundation/AVAudioPlayer.h>

#define ALARMCODE 10022
#define CHARTCODE 128374
extern bool switchFlag;
//保存每条进出线开关的状态
extern bool finalB[8];
//保存最近12个每条进出线的电流IA、IB、IC的值
extern double allPhaseCurrentList[12][2][4][3];
//保存最近12个每条进出线的负荷数
double allTotalBurden[12][2][4];
//保存最近12个每条进出线的电量
double allTotalElectricity[12][2][4];

@interface STUserExperienceLineDetailViewController () <UIAlertViewDelegate,UIActionSheetDelegate>

@end

@implementation STUserExperienceLineDetailViewController {
    long _currentIndex;
    
    NSString *lineName;
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
    
    AVAudioPlayer *player;
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
        if(_currentIndex==0){
            lineName=@"进线A";
        }else if(_currentIndex==1){
            lineName=@"出线A-1";
        }else if(_currentIndex==2){
            lineName=@"出线A-2";
        }else if(_currentIndex==3){
            lineName=@"出线A-3";
        }else if(_currentIndex==4){
            lineName=@"进线B";
        }else if(_currentIndex==5){
            lineName=@"出线B-1";
        }else if(_currentIndex==6){
            lineName=@"出线B-2";
        }else if(_currentIndex==7){
            lineName=@"出线B-3";
        }
        
        UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 80, 320, 340)];
        
        UIButton *btnHistory1=[[UIButton alloc]initWithFrame:CGRectMake(12.5, 5, 90, 30)];
        [btnHistory1 setBackgroundImage:[UIImage imageNamed:@"dy"] forState:UIControlStateNormal];
        [btnHistory1 addTarget:self action:@selector(alarme1:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory1];
        
        UIButton *btnHistory2=[[UIButton alloc]initWithFrame:CGRectMake(115, 5, 90, 30)];
        [btnHistory2 setBackgroundImage:[UIImage imageNamed:@"dl"] forState:UIControlStateNormal];
        [btnHistory2 addTarget:self action:@selector(alarme2:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory2];
        
        UIButton *btnHistory3=[[UIButton alloc]initWithFrame:CGRectMake(217.5, 5, 90, 30)];
        [btnHistory3 setBackgroundImage:[UIImage imageNamed:@"bj1"] forState:UIControlStateNormal];
        [btnHistory3 addTarget:self action:@selector(alarme3:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnHistory3];
        
        UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 40, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"线路名称:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblLineName=[[UILabel alloc]initWithFrame:CGRectMake(150, 40, 120, 20)];
        [lblLineName setTextAlignment:NSTextAlignmentLeft];
        [lblLineName setFont:[UIFont systemFontOfSize:12.0]];
        [lblLineName setText:lineName];
        [lblLineName setTextColor:[UIColor blackColor]];
        [control addSubview:lblLineName];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 65, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"A相电压:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(150, 65, 120, 20)];
        [lbl setTextAlignment:NSTextAlignmentLeft];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"220V"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 90, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"B相电压:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(150, 90, 120, 20)];
        [lbl setTextAlignment:NSTextAlignmentLeft];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"220V"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 115, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"C相电压:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(150, 115, 120, 20)];
        [lbl setTextAlignment:NSTextAlignmentLeft];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"220V"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 140, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"A相电流:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblIA=[[UILabel alloc]initWithFrame:CGRectMake(150, 140, 120, 20)];
        [lblIA setTextAlignment:NSTextAlignmentLeft];
        [lblIA setFont:[UIFont systemFontOfSize:12.0]];
        [lblIA setText:@""];
        [lblIA setTextColor:[UIColor blackColor]];
        [control addSubview:lblIA];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 165, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"B相电流:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblIB=[[UILabel alloc]initWithFrame:CGRectMake(150, 165, 120, 20)];
        [lblIB setTextAlignment:NSTextAlignmentLeft];
        [lblIB setFont:[UIFont systemFontOfSize:12.0]];
        [lblIB setText:@""];
        [lblIB setTextColor:[UIColor blackColor]];
        [control addSubview:lblIB];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 190, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"C相电流:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblIC=[[UILabel alloc]initWithFrame:CGRectMake(150, 190, 120, 20)];
        [lblIC setTextAlignment:NSTextAlignmentLeft];
        [lblIC setFont:[UIFont systemFontOfSize:12.0]];
        [lblIC setText:@""];
        [lblIC setTextColor:[UIColor blackColor]];
        [control addSubview:lblIC];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 215, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"总有功功率:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblTotalBurden=[[UILabel alloc]initWithFrame:CGRectMake(150, 215, 120, 20)];
        [lblTotalBurden setTextAlignment:NSTextAlignmentLeft];
        [lblTotalBurden setFont:[UIFont systemFontOfSize:12.0]];
        [lblTotalBurden setText:@""];
        [lblTotalBurden setTextColor:[UIColor blackColor]];
        [control addSubview:lblTotalBurden];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 240, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"功率因数:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        double r1=(double)(arc4random() % 100)/100;
        
        lblNum=[[UILabel alloc]initWithFrame:CGRectMake(150, 240, 120, 20)];
        [lblNum setTextAlignment:NSTextAlignmentLeft];
        [lblNum setFont:[UIFont systemFontOfSize:12.0]];
        [lblNum setText:[NSString stringWithFormat:@"%.2f",r1]];
        [lblNum setTextColor:[UIColor blackColor]];
        [control addSubview:lblNum];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 265, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"电量值:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblElectricity=[[UILabel alloc]initWithFrame:CGRectMake(150, 265, 120, 20)];
        [lblElectricity setTextAlignment:NSTextAlignmentLeft];
        [lblElectricity setFont:[UIFont systemFontOfSize:12.0]];
        [lblElectricity setText:@""];
        [lblElectricity setTextColor:[UIColor blackColor]];
        [control addSubview:lblElectricity];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(55, 290, 80, 20)];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [lbl setFont:[UIFont systemFontOfSize:12.0]];
        [lbl setText:@"开关状态:"];
        [lbl setTextColor:[UIColor blackColor]];
        [control addSubview:lbl];
        
        lblSwitchStatus=[[UILabel alloc]initWithFrame:CGRectMake(150, 290, 120, 20)];
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
        [lblSwitchStatus setTextColor:[UIColor redColor]];
        [lblSwitchStatus setText:@"合"];
    }else{
        [lblSwitchStatus setTextColor:[UIColor greenColor]];
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
    sheet.tag=CHARTCODE;
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSInteger tag=actionSheet.tag;
    if(tag==ALARMCODE){
        NSLog(@"ALARAMCODE");
    }else if(tag==CHARTCODE){
        if(buttonIndex==0){
            STChartElectricCurrentLineViewController *chartElectricCurrentLineViewController=[[STChartElectricCurrentLineViewController alloc]init];
            [self.navigationController pushViewController:chartElectricCurrentLineViewController animated:YES];
        }else if(buttonIndex==1){
            STChartBurdenLineViewController *chartBurdenLineViewController=[[STChartBurdenLineViewController alloc]initWithIndex:_currentIndex];
            [self.navigationController pushViewController:chartBurdenLineViewController animated:YES];
        }else if(buttonIndex==2){
            STChartElectricityViewController *chartElectricityViewController=[[STChartElectricityViewController alloc]initWithIndex:_currentIndex];
            [self.navigationController pushViewController:chartElectricityViewController animated:YES];
        }else if(buttonIndex==3){
            STChartElectricityValViewController *chartElectricityValViewController=[[STChartElectricityValViewController alloc]initWithIndex:_currentIndex];
            [self.navigationController pushViewController:chartElectricityValViewController animated:YES];
        }else if(buttonIndex==4){
            STChartElectricityPieViewController *chartElectricityPieViewController=[[STChartElectricityPieViewController alloc]init];
            [self.navigationController pushViewController:chartElectricityPieViewController animated:YES];
        }else if(buttonIndex==5){
            STChartElectricityValPieViewController *chartElectricityValPieViewController=[[STChartElectricityValPieViewController alloc]init];
            [chartElectricityValPieViewController setIndex:_currentIndex];
            [self.navigationController pushViewController:chartElectricityValPieViewController animated:YES];
            [chartElectricityValPieViewController buildUI];
        }
    }
}

- (void)alarme1:(id)sender
{
    UIAlertView *alert = [[UIAlertView alloc]
                       initWithTitle:@"电压设置报警体验阈值"
                       message:nil
                       delegate:self
                       cancelButtonTitle:@"取消"
                       otherButtonTitles:@"确定",nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    alert.tag=1;
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
                          delegate:self
                          cancelButtonTitle:@"取消"
                          otherButtonTitles:@"确定",nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    alert.tag=2;
    //设置输入框的键盘类型
    UITextField *tf = [alert textFieldAtIndex:0];
    tf.keyboardType = UIKeyboardTypeNumberPad;
    [alert show];
}

- (void)alarme3:(id)sender
{
    [Common alert:@"开关状态报警已开启，请回主接线上进行开着操作"];
    switchFlag=YES;
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSInteger tag=alertView.tag;
    if(tag==1){
        if(buttonIndex==1){
            NSString *content=[[alertView textFieldAtIndex:0]text];
            if(![@"" isEqualToString:content]){
                NSString *message=[NSString stringWithFormat:@"%@超出电压所设定的阀值，请注意!",lineName];
                [self performSelector:@selector(alaram:) withObject:message afterDelay:20];
            }
        }
    }else if(tag==2){
        if(buttonIndex==1){
            NSString *content=[[alertView textFieldAtIndex:0]text];
            if(![@"" isEqualToString:content]){
                [self alaram:[NSString stringWithFormat:@"%@超出电流所设定的阀值，请注意！",lineName]];
                
            }
        }
    }
}

- (void)alaram:(NSString*)message
{
    //开启报警声音
    NSString *path=[[NSBundle mainBundle] pathForResource: @"alarm" ofType: @"mp3"];
    NSURL *url=[[NSURL alloc] initFileURLWithPath:path];
    NSError *error;
    player=[[AVAudioPlayer alloc] initWithContentsOfURL:url error:&error];
    if (error) {
        NSLog(@"error:%@",[error description]);
        return;
    }
    [player setVolume:1];   //设置音量大小
    player.numberOfLoops = 1;//设置音乐播放次数  -1为一直循环
    [player prepareToPlay];
    [player play];
    UIActionSheet *sheet = [[UIActionSheet alloc]
                            initWithTitle:message
                            delegate:self
                            cancelButtonTitle:nil
                            destructiveButtonTitle:@"确定"
                            otherButtonTitles:nil,nil];
    sheet.tag=ALARMCODE;
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
}

@end
