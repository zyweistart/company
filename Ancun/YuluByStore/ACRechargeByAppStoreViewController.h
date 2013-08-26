//
//  ACAccountViewController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//
#import "BaseTableViewController.h"

@interface ACRechargeByAppStoreViewController :BaseTableViewController<HttpViewDelegate>{
    
    HttpRequest *_loadHttp;
    
    int currentTab;
    
    UIButton *_leftTopTab;
    
    NSMutableArray *_leftDataItemArray;
    
    UIButton *_centerTopTab;
    
    NSMutableArray *_centerDataItemArray;
    
    UIButton *_rightTopTab;
    
    NSMutableArray *_rightDataItemArray;
    
    UILabel *_lblSlid;
    
    MBProgressHUD *_hud;
    
}

@end

