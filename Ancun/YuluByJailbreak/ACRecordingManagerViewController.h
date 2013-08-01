//
//  ACRecordingManagerViewController.h
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "BaseRefreshTableViewController.h"

@interface ACRecordingManagerViewController : BaseRefreshTableViewController<UIActionSheetDelegate>{
    HttpRequest *_loadDataHttp;
}

@end
