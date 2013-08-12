//
//  ACAccountRechargeCell.m
//  Ancun
//
//  Created by Start on 13-8-7.
//
//

#import "ACAccountRechargeCell.h"
#import "ACRechargeConfirmViewController.h"

@implementation ACAccountRechargeCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setBackgroundColor:[UIColor colorWithRed:147 green:222 blue:250 alpha:1]];
        
        _lblName=[[UILabel alloc]initWithFrame:CGRectMake(8, 5, 150, 25)];
        [_lblName setFont:[UIFont systemFontOfSize:17]];
        [_lblName setTextAlignment:NSTextAlignmentLeft];
        [_lblName setTextColor:[UIColor grayColor]];
//        [_lblName setText:@"100元基础包月套餐"];
        [self addSubview:_lblName];
        
        _lblTimeLong=[[UILabel alloc]initWithFrame:CGRectMake(8, 30, 150, 25)];
        [_lblTimeLong setFont:[UIFont systemFontOfSize:15]];
        [_lblTimeLong setTextAlignment:NSTextAlignmentLeft];
        [_lblTimeLong setTextColor:[UIColor grayColor]];
//        [_lblTimeLong setText:@"有效期31天"];
        [self addSubview:_lblTimeLong];
        
        if([@"ACAccountRechargeCell1" isEqualToString:reuseIdentifier]) {
            _lblTime=[[UILabel alloc]initWithFrame:CGRectMake(170, 8, 60, 22)];
            [_lblTime setFont:[UIFont systemFontOfSize:15]];
            [_lblTime setTextAlignment:NSTextAlignmentRight];
            [_lblTime setTextColor:[UIColor redColor]];
//            [_lblTime setText:@"209分钟"];
            [self addSubview:_lblTime];

            _lblStorage=[[UILabel alloc]initWithFrame:CGRectMake(170, 30, 60, 22)];
            [_lblStorage setFont:[UIFont systemFontOfSize:15]];
            [_lblStorage setTextAlignment:NSTextAlignmentRight];
            [_lblStorage setTextColor:[UIColor redColor]];
//            [_lblStorage setText:@"309MB"];
            [self addSubview:_lblStorage];
        } else {
            _lblTimeAndStorage=[[UILabel alloc]initWithFrame:CGRectMake(170, 15, 60, 30)];
            [_lblTimeAndStorage setFont:[UIFont systemFontOfSize:15]];
            [_lblTimeAndStorage setTextAlignment:NSTextAlignmentRight];
            [_lblTimeAndStorage setTextColor:[UIColor redColor]];
//            [_lblStorage setText:@"309MB"];
            [self addSubview:_lblTimeAndStorage];
        }
        
        _btnGoPay=[UIButton buttonWithType:UIButtonTypeCustom];
        [_btnGoPay setFrame:CGRectMake(260, 20, 50, 20)];
        [_btnGoPay setImage:[UIImage imageNamed:@"accountpay_normal"] forState:UIControlStateNormal];
        [_btnGoPay setBackgroundImage:[UIImage imageNamed:@"accountpay_pressed"] forState:UIControlStateHighlighted];
        [_btnGoPay addTarget:self action:@selector(goPay:) forControlEvents:UIControlEventTouchDown];
        [self addSubview:_btnGoPay];
        
        [self setSelectionStyle:UITableViewCellSelectionStyleNone];
    }
    return self;
}

- (void)goPay:(id)sender {
    ACRechargeConfirmViewController *rechargeConfirm=[[ACRechargeConfirmViewController alloc] init];
    [rechargeConfirm setCurrentType:_currentType];
    [rechargeConfirm setData:_data];
    if(_currentType ==1) {
        if([[Config Instance]isOldUser]&&[[[[Config Instance] userInfo]objectForKey:@"payuserflag"]intValue]==1){
            int rectime=[[[[Config Instance]userInfo]objectForKey:@"rectime"]intValue]/60;
            [Common actionSheet:self message:[NSString stringWithFormat:@"您原时长套餐还剩%d分钟，衷心建议您用完原套餐再充值，如果您坚持继续充值，原套餐中的可录音时长会被清零，多可惜啊",rectime] tag:1];
        } else {
            //新用户充值
            [_controler.navigationController pushViewController:rechargeConfirm animated:YES];
        }
    } else if (_currentType==2) {
        
        if([[Config Instance]isOldUser]){
            [Common alert:@"要先购买基础包月套餐后才能购买增值时长套餐，打好基础很重要哦"];
        } else {
            if([[Config Instance]isPayBase]){
                [_controler.navigationController pushViewController:rechargeConfirm animated:YES];
            } else {
                [Common alert:@"请先购买基础套餐"];
            }
        }
    } else if (_currentType==3) {
        if([[Config Instance]isOldUser]){
            [Common alert:@"要先购买基础包月套餐后才能购买增值存储套餐，更多空间更多安心"];
        } else {
            if([[Config Instance]isPayBase]){
                [_controler.navigationController pushViewController:rechargeConfirm animated:YES];
            } else {
                [Common alert:@"请先购买基础套餐"];
            }
        }
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(actionSheet.tag==1){
        if(buttonIndex==0){
            ACRechargeConfirmViewController *rechargeConfirm=[[ACRechargeConfirmViewController alloc] init];
            [rechargeConfirm setCurrentType:_currentType];
            [rechargeConfirm setData:_data];
            [_controler.navigationController pushViewController:rechargeConfirm animated:YES];
        }
    }
}

@end
