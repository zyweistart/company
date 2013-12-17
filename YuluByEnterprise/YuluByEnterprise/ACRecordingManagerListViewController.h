#import <UIKit/UIKit.h>
#import "BaseRefreshTableViewController.h"
#import "ACPlayerView.h"

@interface ACRecordingManagerListViewController :BaseRefreshTableViewController<UIActionSheetDelegate,UIAlertViewDelegate>{
    ACPlayerView *_playerView;
    NSMutableDictionary *_currentDictionary;
}

@end
