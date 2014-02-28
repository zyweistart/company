//
//  STUserExperienceAlarmViewController.m
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceAlarmViewController.h"
#import "STUserExperienceLineDetailViewController.h"

#define DISPLAYLINESTR @"ia=%.2f;\nib=%.2f;\nic=%.2f;"

@interface STUserExperienceAlarmViewController ()<UIAlertViewDelegate,UIActionSheetDelegate>

@end

@implementation STUserExperienceAlarmViewController{
    NSTimer * timer;
    NSTimer * timerElectricity;
}

double electricCurrentLeftA=0.0;
double electricCurrentLeftB=0.0;
double electricCurrentLeftC=0.0;
double electricCurrentRightA=0.0;
double electricCurrentRightB=0.0;
double electricCurrentRightC=0.0;

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
//是否为超负荷体验
bool isTransLoad;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        self.title=@"体验站电气主接线图";
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
        
    }
    return self;
}

- (void)back:(id)sender
{
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
    isTransLoad=NO;
    iaTempLastValue=0;
    //初始默认当前总电量为
    currentTotalElectricity=5;
    //初始调用一次
    [self startBusinessCal];
    [self displaySwitchStatus];
    //以后则每根据设定的时间调用一次
    timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(startBusinessCal) userInfo:nil repeats:YES];
    timerElectricity = [NSTimer scheduledTimerWithTimeInterval:60 target:self selector:@selector(totalElectricity) userInfo:nil repeats:YES];
}

//生成一个0～1之间的随机数小数点后保留两位
- (double)random {
    int r=arc4random() % 100;
    return (double)r/100;
}

