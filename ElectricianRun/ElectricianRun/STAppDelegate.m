//
//  STAppDelegate.m
//  ElectricianRun
//
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STAppDelegate.h"
#import "STGuideViewController.h"
#define REQUESTCODEUPDATELOCATION 58374

@implementation STAppDelegate {
    BOOL isUpdateLocationIng;
}

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

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    if([Common getCacheByBool:@"enabled_preference_gps"]){
        if(self.locationGetter==nil){
            isUpdateLocationIng=NO;
            self.locationGetter=[[LocationGetter alloc]init];
            [self.locationGetter startUpdates];
            self.updateLocationTimer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(updateLocation) userInfo:nil repeats:YES];
        }
    }else{
        if(self.updateLocationTimer){
            [self.updateLocationTimer invalidate];
        }
    }
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    //获取deviceToken需上传至服务端
//    NSLog(@"%@",deviceToken);
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

- (void)updateLocation
{
    if(self.locationGetter!=nil&&!isUpdateLocationIng){
        isUpdateLocationIng=YES;
        
        NSString *latitude=[NSString stringWithFormat:@"%.6f",self.locationGetter.currentLocation.coordinate.latitude ];
        NSString *longitude=[NSString stringWithFormat:@"%.6f",self.locationGetter.currentLocation.coordinate.longitude ];
        
        NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
        [p setObject:latitude forKey:@"latitude"];
        [p setObject:longitude forKey:@"longitude"];
        self.hRequest=[[HttpRequest alloc]init:nil delegate:self responseCode:REQUESTCODEUPDATELOCATION];
        [self.hRequest start:URLnews params:p];
    }
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    if(repCode==REQUESTCODEUPDATELOCATION){
        
        isUpdateLocationIng=NO;
    }
}

- (void)requestFailed:(int)repCode didFailWithError:(NSError *)error
{
    if(repCode==REQUESTCODEUPDATELOCATION){
        
        isUpdateLocationIng=NO;
    }
}

@end
