//
//  STFeedbackViewController.m
//  ElectricianRun
//  反馈
//  Created by Start on 3/3/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STFeedbackViewController.h"
#define SUBMITFEEDBACKREQUESTCODE 501

@interface STFeedbackViewController ()

@end

@implementation STFeedbackViewController {
    UITextField *txtContent;
    UITextField *txtEmail;
    UITextField *txtPhone;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"反馈";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"提交"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(submit:)];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 80, 320, 210)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"内容"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtContent=[[UITextField alloc]initWithFrame:CGRectMake(105, 10, 150, 30)];
    [txtContent setFont:[UIFont systemFontOfSize: 12.0]];
    [txtContent setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtContent setBorderStyle:UITextBorderStyleRoundedRect];
    [txtContent setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtContent setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtContent setPlaceholder:@"请输入您的反馈意见"];
    [control addSubview:txtContent];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"邮箱"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtEmail=[[UITextField alloc]initWithFrame:CGRectMake(105, 50, 150, 30)];
    [txtEmail setFont:[UIFont systemFontOfSize: 12.0]];
    [txtEmail setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtEmail setBorderStyle:UITextBorderStyleRoundedRect];
    [txtEmail setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtEmail setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtEmail setPlaceholder:@"以便将反馈意见告知您"];
    [control addSubview:txtEmail];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 90, 90, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"手机"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtPhone=[[UITextField alloc]initWithFrame:CGRectMake(105, 90, 150, 30)];
    [txtPhone setFont:[UIFont systemFontOfSize: 12.0]];
    [txtPhone setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtPhone setBorderStyle:UITextBorderStyleRoundedRect];
    [txtPhone setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtPhone setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtPhone setPlaceholder:@"请输入手机号码"];
    [control addSubview:txtPhone];
}

- (void)submit:(id)sender
{
    [self backgroundDoneEditing:nil];
    NSString *content=[txtContent text];
    if(![@"" isEqualToString:content]){
        NSString *email=[txtEmail text];
        NSString *phone=[txtPhone text];
        
        NSString *URL=@"http://122.224.247.221:7003/WEB/mobile/addSuggest.aspx";
        NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
        [p setObject:content forKey:@"content"];
        [p setObject:phone forKey:@"phone"];
        [p setObject:email forKey:@"mail"];
        [p setObject:@"2" forKey:@"msgType"];
        
        self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:SUBMITFEEDBACKREQUESTCODE];
        [self.hRequest setIsShowMessage:YES];
        [self.hRequest start:URL params:p];
    }else{
        [Common alert:@"请输入反馈内容"];
    }
    
}


- (void)backgroundDoneEditing:(id)sender
{
    [txtContent resignFirstResponder];
    [txtEmail resignFirstResponder];
    [txtPhone resignFirstResponder];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    if(repCode==SUBMITFEEDBACKREQUESTCODE){
        [Common alert:@"反馈成功!"];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

@end