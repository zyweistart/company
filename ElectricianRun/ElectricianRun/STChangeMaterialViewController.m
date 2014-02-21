//
//  STChangeMaterialViewController.m
//  ElectricianRun
//  更换采集器
//  Created by Start on 2/21/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STChangeMaterialViewController.h"

#import "STScanningViewController.h"
#import "NSString+Utils.h"

@interface STChangeMaterialViewController ()<UIPickerViewDelegate,UIPickerViewDataSource,ScanningDelegate>

@end

@implementation STChangeMaterialViewController{
    UITextField *txtValue2;
    UIPickerView* pickerView;
    NSString *twoDimensionalCode;
    NSArray *selectData;
}

- (id)initWithSerialNo:(NSString*)no {
    self = [super init];
    if (self) {
        self.title=@"变更采集器";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.serialNo=no;
        
        selectData=[[NSArray alloc]initWithObjects:@"--选择--",@"监测数据异常",@"通讯异常",@"采集器损坏",@"采集器不配", nil];
        
        UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 100, 320, 160)];
        [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
        [self.view addSubview:control];
        
        UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 10, 60, 30)];
        lbl.font=[UIFont systemFontOfSize:12.0];
        [lbl setText:@"原序列号"];
        [lbl setTextColor:[UIColor blackColor]];
        [lbl setBackgroundColor:[UIColor clearColor]];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [control addSubview:lbl];
        
        UITextField *txtValue1=[[UITextField alloc]initWithFrame:CGRectMake(80, 10, 150, 30)];
        [txtValue1 setFont:[UIFont systemFontOfSize: 12.0]];
        [txtValue1 setClearButtonMode:UITextFieldViewModeWhileEditing];
        [txtValue1 setBorderStyle:UITextBorderStyleRoundedRect];
        [txtValue1 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
        [txtValue1 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
        [txtValue1 setKeyboardType:UIKeyboardTypePhonePad];
        [txtValue1 setEnabled:YES];
        [txtValue1 setText:self.serialNo];
        [control addSubview:txtValue1];
        
        lbl=[[UILabel alloc]initWithFrame:CGRectMake(10, 50, 60, 30)];
        lbl.font=[UIFont systemFontOfSize:12.0];
        [lbl setText:@"新序列号"];
        [lbl setTextColor:[UIColor blackColor]];
        [lbl setBackgroundColor:[UIColor clearColor]];
        [lbl setTextAlignment:NSTextAlignmentRight];
        [control addSubview:lbl];
        
        txtValue2=[[UITextField alloc]initWithFrame:CGRectMake(80, 50, 150, 30)];
        [txtValue2 setFont:[UIFont systemFontOfSize: 12.0]];
        [txtValue2 setClearButtonMode:UITextFieldViewModeWhileEditing];
        [txtValue2 setBorderStyle:UITextBorderStyleRoundedRect];
        [txtValue2 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
        [txtValue2 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
        [txtValue2 setKeyboardType:UIKeyboardTypePhonePad];
        [control addSubview:txtValue2];
        
        UIButton *btnScan=[[UIButton alloc]initWithFrame:CGRectMake(235, 50, 30, 30)];
        [btnScan setTitle:@"..." forState:UIControlStateNormal];
        [btnScan setBackgroundColor:[UIColor blueColor]];
        [btnScan addTarget:self action:@selector(scanning:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnScan];
        
        UIButton *btnSubmit=[[UIButton alloc]initWithFrame:CGRectMake(110, 120, 100, 30)];
        btnSubmit.titleLabel.font=[UIFont systemFontOfSize:12];
        [btnSubmit setTitle:@"开始变更" forState:UIControlStateNormal];
        [btnSubmit setBackgroundColor:[UIColor blueColor]];
        [btnSubmit addTarget:self action:@selector(submit:) forControlEvents:UIControlEventTouchUpInside];
        [control addSubview:btnSubmit];
        
        pickerView = [ [ UIPickerView alloc] initWithFrame:CGRectMake(0.0,352.0,320.0,216.0)];
        pickerView.delegate = self;
        pickerView.dataSource =  self;
        [self.view addSubview:pickerView];
        
    }
    return self;
}


- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode {
//    NSMutableArray *dataArray=[[NSMutableArray alloc]initWithArray:[[response resultJSON] objectForKey:@"Rows"]];
//    for(NSDictionary *dic in dataArray) {
//        
//        break;
//    }
    
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)submit:(id)sender {
    
    NSString *oldSerialNo=self.serialNo;
    NSString *newSerialNo=[txtValue2 text];
    NSInteger row=[pickerView selectedRowInComponent:0];
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppScanNumber.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"1" forKey:@"OpWap"];
    [p setObject:@"2" forKey:@"OpType"];
    [p setObject:oldSerialNo forKey:@"SerialNo"];
    [p setObject:newSerialNo forKey:@"NewserialNo"];
    [p setObject:[NSString stringWithFormat:@"%ld",row] forKey:@"ChangeReason"];
    
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URL params:p];
}

- (void)scanning:(id)sender {
    STScanningViewController *scanningViewController=[[STScanningViewController alloc]init];
    [scanningViewController setDelegate:self];
    [scanningViewController setResponseCode:500];
    [self presentViewController:scanningViewController animated:YES completion:nil];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    
}

-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
{
    if (!view) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 100, 30)];
        label.text = [selectData objectAtIndex:row];
        label.textColor = [UIColor blueColor];
        label.font=[UIFont systemFontOfSize:14];
        view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 100, 30)];
        [view addSubview:label];
    }
    return view ;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [selectData count];
}

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

-(CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
    return 30.0f;
}

- (void)backgroundDoneEditing:(id)sender {
    [txtValue2 resignFirstResponder];
}

- (void)success:(NSString*)value responseCode:(NSInteger)responseCode{
    [txtValue2 setText:value];
    twoDimensionalCode=[value stringByReplacingOccurrencesOfString:@" " withString:@""];
    [self backgroundDoneEditing:nil];
}

@end
