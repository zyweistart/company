//
//  ACRechargeNav.h
//  Ancun
//
//  Created by Start on 13-8-13.
//
//

#import <UIKit/UIKit.h>

@interface ACRechargeNav : UIView

@property (strong,nonatomic) UILabel *lblTip1;
@property (strong,nonatomic) UILabel *lblTip2;
@property (strong,nonatomic) UILabel *lblTip3;
@property (strong,nonatomic) UILabel *lblTip4;

- (void)firstStep;
- (void)secondStep;
- (void)thirdStep;
- (void)fourthStep;

@end
