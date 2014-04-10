//
//  ACGesturePasswordViewController.h
//  Ancun
//
//  Created by Start on 4/10/14.
//
//

#import "BaseViewController.h"
#import "KKGestureLockView.h"

@interface ACGesturePasswordViewController : BaseViewController<UITabBarControllerDelegate,KKGestureLockViewDelegate>

@property (strong,nonatomic) KKGestureLockView *lockView;

@end
