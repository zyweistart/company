//
//  STAppDelegate.m
//  ElectricianRun
//
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STAppDelegate.h"
#import "STGuideViewController.h"

@implementation STAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [WXApi registerApp:@"wxcefa411f34485347"];
    
    //让设备知道我们想要收到推送通知
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    
    //显示ViewController
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.window.rootViewController=[[STGuideViewController alloc]init];
    self.window.backgroundColor = [UIColor whiteColor];
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    NSDictionary *aps=[userInfo objectForKey:@"aps"];
    NSString *type=[aps objectForKey:@"type"];
    if([@"1" isEqualToString:type]||[@"2" isEqualToString:type]){
        NSString *title=@"功率因数报警推送";
        if([@"2" isEqualToString:type]){
            title=@"需量报警推送";
        }
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle:title
                              message:[aps objectForKey:@"alert"]
                              delegate:nil
                              cancelButtonTitle:@"确定"
                              otherButtonTitles:nil, nil];
        [alert show];
    }else if([@"0" isEqualToString:type]){
        //报警消息推送
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle:@"报警消息推送"
                              message:[aps objectForKey:@"alert"]
                              delegate:nil
                              cancelButtonTitle:@"确定"
                              otherButtonTitles:nil, nil];
        [alert show];
    }
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    //获取deviceToken
}

- (void)application:(UIApplication *)app didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    return [WXApi handleOpenURL:url delegate:self];
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    return [WXApi handleOpenURL:url delegate:self];
}

@end
