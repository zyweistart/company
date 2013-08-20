//
//  ACRecordingCell.h
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

@interface ACRecordingCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *lbl_oppno;
@property (weak, nonatomic) IBOutlet UILabel *lbl_lcalltime;
@property (weak, nonatomic) IBOutlet UILabel *lbl_rtcount;
@property (weak, nonatomic) IBOutlet UILabel *lbl_orttime;

@property (strong, nonatomic) UILabel *lblOppno;
@property (strong, nonatomic) UILabel *lblLcalltime;
@property (strong, nonatomic) UILabel *lblRtcount;
@property (strong, nonatomic) UILabel *lblOrttime;

@end
