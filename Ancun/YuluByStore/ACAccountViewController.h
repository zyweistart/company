//
//  ACAccountViewController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//
#import "BaseRefreshTableViewController.h"

@interface ACAccountViewController :BaseRefreshTableViewController<UIScrollViewDelegate,UIActionSheetDelegate>{
    
    HttpRequest *_loadHttp;
    
    int currentTab;
    
    UILabel *_lblSlid;
    UIButton *_leftTopTab;
    UIButton *_rightTopTab;
    
    NSMutableArray *_leftDataItemArray;
    NSMutableArray *_rightDataItemArray;
}

@end

