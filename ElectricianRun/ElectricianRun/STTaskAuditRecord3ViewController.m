//
//  STTaskAuditRecord3ViewController.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditRecord3ViewController.h"
#import "NSString+Utils.h"

@interface STTaskAuditRecord3ViewController ()

@end

@implementation STTaskAuditRecord3ViewController

- (id)initWithData:(NSDictionary *)data dic:(NSDictionary *)dic;
{
    self = [super init];
    if (self) {
        
        self.data=data;
        self.dic=dic;
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
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

- (void)reloadTableViewDataSource{
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"RW17" forKey:@"GNID"];
    [p setObject:[_data objectForKey:@"TASK_ID"] forKey:@"QTTASK"];
    [p setObject:[_dic objectForKey:@"EQUIPMENT_ID"] forKey:@"QTKEY"];
    [p setObject:@"" forKey:@"QTKEY1"];
    
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
