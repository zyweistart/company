//
//  ACSetGesturePasswordViewController.m
//  Ancun
//
//  Created by Start on 4/10/14.
//
//

#import "ACSetGesturePasswordViewController.h"
#import "NSString+Utils.h"

@interface ACSetGesturePasswordViewController ()

@end

@implementation ACSetGesturePasswordViewController{
    UILabel *lblInfo;
    NSString *firstPassCode;
}

- (id)init
{
    self = [super init];
    if (self) {
        self.title=@"手势设置";
        lblInfo=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 100)];
        [lblInfo setText:@"请绘制解锁图案"];
        [lblInfo setFont:[UIFont systemFontOfSize:15]];
        [lblInfo setTextColor:[UIColor blackColor]];
        [lblInfo setBackgroundColor:[UIColor clearColor]];
        [lblInfo setTextAlignment:NSTextAlignmentCenter];
        [self.view addSubview:lblInfo];
    }
    return self;
}

- (void)gestureLockView:(KKGestureLockView *)gestureLockView didEndWithPasscode:(NSString *)passcode{
    if([passcode length]>6){
        if([firstPassCode isNotEmpty]){
            if([firstPassCode isEqualToString:passcode]){
                [Common setCache:DEFAULTDATA_GESTUREPWD data:passcode];
                [Common alert:@"手势密码设置成功"];
                [self.navigationController popToRootViewControllerAnimated:YES];
            }else{
                [Common alert:@"两次设置不一致，请重试"];
                firstPassCode=nil;
                [lblInfo setText:@"请绘制解锁图案"];
            }
        }else{
            firstPassCode=passcode;
            [lblInfo setText:@"请再绘制一次解锁图案"];
        }
    }else{
        [Common alert:@"手势密码至少四位"];
    }
}

@end