//
//  Common.m
//  ElectricianRun
//
//  Created by Start on 2/10/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "Common.h"

@implementation Common

+ (id)getCache:(NSString *)key{
    NSUserDefaults *settings = [NSUserDefaults standardUserDefaults];
    return [settings objectForKey:key];
}
+ (void)setCache:(NSString *)key data:(id)data{
    NSUserDefaults *setting=[NSUserDefaults standardUserDefaults];
    [setting setObject:data forKey:key];
    [setting synchronize];
}
+ (BOOL)getCacheByBool:(NSString *)key{
    NSUserDefaults * settings = [NSUserDefaults standardUserDefaults];
    return [settings boolForKey:key];
}
+ (void)setCacheByBool:(NSString *)key data:(BOOL)data{
    NSUserDefaults *setting=[NSUserDefaults standardUserDefaults];
    [setting setBool:data forKey:key];
    [setting synchronize];
}

+ (void)alert:(NSString *)message{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"信息"
                          message:message
                          delegate:nil
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:nil, nil];
    [alert show];
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

+ (NSString *)NSNullConvertEmptyString:(id)value
{
    if([value isEqual:[NSNull null]]){
        return @"";
    }
    NSString *d=[NSString stringWithFormat:@"%@",value];
    return [d stringByReplacingOccurrencesOfString:@"+" withString:@" "];
}

+ (void)AsynchronousDownloadWithUrl:(NSString *)u FileName:(NSString *)fName image:(UIImageView*)img
{
    //创建文件管理器
    NSFileManager* fileManager = [NSFileManager defaultManager];
    //获取Documents主目录
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    //得到相应的Documents的路径
    NSString* docDir = [paths objectAtIndex:0];
    //更改到待操作的目录下
    [fileManager changeCurrentDirectoryPath:[docDir stringByExpandingTildeInPath]];
    NSString *path = [docDir stringByAppendingPathComponent:fName];
    //如果图标文件已经存在则进行显示否则进行下载
    if([fileManager fileExistsAtPath:path]){
        if(img!=nil){
            [img setImage:[UIImage imageWithContentsOfFile:path]];
        }
    }else{
        dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_LOW, 0);
        dispatch_async(queue, ^{
            NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:u]];
            dispatch_sync(dispatch_get_main_queue(), ^{
                if (data) {
                    //获取临时目录
                    NSString* tmpDir=NSTemporaryDirectory();
                    //更改到待操作的临时目录
                    [fileManager changeCurrentDirectoryPath:[tmpDir stringByExpandingTildeInPath]];
                    NSString *tmpPath = [tmpDir stringByAppendingPathComponent:fName];
                    //创建数据缓冲区
                    NSMutableData* writer = [[NSMutableData alloc] init];
                    //将字符串添加到缓冲中
                    [writer appendData: data];
                    //将其他数据添加到缓冲中
                    //将缓冲的数据写入到临时文件中
                    [writer writeToFile:tmpPath atomically:YES];
                    //把临时下载好的文件移动到主文档目录下
                    [fileManager moveItemAtPath:tmpPath toPath:path error:nil];
                    if(img!=nil){
                        [img setImage:[UIImage imageWithContentsOfFile:path]];
                    }
                }
            });
        });
    }
}

@end
