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

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    if([Common getCacheByBool:@"enabled_preference_gps"]){
        if(self.locationGetter==nil){
            self.locationGetter=[[LocationGetter alloc]init];
            [self.locationGetter startUpdates];
        }
        if(self.updateLocationTimer==nil){
            self.updateLocationTimer = [NSTimer scheduledTimerWithTimeInterval:60 target:self selector:@selector(updateLocation) userInfo:nil repeats:YES];
        }
    }else{
        if(self.locationGetter!=nil){
            [[self.locationGetter locationManager]stopMonitoringSignificantLocationChanges];
            self.locationGetter=nil;
        }
        if(self.updateLocationTimer!=nil){
            [self.updateLocationTimer invalidate];
            self.updateLocationTimer=nil;
        }
    }
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    BOOL backgroundAccepted = [[UIApplication sharedApplication] setKeepAliveTimeout:600 handler:^{
        [self backgroundHandler];
    }];
    if (backgroundAccepted){
//        NSLog(@"backgrounding accepted");
    }
    [self backgroundHandler];
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
    if(self.locationGetter!=nil){
        
        CLLocation *location=self.locationGetter.currentLocation;
        
        if(location){
            NSString *latitude=[NSString stringWithFormat:@"%f",self.locationGetter.currentLocation.coordinate.latitude ];
            NSString *longitude=[NSString stringWithFormat:@"%f",self.locationGetter.currentLocation.coordinate.longitude ];
            
            if([Account isLogin]){
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss"];
                NSString *date=[formatter stringFromDate:[NSDate date]];
                
                NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
                [p setObject:[Account getUserName] forKey:@"imei"];
                [p setObject:[Account getPassword] forKey:@"authentication"];
                [p setObject:latitude forKey:@"latitude"];
                [p setObject:longitude forKey:@"longitude"];
                [p setObject:[NSString stringWithFormat:@"%f",location.speed] forKey:@"speed"];//为速度
                [p setObject:@"" forKey:@"direction"];//为速度方向
                [p setObject:@"E" forKey:@"longitudeEW"];//为东西经,值为”E”或“W”,其中”E”代表东经，”W”代表西经
                [p setObject:@"N" forKey:@"latitudeNS"];//为南北纬，值为“N”或“S”,其中”N”代表北纬，”S”代表南纬
                [p setObject:date forKey:@"gpsTime"];//为GPS时间，URL编码处理后的数据
                [p setObject:@"4324324" forKey:@"key"];//为国际移动设备身份码
                [p setObject:@"0" forKey:@"iscorrect"];//为是否已纠偏
                
                self.hRequest=[[HttpRequest alloc]init:nil delegate:self responseCode:REQUESTCODEUPDATELOCATION];
                [self.hRequest start:URLsendLocationInfo params:p];
            }
        }
    }
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    if(repCode==REQUESTCODEUPDATELOCATION){
    }
}

- (void)requestFailed:(int)repCode didFailWithError:(NSError *)error
{
    if(repCode==REQUESTCODEUPDATELOCATION){
    }
}

- (void)backgroundHandler {
    
    UIApplication *app = [UIApplication sharedApplication];
    __block UIBackgroundTaskIdentifier bgTask;
    bgTask = [app beginBackgroundTaskWithExpirationHandler:^{
        dispatch_async(dispatch_get_main_queue(), ^{
            if (bgTask != UIBackgroundTaskInvalid) {
                
                bgTask = UIBackgroundTaskInvalid;
            }
        });
    }];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        dispatch_async(dispatch_get_main_queue(), ^{
            if (bgTask != UIBackgroundTaskInvalid) {
                bgTask = UIBackgroundTaskInvalid;
            }
        });
    });
    
}

@end
