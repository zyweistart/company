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

@end
