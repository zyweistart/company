//
//  ACRechargeViewByASController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//
#import "BaseRefreshTableViewController.h"

@class ACRechargeNav;
@interface ACRechargeViewByASController :BaseRefreshTableViewController<UIScrollViewDelegate,UIActionSheetDelegate>{
    
    HttpRequest *_loadHttp;
    
    int currentTab;
    
    ACRechargeNav *_rechargeNav;
    
    UIButton *_leftTopTab;
    NSInteger _leftCurrentPage;
	BOOL _leftReloading;
    BOOL _leftLoadOver;
    NSMutableArray *_leftDataItemArray;
    
    UIButton *_centerTopTab;
    NSInteger _centerCurrentPage;
	BOOL _centerReloading;
    BOOL _centerLoadOver;
    NSMutableArray *_centerDataItemArray;
    
    UIButton *_rightTopTab;
    NSInteger _rightCurrentPage;
	BOOL _rightReloading;
    BOOL _rightLoadOver;
    NSMutableArray *_rightDataItemArray;
    
    UILabel *_lblSlid;
    
    MBProgressHUD *_hud;
    
}

@end

