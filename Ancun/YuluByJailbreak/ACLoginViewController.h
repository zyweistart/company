//
//  ACLoginViewController.h
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//
#import "SSCheckBoxView.h"

@interface ACLoginViewController : UIViewController<UITabBarControllerDelegate,HttpViewDelegate,ResultDelegate>

@property (retain, nonatomic) IBOutlet UITextField *txtPhone;
@property (retain, nonatomic) IBOutlet UITextField *txtPassword;

@end
