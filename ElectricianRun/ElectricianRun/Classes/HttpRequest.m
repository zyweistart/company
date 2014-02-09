//
//  HttpRequest.m
//  ElectricianRun
//
//  Created by Start on 2/9/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "HttpRequest.h"

#import "NSString+Utils.h"

@implementation HttpRequest{
    
    NSMutableData *_resultData;
    
}


- (void)start:(NSString*)URL params:(NSMutableDictionary*)params;{
//    NSString *HTTP_URL=[NSString stringWithFormat:@"http://122.224.247.221:7003/WEB/mobile/checkMobileValid.aspx?imei=zhangyy&&authentication=%@&&Type=2&&IsEncode=2",[@"8888AA" md5]];
    
    
    
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URL]];
    // 设置请求方法
    request.HTTPMethod = @"POST";
    // 60秒请求超时
    request.timeoutInterval = 60;
    // 初始化一个连接
    NSURLConnection *conn = [NSURLConnection connectionWithRequest:request delegate:self];
    // 开始一个异步请求
    [conn start];
}

#pragma mark 该方法在响应connection时调用
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    if(_resultData==nil) {
        _resultData=[[NSMutableData alloc]init];
    }
}

#pragma mark 接收到服务器返回的数据
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [_resultData appendData:data];
}

#pragma mark 服务器的数据已经接收完毕时调用
- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSStringEncoding gbkEncoding =CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSString*pageSource = [[NSString alloc] initWithData:_resultData encoding:gbkEncoding];
    NSString *urlStr=[pageSource stringByReplacingPercentEscapesUsingEncoding:gbkEncoding];
    NSLog(@"请求返回的内容：%@",urlStr);
    _resultData=nil;
}


@end
