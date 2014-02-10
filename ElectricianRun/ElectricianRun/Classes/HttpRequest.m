//
//  HttpRequest.m
//  ElectricianRun
//
//  Created by Start on 2/9/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "HttpRequest.h"

#import "Reachability.h"
#import "ATMHud.h"
#import "MBProgressHUD.h"
#import "NSString+Utils.h"
#import "Common.h"

#define ReachableViaWWANDOWNLOAD 500
#define HTTPSERVERURL @"http://122.224.247.221:7003/WEB/mobile/"

@implementation HttpRequest{
    
    ATMHud *_atmHud;
    MBProgressHUD *_mbpHud;
    long long downloadFileSize;
    NSMutableData *_resultData;
    NSString *_http_url;
    NSMutableDictionary *_params;
    
}

- (id)init:(UIViewController*)controler delegate:(NSObject<HttpRequestDelegate>*)delegate responseCode:(int)repCode{
    self = [super init];
    if (self) {
        [self setController:controler];
        [self setDelegate:delegate];
        [self setResponseCode:repCode];
    }
    return self;
}

//是否已连接网络
+ (BOOL)isNetworkConnection {
    Reachability *reach = [Reachability reachabilityForInternetConnection];
    NetworkStatus netStatus = [reach currentReachabilityStatus];
    if(netStatus==ReachableViaWWAN||netStatus==ReachableViaWiFi) {
        return YES;
    } else {
        return NO;
    }
}

- (void)start:(NSString*)URL params:(NSMutableDictionary*)p{
    
    if ([HttpRequest isNetworkConnection]) {
        _http_url=URL;
        _params=p;
        if(_isFileDownload) {
            //如果为下载是否使用的是3G移动网络
            Reachability *reach = [Reachability reachabilityForInternetConnection];
            if([reach currentReachabilityStatus]!=ReachableViaWWAN) {
                [Common actionSheet:self message:@"即将通过移动网络下载数据，为了节约流量，推荐您使用WIFI无线网络!" tag:ReachableViaWWANDOWNLOAD];
            } else {
                [self handle];
            }
        } else {
            [self handle];
        }
    } else {
        if(self.controller) {
            [Common notificationMessage:@"网络连接出错，请检测网络设置" inView:self.controller.view];
        }
    }
    
}

- (void)handle {
    NSString *HTTP_URL=nil;
    NSMutableString *urlString=[[NSMutableString alloc]initWithString:HTTPSERVERURL];
    [urlString appendString:_http_url];
    
    if(_params!=nil&&[_params count]>0){
        [urlString appendString:@"?"];
        
        for(NSString *key in _params){
            [urlString appendFormat:@"%@=%@&",key,[_params objectForKey:key]];
        }
        
        HTTP_URL=[urlString substringWithRange:NSMakeRange(0, [urlString length]-1)];
    }else{
        HTTP_URL=[NSString stringWithFormat:@"%@",urlString];
    }
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:HTTP_URL]];
    // 设置请求方法
    request.HTTPMethod = @"POST";
    // 60秒请求超时
    request.timeoutInterval = 60;
    // 初始化一个连接
    NSURLConnection *conn = [NSURLConnection connectionWithRequest:request delegate:self];
    // 开始一个异步请求
    [conn start];
    
    if(_isFileDownload){
        _atmHud=[[ATMHud alloc]init];
        [self.controller.view addSubview:_atmHud.view];
        [_atmHud setCaption:@"下载中..."];
        [_atmHud setProgress:0.01];
        [_atmHud show];
    } else {
        if(self.controller&&(self.message!=nil||self.isShowMessage)) {
            _mbpHud = [[MBProgressHUD alloc] initWithView:self.controller.view];
            [self.controller.view addSubview:_mbpHud];
            if(self.message) {
                _mbpHud.labelText = _message;
            }
            _mbpHud.dimBackground = YES;
            _mbpHud.square = YES;
            [_mbpHud show:YES];
        }
    }
}

#pragma mark 该方法在响应connection时调用
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    if(_resultData==nil) {
        _resultData=[[NSMutableData alloc]init];
    }
    
    if(self.isFileDownload) {
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
        if(httpResponse && [httpResponse respondsToSelector:@selector(allHeaderFields)]){
            NSDictionary *httpResponseHeaderFields = [httpResponse allHeaderFields];
            //获取文件文件的大小
            downloadFileSize = [[httpResponseHeaderFields objectForKey:@"Content-Length"] longLongValue];
        }
    }
}

#pragma mark 接收到服务器返回的数据
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [_resultData appendData:data];
    if(self.isFileDownload) {
        //显示下载进度条
        if(_atmHud) {
            float size=[_resultData length]/(float)downloadFileSize;
            if(size>0) {
                [_atmHud setProgress:size];
            }
        }
    }
}

#pragma mark 服务器的数据已经接收完毕时调用
- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSStringEncoding gbkEncoding =CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSString*pageSource = [[NSString alloc] initWithData:_resultData encoding:gbkEncoding];
    NSString *json=[pageSource stringByReplacingPercentEscapesUsingEncoding:gbkEncoding];
    
    Response *response=[[Response alloc]init];
    [response setResultJSON:[NSJSONSerialization JSONObjectWithData:[json dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableLeaves error:nil]];
    
    _resultData=nil;
    
    //调用代理对象
    [_delegate requestFinishedByResponse:response responseCode:_responseCode];
    
    //隐藏下载进度条
    if(_atmHud) {
        [_atmHud hide];
    }
    if(self.isFileDownload) {
        //隐藏等待条
        if (_mbpHud) {
            [_mbpHud hide:YES];
        }
    }
    
}

#pragma mark 网络连接出错时调用
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    
    NSLog(@"网络连接出错:%@",[error localizedDescription]);
    
    _resultData=nil;
    
    [_delegate requestFailed:_responseCode didFailWithError:error];
    
    //隐藏下载进度条
    if(_atmHud) {
        [_atmHud hide];
    }
    if(self.isFileDownload) {
        //隐藏等待条
        if (_mbpHud) {
            [_mbpHud hide:YES];
        }
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(actionSheet.tag==ReachableViaWWANDOWNLOAD) {
        //移动流量下载行为
        if(buttonIndex==0) {
            [self handle];
        }
    }
}


@end
