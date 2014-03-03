//
//  STUserExperienceAlarmViewController.m
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceAlarmViewController.h"
#import "STUserExperienceLineDetailViewController.h"

@interface STUserExperienceAlarmViewController ()

@end

@implementation STUserExperienceAlarmViewController

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

- (void)viewDidLoad
{
    [super viewDidLoad];
    //以后则每根据设定的时间调用一次
    self.timer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(startBusinessCal) userInfo:nil repeats:YES];
    self.timerElectricity = [NSTimer scheduledTimerWithTimeInterval:60 target:self selector:@selector(totalElectricity) userInfo:nil repeats:YES];
    
}

- (void)onClickCurrentDetailInfo:(UITapGestureRecognizer*)sender
{
    UILabel *lblSender=((UILabel*)sender.view);
    long tag=lblSender.tag;
    STUserExperienceLineDetailViewController *userExperienceLineDetailViewController=[[STUserExperienceLineDetailViewController alloc]initWithIndex:tag];
    [self.navigationController pushViewController:userExperienceLineDetailViewController animated:YES];
}

- (IBAction)onClickSwitch:(id)sender
{
 
    UIButton *send=(UIButton*)sender;
    long tag=(send).tag;
    
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
    threePhaseCurrentLeft[0][0]=self.electricCurrentLeftA;
    threePhaseCurrentLeft[0][1]=self.electricCurrentLeftB;
    threePhaseCurrentLeft[0][2]=self.electricCurrentLeftC;
    threePhaseCurrentRight[0][0]=self.electricCurrentRightA;
    threePhaseCurrentRight[0][1]=self.electricCurrentRightB;
    threePhaseCurrentRight[0][2]=self.electricCurrentRightC;
    
    [self startCalculate];
    
    [self displayElectricCurrent];
    
    [self displaySwitchStatus];
    
}


- (IBAction)onClickLoadDetail:(id)sender {
    long tag=((UIButton*)sender).tag;
    STUserExperienceLineDetailViewController *userExperienceLineDetailViewController=[[STUserExperienceLineDetailViewController alloc]initWithIndex:tag];
    [self.navigationController pushViewController:userExperienceLineDetailViewController animated:YES];
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
                self.isTransLoad=YES;
                self.electricCurrentLeftA=[self transCurrent:value];
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
        self.isTransLoad=NO;
        [self startBusinessCal];
    }
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

@end
