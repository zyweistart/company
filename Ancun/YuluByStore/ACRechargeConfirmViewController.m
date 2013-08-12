//
//  ACRechargeConfirmViewController.m
//  Ancun
//
//  Created by Start on 13-8-7.
//
//

#import "ACRechargeConfirmViewController.h"
#import "NSString+Date.h"
#import "AlixPay.h"

@interface ACRechargeConfirmViewController ()

- (void)layoutPayPakcage;
- (void)layoutPayDuration;
- (void)layoutPayStorage;
- (void)layoutPayUpgradeStorage;

//获取最后一个到底的套餐
- (NSMutableDictionary *) lastNewPackages:(int)ctype;
//当前存储套餐是否已经购买
- (BOOL) isCurrentStorageExit:(NSMutableDictionary*)data;

@end

@implementation ACRechargeConfirmViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.navigationItem.title=@"账户充值";
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.view setBackgroundColor:[UIColor grayColor]];
    
	UILabel *lblTip1=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 40)];
    [lblTip1 setFont:[UIFont systemFontOfSize:15]];
    [lblTip1 setTextAlignment:NSTextAlignmentCenter];
    [lblTip1 setBackgroundColor:[UIColor redColor]];
    [lblTip1 setTextColor:[UIColor grayColor]];
    [lblTip1 setText:@"选择套餐"];
    [self.view addSubview:lblTip1];
    
    _lblTip2=[[UILabel alloc]initWithFrame:CGRectMake(80, 0, 80, 40)];
    [_lblTip2 setFont:[UIFont systemFontOfSize:15]];
    [_lblTip2 setTextAlignment:NSTextAlignmentCenter];
    [_lblTip2 setBackgroundColor:[UIColor redColor]];
    [_lblTip2 setTextColor:[UIColor grayColor]];
    [_lblTip2 setText:@"确认信息"];
    [self.view addSubview:_lblTip2];
    
    _lblTip3=[[UILabel alloc]initWithFrame:CGRectMake(160, 0, 80, 40)];
    [_lblTip3 setFont:[UIFont systemFontOfSize:15]];
    [_lblTip3 setTextAlignment:NSTextAlignmentCenter];
    [_lblTip3 setTextColor:[UIColor grayColor]];
    [_lblTip3 setText:@"支付"];
    [self.view addSubview:_lblTip3];
    
    _lblTip4=[[UILabel alloc]initWithFrame:CGRectMake(240, 0, 80, 40)];
    [_lblTip4 setFont:[UIFont systemFontOfSize:15]];
    [_lblTip4 setTextAlignment:NSTextAlignmentCenter];
    [_lblTip4 setTextColor:[UIColor grayColor]];
    [_lblTip4 setText:@"成功"];
    [self.view addSubview:_lblTip4];
    
    if(_currentType==1) {
        //基础套餐
        [self layoutPayPakcage];
    } else if(_currentType==2) {
        //时长
        [self layoutPayDuration];
    } else if(_currentType==3) {
        //存储
        BOOL storageflag=NO;
        for (NSMutableDictionary *data in [[Config Instance] currentPackagesList]) {
            if([[data objectForKey:@"ctype"]intValue]==1){
                //标记当前套餐列表中是否已经存在包月存储套餐
                storageflag=YES;
            }
        }
        if(storageflag) {
            //已经购买过升级
            [self layoutPayUpgradeStorage];
        } else {
            //初次购买
            [self layoutPayStorage];
        }
    }
    
}

