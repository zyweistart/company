//
//  HttpRequest.h
//  ACyulu
//
//  Created by Start on 12-12-6.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

@protocol HttpViewDelegate <NSObject>

@optional
- (void)requestFinished:(ASIHTTPRequest*)request requestCode:(int)reqCode;
- (void)requestFinishedByResponse:(Response*)response requestCode:(int)reqCode;
- (void)requestFailed:(ASIHTTPRequest*)request requestCode:(int)reqCode;

@end

@interface HttpRequest : NSObject<ASIHTTPRequestDelegate,ASIProgressDelegate,UIActionSheetDelegate>{
    ATMHud *proHud;
    NSString* _action;
    NSString* _signKey;
    NSMutableDictionary* _head;
    NSMutableDictionary* _request;
}
//请求码
@property int requestCode;
//是否验证
@property BOOL isVerify;
//是否为文件下载
@property BOOL isFileDownload;
//请求时的提示信息
@property (strong,nonatomic) NSString *message;
//当前请求的控制器
@property (weak,nonatomic) UIViewController *controller;
//属性
@property (strong,nonatomic) NSMutableDictionary *propertys;
//代理对象
@property (weak,nonatomic) NSObject<HttpViewDelegate> *delegate;

+ (BOOL)isNetworkConnection;
- (void)loginhandle:(NSString*)url requestParams:(NSMutableDictionary*)request;
- (void)handle:(NSString*)url signKey:(NSString*)signKey requestParams:(NSMutableDictionary*)request;
- (void)handle:(NSString*)url signKey:(NSString*)signKey  headParams:(NSMutableDictionary*)head requestParams:(NSMutableDictionary*)request;

@end