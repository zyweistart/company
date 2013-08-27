//
//  ACAccountViewController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//
#import "BaseRefreshTableViewController.h"

@interface ACOldAccountViewController :BaseRefreshTableViewController<UIScrollViewDelegate,UIActionSheetDelegate>{
    
    HttpRequest *_loadHttp;
    UILabel *_lblTimeLong;
    
}

@end