// 计算当前时间的商业电价
- (double)businessCalculationTime{
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

//计算负荷超限报警通过开关状态，确定负荷电流
- (double)transCurrent:(double)load {
    double current = 0;
    //随机生成一个0.9~1的随机数
    double d=((double)(arc4random() % 10)/100)+0.9;
    if (!finalB[0] || !finalB[4]) {
        load = load;
    } else if ((!finalB[1] && !finalB[2] && !finalB[3])
               || (!finalB[5] && !finalB[6] && !finalB[7])) {
        load = load;
    } else {
        load = load / 2;
    }
    current = load * 1000 / 220 / d / 3 * 1.15;
    return current;
}

- (void)buildCal
{
    double rn1=[self random];
    if(rn1>0.5){
        electricCurrentLeftB=electricCurrentLeftA*1.05;
        electricCurrentLeftC=electricCurrentLeftA*0.95;
    }else{
        electricCurrentLeftB=electricCurrentLeftA*0.95;
        electricCurrentLeftC=electricCurrentLeftA*1.05;
    }
    
    //进线B
    double rn2=[self random];
    if(rn2>0.5){
        electricCurrentRightA=electricCurrentLeftA*1.05;
    }else{
        electricCurrentRightA=electricCurrentLeftA*0.95;
    }
    
    double rn3=[self random];
    if(rn3>0.5){
        electricCurrentRightB=electricCurrentRightA*1.05;
        electricCurrentRightC=electricCurrentRightA*0.95;
    }else{
        electricCurrentRightB=electricCurrentRightA*0.95;
        electricCurrentRightC=electricCurrentRightA*1.05;
    }
    
    for(int i=0;i<4;i++){
        for(int j=0;j<3;j++){
            threePhaseCurrentLeft[i][j]=0.0;
            threePhaseCurrentRight[i][j]=0.0;
        }
    }
    
    threePhaseCurrentLeft[0][0]=electricCurrentLeftA;
    threePhaseCurrentLeft[0][1]=electricCurrentLeftB;
    threePhaseCurrentLeft[0][2]=electricCurrentLeftC;
    threePhaseCurrentRight[0][0]=electricCurrentRightA;
    threePhaseCurrentRight[0][1]=electricCurrentRightB;
    threePhaseCurrentRight[0][2]=electricCurrentRightC;
    
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

- (void)startBusinessCal {
    //进线A
    double rn0=[self random];
    if(isTransLoad){
        int sign=1;
        if (rn0 > 0.5) {
            sign = -1;
        }
        //保证每次产生的值都在最后一次的基础之上上下浮动5%
        electricCurrentLeftA=electricCurrentLeftA*sign*0.05+electricCurrentLeftA;
    }else{
        if(iaTempLastValue==0){
            //第一次随机数保证rn的值在0.1-1之间
            if(rn0<0.1){
                rn0=0.1+rn0;
            }
            electricCurrentLeftA=1443.4*rn0;
        }else{
            int sign=1;
            if (rn0 > 0.5) {
                sign = -1;
            }
            //保证每次产生的值都在最后一次的基础之上上下浮动5%
            electricCurrentLeftA=iaTempLastValue*sign*0.05+iaTempLastValue;
        }
        
        if(electricCurrentLeftA>1443.4){
            electricCurrentLeftA=1345;
        }else if(electricCurrentLeftA<144.34){
            electricCurrentLeftA=160;
        }
        iaTempLastValue=electricCurrentLeftA;
    }
    [self buildCal];
    
}

- (void)startCalculate {
    
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
    if(currentTotalBurden>0){
        lastTotalBurden=currentTotalBurden;
    }
    currentTotalBurden=tmpBurden;
}

//总电量(更新调用频率一分钟)
- (void)totalElectricity {
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
    currentTotalElectricity=currentTotalElectricity+(lastTotalBurden+currentTotalBurden)/2/60/1000;
}

//显示当前的电流
- (void)displayElectricCurrent {
    //页面显示电流信息
    [_btnInLineAValue setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[0][0],threePhaseCurrentLeft[0][1],threePhaseCurrentLeft[0][2]] forState:UIControlStateNormal];
    [_btnOutLineA1Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[1][0],threePhaseCurrentLeft[1][1],threePhaseCurrentLeft[1][2]] forState:UIControlStateNormal];
    [_btnOutLineA2Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[2][0],threePhaseCurrentLeft[2][1],threePhaseCurrentLeft[2][2]] forState:UIControlStateNormal];
    [_btnOutLineA3Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[3][0],threePhaseCurrentLeft[3][1],threePhaseCurrentLeft[3][2]] forState:UIControlStateNormal];
    
    [_btnInLineBValue setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[0][0],threePhaseCurrentRight[0][1],threePhaseCurrentRight[0][2]] forState:UIControlStateNormal];
    [_btnOutLineB1Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[1][0],threePhaseCurrentRight[1][1],threePhaseCurrentRight[1][2]] forState:UIControlStateNormal];
    [_btnOutLineB2Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[2][0],threePhaseCurrentRight[2][1],threePhaseCurrentRight[2][2]] forState:UIControlStateNormal];
    [_btnOutLineB3Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[3][0],threePhaseCurrentRight[3][1],threePhaseCurrentRight[3][2]] forState:UIControlStateNormal];
    //当前负荷
    [_lblCurrentLoad setText:[NSString stringWithFormat:@"%.2fkW",currentTotalBurden/1000]];
    //当前总电量
    [_lblElectricity setText:[NSString stringWithFormat:@"%.2fkWh",currentTotalElectricity]];
}

//显示开关的状态
- (void)displaySwitchStatus {
    if(finalB[0]){
        [_btnInLineA setTitle:@"进线A-合" forState:UIControlStateNormal];
    }else{
        [_btnInLineA setTitle:@"进线A-分" forState:UIControlStateNormal];
    }
    if(finalB[1]){
        [_btnOutLineA1 setTitle:@"出线A-1-合" forState:UIControlStateNormal];
    }else{
        [_btnOutLineA1 setTitle:@"出线A-1-分" forState:UIControlStateNormal];
    }
    if(finalB[2]){
        [_btnOutLineA2 setTitle:@"出线A-2-合" forState:UIControlStateNormal];
    }else{
        [_btnOutLineA2 setTitle:@"出线A-2-分" forState:UIControlStateNormal];
    }
    if(finalB[3]){
        [_btnOutLineA3 setTitle:@"出线A-3-合" forState:UIControlStateNormal];
    }else{
        [_btnOutLineA3 setTitle:@"出线A-3-分" forState:UIControlStateNormal];
    }
    if(finalB[4]){
        [_btnInLineB setTitle:@"进线B-合" forState:UIControlStateNormal];
    }else{
        [_btnInLineB setTitle:@"进线B-分" forState:UIControlStateNormal];
    }
    if(finalB[5]){
        [_btnOutLineB1 setTitle:@"出线B-1-合" forState:UIControlStateNormal];
    }else{
        [_btnOutLineB1 setTitle:@"出线B-1-分" forState:UIControlStateNormal];
    }
    if(finalB[6]){
        [_btnOutLineB2 setTitle:@"出线B-2-合" forState:UIControlStateNormal];
    }else{
        [_btnOutLineB2 setTitle:@"出线B-2-分" forState:UIControlStateNormal];
    }
    if(finalB[7]){
        [_btnOutLineB3 setTitle:@"出线B-3-合" forState:UIControlStateNormal];
    }else{
        [_btnOutLineB3 setTitle:@"出线B-3-分" forState:UIControlStateNormal];
    }
    if(finalB9){
        [_btnMotherOf setTitle:@"母联开关-合" forState:UIControlStateNormal];
    }else{
        [_btnMotherOf setTitle:@"母联开关-分" forState:UIControlStateNormal];
    }
}

