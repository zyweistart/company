//
//  ACTermsOfServiceViewController.m
//  ACyulu
//
//  Created by Start on 13-3-28.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "ACTermsOfServiceViewController.h"

@interface ACTermsOfServiceViewController ()

@end

@implementation ACTermsOfServiceViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.navigationItem.title=@"服务条款";
        _webView=[[UIWebView alloc]initWithFrame:
                  CGRectMake(0, 0,
                             self.view.frame.size.width,
                             self.view.frame.size.height)];
        [_webView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        NSString *html= [NSString stringWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"TermsOfService" ofType:@"txt"] encoding:NSUTF8StringEncoding error:nil];
        [_webView loadHTMLString:html baseURL:nil];
        [self.view addSubview:_webView];
    }
    return self;
}

- (void)viewDidAppear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewStartWithName:@"ACTermsOfServiceViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewEndWithName:@"ACTermsOfServiceViewController"];
}

@end
