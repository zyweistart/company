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
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                                initWithTitle:@"查询"
                                                style:UIBarButtonItemStyleBordered
                                                target:self
                                                action:@selector(search:)];
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 100, 320, 300)];
//    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(30, 10, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"变压器"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueType=[[UITextField alloc]initWithFrame:CGRectMake(100, 10, 150, 30)];
    [txtValueType setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueType setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueType setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueType setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueType setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValueType setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValueType];
    
    lbl=[[UILabel alloc]initWithFrame:CGRectMake(30, 50, 60, 30)];
    lbl.font=[UIFont systemFontOfSize:12.0];
    [lbl setText:@"线路名称"];
    [lbl setTextColor:[UIColor blackColor]];
    [lbl setBackgroundColor:[UIColor clearColor]];
    [lbl setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lbl];
    
    txtValueName=[[UITextField alloc]initWithFrame:CGRectMake(100, 50, 150, 30)];
    [txtValueName setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValueName setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValueName setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValueName setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValueName setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [control addSubview:txtValueName];
    
}

- (void)search:(id)sender {
    
    NSMutableDictionary *data=[[NSMutableDictionary alloc]init];
    
    [data setObject:[txtValueName text] forKey:@"QTKEY1"];
    if([@"" isEqualToString:[txtValueType text]]){
        [data setObject:@"" forKey:@"QTKEY"];
    }else{
        [data setObject:[tmpDic objectForKey:@"TRANS_NO"] forKey:@"QTKEY"];
    }
    
    [self.delegate startSearch:data];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)reload{
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:@"ZY21" forKey:@"GNID"];
    [p setObject:self.cpId forKey:@"QTCP"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URLAppMonitoringAlarm params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode {
    
    NSArray *tmpData=[[response resultJSON] objectForKey:@"table1"];
    dataItemArray=[[NSMutableArray alloc]init];
    NSMutableArray *names=[[NSMutableArray alloc]init];
    for(NSDictionary *dic in tmpData) {
        NSString *TRANS_NAME=[Common NSNullConvertEmptyString:[dic objectForKey:@"TRANS_NAME"]];
        if(![@"" isEqualToString:TRANS_NAME]){
            [dataItemArray addObject:dic];
            [names addObject:TRANS_NAME];
        }
    }
    
    transformerdpv=[[DataPickerView alloc]initWithData:names];
    [transformerdpv setDelegate:self];
    
    [txtValueType setInputView:transformerdpv];
    
}

- (void)pickerDidPressDoneWithRow:(NSInteger)row {
    if([txtValueType isFirstResponder]){
        if([dataItemArray count]>0){
            tmpDic= [dataItemArray objectAtIndex:row];
            [txtValueType setText:[tmpDic objectForKey:@"TRANS_NAME"]];
        }
        [txtValueType resignFirstResponder];
    }
}

- (void)pickerDidPressCancel{
    if([txtValueType isFirstResponder]){
        [txtValueType resignFirstResponder];
    }
}

@end
