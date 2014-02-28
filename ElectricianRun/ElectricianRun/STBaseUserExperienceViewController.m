//
//  STBaseUserExperienceViewController.m
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STBaseUserExperienceViewController.h"
#import "STSignupViewController.h"

@interface STBaseUserExperienceViewController ()

@end

@implementation STBaseUserExperienceViewController

- (void)back:(id)sender
{
    [self.timer invalidate];
    [self.timerElectricity invalidate];
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //初始化数据
    finalB9 = NO;
    for (int i = 0; i < 8; i++) {
        finalB[i] = YES;
    }
    
    for(int i=0;i<12;i++){
        for(int r=0;r<2;r++){
            for(int j=0;j<4;j++){
                for(int k=0;k<3;k++){
                    allPhaseCurrentList[i][r][j][k]=0.0;
                }
                allTotalBurden[i][r][j]=0.0;
                allTotalElectricity[i][r][j]=0.0;
                allTotalElectricityVal[i][r][j]=0.0;
            }
        }
    }
    self.isTransLoad=NO;
    self.iaTempLastValue=0;
    //初始默认当前总电量为
    self.currentTotalElectricity=5;
    //初始调用一次
    [self startBusinessCal];
    [self displaySwitchStatus];
}

//生成一个0～1之间的随机数小数点后保留两位
- (double)random
{
    int r=arc4random() % 100;
    return (double)r/100;
}

// 计算当前时间的商业电价
- (double)businessCalculationTime
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"HH"];
    int hour = [[formatter stringFromDate:[NSDate date]]intValue];
    
    double money = 0;
    if (hour >= 19 && hour < 21) {
        money = 1.406;
    } else if ((hour >= 8 && hour < 11) || (hour >= 13 && hour < 19)
               || (hour >= 21 && hour < 22)) {
        money = 1.108;
    } else {
        money = 0.596;
    }
    return money;
}

- (void)startBusinessCal
{
    //进线A
    double rn0=[self random];
    if(self.isTransLoad){
        int sign=1;
        if (rn0 > 0.5) {
            sign = -1;
        }
        //保证每次产生的值都在最后一次的基础之上上下浮动5%
        self.electricCurrentLeftA=self.electricCurrentLeftA*sign*0.05+self.electricCurrentLeftA;
    }else{
        if(self.iaTempLastValue==0){
            //第一次随机数保证rn的值在0.1-1之间
            if(rn0<0.1){
                rn0=0.1+rn0;
            }
            self.electricCurrentLeftA=1443.4*rn0;
        }else{
            int sign=1;
            if (rn0 > 0.5) {
                sign = -1;
            }
            //保证每次产生的值都在最后一次的基础之上上下浮动5%
            self.electricCurrentLeftA=self.iaTempLastValue*sign*0.05+self.iaTempLastValue;
        }
        
        if(self.electricCurrentLeftA>1443.4){
            self.electricCurrentLeftA=1345;
        }else if(self.electricCurrentLeftA<144.34){
            self.electricCurrentLeftA=160;
        }
        self.iaTempLastValue=self.electricCurrentLeftA;
    }
    [self buildCal];
    
}

- (void)buildCal
{
    double rn1=[self random];
    if(rn1>0.5){
        self.electricCurrentLeftB=self.electricCurrentLeftA*1.05;
        self.electricCurrentLeftC=self.electricCurrentLeftA*0.95;
    }else{
        self.electricCurrentLeftB=self.electricCurrentLeftA*0.95;
        self.electricCurrentLeftC=self.electricCurrentLeftA*1.05;
    }
    
    //进线B
    double rn2=[self random];
    if(rn2>0.5){
        self.electricCurrentRightA=self.electricCurrentLeftA*1.05;
    }else{
        self.electricCurrentRightA=self.electricCurrentLeftA*0.95;
    }
    
    double rn3=[self random];
    if(rn3>0.5){
        self.electricCurrentRightB=self.electricCurrentRightA*1.05;
        self.electricCurrentRightC=self.electricCurrentRightA*0.95;
    }else{
        self.electricCurrentRightB=self.electricCurrentRightA*0.95;
        self.electricCurrentRightC=self.electricCurrentRightA*1.05;
    }
    
    for(int i=0;i<4;i++){
        for(int j=0;j<3;j++){
            threePhaseCurrentLeft[i][j]=0.0;
            threePhaseCurrentRight[i][j]=0.0;
        }
    }
    
    threePhaseCurrentLeft[0][0]=self.electricCurrentLeftA;
    threePhaseCurrentLeft[0][1]=self.electricCurrentLeftB;
    threePhaseCurrentLeft[0][2]=self.electricCurrentLeftC;
    threePhaseCurrentRight[0][0]=self.electricCurrentRightA;
    threePhaseCurrentRight[0][1]=self.electricCurrentRightB;
    threePhaseCurrentRight[0][2]=self.electricCurrentRightC;
    
    //把数组中所有的值往前移一位把数组最后一位空出来保存新值
    int length=11;
    for(int i=0;i<length;i++){
        for(int r=0;r<2;r++){
            for(int j=0;j<4;j++){
                for(int k=0;k<3;k++){
                    //电流数组
                    allPhaseCurrentList[i][r][i][j]=allPhaseCurrentList[i+1][r][j][k];
                }
                //每条进出线的负荷数组
                allTotalBurden[i][r][j]=allTotalBurden[i+1][r][j];
            }
        }
    }
    
    [self startCalculate];
    
    [self displayElectricCurrent];
}

