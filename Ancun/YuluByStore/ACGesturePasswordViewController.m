//
//  ACGesturePasswordViewController.m
//  Ancun
//
//  Created by Start on 4/10/14.
//
//

#import "ACGesturePasswordViewController.h"
#import "ACLoginViewController.h"

@interface ACGesturePasswordViewController () 

@end

@implementation ACGesturePasswordViewController{
    int errorCount;
}

- (id)init
{
    self = [super init];
    if (self) {
        self.view.backgroundColor = [UIColor whiteColor];
        self.lockView=[[KKGestureLockView alloc]initWithFrame:self.view.bounds];
        [self.view addSubview:self.lockView];
        self.lockView.normalGestureNodeImage = [UIImage imageNamed:@"gesture_node_normal"];
        self.lockView.selectedGestureNodeImage = [UIImage imageNamed:@"gesture_node_selected"];
        self.lockView.lineColor = [[UIColor orangeColor] colorWithAlphaComponent:0.3];
        self.lockView.lineWidth = 12;
        self.lockView.delegate = self;
        self.lockView.contentInsets = UIEdgeInsetsMake(150, 20, 100, 20);
        errorCount=0;
    }
    return self;
}

- (id)initWithFlag:(BOOL)flag
{
    self=[self init];
    if(self){
        _flag=flag;
    }
    return self;
}

- (void)gestureLockView:(KKGestureLockView *)gestureLockView didEndWithPasscode:(NSString *)passcode{
    if([passcode length]>6){
        NSString *value=[Common getCache:DEFAULTDATA_GESTUREPWD];
        if([passcode isEqualToString:value]){
            if(_flag){
                [self dismissViewControllerAnimated:YES completion:nil];
            }else{
                [Common setCacheByBool:DEFAULTDATA_AUTOLOGIN data:YES];
                [self goLoginPage];
            }
            return;
        }
    }
    errorCount++;
    if(errorCount>2){
        [Common alert:@"超过限制请重新登录"];
        //清除登录密码
        [Common setCache:DEFAULTDATA_PASSWORD data:@""];
        //清除手势密码
        [Common setCache:DEFAULTDATA_GESTUREPWD data:@""];
        [self goLoginPage];
    }else{
        [Common alert:@"手势密码出错，请重试!"];
    }
}

- (void)goLoginPage
{
    ACLoginViewController *loginViewController=[[ACLoginViewController alloc]init];
    [self presentViewController:loginViewController animated:YES completion:nil];
}

@end
