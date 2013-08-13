//
//  ACBaseViewController.m
//  Ancun
//
//  Created by Start on 13-8-13.
//
//

#import "ACBaseViewController.h"
#ifndef TEST
#import "BaiduMobStat.h"
#endif

@interface ACBaseViewController ()

@end

@implementation ACBaseViewController

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    //百度统计
#ifndef TEST
[[BaiduMobStat defaultStat] pageviewStartWithName:[NSString stringWithUTF8String:object_getClassName(self)]];
#endif
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    //百度统计
#ifndef TEST
    [[BaiduMobStat defaultStat] pageviewEndWithName:[NSString stringWithUTF8String:object_getClassName(self)]];
#endif
}

@end