- (void)startCalculate
{
    for(int index=0;index<3;index++) {
        //母联开关是否合并
        if (finalB9) {
            if (!finalB[0] && !finalB[4]) {
                threePhaseCurrentLeft[0][index]= 0.0;
                threePhaseCurrentLeft[1][index]= 0.0;
                threePhaseCurrentLeft[2][index]= 0.0;
                threePhaseCurrentLeft[3][index]= 0.0;
                
                threePhaseCurrentRight[0][index]= 0.0;
                threePhaseCurrentRight[1][index]= 0.0;
                threePhaseCurrentRight[2][index]= 0.0;
                threePhaseCurrentRight[3][index]= 0.0;
            } else {
                double electricCurrent=0.0;
                if (finalB[0]) {
                    electricCurrent=threePhaseCurrentLeft[0][index];
                } else if (finalB[4]) {
                    electricCurrent=threePhaseCurrentRight[0][index];
                }
                threePhaseCurrentLeft[1][index] = electricCurrent * 0.15;// I*1
                threePhaseCurrentLeft[2][index] = electricCurrent * 0.25;// I*2
                threePhaseCurrentLeft[3][index] = electricCurrent * 0.1;// I*3
                
                threePhaseCurrentRight[1][index] = electricCurrent * 0.15;// I*1
                threePhaseCurrentRight[2][index] = electricCurrent * 0.25;// I*2
                threePhaseCurrentRight[3][index] = electricCurrent * 0.1;// I*3
                //出线A
                if (!finalB[1]) {
                    electricCurrent = electricCurrent - threePhaseCurrentLeft[1][index];
                    threePhaseCurrentLeft[1][index] = 0.0;
                }
                if (!finalB[2]) {
                    electricCurrent = electricCurrent - threePhaseCurrentLeft[2][index];
                    threePhaseCurrentLeft[2][index] = 0.0;
                }
                if (!finalB[3]) {
                    electricCurrent = electricCurrent - threePhaseCurrentLeft[3][index];
                    threePhaseCurrentLeft[3][index] = 0.0;
                }
                //出线B
                if (!finalB[5]) {
                    electricCurrent = electricCurrent - threePhaseCurrentRight[1][index];
                    threePhaseCurrentRight[1][index] = 0.0;
                }
                if (!finalB[6]) {
                    electricCurrent = electricCurrent - threePhaseCurrentRight[2][index];
                    threePhaseCurrentRight[2][index] = 0.0;
                }
                if (!finalB[7]) {
                    electricCurrent = electricCurrent - threePhaseCurrentRight[3][index];
                    threePhaseCurrentRight[3][index] = 0.0;
                }
                if (finalB[0]) {
                    //进线A
                    threePhaseCurrentRight[0][index] = 0.0;
                    threePhaseCurrentLeft[0][index] = electricCurrent;
                } else if (finalB[4]) {
                    //进线B
                    threePhaseCurrentLeft[0][index] = 0.0;
                    threePhaseCurrentRight[0][index] = electricCurrent;
                }
            }
        } else {
            if(!finalB[0]) {
                threePhaseCurrentLeft[0][index]=0;
                threePhaseCurrentLeft[1][index]=0;
                threePhaseCurrentLeft[2][index]=0;
                threePhaseCurrentLeft[3][index]=0;
            } else {
                double phaseValue=threePhaseCurrentLeft[0][index];
                threePhaseCurrentLeft[1][index] = phaseValue * 0.2;// I*1
                threePhaseCurrentLeft[2][index] = phaseValue * 0.3;// I*2
                threePhaseCurrentLeft[3][index] = phaseValue * 0.5;// I*3
                if (!finalB[1]) {
                    phaseValue = phaseValue - threePhaseCurrentLeft[1][index];
                    threePhaseCurrentLeft[1][index] = 0.0;
                }
                if (!finalB[2]) {
                    phaseValue = phaseValue - threePhaseCurrentLeft[2][index];
                    threePhaseCurrentLeft[2][index] = 0.0;
                }
                if (!finalB[3]) {
                    phaseValue = phaseValue - threePhaseCurrentLeft[3][index];
                    threePhaseCurrentLeft[3][index] = 0.0;
                }
                threePhaseCurrentLeft[0][index]=phaseValue;
            }
            if(!finalB[4]) {
                threePhaseCurrentRight[0][index]=0;
                threePhaseCurrentRight[1][index]=0;
                threePhaseCurrentRight[2][index]=0;
                threePhaseCurrentRight[3][index]=0;
            } else {
                double phaseValue=threePhaseCurrentRight[0][index];
                threePhaseCurrentRight[1][index] = phaseValue * 0.2;// I*1
                threePhaseCurrentRight[2][index] = phaseValue * 0.3;// I*2
                threePhaseCurrentRight[3][index] = phaseValue * 0.5;// I*3
                if (!finalB[5]) {
                    phaseValue = phaseValue - threePhaseCurrentRight[1][index];
                    threePhaseCurrentRight[1][index] = 0.0;
                }
                if (!finalB[6]) {
                    phaseValue = phaseValue - threePhaseCurrentRight[2][index];
                    threePhaseCurrentRight[2][index] = 0.0;
                }
                if (!finalB[7]) {
                    phaseValue = phaseValue - threePhaseCurrentRight[3][index];
                    threePhaseCurrentRight[3][index] = 0.0;
                }
                threePhaseCurrentRight[0][index]=phaseValue;
            }
        }
    }
    
    int length=11;
    //最新的值永远保存在最后一位
    for(int i=0;i<4;i++){
        for(int j=0;j<3;j++){
            allPhaseCurrentList[length][0][i][j]=threePhaseCurrentLeft[i][j];
            allPhaseCurrentList[length][1][i][j]=threePhaseCurrentRight[i][j];
        }
    }
    //随机生成一个0.9~1的随机数
    int r=arc4random() % 10;
    double d=(double)r/100+0.9;
    
    for(int i=0;i<4;i++){
        
        if(finalB[i]){
            allTotalBurden[length][0][i]=threePhaseCurrentLeft[i][0]*220*d+threePhaseCurrentLeft[i][1]*220*d+threePhaseCurrentLeft[i][2]*220*d;
        } else {
            allTotalBurden[length][0][i]=0.0;
        }
        
        if(finalB[4+i]){
            allTotalBurden[length][1][i]=threePhaseCurrentRight[i][0]*220*d+threePhaseCurrentRight[i][1]*220*d+threePhaseCurrentRight[i][2]*220*d;
        } else {
            allTotalBurden[length][1][i]=0.0;
        }
        
    }
    
    //计算总负荷
    double tmpBurden=0.0;
    if(finalB[0]){
        tmpBurden  = allTotalBurden[length][0][0];
    }
    if(finalB[4]){
        tmpBurden = tmpBurden + allTotalBurden[length][1][0];
    }
    if(self.currentTotalBurden>0){
        self.lastTotalBurden=self.currentTotalBurden;
    }
    self.currentTotalBurden=tmpBurden;
}