- (IBAction)onClickSwitch:(id)sender {
    
    long tag=((UIButton*)sender).tag;
    if(tag==0){
        //母联开关
        if(finalB9){
            if(!finalB[0]&&finalB[4]){
                [Common alert:@"请先断开母联开关，再合上进线开关。"];
                return;
            }
        }
        
        if(finalB[0]&&!finalB[4]){
            [Common alert:@"进线开关全部断开将造成全站失电。"];
            return;
        }
        
    }else if(tag==4){
        //母联开关
        if(finalB9){
            if(finalB[0]&&!finalB[4]){
                [Common alert:@"请先断开母联开关，再合上进线开关。"];
                return;
            }
        }
        
        if(!finalB[0]&&finalB[4]){
            [Common alert:@"进线开关全部断开将造成全站失电。"];
            return;
        }
        
    }else if(tag==8){
        if(!finalB9){
            if(finalB[0]&&finalB[4]){
                [Common alert:@"先断开一条进线开关后，才可再合上母联开关。"];
                return;
            }
        }
        finalB9=!finalB9;
    }
    if(tag<8) {
        finalB[tag]=!finalB[tag];
    }
    
    //把最后一次生成的电流值重新进行赋值计算
    threePhaseCurrentLeft[0][0]=electricCurrentLeftA;
    threePhaseCurrentLeft[0][1]=electricCurrentLeftB;
    threePhaseCurrentLeft[0][2]=electricCurrentLeftC;
    threePhaseCurrentRight[0][0]=electricCurrentRightA;
    threePhaseCurrentRight[0][1]=electricCurrentRightB;
    threePhaseCurrentRight[0][2]=electricCurrentRightC;
    
    [self startCalculate];
    
    [self displayElectricCurrent];
    
    [self displaySwitchStatus];
    
}

- (IBAction)onClickLoadDetail:(id)sender {
    long tag=((UIButton*)sender).tag;
    STUserExperienceLineDetailViewController *userExperienceLineDetailViewController=[[STUserExperienceLineDetailViewController alloc]initWithIndex:tag];
    [self.navigationController pushViewController:userExperienceLineDetailViewController animated:YES];
}

//我要报名
- (IBAction)onClickSignup:(id)sender {
    NSLog(@"我要报名");
}

//负荷超限报警体验
- (IBAction)onClickAlarmExperience:(id)sender {
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"企业总负荷超限报警体验阈值"
                          message:@"提示：输入阈值范围在0～2500之间"
                          delegate:self
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:@"取消",nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    //设置输入框的键盘类型
    UITextField *tf = [alert textFieldAtIndex:0];
    tf.keyboardType = UIKeyboardTypeNumberPad;
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex==0){
        NSString *content=[[alertView textFieldAtIndex:0]text];
        if(![@"" isEqualToString:content]){
            int value=[content intValue];
            if(value>0&&value<2500){
                isTransLoad=YES;
                electricCurrentLeftA=[self transCurrent:value];
                [self buildCal];
                UIActionSheet *sheet = [[UIActionSheet alloc]
                                        initWithTitle:@"企业总负荷超过所设定的阀值，请注意！"
                                        delegate:self
                                        cancelButtonTitle:nil
                                        destructiveButtonTitle:@"确定"
                                        otherButtonTitles:nil,nil];
                [sheet showInView:[UIApplication sharedApplication].keyWindow];
                //开启报警声音
            }else{
                [Common alert:@"输入阈值范围在0～2500之间"];
            }
        }else{
            [Common alert:@"阈值不能为空"];
        }
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex==0){
        //关闭报警声音
        isTransLoad=NO;
        [self startBusinessCal];
    }
}

@end