- (void)layoutPayPakcage{
    
    UIView *mainView=[[UIView alloc]initWithFrame:CGRectMake(10, 50, 300, 300)];
    [mainView setBackgroundColor:[UIColor whiteColor]];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 40, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐名称:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 40, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[_data objectForKey:@"name"]];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 70, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐价值:"];
    [mainView addSubview:lbl1];
    //价值
    UILabel *lblValue=[[UILabel alloc]initWithFrame:CGRectMake(115, 70, 150, 30)];
    [lblValue setFont:[UIFont systemFontOfSize:15]];
    [lblValue setTextColor:[UIColor redColor]];
    [lblValue setText:[NSString stringWithFormat:@"%@分钟 %@MB",[_data objectForKey:@"duration"],[_data objectForKey:@"storage"]]];
    [mainView addSubview:lblValue];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 100, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"支付金额:"];
    [mainView addSubview:lbl1];
    //支付金额
    UILabel *lblMoney=[[UILabel alloc]initWithFrame:CGRectMake(115, 100, 70, 30)];
    [lblMoney setFont:[UIFont systemFontOfSize:15]];
    [lblMoney setTextColor:[UIColor redColor]];
    [lblMoney setText:[NSString stringWithFormat:@"%@元",[_data objectForKey:@"newprice"]]];
    [mainView addSubview:lblMoney];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 130, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"生效日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblStartDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 130, 150, 30)];
    [lblStartDay setFont:[UIFont systemFontOfSize:15]];
    [lblStartDay setTextColor:[UIColor redColor]];
    [mainView addSubview:lblStartDay];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 160, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"到期日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblEndDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 160, 150, 30)];
    [lblEndDay setFont:[UIFont systemFontOfSize:15]];
    [lblEndDay setTextColor:[UIColor redColor]];
    [mainView addSubview:lblEndDay];
    
    NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
    [formatter setDateStyle:NSDateFormatterFullStyle];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSTimeInterval secondsPerDay=24*60*60*[[_data objectForKey:@"valid"]intValue];
    if([[Config Instance] isPayBase]) {
        NSMutableDictionary *nData=[self lastNewPackages:3];
        NSDate *nEndtime=[[nData objectForKey:@"endtime"] stringConvertDate];
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        [lblStartDay setText:[formatter stringFromDate:[nEndtime dateByAddingTimeInterval:24*60*60]]];
        [lblEndDay setText:[formatter stringFromDate:[nEndtime dateByAddingTimeInterval:secondsPerDay]]];
    } else {
        NSDate *date=[[NSDate alloc]init];
        NSDate *tomorrow=[NSDate dateWithTimeIntervalSinceNow:secondsPerDay];
        [lblStartDay setText:[formatter stringFromDate:date]];
        [lblEndDay setText:[formatter stringFromDate:tomorrow]];
    }
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 190, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"充值账户:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 190, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[[[Config Instance]userInfo]objectForKey:@"phone"]];
    [mainView addSubview:lbl1];
    
    UIButton *_btnConfirm=[UIButton buttonWithType:UIButtonTypeCustom];
    [_btnConfirm setTitle:@"确认充值" forState:UIControlStateNormal];
    [_btnConfirm setFrame:CGRectMake(97, 240, 127, 35)];
//    [_btnPay setImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm setBackgroundImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm addTarget:self action:@selector(confirm:) forControlEvents:UIControlEventTouchDown];
    [mainView addSubview:_btnConfirm];
    
    [self.view addSubview:mainView];
}

- (void)layoutPayDuration{
    UIView *mainView=[[UIView alloc]initWithFrame:CGRectMake(10, 50, 300, 300)];
    [mainView setBackgroundColor:[UIColor whiteColor]];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 40, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐名称:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 40, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[_data objectForKey:@"name"]];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 70, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"购买份数:"];
    [mainView addSubview:lbl1];
    
    UIButton *btnNum=[UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnNum setTitle:@"选择" forState:UIControlStateNormal];
    [btnNum setFrame:CGRectMake(115, 70, 50, 30)];
    [btnNum addTarget:self action:@selector(confirm:) forControlEvents:UIControlEventTouchDown];
    [mainView addSubview:btnNum];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 100, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐价值:"];
    [mainView addSubview:lbl1];
    //价值
    UILabel *lblValue=[[UILabel alloc]initWithFrame:CGRectMake(115, 100, 150, 30)];
    [lblValue setFont:[UIFont systemFontOfSize:15]];
    [lblValue setTextColor:[UIColor redColor]];
    [lblValue setText:[NSString stringWithFormat:@"%@分钟",[_data objectForKey:@"duration"]]];
    [mainView addSubview:lblValue];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 130, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"支付金额:"];
    [mainView addSubview:lbl1];
    //支付金额
    UILabel *lblMoney=[[UILabel alloc]initWithFrame:CGRectMake(115, 130, 70, 30)];
    [lblMoney setFont:[UIFont systemFontOfSize:15]];
    [lblMoney setTextColor:[UIColor redColor]];
    [lblMoney setText:[NSString stringWithFormat:@"%@元",[_data objectForKey:@"newprice"]]];
    [mainView addSubview:lblMoney];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 160, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"有效期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblStartDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 160, 150, 30)];
    [lblStartDay setFont:[UIFont systemFontOfSize:15]];
    [lblStartDay setTextColor:[UIColor redColor]];
    [lblStartDay setText:@"无时间限制"];
    [mainView addSubview:lblStartDay];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 190, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"充值账户:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 190, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[[[Config Instance]userInfo]objectForKey:@"phone"]];
    [mainView addSubview:lbl1];
    
    UIButton *_btnConfirm=[UIButton buttonWithType:UIButtonTypeCustom];
    [_btnConfirm setTitle:@"确认充值" forState:UIControlStateNormal];
    [_btnConfirm setFrame:CGRectMake(97, 240, 127, 35)];
