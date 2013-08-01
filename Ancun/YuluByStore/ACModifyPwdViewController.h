//
//  ACModifyPwdViewController.h
//  ACyulu
//
//  Created by Start on 13-1-11.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

@interface ACModifyPwdViewController : UIViewController<HttpViewDelegate>

@property (retain, nonatomic) IBOutlet UITextField *inputOldPassword;

@property (retain, nonatomic) IBOutlet UITextField *inputNewPassword;

@property (retain, nonatomic) IBOutlet UITextField *inputReNewPassword;

- (IBAction)submitPwdModify:(id)sender;

- (IBAction)backgroundDoneEditing:(id)sender;

@end
