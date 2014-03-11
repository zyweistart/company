//
//  STChartBurdenLineViewController.m
//  ElectricianRun
//  进出线负荷曲线图
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChartBurdenLineViewController.h"
//保存最近12个每条进出线的负荷数
extern double allTotalBurden[12][2][4];

@interface STChartBurdenLineViewController ()

@end

@implementation STChartBurdenLineViewController {
    int count;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"进出线负荷曲线图";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        count=12;
    }
    return self;
}

- (void)viewDidLoad
{
    
    CGRect webFrame = self.view.frame;
    webFrame.origin.x = 0;
    webFrame.origin.y =  0;
    
    _webViewForSelectDate = [[UIWebView alloc] initWithFrame:webFrame];
    _webViewForSelectDate.delegate = self;
    _webViewForSelectDate.scalesPageToFit = YES;
    _webViewForSelectDate.opaque = NO;
    _webViewForSelectDate.backgroundColor = [UIColor clearColor];
    _webViewForSelectDate.autoresizingMask = (UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight);
    [self.view addSubview:_webViewForSelectDate];
    
    //所有的资源都在source.bundle这个文件夹里
    NSString* htmlPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:@"source.bundle/index.html"];
    
    NSURL* url = [NSURL fileURLWithPath:htmlPath];
    NSURLRequest* request = [NSURLRequest requestWithURL:url];
    [_webViewForSelectDate loadRequest:request];
    
    [super viewDidLoad];
}

-(void)updateData
{
    //取得当前时间，x轴
    NSDate* nowDate = [[NSDate alloc]init];
    NSTimeInterval nowTimeInterval = [nowDate timeIntervalSince1970] * 1000;
    
    count--;
    
    float temperature=allTotalBurden[count%12][_currentIndex/4][_currentIndex%4]/1000;
    
    if(count==0){
        count=12;
    }
    
    
    NSMutableString* jsStr = [[NSMutableString alloc] initWithCapacity:0];
    [jsStr appendFormat:@"updateData(%f,%f)",nowTimeInterval,temperature];
    
    [_webViewForSelectDate stringByEvaluatingJavaScriptFromString:jsStr];
}

#pragma mark - delegate of webview
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    return YES;
}

-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    //等webview加载完毕再更新数据
    _timer = [NSTimer scheduledTimerWithTimeInterval: 5
                                             target: self
                                           selector: @selector(updateData)
                                           userInfo: nil
                                            repeats: YES];
}
@end
