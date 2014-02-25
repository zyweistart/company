//
//  STUserExperienceAlarmViewController.m
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceAlarmViewController.h"

#import "STChartViewController.h"

#define DISPLAYLINESTR @"ia=%.2f;\nib=%.2f;\nic=%.2f;"

@interface STUserExperienceAlarmViewController ()

@end

@implementation STUserExperienceAlarmViewController{
    NSTimer * timer;
}

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

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self cal1];
    [self displayerButtonName];
    
    for (int i = 0; i < 8; i++) {
        finalB[i] = YES;
    }
    finalB9 = NO;
    
    timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(cal1) userInfo:nil repeats:YES];
}

double threePhaseCurrentLeft[4][3];
double threePhaseCurrentRight[4][3];
bool finalB[8]={YES,YES,YES,YES,YES,YES,YES,YES};
bool finalB9=NO;// 中间开关，默认为跳闸状态

- (void) cal1 {
    
    for(int i=0;i<4;i++){
        for(int j=0;j<3;j++){
            threePhaseCurrentLeft[i][j]=0.0;
            threePhaseCurrentRight[i][j]=0.0;
        }
    }
    
    //生成一个随机数
    int r0=arc4random() % 100;
    int r1=arc4random() % 100;
    int r2=arc4random() % 100;
    int r3=arc4random() % 100;
    
    double rn0=(double)r0/100;
    double rn1=((double)r1/100);
    double rn2=((double)r2/100);
    double rn3=((double)r3/100);
    
    double electricCurrentLeftA=0.0;
    double electricCurrentLeftB=0.0;
    double electricCurrentLeftC=0.0;
    double electricCurrentRightA=0.0;
    double electricCurrentRightB=0.0;
    double electricCurrentRightC=0.0;
    
    //保证rn的值在0.1-1之间
    if(rn0<0.1){
        rn0=0.1+rn0;
    }
    
    //进线A
    electricCurrentLeftA=1443.4*rn0;
    
    if(electricCurrentLeftA>1443.4){
        electricCurrentLeftA=1345;
    }else if(electricCurrentLeftA<144.34){
        electricCurrentLeftA=160;
    }
    
    if(rn1>0.5){
        electricCurrentLeftB=electricCurrentLeftA*1.05;
        electricCurrentLeftC=electricCurrentLeftA*0.95;
    }else{
        electricCurrentLeftB=electricCurrentLeftA*0.95;
        electricCurrentLeftC=electricCurrentLeftA*1.05;
    }
    
    //进线B
    if(rn2>0.5){
        electricCurrentRightA=electricCurrentLeftA*1.05;
    }else{
        electricCurrentRightA=electricCurrentLeftA*0.95;
    }
    
    if(rn3>0.5){
        electricCurrentRightB=electricCurrentRightA*1.05;
        electricCurrentRightC=electricCurrentRightA*0.95;
    }else{
        electricCurrentRightB=electricCurrentRightA*0.95;
        electricCurrentRightC=electricCurrentRightA*1.05;
    }
    
    threePhaseCurrentLeft[0][0]=electricCurrentLeftA;
    threePhaseCurrentLeft[0][1]=electricCurrentLeftB;
    threePhaseCurrentLeft[0][2]=electricCurrentLeftC;
    threePhaseCurrentRight[0][0]=electricCurrentRightA;
    threePhaseCurrentRight[0][1]=electricCurrentRightB;
    threePhaseCurrentRight[0][2]=electricCurrentRightC;
    
    //母联开关是否合并
    for(int i=0;i<3;i++) {
        if (finalB9) {
            consolidated(i);
        } else {
            calculate1(threePhaseCurrentLeft, i, 0);
            calculate1(threePhaseCurrentRight, i, 1);
        }
    }
    
    //页面显示电流信息
    [_btnInLineAValue setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[0][0],threePhaseCurrentLeft[0][1],threePhaseCurrentLeft[0][2]] forState:UIControlStateNormal];
    [_btnOutLineA1Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[1][0],threePhaseCurrentLeft[1][1],threePhaseCurrentLeft[1][2]] forState:UIControlStateNormal];
    [_btnOutLineA2Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[2][0],threePhaseCurrentLeft[2][1],threePhaseCurrentLeft[2][2]] forState:UIControlStateNormal];
    [_btnOutLineA3Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentLeft[3][0],threePhaseCurrentLeft[3][1],threePhaseCurrentLeft[3][2]] forState:UIControlStateNormal];
    
    [_btnInLineBValue setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[0][0],threePhaseCurrentRight[0][1],threePhaseCurrentRight[0][2]] forState:UIControlStateNormal];
    [_btnOutLineB1Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[1][0],threePhaseCurrentRight[1][1],threePhaseCurrentRight[1][2]] forState:UIControlStateNormal];
    [_btnOutLineB2Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[2][0],threePhaseCurrentRight[2][1],threePhaseCurrentRight[2][2]] forState:UIControlStateNormal];
    [_btnOutLineB3Value setTitle:[NSString stringWithFormat:DISPLAYLINESTR,threePhaseCurrentRight[3][0],threePhaseCurrentRight[3][1],threePhaseCurrentRight[3][2]] forState:UIControlStateNormal];
}

