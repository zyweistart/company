//
//  ACAccountPay1Cell.h
//  Ancun
//
//  Created by Start on 4/4/14.
//
//

#import <UIKit/UIKit.h>

@interface ACAccountPay1Cell : UITableViewCell<UIActionSheetDelegate>


@property (strong,nonatomic) UILabel *lblName;
@property (strong,nonatomic) UILabel *lblTimeLong;
@property (strong,nonatomic) UILabel *lblTime;
@property (strong,nonatomic) UILabel *lblStorage;
@property (strong,nonatomic) UILabel *lblTimeAndStorage;
@property (strong,nonatomic) UIButton *btnGoPay;

@property (assign,nonatomic) int currentType;
@property (strong,nonatomic) NSMutableDictionary *data;
@property (strong,nonatomic) UIViewController *controler;

@end
