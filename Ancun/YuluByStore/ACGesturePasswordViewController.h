//
//  ACGesturePasswordViewController.h
//  Ancun
//
//  Created by Start on 4/10/14.
//
//

#import "BaseViewController.h"
#import "KKGestureLockView.h"

@interface ACGesturePasswordViewController : BaseViewController<KKGestureLockViewDelegate>

@property (strong,nonatomic) KKGestureLockView *lockView;

- (id)initWithFlag:(BOOL)flag;

@property BOOL flag;

@end
