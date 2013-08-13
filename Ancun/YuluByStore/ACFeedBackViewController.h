//
//  ACFeedBackViewController.h
//  ACyulu
//
//  Created by Start on 13-1-11.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

@interface ACFeedBackViewController : ACBaseViewController<UITextViewDelegate,HttpViewDelegate>

@property (retain, nonatomic) IBOutlet UITextView *fbContent;
@property (retain, nonatomic) IBOutlet UITextField *fbContact;
@property (retain, nonatomic) IBOutlet UILabel *lblContentPlaceholder;

- (IBAction)submitFeedBack:(id)sender;
- (IBAction)backgroundDoneEditing:(id)sender;
- (IBAction)viewMoveUp:(id)sender;
- (IBAction)viewMoveDown:(id)sender;



@end
