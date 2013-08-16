//
//  ACRecordingManagerDetailListOldViewController.h
//  Ancun
//
//  Created by Start on 13-8-15.
//
//

#import "BaseRefreshTableViewController.h"
#import "ACPlayerView.h"

@interface ACRecordingManagerDetailListOldViewController :BaseRefreshTableViewController<UIActionSheetDelegate,UIAlertViewDelegate>{
    ACPlayerView *_playerView;
    NSMutableDictionary *_currentDictionary;
}

@end
