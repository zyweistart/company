//
//  STAlarmManagerSearchViewController.m
//  ElectricianRun
//
//  Created by Start on 2/21/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STAlarmManagerSearchViewController.h"

@interface STAlarmManagerSearchViewController ()

@end

@implementation STAlarmManagerSearchViewController {
    UITextField *txtValueName;
    UITextField *txtValueLevel;
    UITextField *txtValueCategory;
    
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"报警查询";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 300)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"客户名称"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueName=[[UITextField alloc]initWithFrame:CGRectMake(80, 10, 150, 30)];
    [txtValueName setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueName setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueName setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueName setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueName setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueName setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValueName];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"报警级别"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueLevel=[[UITextField alloc]initWithFrame:CGRectMake(80, 50, 150, 30)];
    [txtValueLevel setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueLevel setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueLevel setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueLevel setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueLevel setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueLevel setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValueLevel];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 90, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"报警分类"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueCategory=[[UITextField alloc]initWithFrame:CGRectMake(80, 90, 150, 30)];
    [txtValueCategory setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueCategory setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueCategory setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueCategory setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueCategory setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueCategory setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValueCategory];
    
    UIButton *btnSearch=[[UIButton alloc]initWithFrame:CGRectMake(110, 130, 100, 30)];
    [btnSearch setTitle:@"查询" forState:UIControlStateNormal];
    [btnSearch setBackgroundColor:[UIColor blueColor]];
    [btnSearch addTarget:self action:@selector(search:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnSearch];
    
}

- (void)backgroundDoneEditing:(id)sender {
    [txtValueName resignFirstResponder];
    [txtValueLevel resignFirstResponder];
    [txtValueCategory resignFirstResponder];
}

- (void)search:(id)sender {
    NSString *name=[txtValueName text];
    NSString *level=[txtValueLevel text];
    NSString *category=[txtValueCategory text];
    NSMutableDictionary *data=[[NSMutableDictionary alloc]init];
    [data setObject:name forKey:@"QTKEY"];
    [data setObject:level forKey:@"QTKEY1"];
    [data setObject:category forKey:@"QTKEY2"];
    [self.delegate startSearch:data];
    [self.navigationController popViewControllerAnimated:YES];
}

@end