//总电量(更新调用频率一分钟)
- (void)totalElectricity
{
    int length=11;
    //把数组中所有的值往前移一位把数组最后一位空出来保存新值
    for(int i=0;i<length;i++){
        for(int j=0;j<2;j++){
            for(int k=0;k<4;k++){
                allTotalElectricity[i][j][k]=allTotalElectricity[i+1][j][k];
                allTotalElectricityVal[i][j][k]=allTotalElectricityVal[i+1][j][k];
            }
        }
    }
    for(int i=0;i<2;i++){
        for(int j=0;j<4;j++){
            allTotalElectricity[length][i][j]=(allTotalBurden[length-1][i][j]+allTotalBurden[length][i][j])/2/60/1000;
            allTotalElectricityVal[length][i][j]=allTotalElectricity[length][i][j]*[self businessCalculationTime];
        }
    }
    self.currentTotalElectricity=self.currentTotalElectricity+(self.lastTotalBurden+self.currentTotalBurden)/2/60/1000;
}

//显示当前的电流
- (void)displayElectricCurrent
{
    //页面显示电流信息
    [self.btnInLineAValue setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[0][0],threePhaseCurrentLeft[0][1],threePhaseCurrentLeft[0][2]] forState:UIControlStateNormal];
    [self.btnOutLineA1Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[1][0],threePhaseCurrentLeft[1][1],threePhaseCurrentLeft[1][2]] forState:UIControlStateNormal];
    [self.btnOutLineA2Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[2][0],threePhaseCurrentLeft[2][1],threePhaseCurrentLeft[2][2]] forState:UIControlStateNormal];
    [self.btnOutLineA3Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[3][0],threePhaseCurrentLeft[3][1],threePhaseCurrentLeft[3][2]] forState:UIControlStateNormal];
    
    [self.btnInLineBValue setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[0][0],threePhaseCurrentRight[0][1],threePhaseCurrentRight[0][2]] forState:UIControlStateNormal];
    [self.btnOutLineB1Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[1][0],threePhaseCurrentRight[1][1],threePhaseCurrentRight[1][2]] forState:UIControlStateNormal];
    [self.btnOutLineB2Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[2][0],threePhaseCurrentRight[2][1],threePhaseCurrentRight[2][2]] forState:UIControlStateNormal];
    [self.btnOutLineB3Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[3][0],threePhaseCurrentRight[3][1],threePhaseCurrentRight[3][2]] forState:UIControlStateNormal];
    //当前负荷
    [self.lblCurrentLoad setText:[NSString stringWithFormat:@"%.2fkW",self.currentTotalBurden/1000]];
    //当前总电量
    [self.lblElectricity setText:[NSString stringWithFormat:@"%.2fkWh",self.currentTotalElectricity]];
}

