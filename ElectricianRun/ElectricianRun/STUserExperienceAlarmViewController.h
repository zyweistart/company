//
//  STUserExperienceAlarmViewController.h
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>

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

@interface STUserExperienceAlarmViewController : UIViewController

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

@end