//    [_btnPay setImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm setBackgroundImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm addTarget:self action:@selector(confirm:) forControlEvents:UIControlEventTouchDown];
    [mainView addSubview:_btnConfirm];
    
    [self.view addSubview:mainView];
}

- (void)layoutPayStorage{
    UIView *mainView=[[UIView alloc]initWithFrame:CGRectMake(10, 50, 300, 300)];
    [mainView setBackgroundColor:[UIColor whiteColor]];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 40, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐名称:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 40, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[_data objectForKey:@"name"]];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 70, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐价值:"];
    [mainView addSubview:lbl1];
    //价值
    UILabel *lblValue=[[UILabel alloc]initWithFrame:CGRectMake(115, 70, 150, 30)];
    [lblValue setFont:[UIFont systemFontOfSize:15]];
    [lblValue setTextColor:[UIColor redColor]];
    [lblValue setText:[NSString stringWithFormat:@"%@MB",[_data objectForKey:@"storage"]]];
    [mainView addSubview:lblValue];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 100, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"支付金额:"];
    [mainView addSubview:lbl1];
    //支付金额
    UILabel *lblMoney=[[UILabel alloc]initWithFrame:CGRectMake(115, 100, 70, 30)];
    [lblMoney setFont:[UIFont systemFontOfSize:15]];
    [lblMoney setTextColor:[UIColor redColor]];
    [lblMoney setText:[NSString stringWithFormat:@"%@元",[_data objectForKey:@"newprice"]]];
    [mainView addSubview:lblMoney];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 130, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"生效日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblStartDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 130, 150, 30)];
    [lblStartDay setFont:[UIFont systemFontOfSize:15]];
    [lblStartDay setTextColor:[UIColor redColor]];
    [mainView addSubview:lblStartDay];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 160, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"到期日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblEndDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 160, 150, 30)];
    [lblEndDay setFont:[UIFont systemFontOfSize:15]];
    [lblEndDay setTextColor:[UIColor redColor]];
    [mainView addSubview:lblEndDay];
    
    NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
    [formatter setDateStyle:NSDateFormatterFullStyle];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSDate *date=[[NSDate alloc]init];
    NSTimeInterval secondsPerDay=24*60*60*[[_data objectForKey:@"valid"]intValue];
    NSDate *tomorrow=[NSDate dateWithTimeIntervalSinceNow:secondsPerDay];
    [lblStartDay setText:[formatter stringFromDate:date]];
    [lblEndDay setText:[formatter stringFromDate:tomorrow]];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 190, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"充值账户:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 190, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[[[Config Instance]userInfo]objectForKey:@"phone"]];
    [mainView addSubview:lbl1];
    
    UIButton *_btnConfirm=[UIButton buttonWithType:UIButtonTypeCustom];
    [_btnConfirm setTitle:@"确认充值" forState:UIControlStateNormal];
    [_btnConfirm setFrame:CGRectMake(97, 240, 127, 35)];
    //    [_btnPay setImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm setBackgroundImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm addTarget:self action:@selector(confirm:) forControlEvents:UIControlEventTouchDown];
    [mainView addSubview:_btnConfirm];
    
    [self.view addSubview:mainView];
}

