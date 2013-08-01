//
//  Config.h
//  ACyulu
//
//  Created by Start on 12-12-6.
//  Copyright (c) 2012年 ancun. All rights reserved.
//
#import "ResultDelegate.h"

@interface Config : NSObject

//是否已登陆
@property Boolean isLogin;
//是否需要计算缓存空间大小
@property Boolean isCalculateTotal;
//是否需要刷新出证列表页面
@property Boolean isRefreshNotaryList;
//是否需要刷新提取列表页面
@property Boolean isRefreshExtractionList;
//是否需要刷新录音详细页面
@property Boolean isRefreshRecordingList;
//唯一缓存键名称
@property (strong,nonatomic) NSString *cacheKey;
//用户信息
@property (strong,nonatomic) NSMutableDictionary *userInfo;
//登陆代理
@property (strong,nonatomic) NSObject<ResultDelegate> *loginResultDelegate;
//联系人
@property (strong,nonatomic) NSMutableDictionary *contact;
//不能通过软件拔打的号码
@property (strong,nonatomic) NSMutableArray *noDialPhoneNumber;

+ (Config *) Instance;

- (BOOL) isOldUser;

@end