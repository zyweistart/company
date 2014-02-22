//
//  STDataMonitoringLineSearchViewController.m
//  ElectricianRun
//
//  Created by Start on 2/21/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STDataMonitoringLineSearchViewController.h"

@interface STDataMonitoringLineSearchViewController ()

@end

@implementation STDataMonitoringLineSearchViewController{
    UIPickerView *pvSelectType;
    UITextField *txtValueName;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"搜索";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 300)];
    //    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 100, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"客户名称"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueName=[[UITextField alloc]initWithFrame:CGRectMake(80, 100, 150, 30)];
    [txtValueName setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueName setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueName setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueName setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueName setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueName setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValueName];
    
    UIButton *btnSearch=[[UIButton alloc]initWithFrame:CGRectMake(110, 160, 100, 30)];
    [btnSearch setTitle:@"查询" forState:UIControlStateNormal];
    [btnSearch setBackgroundColor:[UIColor blueColor]];
    [btnSearch addTarget:self action:@selector(search:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnSearch];
    
}

- (void)search:(id)sender {
    NSString *name=[txtValueName text];
    NSMutableDictionary *data=[[NSMutableDictionary alloc]init];
    [data setObject:name forKey:@"name"];
    [self.delegate startSearch:data];
    [self.navigationController popViewControllerAnimated:YES];
}
@end
