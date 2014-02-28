//
//  STDataMonitoringLineSearchViewController.m
//  ElectricianRun
//
//  Created by Start on 2/21/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STDataMonitoringLineSearchViewController.h"
#import "DataPickerView.h"
#import "NSString+Utils.h"

@interface STDataMonitoringLineSearchViewController ()  <DataPickerViewDelegate>

@end

@implementation STDataMonitoringLineSearchViewController{
    UIPickerView *pvSelectType;
    UITextField *txtValueType;
    UITextField *txtValueName;
    DataPickerView *transformerdpv;
    NSMutableArray *dataItemArray;
    NSDictionary *tmpDic;
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
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"变压器"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueType=[[UITextField alloc]initWithFrame:CGRectMake(80, 10, 150, 30)];
    [txtValueType setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueType setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueType setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueType setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueType setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueType setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValueType];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"线路名称"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueName=[[UITextField alloc]initWithFrame:CGRectMake(80, 50, 150, 30)];
    [txtValueName setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueName setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueName setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueName setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueName setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [control addSubview:txtValueName];
    
    UIButton *btnSearch=[[UIButton alloc]initWithFrame:CGRectMake(110, 90, 100, 30)];
    [btnSearch setTitle:@"查询" forState:UIControlStateNormal];
    [btnSearch setBackgroundColor:[UIColor blueColor]];
    [btnSearch addTarget:self action:@selector(search:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnSearch];
    
}

- (void)search:(id)sender {
    
    NSMutableDictionary *data=[[NSMutableDictionary alloc]init];
    
    [data setObject:[txtValueName text] forKey:@"QTKEY"];
    if([@"" isEqualToString:[txtValueType text]]){
        [data setObject:@"" forKey:@"QTKEY1"];
    }else{
        [data setObject:[tmpDic objectForKey:@"TRANS_NO"] forKey:@"QTKEY1"];
    }
    
    [self.delegate startSearch:data];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)reload{
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"ZY21" forKey:@"GNID"];
    [p setObject:self.cpId forKey:@"QTCP"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URL params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode {
    
    NSArray *tmpData=[[response resultJSON] objectForKey:@"table1"];
    dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
    
    NSMutableArray *d=[[NSMutableArray alloc]init];
    for(NSDictionary *dic in dataItemArray) {
        [d addObject:[dic objectForKey:@"TRANS_NAME"]];
    }
    
    transformerdpv=[[DataPickerView alloc]initWithData:d];
    [transformerdpv setDelegate:self];
    
    [txtValueType setInputView:transformerdpv];
    
}

- (void)pickerDidPressDoneWithRow:(NSInteger)row {
    if([txtValueType isFirstResponder]){
        tmpDic= [dataItemArray objectAtIndex:row];
        [txtValueType setText:[tmpDic objectForKey:@"TRANS_NAME"]];
        [txtValueType resignFirstResponder];
    }
}

- (void)pickerDidPressCancel{
    if([txtValueType isFirstResponder]){
        [txtValueType resignFirstResponder];
    }
}

@end