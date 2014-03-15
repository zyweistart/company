//
//  STTaskManagerConsumptionViewController.m
//  ElectricianRun
//
//  Created by Start on 2/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskManagerConsumptionViewController.h"
#import "DatePickerView.h"
#import "STScanningViewController.h"
#define REQUESTCODERSUBMIT 50000
#define REQUESTCODEBUILDDATA 374283
#define RESPONSECODESCANADD 2473821

@interface STTaskManagerConsumptionViewController () <DatePickerViewDelegate,ScanningDelegate>

@end

@implementation STTaskManagerConsumptionViewController {
    NSDictionary *_data;
    NSString *_taskId;
    NSString *_gnid;
    NSInteger _type;
    NSString *scanncode;
    UIScrollView *scroll;
    NSArray *dataItemArray;
    DatePickerView *datePicker;
}

- (id)initWithData:(NSDictionary *)data taskId:(NSString *)taskId gnid:(NSString *)g type:(NSInteger)t {
    self=[super init];
    if(self){
        [self.view setBackgroundColor:[UIColor whiteColor]];
        _data=data;
        _taskId=taskId;
        _gnid=g;
        _type=t;
        
        NSString *name=[_data objectForKey:@"NAME"];
        self.title=name;
        
        if([name rangeOfString:@"受总" options:NSCaseInsensitiveSearch].length>0){
            self.navigationItem.rightBarButtonItems=[[NSArray alloc]initWithObjects:
                                                     [[UIBarButtonItem alloc]
                                                      initWithTitle:@"提交"
                                                      style:UIBarButtonItemStyleBordered
                                                      target:self
                                                      action:@selector(submit:)],
                                                     [[UIBarButtonItem alloc]
                                                      initWithTitle:@"扫描"
                                                      style:UIBarButtonItemStyleBordered
                                                      target:self
                                                      action:@selector(scann:)], nil];
        }else{
            self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                                    initWithTitle:@"提交"
                                                    style:UIBarButtonItemStyleBordered
                                                    target:self
                                                    action:@selector(submit:)];
        }
        
        if([name rangeOfString:@"时间" options:NSCaseInsensitiveSearch].length>0){
            datePicker = [[DatePickerView alloc] init];
            [datePicker setDelegate:self];
        }
        scanncode=@"";
        self.automaticallyAdjustsScrollViewInsets=NO;
    }
    return self;
}

- (void)scann:(id)sender
{
    STScanningViewController *scanningViewController=[[STScanningViewController alloc]init];
    [scanningViewController setDelegate:self];
    [scanningViewController setResponseCode:RESPONSECODESCANADD];
    [self presentViewController:scanningViewController animated:YES completion:nil];
}

- (void)submit:(id)sender
{
    NSMutableString *keys=[[NSMutableString alloc]init];
    NSMutableString *subs=[[NSMutableString alloc]init];
    NSMutableString *values=[[NSMutableString alloc]init];
    NSArray *subViews=[scroll subviews];
    for(UIView *view in subViews){
        if([view isKindOfClass:[UISwitch class]]||[view isKindOfClass:[UITextField class]]){
            NSDictionary *d=[dataItemArray objectAtIndex:view.tag];
            NSString *CODE_ID=[d objectForKey:@"CODE_ID"];
            [keys appendString:[Common NSNullConvertEmptyString:[d objectForKey:@"RESULT_ID"]]];
            [subs appendString:[Common NSNullConvertEmptyString:[d objectForKey:@"MODEL_SUB_ID"]]];
            if([@"32" isEqualToString:CODE_ID]){
                UITextField *txt1=(UITextField*)view;
                NSString *v=[txt1 text];
                if([@"" isEqualToString:v]){
                    [Common alert:[NSString stringWithFormat:@"%@不能为空",[d objectForKey:@"SCOUTCHECK_CONTENT"]]];
                    return;
                }
                [values appendString:v];
            }else{
                UISwitch *sw=(UISwitch*)view;
                if(sw.on){
                    if([@"18" isEqualToString:CODE_ID]){
                        [values appendString:@"34"];
                    }else{
                        [values appendString:@"56"];
                    }
                }else{
                    if([@"18" isEqualToString:CODE_ID]){
                        [values appendString:@"35"];
                    }else{
                        [values appendString:@"57"];
                    }
                }
            }
            [keys appendString:@";"];
            [subs appendString:@";"];
            [values appendString:@";"];
        }
    }
    NSString *ks=keys;
    NSString *ss=subs;
    NSString *vs=values;
    if([keys length]>0&&[ss length]>0&&[values length]>0){
        ks=[keys substringToIndex:[keys length]-1];
        ss=[subs substringToIndex:[subs length]-1];
        vs=[values substringToIndex:[values length]-1];
    }

    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:@"RW13" forKey:@"GNID"];
    [p setObject:_taskId forKey:@"QTTASK"];
    [p setObject:ss forKey:@"QTKEY"];
    [p setObject:ks forKey:@"QTKEY2"];
    [p setObject:vs forKey:@"QTVAL"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:REQUESTCODERSUBMIT];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URLAppMonitoringAlarm params:p];
}

