//
//  STBaseUserExperienceViewController.h
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>

#define DISPLAYLINESTR @"ia=%.2f;\nib=%.2f;\nic=%.2f;"

//保存每条进出线开关的状态
bool finalB[8];
//保存母联开关的状态
bool finalB9;
//保存最近12个每条进出线的电流IA、IB、IC的值
double allPhaseCurrentList[12][2][4][3];
//保存最近12个每条进出线的负荷数
double allTotalBurden[12][2][4];
//保存最近12个每条进出线的电量
double allTotalElectricity[12][2][4];
//保存最近12个每条进出线的电费
double allTotalElectricityVal[12][2][4];
//是否为超负荷体验
bool isTransLoad;
double electricCurrentLeftA;
double electricCurrentLeftB;
double electricCurrentLeftC;
double electricCurrentRightA;
double electricCurrentRightB;
double electricCurrentRightC;
double threePhaseCurrentLeft[4][3];
double threePhaseCurrentRight[4][3];
//最后一次进线A的值
double iaTempLastValue;
//最后一次负荷
double lastTotalBurden;
//当前负荷
double currentTotalBurden;
//总电量
double currentTotalElectricity;

@interface STBaseUserExperienceViewController : UIViewController<UIAlertViewDelegate,UIActionSheetDelegate>

//当前负荷
@property (weak, nonatomic) IBOutlet UILabel *lblCurrentLoad;
//当前总电量
@property (weak, nonatomic) IBOutlet UILabel *lblElectricity;
//进线A值
@property (weak, nonatomic) IBOutlet UIButton *btnInLineAValue;
//进线A
@property (weak, nonatomic) IBOutlet UIButton *btnInLineA;
//出线A-1值
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineA1Value;
//进线A-1
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineA1;
//出线A-2值
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineA2Value;
//进线A-2
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineA2;
//出线A-3值
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineA3Value;
//进线A-3
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineA3;
//出线B值
@property (weak, nonatomic) IBOutlet UIButton *btnInLineBValue;
//进线B
@property (weak, nonatomic) IBOutlet UIButton *btnInLineB;
//出线B-1值
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineB1Value;
//进线B-1
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineB1;
//出线B-2值
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineB2Value;
//进线B-2
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineB2;
//出线B-3值
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineB3Value;
//进线B-3
@property (weak, nonatomic) IBOutlet UIButton *btnOutLineB3;
//母联开关
@property (weak, nonatomic) IBOutlet UIButton *btnMotherOf;

//当前点击的线路名称
@property (strong,nonatomic) NSString *currentSelectLineName;

@property NSTimer * timer;
@property NSTimer * timerElectricity;

//后退
- (void)back:(id)sender;
//生成一个0～1之间的随机数小数点后保留两位
- (double)random;
// 计算当前时间的商业电价
- (double)businessCalculationTime;
- (void)startBusinessCal;
- (void)startCalculate;
- (void)buildCal;
- (void)totalElectricity;
//显示当前的电流
- (void)displayElectricCurrent;
//显示开关的状态
- (void)displaySwitchStatus;
//进出线电流开关
- (IBAction)onClickSwitch:(id)sender;
//电流详细信息
- (IBAction)onClickLoadDetail:(id)sender;

@end
