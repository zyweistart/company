//
//  ACRecordingManagerDetailListViewController.h
//  ACyulu
//
//  Created by Start on 12-12-7.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "BaseRefreshTableViewController.h"
#import "ACPlayerView.h"

@interface ACRecordingManagerDetailListViewController :BaseRefreshTableViewController<UIActionSheetDelegate,UIAlertViewDelegate>{
    NSString *_oppno;
    ACPlayerView *_playerView;
    NSMutableDictionary *_currentDictionary;
}

- (id)initWithOppno:(NSString*)_oppno;

@end
