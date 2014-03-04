//
//  DownloadIcon.m
//  ElectricianRun
//
//  Created by Start on 3/2/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "DownloadIcon.h"
#import "NSString+Utils.h"

#define DOWNLOADICONFILE 499

@implementation DownloadIcon

- (void)startWithUrl:(NSString*)url
{
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:url]];
    // 设置请求方法
//    request.HTTPMethod = @"POST";
    // 60秒请求超时
    request.timeoutInterval = 10;
    // 初始化一个连接
    NSURLConnection *conn = [NSURLConnection connectionWithRequest:request delegate:self];
    // 开始一个异步请求
    [conn start];
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    if(_resultData==nil) {
        _resultData=[[NSMutableData alloc]init];
    }
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [_resultData appendData:data];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
//    NSString *responseString =[[NSString alloc] initWithData:_resultData encoding:NSUTF8StringEncoding];
//    NSStringEncoding gbkEncoding =CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
//    NSString*pageSource = [[NSString alloc] initWithData:_resultData encoding:gbkEncoding];
//    NSLog(@"%@",pageSource);
    //创建文件管理器
    NSFileManager* fileManager = [NSFileManager defaultManager];
    //获取临时目录
    NSString* tmpDir=NSTemporaryDirectory();
    //更改到待操作的临时目录
    [fileManager changeCurrentDirectoryPath:[tmpDir stringByExpandingTildeInPath]];
    NSString *tmpPath = [tmpDir stringByAppendingPathComponent:[[self fileName] md5]];
    
    //创建数据缓冲区
    NSMutableData* writer = [[NSMutableData alloc] init];
    //将字符串添加到缓冲中
    [writer appendData: _resultData];
    //将其他数据添加到缓冲中
    //将缓冲的数据写入到临时文件中
    [writer writeToFile:tmpPath atomically:YES];
    
    //获取Documents主目录
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    //得到相应的Documents的路径
    NSString* docDir = [paths objectAtIndex:0];
    //更改到待操作的目录下
    [fileManager changeCurrentDirectoryPath:[docDir stringByExpandingTildeInPath]];
    NSString *path = [docDir stringByAppendingPathComponent:[[self fileName]md5]];
    //把临时下载好的文件移动到主文档目录下
    [fileManager moveItemAtPath:tmpPath toPath:path error:nil];
}

#pragma mark 网络连接出错时调用
- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    NSLog(@"网络连接出错%@",[error localizedDescription]);
}

@end