void calculate1(double threePhaseCurrent[4][3], int index, int j) {
    if(!finalB[0]||!finalB[4]) {
        threePhaseCurrent[0][index]=0;
        threePhaseCurrent[1][index]=0;
        threePhaseCurrent[2][index]=0;
        threePhaseCurrent[3][index]=0;
    } else {
        double electricCurrent=threePhaseCurrent[0][index];
        if(j==0){
            threePhaseCurrent[1][index] = electricCurrent * 0.2;// I*1
            threePhaseCurrent[2][index] = electricCurrent * 0.3;// I*2
            threePhaseCurrent[3][index] = electricCurrent * 0.5;// I*3
            if (!finalB[1]) {
                threePhaseCurrent[0][index] = threePhaseCurrent[0][index] - threePhaseCurrent[1][index];
                threePhaseCurrent[1][index] = 0.0;
            }
            if (!finalB[2]) {
                threePhaseCurrent[0][index] = threePhaseCurrent[0][index] - threePhaseCurrent[2][index];
                threePhaseCurrent[2][index] = 0.0;
            }
            if (!finalB[3]) {
                threePhaseCurrent[0][index] = threePhaseCurrent[0][index] - threePhaseCurrent[3][index];
                threePhaseCurrent[3][index] = 0.0;
            }
        } else {
            threePhaseCurrent[1][index] = electricCurrent * 0.3;// I*1
            threePhaseCurrent[2][index] = electricCurrent * 0.5;// I*2
            threePhaseCurrent[3][index] = electricCurrent * 0.2;// I*3
            if (!finalB[5]) {
                threePhaseCurrent[0][index] = threePhaseCurrent[0][index]
                - threePhaseCurrent[1][index];
                threePhaseCurrent[1][index] = 0.0;
            }
            if (!finalB[6]) {
                threePhaseCurrent[0][index] = threePhaseCurrent[0][index]
                - threePhaseCurrent[2][index];
                threePhaseCurrent[2][index] = 0.0;
            }
            if (!finalB[7]) {
                threePhaseCurrent[0][index] = threePhaseCurrent[0][index]
                - threePhaseCurrent[3][index];
                threePhaseCurrent[3][index] = 0.0;
            }
        }
        if (threePhaseCurrent[0][index] < 5){
            threePhaseCurrent[0][index] = 0.0;
        }
    }
}

