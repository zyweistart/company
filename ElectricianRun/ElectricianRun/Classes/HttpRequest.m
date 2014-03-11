


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
#define boundary @"_YJAYgDL082HLBJkC1laRbpjU5PlLgeJh_"

@implementation HttpRequest{
    
    ATMHud *_atmHud;
    MBProgressHUD *_mbpHud;
    long long downloadFileSize;
    NSString *_http_url;
    NSMutableDictionary *_params;
    
}

- (id)init:(UIViewController*)controler delegate:(NSObject<HttpRequestDelegate>*)delegate responseCode:(int)repCode{
    self = [super init];
    if (self) {
        _isBodySubmit=NO;
        _isReachableViaWiFiMessage=YES;
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
            if(_isReachableViaWiFiMessage){
                //如果为下载是否使用的是3G移动网络
                Reachability *reach = [Reachability reachabilityForInternetConnection];
                if([reach currentReachabilityStatus]==ReachableViaWWAN) {
                    [Common actionSheet:self message:@"即将通过移动网络下载数据，为了节约流量，推荐您使用WIFI无线网络!" tag:ReachableViaWWANDOWNLOAD];
                } else {
                    [self handle];
                }
            }else{
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
    NSMutableString *urlString=[[NSMutableString alloc]initWithString:_http_url];
    NSString *HTTP_URL=[NSString stringWithFormat:@"%@",urlString];
    if(!_isBodySubmit){
        if(_params!=nil&&[_params count]>0){
            [urlString appendString:@"?"];
            for(NSString *key in _params){
                [urlString appendFormat:@"%@=%@&",key,[_params objectForKey:key]];
            }
            HTTP_URL=[urlString substringWithRange:NSMakeRange(0, [urlString length]-1)];
        }
    }
    NSStringEncoding gbkEncoding =CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSString *url=[HTTP_URL stringByAddingPercentEscapesUsingEncoding:gbkEncoding];
//    NSLog(@"%@",url);
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
    // 设置请求方法
    request.HTTPMethod = @"POST";
    // 60秒请求超时
    request.timeoutInterval = 30;
    if(_isBodySubmit){
        NSString *TWITTERFON_FORM_BOUNDARY = @"AaB03x";
        //根据url初始化request
        NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]
                                                               cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                           timeoutInterval:10];
        //分界线 --AaB03x
        NSString *MPboundary=[[NSString alloc]initWithFormat:@"--%@",TWITTERFON_FORM_BOUNDARY];
        //结束符 AaB03x--
        NSString *endMPboundary=[[NSString alloc]initWithFormat:@"%@--",MPboundary];
        //要上传的图片
//        UIImage *image=[_params objectForKey:@"pic"];
        //得到图片的data
//        NSData* data = UIImagePNGRepresentation(image);
        //http body的字符串
        NSMutableString *body=[[NSMutableString alloc]init];
        for(NSString *key in _params){
            id value=[_params objectForKey:key];
            if([value isKindOfClass:[NSString class]]){
                //添加分界线，换行
                [body appendFormat:@"%@\r\n",MPboundary];
                //添加字段名称，换2行
                [body appendFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n",key];
                //添加字段的值
                [body appendFormat:@"%@\r\n",[_params objectForKey:key]];
            }
        }
        
        NSMutableString *uplodaBody=[[NSMutableString alloc]init];
        ////添加分界线，换行
        [uplodaBody appendFormat:@"%@\r\n",MPboundary];
        //声明pic字段，文件名为boris.png
        [uplodaBody appendFormat:@"Content-Disposition: form-data; name=\"identityImg\"; filename=\"identityImg.png\"\r\n"];
        //声明上传文件的格式
        [uplodaBody appendFormat:@"Content-Type: image/png\r\n\r\n"];
        //声明结束符：--AaB03x--
        NSString *end=[[NSString alloc]initWithFormat:@"\r\n%@",endMPboundary];
        //声明myRequestData，用来放入http body
        NSMutableData *myRequestData=[NSMutableData data];
        //将body字符串转化为UTF8格式的二进制
        [myRequestData appendData:[[NSString stringWithFormat:@"%@%@",body,uplodaBody] dataUsingEncoding:gbkEncoding]];
        //将image的data加入
        [myRequestData appendData:[_params objectForKey:@"identityImg"]];
        //加入结束符--AaB03x--
        [myRequestData appendData:[end dataUsingEncoding:gbkEncoding]];
        
        uplodaBody=[[NSMutableString alloc]init];
        ////添加分界线，换行
        [uplodaBody appendFormat:@"%@\r\n",MPboundary];
        //声明pic字段，文件名为boris.png
        [uplodaBody appendFormat:@"Content-Disposition: form-data; name=\"elecImg\"; filename=\"elecImg.png\"\r\n"];
        //声明上传文件的格式
        [uplodaBody appendFormat:@"Content-Type: image/png\r\n\r\n"];
        //将body字符串转化为UTF8格式的二进制
        [myRequestData appendData:[[NSString stringWithFormat:@"%@%@",body,uplodaBody] dataUsingEncoding:gbkEncoding]];
        //将image的data加入
        [myRequestData appendData:[_params objectForKey:@"elecImg"]];
        //加入结束符--AaB03x--
        [myRequestData appendData:[end dataUsingEncoding:gbkEncoding]];

        //设置HTTPHeader中Content-Type的值
        NSString *content=[[NSString alloc]initWithFormat:@"multipart/form-data; boundary=%@",TWITTERFON_FORM_BOUNDARY];
        //设置HTTPHeader
        [request setValue:content forHTTPHeaderField:@"Content-Type"];
        //设置Content-Length
        [request setValue:[NSString stringWithFormat:@"%d", [myRequestData length]] forHTTPHeaderField:@"Content-Length"];
        
        
//        [request setValue:@"multipart/form-data" forHTTPHeaderField: @"Content-Type"];
//        NSMutableData *body = [NSMutableData data];
//        for(NSString *key in _params){
//            id value=[_params objectForKey:key];
//            if([value isKindOfClass:[NSData class]]){
//                [body appendData:[[NSString stringWithFormat:@"--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
//                [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"; filename=\"data\"\r\n", key] dataUsingEncoding:NSUTF8StringEncoding]];
//                [body appendData:[@"Content-Type: image/jpeg\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
//                [body appendData:value];
//                [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
//            }else{
//                [body appendData:[[NSString stringWithFormat:@"--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
//                [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", key] dataUsingEncoding:NSUTF8StringEncoding]];
//                [body appendData:[[NSString stringWithFormat:@"%@\r\n", value] dataUsingEncoding:NSUTF8StringEncoding]];
//            }
//        }
//        [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
//        NSLog(@"%@",body);
//        request.HTTPBody=body;
//        NSString *postLength = [NSString stringWithFormat:@"%d",[body length]];
//        [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    }
    // 初始化一个连接
    NSURLConnection *conn = [NSURLConnection connectionWithRequest:request delegate:self];
    // 开始一个异步请求
    [conn start];
    
    if(_isFileDownload){
        if(self.isShowMessage){
            _atmHud=[[ATMHud alloc]init];
            [self.controller.view addSubview:_atmHud.view];
            [_atmHud setCaption:@"下载中..."];
            [_atmHud setProgress:0.01];
            [_atmHud show];
        }
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
        if(self.isShowMessage){
            NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
            if(httpResponse && [httpResponse respondsToSelector:@selector(allHeaderFields)]){
                NSDictionary *httpResponseHeaderFields = [httpResponse allHeaderFields];
                //获取文件文件的大小
                downloadFileSize = [[httpResponseHeaderFields objectForKey:@"Content-Length"] longLongValue];
            }
        }
    }
}

#pragma mark 接收到服务器返回的数据
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [_resultData appendData:data];
    if(self.isFileDownload) {
        if(self.isShowMessage){
            //显示下载进度条
            if(_atmHud) {
                float size=[_resultData length]/(float)downloadFileSize;
                if(size>0) {
                    [_atmHud setProgress:size];
                }
            }
        }
    }
}

#pragma mark 服务器的数据已经接收完毕时调用
- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    Response *response=[[Response alloc]init];
    [response setResultData:_resultData];
    if(!self.isFileDownload){
        NSStringEncoding gbkEncoding =CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
        NSString*pageSource = [[NSString alloc] initWithData:_resultData encoding:gbkEncoding];
//        NSLog(@"%@",pageSource);
        if(pageSource!=nil) {
            NSString *responseString=[pageSource stringByReplacingPercentEscapesUsingEncoding:gbkEncoding];
            [response setResponseString:responseString];
            if(responseString!=nil){
                [response setResultJSON:[NSJSONSerialization JSONObjectWithData:[responseString dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableLeaves error:nil]];
            }
        }
    }
    
    if( [_delegate respondsToSelector: @selector(requestFinishedByResponse:responseCode:)]) {
        //调用代理对象
        [_delegate requestFinishedByResponse:response responseCode:_responseCode];
    }
    
    //隐藏等待条
    if (_mbpHud) {
        [_mbpHud hide:YES];
    }
    if(self.isFileDownload) {
        if(self.isShowMessage){
            //隐藏下载进度条
            if(_atmHud) {
                [_atmHud hide];
            }
        }
    }
    
}

#pragma mark 网络连接出错时调用
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    
    _resultData=nil;
    if( [_delegate respondsToSelector: @selector(requestFailed:didFailWithError:)]) {
        [_delegate requestFailed:_responseCode didFailWithError:error];
    } else {
        [Common alert:[NSString stringWithFormat:@"网络连接出错:%@",[error localizedDescription]]];
    }
    
    //隐藏等待条
    if (_mbpHud) {
        [_mbpHud hide:YES];
    }
    if(self.isFileDownload) {
        if(self.isShowMessage){
            //隐藏下载进度条
            if(_atmHud) {
                [_atmHud hide];
            }
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
