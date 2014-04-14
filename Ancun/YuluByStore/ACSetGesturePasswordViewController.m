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
        self.title=@"密码绘制";
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
    if([firstPassCode isNotEmpty]){
        if([firstPassCode isEqualToString:passcode]){
            [Common setCache:DEFAULTDATA_GESTUREPWD data:passcode];
            [Common setCache:DEFAULTDATA_PHONE data:[[Config Instance] USERNAME]];
            [Common setCache:DEFAULTDATA_PASSWORD data:[[Config Instance] PASSWORD]];
            [Common setCacheByBool:DEFAULTDATA_AUTOLOGIN data:YES];
            [lblInfo setText:@"手势密码设置成功"];
            [self performSelector:@selector(popToRootViewController) withObject:nil afterDelay:1];
        }else{
            firstPassCode=nil;
            [lblInfo setText:@"与上一次输入不一致，请重试"];
        }
    }else{
        firstPassCode=passcode;
        [lblInfo setText:@"请再次绘制解锁图案"];
    }
}

- (void)popToRootViewController
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end