void consolidated(int index) {
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
        //进线A
        if (finalB[0]) {
            threePhaseCurrentRight[0][index] = 0.0;
            if (electricCurrent < 5) {
                threePhaseCurrentLeft[0][index] = 0.0;
            } else {
                threePhaseCurrentLeft[0][index] = electricCurrent;
            }
        }
        //进线B
        if (finalB[4]) {
            threePhaseCurrentLeft[0][index] = 0.0;
            if (electricCurrent < 5) {
                threePhaseCurrentRight[0][index] = 0.0;
            } else {
                threePhaseCurrentRight[0][index] = electricCurrent;
            }
        }
        
    }
    
}

//总电量
- (void)totalcurrentLoad {
    
}

//总负荷=总有功功率
- (void)totalElectricity {
    
}

- (void)displayerButtonName {
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
    
    UIButton *btnSender=((UIButton*)sender);
    long tag=((UIButton*)sender).tag;
    
    if(tag==4){
        if(!finalB[tag]){
            if(finalB[0]&&finalB[4]){
                [Common alert:@"先断开一条进线开关后，才可再合上母联开关。"];
                return;
            }
        }
    }else if(tag==0){
        if(!finalB9){
            if(!finalB[0]&&finalB[4]){
                [Common alert:@"请先断开母联开关，再合上进线开关。"];
                return;
            }
        }
        
        if(finalB[0]&&!finalB[4]){
            [Common alert:@"进线开关全部断开将造成全站失电。"];
            return;
        }
        
    }else if(tag==5){
        if(finalB[4]){
            if(finalB[0]&&!finalB[5]){
                [Common alert:@"请先断开母联开关，再合上进线开关。"];
                return;
            }
        }
        
        if(!finalB[0]&&finalB[4]){
            [Common alert:@"进线开关全部断开将造成全站失电。"];
            return;
        }
        
    }
    
    NSString *flagStr=nil;
    if(finalB[tag]){
        finalB[tag]=NO;
        flagStr=@"分";
    }else{
        finalB[tag]=YES;
        flagStr=@"合";
    }
    
    NSString *displayerStr=nil;
    if(tag==0){
       displayerStr=[NSString stringWithFormat:@"进线A-%@",flagStr];
    }else if(tag==1){
       displayerStr=[NSString stringWithFormat:@"出线A-1-%@",flagStr];
    }else if(tag==2){
       displayerStr=[NSString stringWithFormat:@"出线A-2-%@",flagStr];
    }else if(tag==3){
       displayerStr=[NSString stringWithFormat:@"出线A-3-%@",flagStr];
    }else if(tag==4){
       displayerStr=[NSString stringWithFormat:@"母联开关-%@",flagStr];
    }else if(tag==5){
       displayerStr=[NSString stringWithFormat:@"进线B-%@",flagStr];
    }else if(tag==6){
       displayerStr=[NSString stringWithFormat:@"出线B-1-%@",flagStr];
    }else if(tag==7){
       displayerStr=[NSString stringWithFormat:@"出线B-2-%@",flagStr];
    }else if(tag==8){
       displayerStr=[NSString stringWithFormat:@"出线B-3-%@",flagStr];
    }
    [btnSender setTitle:displayerStr forState:UIControlStateNormal];
    //开关重置后数据重新进行计算
    [self cal1];
}

- (IBAction)onClickLoadDetail:(id)sender {
    UIButton *btnSender=((UIButton*)sender);
    long tag=btnSender.tag;
    if(tag==0){
        _currentSelectLineName=@"进线A";
    }else if(tag==1){
        _currentSelectLineName=@"出线A-1";
    }else if(tag==2){
        _currentSelectLineName=@"出线A-2";
    }else if(tag==3){
        _currentSelectLineName=@"出线A-3";
    }else if(tag==5){
        _currentSelectLineName=@"进线B";
    }else if(tag==6){
        _currentSelectLineName=@"出线B-1";
    }else if(tag==7){
        _currentSelectLineName=@"出线B-2";
    }else if(tag==8){
        _currentSelectLineName=@"出线B-3";
    }
    STChartViewController *chartViewController=[[STChartViewController alloc]init];
    [self.navigationController pushViewController:chartViewController animated:YES];
}

@end
