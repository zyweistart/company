//
//  ACExtractionDetailViewController.h
//  ACyulu
//
//  Created by Start on 13-1-16.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import <MessageUI/MessageUI.h>

@interface ACExtractionDetailViewController : ACBaseViewController<MFMessageComposeViewControllerDelegate,UIActionSheetDelegate,HttpViewDelegate>{
    
    NSMutableDictionary *_resultDictionary;
    
}
@property BOOL load;
@property (retain,nonatomic) NSString *fileno;
@property (retain,nonatomic) NSMutableDictionary *extractionDics;
@property (strong,nonatomic) NSObject<ResultDelegate> *resultDelegate;

@property (retain, nonatomic) IBOutlet UILabel *lbl_accendcode;
@property (retain, nonatomic) IBOutlet UILabel *lbl_accendtime;

- (IBAction)cancelextraction:(id)sender;
- (IBAction)pasteBoard:(id)sender;
- (IBAction)sendMessage:(id)sender;

@end
