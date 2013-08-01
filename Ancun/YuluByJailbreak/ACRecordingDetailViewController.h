//
//  ACRecordingDetailViewController.h
//  ACyulu
//
//  Created by Start on 12-12-8.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

@interface ACRecordingDetailViewController : UIViewController<ResultDelegate,HttpViewDelegate,UITextViewDelegate,UIActionSheetDelegate>
{
    NSMutableDictionary *_mainData;
}
@property (retain,nonatomic) NSString *fileno;

@property (retain, nonatomic) IBOutlet UILabel *lbl_callerno;
@property (retain, nonatomic) IBOutlet UILabel *lbl_calledno;
@property (retain, nonatomic) IBOutlet UILabel *lbl_begintime;
@property (retain, nonatomic) IBOutlet UILabel *lbl_endtime;
@property (retain, nonatomic) IBOutlet UILabel *lbl_duration;
@property (retain, nonatomic) IBOutlet UITextView *tv_remark;

@property (retain, nonatomic) IBOutlet UIButton *btn_notary;
@property (retain, nonatomic) IBOutlet UIButton *btn_extraction;

@property (retain,nonatomic) NSObject<ResultDelegate> *resultDelegate;
@property (retain,nonatomic) NSMutableDictionary *extractionDic;
//出证
- (IBAction)notary:(id)sender;
//提取码
- (IBAction)extraction:(id)sender;

- (IBAction)backgroundDoneEditing:(id)sender;

@end