- (void)layoutPayUpgradeStorage{
    
    int HEIGHT=iPhone5?395:300;
    
    UIScrollView *sView=[[UIScrollView alloc]initWithFrame:CGRectMake(10, 50, 300, HEIGHT)];
    
    UIView *mainView=[[UIView alloc]initWithFrame:CGRectMake(0, 0, 300, 405)];
    [mainView setBackgroundColor:[UIColor whiteColor]];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentLeft];
    [lbl1 setText:@"新选套餐"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 40, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐名称:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 40, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[_data objectForKey:@"name"]];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 70, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐价值:"];
    [mainView addSubview:lbl1];
    //价值
    UILabel *lblValue=[[UILabel alloc]initWithFrame:CGRectMake(115, 70, 150, 30)];
    [lblValue setFont:[UIFont systemFontOfSize:15]];
    [lblValue setTextColor:[UIColor redColor]];
    [mainView addSubview:lblValue];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 100, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"支付金额:"];
    [mainView addSubview:lbl1];
    //支付金额
    UILabel *lblMoney=[[UILabel alloc]initWithFrame:CGRectMake(115, 100, 200, 30)];
    [lblMoney setFont:[UIFont systemFontOfSize:15]];
    [lblMoney setTextColor:[UIColor redColor]];
    [lblMoney setText:[NSString stringWithFormat:@"%@元",[_data objectForKey:@"newprice"]]];
    [mainView addSubview:lblMoney];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 130, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"生效日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblStartDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 130, 150, 30)];
    [lblStartDay setFont:[UIFont systemFontOfSize:15]];
    [lblStartDay setTextColor:[UIColor redColor]];
    [mainView addSubview:lblStartDay];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 160, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"到期日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblEndDay=[[UILabel alloc]initWithFrame:CGRectMake(115, 160, 150, 30)];
    [lblEndDay setFont:[UIFont systemFontOfSize:15]];
    [lblEndDay setTextColor:[UIColor redColor]];
    [mainView addSubview:lblEndDay];
    
    NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
    [formatter setDateStyle:NSDateFormatterFullStyle];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSDate *date=[[NSDate alloc]init];
    NSTimeInterval secondsPerDay=24*60*60*[[_data objectForKey:@"valid"]intValue];
    NSDate *tomorrow=[NSDate dateWithTimeIntervalSinceNow:secondsPerDay];
    [lblStartDay setText:[formatter stringFromDate:date]];
    [lblEndDay setText:[formatter stringFromDate:tomorrow]];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 190, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"充值账户:"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(115, 190, 150, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextColor:[UIColor redColor]];
    [lbl1 setText:[[[Config Instance]userInfo]objectForKey:@"phone"]];
    [mainView addSubview:lbl1];

    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(10, 220, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentLeft];
    [lbl1 setText:@"原套餐"];
    [mainView addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 250, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"套餐价值:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblOldStorageValue=[[UILabel alloc]initWithFrame:CGRectMake(115, 250, 150, 30)];
    [lblOldStorageValue setFont:[UIFont systemFontOfSize:15]];
    [lblOldStorageValue setTextColor:[UIColor redColor]];
    [mainView addSubview:lblOldStorageValue];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 280, 70, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setTextAlignment:NSTextAlignmentRight];
    [lbl1 setText:@"到期日期:"];
    [mainView addSubview:lbl1];
    
    UILabel *lblOldEndTime=[[UILabel alloc]initWithFrame:CGRectMake(115, 280, 150, 30)];
    [lblOldEndTime setFont:[UIFont systemFontOfSize:15]];
    [lblOldEndTime setTextColor:[UIColor redColor]];
    [mainView addSubview:lblOldEndTime];
 
    
    UIButton *_btnConfirm=[UIButton buttonWithType:UIButtonTypeCustom];
    [_btnConfirm setTitle:@"确认充值" forState:UIControlStateNormal];
    [_btnConfirm setFrame:CGRectMake(97, 350, 127, 35)];
