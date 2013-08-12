//
//  ACAccountViewController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//
#import "BaseRefreshTableViewController.h"

@interface ACRechargeViewController :BaseRefreshTableViewController<UIScrollViewDelegate,UIActionSheetDelegate>{
    
    HttpRequest *_loadHttp;
    
    int currentTab;
    
    UILabel *_lblTip1;
    UILabel *_lblTip2;
    UILabel *_lblTip3;
    UILabel *_lblTip4;
    UILabel *_lblSlid;
    
    UIButton *_leftTopTab;
    UIButton *_centerTopTab;
    UIButton *_rightTopTab;
    
    NSInteger _leftCurrentPage;
	BOOL _leftReloading;
    BOOL _leftLoadOver;
    NSMutableArray *_leftDataItemArray;
    
    NSInteger _centerCurrentPage;
	BOOL _centerReloading;
    BOOL _centerLoadOver;
    NSMutableArray *_centerDataItemArray;
    
    NSInteger _rightCurrentPage;
	BOOL _rightReloading;
    BOOL _rightLoadOver;
    NSMutableArray *_rightDataItemArray;
    
}

@end

