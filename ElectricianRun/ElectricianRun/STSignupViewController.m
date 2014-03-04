//
//  STSignupViewController.m
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STSignupViewController.h"

@interface STSignupViewController ()

@end

@implementation STSignupViewController {
    UITextField *txtName;
    UITextField *txtPhone;
    UITextField *txtCard;
    UITextField *txtProvince;
    UITextField *txtCity;
    UITextField *txtCounty;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"用户注册";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 290)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"姓名:"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtName=[[UITextField alloc]initWithFrame:CGRectMake(105, 10, 150, 30)];
    [txtName setFont:[UIFont systemFontOfSize: 12.0]];
    [txtName setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtName setBorderStyle:UITextBorderStyleRoundedRect];
    [control addSubview:txtName];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"手机:"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtPhone=[[UITextField alloc]initWithFrame:CGRectMake(105, 50, 150, 30)];
    [txtPhone setFont:[UIFont systemFontOfSize: 12.0]];
    [txtPhone setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtPhone setBorderStyle:UITextBorderStyleRoundedRect];
    [control addSubview:txtPhone];
 
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 90, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"身份证号码:"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtCard=[[UITextField alloc]initWithFrame:CGRectMake(105, 90, 150, 30)];
    [txtCard setFont:[UIFont systemFontOfSize: 12.0]];
    [txtCard setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtCard setBorderStyle:UITextBorderStyleRoundedRect];
    [control addSubview:txtCard];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 130, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"希望工作地"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 170, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"省份:"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtProvince=[[UITextField alloc]initWithFrame:CGRectMake(105, 170, 150, 30)];
    [txtProvince setFont:[UIFont systemFontOfSize: 12.0]];
    [txtProvince setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtProvince setBorderStyle:UITextBorderStyleRoundedRect];
    [control addSubview:txtProvince];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 210, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"城市:"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtCity=[[UITextField alloc]initWithFrame:CGRectMake(105, 210, 150, 30)];
    [txtCity setFont:[UIFont systemFontOfSize: 12.0]];
    [txtCity setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtCity setBorderStyle:UITextBorderStyleRoundedRect];
    [control addSubview:txtCity];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 250, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"县城镇:"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtCounty=[[UITextField alloc]initWithFrame:CGRectMake(105, 250, 150, 30)];
    [txtCounty setFont:[UIFont systemFontOfSize: 12.0]];
    [txtCounty setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtCounty setBorderStyle:UITextBorderStyleRoundedRect];
    [control addSubview:txtCounty];
    
}

- (void)backgroundDoneEditing:(id)sender {
    [txtName resignFirstResponder];
    [txtPhone resignFirstResponder];
    [txtCard resignFirstResponder];
    [txtProvince resignFirstResponder];
    [txtCity resignFirstResponder];
    [txtCounty resignFirstResponder];
}

@end
