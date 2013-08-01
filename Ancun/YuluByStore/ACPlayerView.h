//
//  ACPlayerView.h
//  ACyulu
//
//  Created by Start on 13-1-4.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import <AVFoundation/AVFoundation.h>

@interface ACPlayerView : UIView<AVAudioPlayerDelegate,UIActionSheetDelegate,ResultDelegate,HttpViewDelegate>{
    
    NSTimer *_sliderTimer;
    AVAudioPlayer *_player;
    
}

@property (strong,nonatomic) NSString *path;
@property (strong,nonatomic) NSMutableDictionary *dictionary;
@property (strong,nonatomic) UIViewController *controller;

@property (weak, nonatomic) IBOutlet UIButton *btn_notary;
@property (weak, nonatomic) IBOutlet UIButton *btn_extraction;
@property (weak, nonatomic) IBOutlet UIButton *btn_player;
@property (weak, nonatomic) IBOutlet UISlider *sider_player;
@property (weak, nonatomic) IBOutlet UILabel *lbl_playertimerlong;
@property (weak, nonatomic) IBOutlet UILabel *lbl_playertimertotallong;

+ (ACPlayerView *)instance:(UIViewController *)viewController;

- (void)player:(NSString *)playerPath dictionary:(NSMutableDictionary*)dic;
- (void)stop;

- (IBAction)btnPlayer:(id)sender;
- (IBAction)sliderChanged:(UISlider *)sender;
- (IBAction)notary:(id)sender;
- (IBAction)extraction:(id)sender;

@end