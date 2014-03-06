//
//  STSignupViewController.m
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STSignupViewController.h"
#import "CityParser.h"
#import "DataPickerView.h"
#import "ChineseToPinyin.h"

@interface STSignupViewController () <DataPickerViewDelegate>

@end

@implementation STSignupViewController {
    UITextField *txtName;
    UITextField *txtPhone;
    UITextField *txtCard;
    UITextField *txtProvince;
    UITextField *txtCity;
    UITextField *txtCounty;
    CityParser *citys;
    DataPickerView *dpvProvince;
    DataPickerView *dpvCity;
    DataPickerView *dpvCounty;
    
    NSMutableArray *dataProvinces;
    NSMutableArray *dataCitys;
    NSMutableArray *dataCountys;
    
    
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"电工注册";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    citys=[[CityParser alloc]init];
    [citys startParser];
    
    dataProvinces=[[citys citys] objectForKey:@"province_item"];
    dataCitys=[[citys citys] objectForKey:@"province_item"];
    dataCountys=[[citys citys] objectForKey:@"province_item"];
    
    dpvCity=[[DataPickerView alloc]initWithData:dataProvinces];
    [dpvCity setDelegate:self];
    
    dpvProvince=[[DataPickerView alloc]initWithData:dataCitys];
    [dpvProvince setDelegate:self];
    
    dpvCounty=[[DataPickerView alloc]initWithData:dataCountys];
    [dpvCounty setDelegate:self];
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 330)];
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
//    [txtProvince setInputView:dpvProvince];
    [txtProvince setText:@"北京市"];
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
//    [txtCity setInputView:dpvCity];
    [txtCity setText:@"市辖区"];
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
//    [txtCounty setInputView:dpvCounty];
    [txtCounty setText:@"东城区"];
    [control addSubview:txtCounty];
    
    UIButton *btnSubmit=[[UIButton alloc]initWithFrame:CGRectMake(85, 290, 150, 30)];
    [btnSubmit setTitle:@"提交" forState:UIControlStateNormal];
    [btnSubmit setBackgroundColor:[UIColor colorWithRed:(55/255.0) green:(55/255.0) blue:(139/255.0) alpha:1]];
    [btnSubmit addTarget:self action:@selector(submit:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnSubmit];
    
}

- (void)backgroundDoneEditing:(id)sender
{
    [txtName resignFirstResponder];
    [txtPhone resignFirstResponder];
    [txtCard resignFirstResponder];
    [txtProvince resignFirstResponder];
    [txtCity resignFirstResponder];
    [txtCounty resignFirstResponder];
}

- (void)submit:(id)sender
{
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"" forKey:@"name"];//姓名
    [p setObject:@"13738873386" forKey:@"telNum"];//手机号码
    [p setObject:@"330381198906240313" forKey:@"identityNo"];//身份证号码
    [p setObject:@"" forKey:@"intentArea"];//意向工作地区
    [p setObject:@"" forKey:@"identityImg"];
    [p setObject:@"" forKey:@"elecImg"];
    [p setObject:@"2" forKey:@"operateType"];
    [p setObject:@"" forKey:@"province"];//省
    [p setObject:@"" forKey:@"city"];//市
    [p setObject:@"" forKey:@"area"];//区
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URLelecRegister params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    NSLog(@"%@",[response responseString]);
}

- (void)pickerDidPressDoneWithRow:(NSInteger)row {
    if([txtProvince isFirstResponder]){
        NSString *name=[dataProvinces objectAtIndex:row];
        NSString *pinyin=[[ChineseToPinyin pinyinFromChiniseString:name] lowercaseString];
        dataCitys=[[citys citys]objectForKey:[NSString stringWithFormat:@"%@_province_item",pinyin]];
        [dpvCity setData:dataCitys];
        [txtProvince setText:name];
        [txtProvince resignFirstResponder];
    }
    if([txtCity isFirstResponder]){
        NSString *firstpinyin=[[ChineseToPinyin pinyinFromChiniseString:[txtProvince text]] lowercaseString];
        NSString *name=[dataCitys objectAtIndex:row];
        NSString *pinyin=[[ChineseToPinyin pinyinFromChiniseString:name] lowercaseString];
        dataCountys=[[citys citys]objectForKey:[NSString stringWithFormat:@"%@%@_city_item",firstpinyin,pinyin]];
        NSLog(@"%@",dataCountys);
        NSLog(@"%@-----beijingshixiaqu_city_item",[NSString stringWithFormat:@"%@%@_city_item",firstpinyin,pinyin]);
        [dpvCounty setData:dataCountys];
        [txtCity setText:name];
        [txtCity resignFirstResponder];
    }
    if([txtCounty isFirstResponder]){
        [txtCity setText:[dataCitys objectAtIndex:row]];
        [txtCounty resignFirstResponder];
    }
}

- (void)pickerDidPressCancel{
    if([txtProvince isFirstResponder]){
        [txtProvince resignFirstResponder];
    }
    if([txtCity isFirstResponder]){
        [txtCity resignFirstResponder];
    }
    if([txtCounty isFirstResponder]){
        [txtCounty resignFirstResponder];
    }
}

@end