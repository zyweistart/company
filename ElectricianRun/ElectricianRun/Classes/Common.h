//
//  Common.h
//  ElectricianRun
//
//  Created by Start on 2/10/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Common : NSObject

+ (void)alert:(NSString *)message;

+ (void)notificationMessage:(NSString *)message inView:(UIView *)aView;

+ (void)actionSheet:(id<UIActionSheetDelegate>)delegate message:(NSString *)message tag:(NSInteger)tag;

@end
