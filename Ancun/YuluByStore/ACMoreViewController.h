//
//  ACMoreViewController.h
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//
#import "BaseTableViewController.h"

@interface ACMoreViewController : BaseTableViewController<UIActionSheetDelegate>{
    NSMutableDictionary *_moreInSection;
    long long cachesize;
}

@end
