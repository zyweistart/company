//
//  ACSetGesturePasswordViewController.m
//  Ancun
//
//  Created by Start on 4/10/14.
//
//

#import "ACSetGesturePasswordViewController.h"

@interface ACSetGesturePasswordViewController ()

@end

@implementation ACSetGesturePasswordViewController

- (id)init
{
    self = [super init];
    if (self) {
        self.title=@"手势设置";
    }
    return self;
}

- (void)gestureLockView:(KKGestureLockView *)gestureLockView didEndWithPasscode:(NSString *)passcode{
    NSLog(@"%@",passcode);
}

@end