//    [_btnPay setImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm setBackgroundImage:[UIImage imageNamed:@"extraction_gb"] forState:UIControlStateNormal];
    [_btnConfirm addTarget:self action:@selector(confirm:) forControlEvents:UIControlEventTouchDown];
    [mainView addSubview:_btnConfirm];
    
    int tarvalue=[[_data objectForKey:@"storage"]intValue];
    [lblValue setText:[NSString stringWithFormat:@"%dMB",tarvalue]];
    for (NSMutableDictionary *data in [[Config Instance] currentPackagesList]) {
        //只过滤出包月套餐
        if([[data objectForKey:@"ctype"]intValue]==1){
            NSDate *currentDate=[NSDate date];
            NSDate *starttime = [[data objectForKey:@"starttime"] stringConvertDate];
            NSDate *endtime = [[data objectForKey:@"endtime"] stringConvertDate];
            if([currentDate compare:starttime]>=0&& [endtime compare:currentDate]>=0){
                if([self isCurrentStorageExit:data]){
                    //只能续费一次
                    [Common alert:[NSString stringWithFormat:@"%@已续费",[_data objectForKey:@"name"]]];
                } else {
                    //已购买的存储容量
                    int storsum=[[data objectForKey:@"auciquotalimit"]intValue]/1024/1024;
                    if(tarvalue>=storsum){
                        //升级、续费
                        [lblOldStorageValue setText:[NSString stringWithFormat:@"%dMB",storsum]];
                        if(tarvalue>storsum) {
                            lbl1=[[UILabel alloc]initWithFrame:CGRectMake(40, 310, 70, 30)];
                            [lbl1 setFont:[UIFont systemFontOfSize:15]];
                            [lbl1 setTextAlignment:NSTextAlignmentRight];
                            [lbl1 setText:@"折算金额:"];
                            [mainView addSubview:lbl1];
                            
                            UILabel *lblConvertMoney=[[UILabel alloc]initWithFrame:CGRectMake(115, 310, 150, 30)];
                            [lblConvertMoney setFont:[UIFont systemFontOfSize:15]];
                            [lblConvertMoney setTextColor:[UIColor redColor]];
                            [mainView addSubview:lblConvertMoney];
                            //升级
                            long endtimeLong=[endtime timeIntervalSince1970];
                            long currentLong=[[NSDate date]timeIntervalSince1970];
                            int oridays=ceilf((float)(endtimeLong-currentLong)/86400);
                            int leftdays=[[data objectForKey:@"buyvtime"]intValue];
                            double buyprice=[[data objectForKey:@"buyprice"]doubleValue];
                            double deamount=buyprice*oridays/leftdays;
                            double newprice=[[_data objectForKey:@"newprice"]doubleValue];
                            double amount=newprice-deamount;
                            if(amount<=0) {
                                [Common alert:@"无法购买该套餐"];
                            } else {
                                [lblMoney setText:[NSString stringWithFormat:@"%0.2f-%0.2f=%0.2f",newprice,deamount,amount]];
                                [lblConvertMoney setText:[NSString stringWithFormat:@"%0.2f*%d/%d=%0.2f",buyprice,oridays,leftdays,deamount]];
                                [lblOldEndTime setText:[[data objectForKey:@"endtime"] substringWithRange:NSMakeRange(0, 10)]];
                            }
                        } else {
                            //续费
                            NSMutableDictionary *lastData=[self lastNewPackages:1];
                            NSString *newStartTime=[[lastData objectForKey:@"endtime"] substringWithRange:NSMakeRange(0, 10)];
                            NSLog(@"%@",newStartTime);
                            NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
                            [formatter setDateStyle:NSDateFormatterFullStyle];
                            [formatter setDateFormat:@"yyyy-MM-dd"];
                            NSDate *date=[formatter dateFromString:newStartTime];
                            //增加一天
                            date=[date dateByAddingTimeInterval:24*60*60];
                            NSTimeInterval secondsPerDay=24*60*60*[[_data objectForKey:@"valid"]intValue];
                            NSDate *tomorrow=[date dateByAddingTimeInterval:secondsPerDay];
                            [lblStartDay setText:[formatter stringFromDate:date]];
                            [lblEndDay setText:[formatter stringFromDate:tomorrow]];
                            [lblOldEndTime setText:[[data objectForKey:@"endtime"] substringWithRange:NSMakeRange(0, 10)]];
                            
                        }
                    } else {
                        [Common alert:@"暂不支持存储套餐降容量级别购买，请选择存储容量等于或大于您当前在使用套餐的存储服务，多点空间才能有备无患哦。"];
                        //返回前一页
                    }
                }
            }
        }
    }
    
    [sView addSubview:mainView];
    sView.contentSize=mainView.frame.size;
    
    [self.view addSubview:sView];
    
}

- (NSMutableDictionary *)lastNewPackages:(int)ctype{
    NSDate *nEndtime;
    NSMutableDictionary *nData;
    for (NSMutableDictionary *data in [[Config Instance] currentPackagesList]) {
        //只过滤出包月套餐
        if([[data objectForKey:@"ctype"]intValue]==ctype){
            NSDate *endtime=[[data objectForKey:@"endtime"] stringConvertDate];
            if(nEndtime) {
                if([endtime compare:nEndtime]>=0){
                    nEndtime=endtime;
                    nData=data;
                }
            } else {
                nEndtime=endtime;
                nData=data;
            }
        }
    }
    return nData;
}

