//
//  STUserExperienceSelectViewController.m
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceSelectViewController.h"
#import "STUserExperienceAlarmViewController.h"
#import "STUserExperienceViewController.h"
#import <AVFoundation/AVFoundation.h>

@interface STUserExperienceSelectViewController () <AVAudioPlayerDelegate>

@end

@implementation STUserExperienceSelectViewController {
    AVAudioPlayer *player;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"用户体验";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
    }
    return self;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    float height=self.view.frame.size.height-64;
    self.automaticallyAdjustsScrollViewInsets=NO;
    UIScrollView *control=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 64, 320, height)];
    control.contentSize = CGSizeMake(640,height);
    [control setScrollEnabled:YES];
    UIView *firstView=[[UIView alloc]initWithFrame:CGRectMake(0, 0, 320, height)];
    UIButton *btnAlarm=[[UIButton alloc]initWithFrame:CGRectMake(100, 100, 150, 30)];
    [btnAlarm setTitle:@"报警体验" forState:UIControlStateNormal];
    [btnAlarm setBackgroundColor:[UIColor blueColor]];
    [btnAlarm addTarget:self action:@selector(alarm:) forControlEvents:UIControlEventTouchUpInside];
    [firstView addSubview:btnAlarm];
    [control addSubview:firstView];
    
    UIView *secondView=[[UIView alloc]initWithFrame:CGRectMake(320, 0, 320, height)];
    UIButton *btnBusiness=[[UIButton alloc]initWithFrame:CGRectMake(100, 150, 150, 30)];
    [btnBusiness setTitle:@"商业用户" forState:UIControlStateNormal];
    [btnBusiness setBackgroundColor:[UIColor blueColor]];
    [btnBusiness addTarget:self action:@selector(business:) forControlEvents:UIControlEventTouchUpInside];
    [secondView addSubview:btnBusiness];

    UIButton *btnIndustrial=[[UIButton alloc]initWithFrame:CGRectMake(100, 200, 150, 30)];
    [btnIndustrial setTitle:@"大工业用户" forState:UIControlStateNormal];
    [btnIndustrial setBackgroundColor:[UIColor blueColor]];
    [btnIndustrial addTarget:self action:@selector(industrial:) forControlEvents:UIControlEventTouchUpInside];
    [secondView addSubview:btnIndustrial];
    [control addSubview:secondView];
    
    [self.view addSubview:control];
}

- (void)alarm:(id)sender
{
//    UINavigationController *userExperienceAlarmViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceAlarmViewController alloc]init]];
//    [self presentViewController:userExperienceAlarmViewControllerNav animated:YES completion:nil];
    NSString *path=[[NSBundle mainBundle] pathForResource: @"alarm" ofType: @"mp3"];
    NSURL *url=[[NSURL alloc] initFileURLWithPath:path];
    NSError *error;
    player=[[AVAudioPlayer alloc] initWithContentsOfURL:url error:&error];
    if (error) {
        NSLog(@"error:%@",[error description]);
        return;
    }
//    player.delegate=self;
    //    [player setVolume:1.0];
    [player setVolume:1];   //设置音量大小
    player.numberOfLoops = -1;//设置音乐播放次数  -1为一直循环
    [player prepareToPlay];
    [player play];
    NSLog(@"%@",path);
}

- (void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag
{
    NSLog(@"audioPlayerDidFinishPlaying");
}

- (void)audioPlayerDecodeErrorDidOccur:(AVAudioPlayer *)player error:(NSError *)error
{
    NSLog(@"audioPlayerDecodeErrorDidOccur");
}

- (void)business:(id)sender
{
    UINavigationController *userExperienceViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceViewController alloc]initWithUserType:2]];
    [self presentViewController:userExperienceViewControllerNav animated:YES completion:nil];
}

- (void)industrial:(id)sender
{
    UINavigationController *userExperienceViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceViewController alloc]initWithUserType:1]];
    [self presentViewController:userExperienceViewControllerNav animated:YES completion:nil];
}

@end