- (void)reloadTableViewDataSource
{
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:_gnid forKey:@"GNID"];
    [p setObject:_taskId forKey:@"QTTASK"];
    [p setObject:[_data objectForKey:@"EQUIPMENT_ID"] forKey:@"QTKEY"];
    [p setObject:scanncode forKey:@"QTKEY1"];
    scanncode=@"";
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:REQUESTCODEBUILDDATA];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest start:URLAppMonitoringAlarm params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    NSDictionary *json=[response resultJSON];
    if(json!=nil) {
        NSDictionary *pageinfo=[json objectForKey:@"Rows"];
        if(REQUESTCODEBUILDDATA==repCode){
            int result=[[pageinfo objectForKey:@"result"] intValue];
            if(result>0){
                dataItemArray=[json objectForKey:@"table1"];
                [self buildUI:dataItemArray];
            } else {
                [Common alert:[pageinfo objectForKey:@"remark"]];
            }
        }else if(REQUESTCODERSUBMIT==repCode){
            [Common alert:[pageinfo objectForKey:@"remark"]];
        }
    }
}

- (void)buildUI:(NSArray *)array
{
    scroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 70, 320, 400)];
    [scroll setBackgroundColor:[UIColor whiteColor]];
    scroll.contentSize = CGSizeMake(320,[array count]*60);
    [scroll setScrollEnabled:YES];
    [self.view addSubview:scroll];
    
    for(int i=0;i<[array count];i++){
        NSDictionary *d=[array objectAtIndex:i];
        int height=(i+1)*5+i*30;
        UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(5, height, 150, 30)];
        [lbl1 setFont:[UIFont systemFontOfSize:10]];
        [lbl1 setTextColor:[UIColor blackColor]];
        [lbl1 setTextAlignment:NSTextAlignmentRight];
        [lbl1 setText:[d objectForKey:@"SCOUTCHECK_CONTENT"]];
        [scroll addSubview:lbl1];
        
        NSString *CODE_ID=[d objectForKey:@"CODE_ID"];
        
        NSString *SCOUTCHECK=[Common NSNullConvertEmptyString:[d objectForKey:@"SCOUTCHECK"]];
        
        if([@"32" isEqualToString:CODE_ID]){
            UITextField *txt1=[[UITextField alloc]initWithFrame:CGRectMake(160, height, 130, 30)];
            [txt1 setFont:[UIFont systemFontOfSize: 10.0]];
            [txt1 setClearButtonMode:UITextFieldViewModeWhileEditing];
            [txt1 setBorderStyle:UITextBorderStyleRoundedRect];
            [txt1 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
            [txt1 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
            [txt1 setText:SCOUTCHECK];
            if(datePicker){
                [txt1 setInputView:datePicker];
            }
            txt1.tag=i;
            [scroll addSubview:txt1];
        }else{
            UISwitch *sw=[[UISwitch alloc]initWithFrame:CGRectMake(170, height, 80, 30)];
            sw.tag=i;
            [scroll addSubview:sw];
            lbl1=[[UILabel alloc]initWithFrame:CGRectMake(255, height, 50, 30)];
            [lbl1 setFont:[UIFont systemFontOfSize:10]];
            [lbl1 setTextColor:[UIColor blackColor]];
            [lbl1 setTextAlignment:NSTextAlignmentLeft];
            if([@"18" isEqualToString:CODE_ID]){
                [lbl1 setText:@"是"];
            }else{
                [lbl1 setText:@"正常"];
            }
            [scroll addSubview:lbl1];
            if([@"" isEqualToString:SCOUTCHECK]){
                [sw setOn:YES];
            }else{
                if([@"18" isEqualToString:CODE_ID]){
                    if([@"34" isEqualToString:SCOUTCHECK]){
                        [sw setOn:YES];
                    }else{
                        [sw setOn:NO];
                    }
                }else{
                    if([@"56" isEqualToString:SCOUTCHECK]){
                        [sw setOn:YES];
                    }else{
                        [sw setOn:NO];
                    }
                }
            }
        }
    }
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    for(UIView *view in [scroll subviews]){
        if([view isKindOfClass:[UITextField class]]){
            UITextField *txt=(UITextField*)view;
            if([txt isFirstResponder]){
                NSString *d=[txt text];
                if(![@"" isEqualToString:d]){
                    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
                    NSDate *date = [dateFormatter dateFromString:d];
                    [[datePicker datePicker]setDate:date];
                }
                break;
            }
        }
    }
}

- (void)success:(NSString*)value responseCode:(NSInteger)responseCode{
    if(responseCode==RESPONSECODESCANADD){
        scanncode=value;
        [self reloadTableViewDataSource];
    }
}

- (void)pickerDidPressDoneWithDate:(NSDate*)date {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd hh:mm:ss"];
    NSString *currentDateStr = [dateFormatter stringFromDate:date];
    for(UIView *view in [scroll subviews]){
        if([view isKindOfClass:[UITextField class]]){
            UITextField *txt=(UITextField*)view;
            if([txt isFirstResponder]){
                [txt setText:currentDateStr];
                [txt resignFirstResponder];
                break;
            }
        }
    }
}

- (void)pickerDidPressCancel {
    [self backgroundDoneEditing:nil];
}

- (void)backgroundDoneEditing:(id)sender {
    for(UIView *view in [scroll subviews]){
        if([view isKindOfClass:[UITextField class]]){
            UITextField *txt=(UITextField*)view;
            if([txt isFirstResponder]){
                [txt resignFirstResponder];
                break;
            }
        }
    }
}

@end