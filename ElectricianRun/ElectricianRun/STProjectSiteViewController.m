//
//  STProjectSiteViewController.m
//  ElectricianRun
//  工程建站
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STProjectSiteViewController.h"
#import "STScanningViewController.h"

@interface STProjectSiteViewController ()<ScanningDelegate>

@end

@implementation STProjectSiteViewController{
    UITextField *txtValue1;
    UITextField *txtValue2;
    UITextField *txtValue3;
    UITextField *txtValue4;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        self.title=@"工程建站";
        
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
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 164, 320, 300)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    //序列号
    UILabel *lblValue1=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 60, 30)];
    lblValue1.font=[UIFont systemFontOfSize:12.0];
    [lblValue1 setText:@"序列号"];
    [lblValue1 setTextColor:[UIColor blackColor]];
    [lblValue1 setBackgroundColor:[UIColor clearColor]];
    [lblValue1 setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblValue1];
    
    txtValue1=[[UITextField alloc]initWithFrame:CGRectMake(80, 10, 150, 30)];
    [txtValue1 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue1 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue1 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue1 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue1 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue1 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue1];
    
    UIButton *btnCalculate=[[UIButton alloc]initWithFrame:CGRectMake(235, 10, 30, 30)];
    [btnCalculate setTitle:@"..." forState:UIControlStateNormal];
    [btnCalculate setBackgroundColor:[UIColor blueColor]];
    //    [btnCalculate setBackgroundImage:[UIImage imageNamed:@"button_gb"] forState:UIControlStateNormal];
    [btnCalculate addTarget:self action:@selector(scanning:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnCalculate];
    //客户名称
    UILabel *lblValue2=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 60, 30)];
    lblValue2.font=[UIFont systemFontOfSize:12.0];
    [lblValue2 setText:@"客户名称"];
    [lblValue2 setTextColor:[UIColor blackColor]];
    [lblValue2 setBackgroundColor:[UIColor clearColor]];
    [lblValue2 setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblValue2];
    
    txtValue2=[[UITextField alloc]initWithFrame:CGRectMake(80, 50, 150, 30)];
    [txtValue2 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue2 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue2 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue2 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue2 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue2 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue2];
    //唯一标识
    UILabel *lblValue3=[[UILabel alloc]initWithFrame:CGRectMake(10, 90, 60, 30)];
    lblValue3.font=[UIFont systemFontOfSize:12.0];
    [lblValue3 setText:@"唯一标识"];
    [lblValue3 setTextColor:[UIColor blackColor]];
    [lblValue3 setBackgroundColor:[UIColor clearColor]];
    [lblValue3 setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblValue3];
    
    txtValue3=[[UITextField alloc]initWithFrame:CGRectMake(80, 90, 150, 30)];
    [txtValue3 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue3 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue3 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue3 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue3 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue3 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue3];
    //配电房编号
    UILabel *lblValue4=[[UILabel alloc]initWithFrame:CGRectMake(10, 130, 60, 30)];
    lblValue4.font=[UIFont systemFontOfSize:12.0];
    [lblValue4 setText:@"唯一标识"];
    [lblValue4 setTextColor:[UIColor blackColor]];
    [lblValue4 setBackgroundColor:[UIColor clearColor]];
    [lblValue4 setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblValue4];
    
    txtValue4=[[UITextField alloc]initWithFrame:CGRectMake(80, 130, 150, 30)];
    [txtValue4 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue4 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue4 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue4 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue4 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue4 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue4];
    
    //建站
    UIButton *btnSite=[[UIButton alloc]initWithFrame:CGRectMake(80, 170, 80, 30)];
    [btnSite setTitle:@"建站" forState:UIControlStateNormal];
    btnSite.titleLabel.font=[UIFont systemFontOfSize: 12.0];
    [btnSite setBackgroundColor:[UIColor blueColor]];
//    [btnSubmit setBackgroundImage:[UIImage imageNamed:@"button_gb"] forState:UIControlStateNormal];
    [btnSite addTarget:self action:@selector(site:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnSite];
    //添加线路
    UIButton *btnAdd=[[UIButton alloc]initWithFrame:CGRectMake(160, 170, 80, 30)];
    [btnAdd setTitle:@"添加线路" forState:UIControlStateNormal];
    btnAdd.titleLabel.font=[UIFont systemFontOfSize: 12.0];
    [btnAdd setBackgroundColor:[UIColor blueColor]];
//    [btnSubmit setBackgroundImage:[UIImage imageNamed:@"button_gb"] forState:UIControlStateNormal];
    [btnAdd addTarget:self action:@selector(add:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnAdd];
    
}

- (void)success:(NSString*)value {
    [txtValue1 setText:value];
    [self backgroundDoneEditing:nil];
}

- (void)scanning:(id)sender {
    STScanningViewController *scanningViewController=[[STScanningViewController alloc]init];
    [scanningViewController setDelegate:self];
    [self presentViewController:scanningViewController animated:YES completion:nil];
}

- (void)site:(id)sender {
    
}
- (void)add:(id)sender {
    
}

- (void)backgroundDoneEditing:(id)sender {
    [txtValue1 resignFirstResponder];
    [txtValue2 resignFirstResponder];
    [txtValue3 resignFirstResponder];
    [txtValue4 resignFirstResponder];
}

@end
