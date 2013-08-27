//
//  ACAccountViewController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//
#import "BaseRefreshTableViewController.h"

@class ACRechargeNav;
@interface ACRechargeByAppStoreViewController :BaseRefreshTableViewController<HttpViewDelegate,UIActionSheetDelegate>{
    
    HttpRequest *_loadHttp;
    
    ACRechargeNav *_rechargeNav;
    
    MBProgressHUD *_hud;
    
}

@end