//显示开关的状态
- (void)displaySwitchStatus
{
    if(finalB[0]){
        [self.btnInLineA setTitle:@"进线A-合" forState:UIControlStateNormal];
    }else{
        [self.btnInLineA setTitle:@"进线A-分" forState:UIControlStateNormal];
    }
    if(finalB[1]){
        [self.btnOutLineA1 setTitle:@"出线A-1-合" forState:UIControlStateNormal];
    }else{
        [self.btnOutLineA1 setTitle:@"出线A-1-分" forState:UIControlStateNormal];
    }
    if(finalB[2]){
        [self.btnOutLineA2 setTitle:@"出线A-2-合" forState:UIControlStateNormal];
    }else{
        [self.btnOutLineA2 setTitle:@"出线A-2-分" forState:UIControlStateNormal];
    }
    if(finalB[3]){
        [self.btnOutLineA3 setTitle:@"出线A-3-合" forState:UIControlStateNormal];
    }else{
        [self.btnOutLineA3 setTitle:@"出线A-3-分" forState:UIControlStateNormal];
    }
    if(finalB[4]){
        [self.btnInLineB setTitle:@"进线B-合" forState:UIControlStateNormal];
    }else{
        [self.btnInLineB setTitle:@"进线B-分" forState:UIControlStateNormal];
    }
    if(finalB[5]){
        [self.btnOutLineB1 setTitle:@"出线B-1-合" forState:UIControlStateNormal];
    }else{
        [self.btnOutLineB1 setTitle:@"出线B-1-分" forState:UIControlStateNormal];
    }
    if(finalB[6]){
        [self.btnOutLineB2 setTitle:@"出线B-2-合" forState:UIControlStateNormal];
    }else{
        [self.btnOutLineB2 setTitle:@"出线B-2-分" forState:UIControlStateNormal];
    }
    if(finalB[7]){
        [self.btnOutLineB3 setTitle:@"出线B-3-合" forState:UIControlStateNormal];
    }else{
        [self.btnOutLineB3 setTitle:@"出线B-3-分" forState:UIControlStateNormal];
    }
    if(finalB9){
        [self.btnMotherOf setTitle:@"母联开关-合" forState:UIControlStateNormal];
    }else{
        [self.btnMotherOf setTitle:@"母联开关-分" forState:UIControlStateNormal];
    }
}

//我要报名
- (IBAction)onClickSignup:(id)sender {
    STSignupViewController *signupViewController=[[STSignupViewController alloc]init];
    [self.navigationController pushViewController:signupViewController animated:YES];
}

- (IBAction)onClickSwitch:(id)sender
{
    
}

- (IBAction)onClickLoadDetail:(id)sender
{
    
}

@end