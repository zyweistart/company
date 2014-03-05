//
//  STTaskManagerConsumptionViewController.m
//  ElectricianRun
//
//  Created by Start on 2/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskManagerConsumptionViewController.h"

@interface STTaskManagerConsumptionViewController ()

@end

@implementation STTaskManagerConsumptionViewController {
    NSDictionary *_data;
    NSString *_taskId;
    NSString *_gnid;
    NSInteger _type;
}

- (id)initWithData:(NSDictionary *)data taskId:(NSString *)taskId gnid:(NSString *)g type:(NSInteger)t {
    self=[super init];
    if(self){
        [self.view setBackgroundColor:[UIColor whiteColor]];
        _data=data;
        _taskId=taskId;
        _gnid=g;
        _type=t;
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
    
    cell.textLabel.text=[Common NSNullConvertEmptyString:[dictionary objectForKey:@"SCOUTCHECK_CONTENT"]];
    
    return cell;
}

- (void)reloadTableViewDataSource
{
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:_gnid forKey:@"GNID"];
    [p setObject:_taskId forKey:@"QTTASK"];
    [p setObject:[_data objectForKey:@"EQUIPMENT_ID"] forKey:@"QTKEY"];
    [p setObject:@"" forKey:@"QTKEY1"];

    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URLAppMonitoringAlarm params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    NSLog(@"%@",[response responseString]);
    NSDictionary *json=[response resultJSON];
    if(json!=nil) {
        NSDictionary *pageinfo=[json objectForKey:@"Rows"];
        
        int result=[[pageinfo objectForKey:@"result"] intValue];
        if(result>0){
            NSArray *tmpData=[json objectForKey:@"table1"];
            
            self.dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
            
            // 刷新表格
            [self.tableView reloadData];
        } else {
            [Common alert:[pageinfo objectForKey:@"remark"]];
        }
    }
}

@end