//验证当前套餐是否已经续过费
- (BOOL)isCurrentStorageExit:(NSMutableDictionary*)data{
    for (NSMutableDictionary *nData in [[Config Instance] currentPackagesList]) {
        //只过滤出包月套餐
        if([[nData objectForKey:@"ctype"]intValue]==1){
            NSDate *currentDate=[NSDate date];
            NSDate *starttime = [[nData objectForKey:@"starttime"] stringConvertDate];
            NSDate *endtime = [[nData objectForKey:@"endtime"] stringConvertDate];
            if(!([currentDate compare:starttime]>=0&& [endtime compare:currentDate]>=0)) {
                if([[nData objectForKey:@"auciquotalimit"]intValue]==[[data objectForKey:@"auciquotalimit"]intValue]) {
                    return YES;
                }
            }
        }
    }
    return NO;
}

- (void)confirm:(id)sender{
    if([[Config Instance]isLogin]){
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:@"3" forKey:@"payuse"];
        [requestParams setObject:[_data objectForKey:@"recordno"] forKey:@"recprod"];
        [requestParams setObject:@"1" forKey:@"actflag"];
        if(_currentType==2) {
            //时长可以购买多份
            [requestParams setObject:@"1" forKey:@"quantity"];
        } else {
            //基础套餐，存储只能购买一份
            [requestParams setObject:@"1" forKey:@"quantity"];
            if(_currentType==3) {
                //存储
                for (NSMutableDictionary *data in [[Config Instance] currentPackagesList]) {
                    if([[data objectForKey:@"ctype"]intValue]==1){
                        NSDate *currentDate=[NSDate date];
                        NSDate *starttime = [[data objectForKey:@"starttime"] stringConvertDate];
                        NSDate *endtime = [[data objectForKey:@"endtime"] stringConvertDate];
                        if([currentDate compare:starttime]>=0&& [endtime compare:currentDate]>=0){
                            int storsum=[[data objectForKey:@"auciquotalimit"]intValue]/1024/1024;
                            int tarvalue=[[_data objectForKey:@"storage"]intValue];
                            if(tarvalue>storsum) {
                                //升级
                                [requestParams setObject:@"2" forKey:@"actflag"];
                            } else if(tarvalue==storsum) {
                                //续费
                                [requestParams setObject:@"1" forKey:@"actflag"];
                            } else {
                                [Common alert:@"暂不支持存储套餐降容量级别购买，请选择存储容量等于或大于您当前在使用套餐的存储服务，多点空间才能有备无患哦。"];
                                return;
                            }
                        }
                    }
                }
            }
        }
        _alipayHttp=[[HttpRequest alloc]init];
        [_alipayHttp setDelegate:self];
        [_alipayHttp setController:self];
        [_alipayHttp setRequestCode:10000000];
        NSLog(@"%@",requestParams);
//        [_alipayHttp loginhandle:@"v4alipayReq" requestParams:requestParams];
    }else{
        [Common noLoginAlert:self];
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==0){
        [Common resultLoginViewController:self resultCode:RESULTCODE_ACLoginViewController_1 requestCode:0 data:nil];
    }
}

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if(reqCode==10000000){
        if([response successFlag]){
            NSString *orderString=[[[response mainData] objectForKey:@"alipayinfo"] objectForKey:@"reqcontent"];
            
            orderString=[orderString stringByReplacingOccurrencesOfString:@"&amp;" withString:@"&"];
            
            AlixPay * alixpay = [AlixPay shared];
            int ret = [alixpay pay:orderString applicationScheme:@"iOSAlipay"];
            
            if (ret == kSPErrorAlipayClientNotInstalled) {
                UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                     message:@"您还没有安装支付宝快捷支付，请先安装。"
                                                                    delegate:self
                                                           cancelButtonTitle:@"确定"
                                                           otherButtonTitles:nil];
                [alertView setTag:123];
                [alertView show];
            }
        } else {
            NSLog(@"充值产品不存在或暂不支持");
        }
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	if (alertView.tag == 123) {
		NSString * URLString = @"http://itunes.apple.com/cn/app/id535715926?mt=8";
		[[UIApplication sharedApplication] openURL:[NSURL URLWithString:URLString]];
	}
}

@end
