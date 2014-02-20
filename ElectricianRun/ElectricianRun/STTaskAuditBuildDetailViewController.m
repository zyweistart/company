//
//  STTaskAuditBuildDetailViewController.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditBuildDetailViewController.h"

@interface STTaskAuditBuildDetailViewController ()

@end

@implementation STTaskAuditBuildDetailViewController {
    UITextField *txtValue1;
    UITextField *txtValue2;
    UITextField *txtValue3;
    UITextField *txtValue4;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"巡检任务生成";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 80, 320, 210)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lblName=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 90, 30)];
    lblName.font=[UIFont systemFontOfSize:12.0];
    [lblName setText:@"客户名称"];
    [lblName setTextColor:[UIColor blackColor]];
    [lblName setBackgroundColor:[UIColor clearColor]];
    [lblName setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblName];
    
    txtValue1=[[UITextField alloc]initWithFrame:CGRectMake(105, 10, 150, 30)];
    [txtValue1 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue1 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue1 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue1 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue1 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue1 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue1];
    
    lblName=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 90, 30)];
    lblName.font=[UIFont systemFontOfSize:12.0];
    [lblName setText:@"站点名称"];
    [lblName setTextColor:[UIColor blackColor]];
    [lblName setBackgroundColor:[UIColor clearColor]];
    [lblName setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblName];
    
    txtValue2=[[UITextField alloc]initWithFrame:CGRectMake(105, 50, 150, 30)];
    [txtValue2 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue2 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue2 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue2 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue2 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue2 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue2];
    
    lblName=[[UILabel alloc]initWithFrame:CGRectMake(10, 90, 90, 30)];
    lblName.font=[UIFont systemFontOfSize:12.0];
    [lblName setText:@"任务开始时间"];
    [lblName setTextColor:[UIColor blackColor]];
    [lblName setBackgroundColor:[UIColor clearColor]];
    [lblName setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblName];
    
    txtValue3=[[UITextField alloc]initWithFrame:CGRectMake(105, 90, 150, 30)];
    [txtValue3 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue3 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue3 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue3 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue3 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue3 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue3];
    
    lblName=[[UILabel alloc]initWithFrame:CGRectMake(10, 130, 90, 30)];
    lblName.font=[UIFont systemFontOfSize:12.0];
    [lblName setText:@"任务结束时间"];
    [lblName setTextColor:[UIColor blackColor]];
    [lblName setBackgroundColor:[UIColor clearColor]];
    [lblName setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblName];
    
    txtValue4=[[UITextField alloc]initWithFrame:CGRectMake(105, 130, 150, 30)];
    [txtValue4 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue4 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue4 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue4 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue4 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue4 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue4];
    
}

- (void)backgroundDoneEditing:(id)sender {
    [txtValue1 resignFirstResponder];
    [txtValue2 resignFirstResponder];
    [txtValue3 resignFirstResponder];
    [txtValue4 resignFirstResponder];
}

@end
