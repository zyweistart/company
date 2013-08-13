//
//  ACDialViewController.h
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

@interface ACDialViewController : ACBaseViewController<ABNewPersonViewControllerDelegate,UIActionSheetDelegate,HttpViewDelegate>{
    
    NSMutableString *_dialString;
    //退格定时器
    NSTimer *_timerBackDel;
    //长按标记
    Boolean longFlag;
    
}

@property (retain, nonatomic) IBOutlet UILabel *lbl_dial;
@property (retain, nonatomic) IBOutlet UILabel *lbl_name;

- (IBAction) btnTouchUpInside:(id)sender;

@end
