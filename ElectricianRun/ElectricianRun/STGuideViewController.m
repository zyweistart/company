//
//  STGuideViewController.m
//  ElectricianRun
//
//  Created by Start on 2/11/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STGuideViewController.h"
#import "STIndexViewController.h"
#import "STStudyViewController.h"
#import "STMyeViewController.h"
#import "STUserExperienceAlarmViewController.h"

#import "SQLiteOperate.h"

#define DOWNLOADPIC 500
#define DOWNLOADHTML 501
#define DOWNLOADICONFILE 499

@interface STGuideViewController ()

@end

@implementation STGuideViewController {
    SQLiteOperate *db;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.view setBackgroundColor:[UIColor whiteColor]];
    //获取最后保存的版本号不存在则为0
    float lastVersionNo=[[Common getCache:DEFAULTDATA_LASTVERSIONNO] floatValue];
    NSDictionary* infoDict =[[NSBundle mainBundle] infoDictionary];
    //获取当前使用的版本号
    NSString *currentVersionNo=[infoDict objectForKey:@"CFBundleShortVersionString"];
    if([currentVersionNo floatValue]>lastVersionNo){
        [self showIntroWithCrossDissolve];
    }else{
        [self gotoMainPage];
    }
}

- (void)showIntroWithCrossDissolve {
    EAIntroPage *page1 = [EAIntroPage page];
    page1.title = @"Hello world";
    page1.desc = @"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    page1.bgImage = [UIImage imageNamed:@"1"];
    page1.titleImage = [UIImage imageNamed:@"original"];
    
    EAIntroPage *page2 = [EAIntroPage page];
    page2.title = @"This is page 2";
    page2.desc = @"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore.";
    page2.bgImage = [UIImage imageNamed:@"2"];
    page2.titleImage = [UIImage imageNamed:@"supportcat"];
    
    EAIntroPage *page3 = [EAIntroPage page];
    page3.title = @"This is page 3";
    page3.desc = @"Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.";
    page3.bgImage = [UIImage imageNamed:@"3"];
    page3.titleImage = [UIImage imageNamed:@"femalecodertocat"];
    
    EAIntroView *intro = [[EAIntroView alloc] initWithFrame:self.view.bounds andPages:@[page1,page2,page3]];
    
    [intro setDelegate:self];
    [intro showInView:self.view animateDuration:0.0];
}

- (void)introDidFinish {
    NSDictionary* infoDict =[[NSBundle mainBundle] infoDictionary];
    NSString *currentVersionNo=[infoDict objectForKey:@"CFBundleShortVersionString"];
    [Common setCache:DEFAULTDATA_LASTVERSIONNO data:currentVersionNo];
    [self gotoMainPage];
}

- (void)gotoMainPage
{
    
    //首页
    UINavigationController *indexViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STIndexViewController alloc]init]];
    indexViewControllerNav.navigationBarHidden=YES;
    indexViewControllerNav.tabBarItem.title=@"首页";
    //我要学习
    UINavigationController *studyViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STStudyViewController alloc]init]];
    studyViewControllerNav.tabBarItem.title=@"我要学习";
    //我的E电工
    UINavigationController *myeViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STMyeViewController alloc]init]];
    myeViewControllerNav.tabBarItem.title=@"我的E电工";
    
    [self setViewControllers:[NSArray arrayWithObjects:
                             indexViewControllerNav,
                             studyViewControllerNav,
                             myeViewControllerNav,
                              nil] animated:YES];
    
//    [self downLoadPicture];
//    db=[[SQLiteOperate alloc]init];
//    [self downLoadHtml];

    
//    self.downloadIcon=[[DownloadIcon alloc]init];
//    [self.downloadIcon startWithUrl:@"http://122.224.247.221:7003/html/AppNews/images/app_20140117095452.jpg"];
    
}

//下载图片
- (void)downLoadPicture
{
    NSString *URL=@"http://122.224.247.221:7003/WEB/mobile/news.aspx";
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"0" forKey:@"type"];
    [p setObject:@"3" forKey:@"num"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:DOWNLOADPIC];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URL params:p];
}

//下载新闻
- (void)downLoadHtml
{
    NSString *URL=@"http://122.224.247.221:7003/WEB/mobile/news.aspx";
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"1" forKey:@"type"];
    [p setObject:@"7" forKey:@"num"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:DOWNLOADHTML];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URL params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    if(repCode==DOWNLOADPIC){
        NSDictionary *dic=[[response resultJSON]objectForKey:@"Rows"];
        if(dic!=nil) {
            if([@"0" isEqualToString:[dic objectForKey:@"result"]]){
                [Common alert:[dic objectForKey:@"remark"]];
            }
        }
    }else if(repCode==DOWNLOADHTML){
        NSString *url=[[response resultJSON] objectForKey:@"URL"];
        NSMutableArray *outdata=[[response resultJSON] objectForKey:@"FILE_NAMES"];
        
        if([db openDB]){
            if([db createTable]){
                NSString *sqlQuery = [NSString stringWithFormat:@"SELECT * FROM NEW WHERE URL='%@'",url];
                NSMutableArray *indata=[db query:sqlQuery];
                for(NSDictionary *outd in outdata){
                    
                    NSString *outname=[outd objectForKey:@"NAME"];
                    NSString *outicon_name=[outd objectForKey:@"ICO_NAME"];
                    NSString *outfile_name=[outd objectForKey:@"FILE_NAME"];
                    NSString *outcontent=[outd objectForKey:@"CONTENT"];

                    NSMutableString *iconurl=[[NSMutableString alloc]initWithString:url];
                    [iconurl appendFormat:@"images/%@",outicon_name];
                    
                    self.downloadIcon=[[DownloadIcon alloc]init];
                    [self.downloadIcon startWithUrl:iconurl];
                    
                    break;
                    
                    
                    
                    
                    
//                    BOOL flag=NO;
//                    for(NSDictionary *ind in indata){
//                        flag=NO;
//                        NSString *inname=[ind objectForKey:@"name"];
//                        NSString *inicon_name=[ind objectForKey:@"icon_name"];
//                        NSString *infile_name=[ind objectForKey:@"file_name"];
//                        NSString *incontent=[ind objectForKey:@"content"];
//                        
//                        if([outname isEqualToString:inname]&&
//                           [outicon_name isEqualToString:inicon_name]&&
//                           [outfile_name isEqualToString:infile_name]&&
//                           [outcontent isEqualToString:incontent]){
//                            
//                            flag=YES;
//                            
//                            break;
//                        }
//                        
//                    }
//                    if(!flag){
//                        NSString *sql = [NSString stringWithFormat:
//                                         @"INSERT INTO NEW (URL,NAME,ICO_NAME,FILE_NAME,CONTENT) VALUES ('%@', '%@', '%@' ,'%@' ,'%@')",url, outname, outicon_name, outfile_name, outcontent];
//                        if([db execSql:sql]){
//                            
//                            
//                        }
//                    }
                }
            }
        }
        
        
    }else if(repCode==DOWNLOADICONFILE){
        
        
        
    }
    
}

@end
