//
//  Account.m
//  ElectricianRun
//
//  Created by Start on 3/2/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "Account.h"
#import "NSString+Utils.h"

@implementation Account

+ (void)LoginSuccessWithUserName:(NSString*)u Password:(NSString*)p Data:(NSDictionary*) d
{
    [Common setCacheByBool:ISACCOUNTLOGIN data:YES];
    [Common setCache:ACCOUNTUSERNAME data:u];
    //转大写并用MD5签名
    [Common setCache:ACCOUNTPASSWORD data:[[p uppercaseString] md5]];
    [Common setCache:ACCOUNTRESULTDATA data:d];
}

+ (void)clear
{
    [Common setCacheByBool:ISACCOUNTLOGIN data:NO];
    [Common setCache:ACCOUNTUSERNAME data:@""];
    [Common setCache:ACCOUNTPASSWORD data:@""];
    [Common setCache:ACCOUNTRESULTDATA data:nil];
}

+ (BOOL)isLogin
{
    if([Common getCacheByBool:ISACCOUNTLOGIN]){
        return YES;
    }else{
        return NO;
    }
}

+ (NSString*)getUserName
{
    return [Common getCache:ACCOUNTUSERNAME];
}

+ (NSString*)getPassword
{
    return [Common getCache:ACCOUNTPASSWORD];
}

@end
