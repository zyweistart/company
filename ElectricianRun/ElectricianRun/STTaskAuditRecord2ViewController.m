//
//  STTaskAuditRecord2ViewController.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditRecord2ViewController.h"
#import "STTaskAuditRecord3ViewController.h"
#import "NSString+Utils.h"

@interface STTaskAuditRecord2ViewController ()

@end

@implementation STTaskAuditRecord2ViewController {
    NSInteger _type;
    NSDictionary *_data;
}

- (id)initWithData:(NSDictionary *)data type:(NSInteger)t
{
    self = [super init];
    if (self) {
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        _type=t;
        _data=data;
        
        if(_type==1){
            self.title=@"站点电耗量信息";
        } else if(_type==3){
            self.title=@"运行设备温度、外观检查";
        } else if(_type==2){
            self.title=@"受总柜运行情况";
        } else if(_type==4){
            self.title=@"TRMS系统巡视检查";
        }
        
        [self reloadTableViewDataSource];
        
    }
    return self;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSUInteger row=[indexPath row];
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
    cell.textLabel.text=[NSString stringWithFormat:@"第:%ld,%@",row+1,[dictionary objectForKey:@"NAME"]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSUInteger row=[indexPath row];
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
    STTaskAuditRecord3ViewController *taskAuditRecord3ViewController=[[STTaskAuditRecord3ViewController alloc]initWithData:_data dic:dictionary type:_type];
    [self.navigationController pushViewController:taskAuditRecord3ViewController animated:YES];
}

- (void)reloadTableViewDataSource{
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"RW12" forKey:@"GNID"];
    [p setObject:[_data objectForKey:@"TASK_ID"] forKey:@"QTTASK"];
    [p setObject:[NSString stringWithFormat:@"%ld",_type] forKey:@"QTKEY"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URL params:p];
    
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    
    
    NSArray *tmpData=[[response resultJSON] objectForKey:@"table1"];
    
    self.dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
    
    // 刷新表格
    [self.tableView reloadData];
    
}

- (void)requestFailed:(int)repCode didFailWithError:(NSError *)error
{
    
}

@end

