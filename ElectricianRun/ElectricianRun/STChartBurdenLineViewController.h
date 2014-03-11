//
//  STChartBurdenLineViewController.h
//  ElectricianRun
//
//  Created by Start on 2/27/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface STChartBurdenLineViewController : UIViewController<UIWebViewDelegate,UITextFieldDelegate>

@property(strong,nonatomic)UIWebView* webViewForSelectDate;
@property(strong,nonatomic)NSTimer* timer;

@property NSUInteger currentIndex;

@end

