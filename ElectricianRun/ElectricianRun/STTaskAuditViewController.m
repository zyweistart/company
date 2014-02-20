//
//  STTaskAuditViewController.m
//  ElectricianRun
//  任务稽核
//  Created by Start on 1/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditViewController.h"
#import "STMapViewController.h"

@interface STTaskAuditViewController ()

@end

@implementation STTaskAuditViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"任务稽核";
        
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
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 300)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UIButton *btn1=[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 110, 40)];
    btn1.titleLabel.font=[UIFont systemFontOfSize:12.0f];
    [btn1 setTitle:@"巡检任务生成" forState:UIControlStateNormal];
    [btn1 setBackgroundColor:[UIColor blueColor]];
    [btn1 addTarget:self action:@selector(build:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btn1];
    
    UIButton *btn2=[[UIButton alloc]initWithFrame:CGRectMake(110, 0, 100, 40)];
    btn2.titleLabel.font=[UIFont systemFontOfSize:12.0f];
    [btn2 setTitle:@"巡检记录" forState:UIControlStateNormal];
    [btn2 setBackgroundColor:[UIColor blueColor]];
    [btn2 addTarget:self action:@selector(recording:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btn2];
    
    UIButton *btn3=[[UIButton alloc]initWithFrame:CGRectMake(210, 0, 110, 40)];
    btn3.titleLabel.font=[UIFont systemFontOfSize:12.0f];
    [btn3 setTitle:@"工作人员位置" forState:UIControlStateNormal];
    [btn3 setBackgroundColor:[UIColor blueColor]];
    [btn3 addTarget:self action:@selector(maplocation:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btn3];
    
}

- (void)build:(id)sender {
    NSLog(@"巡检任务生成");
}

- (void)recording:(id)sender {
    NSLog(@"巡检记录");
}

- (void)maplocation:(id)sender {
    STMapViewController *mapViewController=[[STMapViewController alloc]init];
    [self.navigationController pushViewController:mapViewController animated:YES];
}

- (void)backgroundDoneEditing:(id)sender {
//    [txtValue1 resignFirstResponder];
}

@end
