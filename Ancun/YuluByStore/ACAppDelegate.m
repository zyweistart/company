//
//  ACAppDelegate.m
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "ACAppDelegate.h"
#import <AVFoundation/AVFoundation.h>
#import "ACGuideViewController.h"
#import "ACLoginViewController.h"
#import "ACRechargeConfirmViewController.h"
#import "ACRechargeNav.h"
#ifndef TEST
#import "BaiduMobStat.h"
#endif
#ifdef JAILBREAK
    #import "AlixPay.h"
    #import "AlixPayResult.h"
#else
    #import "IAPHelper.h"
#endif

@implementation ACAppDelegate {
    HttpRequest *_loadHttp;
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions{
    //显示系统托盘
    [application setStatusBarHidden:NO withAnimation:UIStatusBarAnimationFade];
    
#ifndef TEST
    //测试环境下不进行百度统计
    BaiduMobStat* statTracker = [BaiduMobStat defaultStat];
    statTracker.enableExceptionLog = NO;
    //渠道
    statTracker.channelId = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"ChannelId"];
    statTracker.logStrategy=BaiduMobStatLogStrategyAppLaunch;
    statTracker.sessionResumeInterval = 60;
    //应用ID
    [statTracker startWithAppId:[[NSBundle mainBundle] objectForInfoDictionaryKey:@"BaiduWithAppId"]];
#endif
    
    //后台播放音频设置
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setActive:YES error:nil];
    [session setCategory:AVAudioSessionCategoryPlayback error:nil];
    
#ifndef JAILBREAK
    //添加支付监听
    if([SKPaymentQueue canMakePayments]) {
        [[SKPaymentQueue defaultQueue] addTransactionObserver:[IAPHelper sharedHelper]];
    }
#endif
    
    //获取最后保存的版本号不存在则为0
    float lastVersionNo=[[Common getCache:DEFAULTDATA_LASTVERSIONNO] floatValue];
    NSDictionary* infoDict =[[NSBundle mainBundle] infoDictionary];
    //获取当前使用的版本号
    NSString *currentVersionNo=[infoDict objectForKey:@"CFBundleVersion"];
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    if([currentVersionNo floatValue]>lastVersionNo){
        //新安装或升级则使用引导页
        self.window.rootViewController=[[ACGuideViewController alloc]init];
    }else{
        //直接进入登录页
        self.window.rootViewController=[[ACLoginViewController alloc]init];
    }
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];
    [Common setCache:DEFAULTDATA_LASTVERSIONNO data:currentVersionNo];
    
    return YES;
}

#ifdef JAILBREAK
- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    AlixPay *alixpay = [AlixPay shared];
	AlixPayResult *result = [alixpay handleOpenURL:url];
	if (result) {
		//是否支付成功
		if (9000 == result.statusCode) {
//            //服务端签名验证
//            NSMutableString *verifyString=[[NSMutableString alloc]init];
//            [verifyString appendFormat:@"resultStatus={%d};",result.statusCode];
//            [verifyString appendFormat:@"memo={%@};",result.statusMessage];
//            [verifyString appendString:@"result={"];
//            [verifyString appendString:result.resultString];
//            [verifyString appendString:@"&"];
//            [verifyString appendFormat:@"sign_type=\"%@\"",result.signType];
//            [verifyString appendString:@"&"];
//            [verifyString appendFormat:@"sign=\"%@\"",result.signString];
//            [verifyString appendString:@"}"];
//            NSLog(@"%@",verifyString);
//            NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
//            [requestParams setObject:ACCESSID forKey:@"accessid"];
//            [requestParams setObject:@"3"  forKey:@"payproduct"];
//            [requestParams setObject:verifyString  forKey:@"rescontent"];
//            _loadHttp=[[HttpRequest alloc]init];
//            [_loadHttp setDelegate:self];
//            [_loadHttp setController:[application.keyWindow rootViewController]];
//            [_loadHttp handle:@"v4ealipayRes" signKey:ACCESSKEY headParams:nil requestParams:requestParams];
            //显示成功页面
            UIViewController *controller=[[Config Instance]currentViewController];
            if(controller) {
                if([controller isKindOfClass:[ACRechargeConfirmViewController class]]) {
                    ACRechargeConfirmViewController *viewController=(ACRechargeConfirmViewController *)controller;
                    [viewController layoutSuccessPage];
                }
            }
		} else {
            //充值失败
            if(result.statusCode==6001) {
                //用户中途取消
            }
            //使引导箭头标为第二步
            UIViewController *controller=[[Config Instance]currentViewController];
            if(controller) {
                if([controller isKindOfClass:[ACRechargeConfirmViewController class]]) {
                    ACRechargeConfirmViewController *viewController=(ACRechargeConfirmViewController *)controller;
                    [viewController.rechargeNav secondStep];
                }
            }
            [Common alert:result.statusMessage];
		}
	}
	return YES;
}
#endif

@end
