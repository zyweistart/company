//
//  ACPaymentCell.h
//  ACluyin
//
//  Created by Start on 13-5-3.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ACPaymentCell : UITableViewCell<UIActionSheetDelegate>

@property (strong, nonatomic) UILabel *lblName;
@property (strong, nonatomic) UILabel *lblDescription;

@property (assign,nonatomic) int currentType;
@property (strong,nonatomic) NSMutableDictionary *data;
@property (strong,nonatomic) UIViewController *controler;

@end
