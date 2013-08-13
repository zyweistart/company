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
#ifdef JAILBREAK
#import "AlixPay.h"
#import "AlixPayResult.h"
#else
#import "IAPHelper.h"
#endif
#ifndef TEST
#import "BaiduMobStat.h"
#endif

@implementation ACAppDelegate

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

#pragma mark 越狱版支持支付宝
#ifdef JAILBREAK
- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    AlixPay *alixpay = [AlixPay shared];
	AlixPayResult *result = [alixpay handleOpenURL:url];
	if (result) {
		//是否支付成功
		if (9000 == result.statusCode) {
			UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                 message:result.resultString
                                                                delegate:nil
                                                       cancelButtonTitle:@"确定"
                                                       otherButtonTitles:nil];
            [alertView show];
		} else {
            //如果支付失败,可以通过result.statusCode查询错误码
			UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"提示"
																 message:result.statusMessage
																delegate:nil
													   cancelButtonTitle:@"确定"
													   otherButtonTitles:nil];
			[alertView show];
		}
		
	}
    
	return YES;
}
#endif

@end
