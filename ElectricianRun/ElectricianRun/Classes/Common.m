//
//  Common.m
//  ElectricianRun
//
//  Created by Start on 2/10/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "Common.h"
#import "GCDiscreetNotificationView.h"

@implementation Common

+ (id)getCache:(NSString *)key{
    NSUserDefaults *settings = [NSUserDefaults standardUserDefaults];
    return [settings objectForKey:key];
}
+ (void)setCache:(NSString *)key data:(id)data{
    NSUserDefaults *setting=[NSUserDefaults standardUserDefaults];
    [setting setObject:data forKey:key];
    [setting synchronize];
}
+ (BOOL)getCacheByBool:(NSString *)key{
    NSUserDefaults * settings = [NSUserDefaults standardUserDefaults];
    return [settings boolForKey:key];
}
+ (void)setCacheByBool:(NSString *)key data:(BOOL)data{
    NSUserDefaults *setting=[NSUserDefaults standardUserDefaults];
    [setting setBool:data forKey:key];
    [setting synchronize];
}

+ (void)alert:(NSString *)message{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"信息"
                          message:message
                          delegate:nil
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:nil, nil];
    [alert show];
}

+ (void)notificationMessage:(NSString *)message inView:(UIView *)aView{
    GCDiscreetNotificationView *notificationView = [[GCDiscreetNotificationView alloc] initWithText:message showActivity:NO inPresentationMode:GCDiscreetNotificationViewPresentationModeTop inView:aView];
    [notificationView show:YES];
    [notificationView hideAnimatedAfter:2.6];
}

+ (void)actionSheet:(id<UIActionSheetDelegate>)delegate message:(NSString *)message tag:(NSInteger)tag{
    UIActionSheet *sheet = [[UIActionSheet alloc]
                            initWithTitle:message
                            delegate:delegate
                            cancelButtonTitle:@"取消"
                            destructiveButtonTitle:@"确定"
                            otherButtonTitles:nil,nil];
    sheet.tag=tag;
    //UIActionSheet与UITabBarController结合使用不能使用[sheet showInView:self.view];
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
}

+ (NSString *)NSNullConvertEmptyString:(id)value
{
    if([value isEqual:[NSNull null]]){
        return @"";
    }
    return [NSString stringWithFormat:@"%@",value];
}

+ (NSString *)ConvertByNSDate:(NSString*)value
{
    return [Common ConvertByNSDate:value format:@"yyyy/MM/dd+hh:mm:ss"];
}

+ (NSString *)ConvertByNSDate:(NSString*)value format:(NSString*)format
{
    NSString *d=[Common NSNullConvertEmptyString:value];
    if(![@"" isEqualToString:d]){
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat: format];
        NSDate *destDate= [dateFormatter dateFromString:d];
        if(destDate!=nil){
            NSDateFormatter *dateFormatter1 = [[NSDateFormatter alloc] init];
            [dateFormatter1 setDateFormat: @"yyyy-MM-dd hh:mm:ss"];
            NSString *v=[dateFormatter1 stringFromDate:destDate];
            if(v!=nil){
                return v;
            }
        }
        return d;
    }
    return @"";
}

@end
