//
//  STDataMonitoringLineDetailListViewController.m
//  ElectricianRun
//  数据监测-线路-详细-耗量列表
//  Created by Start on 1/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STDataMonitoringLineDetailListViewController.h"
#import "NSString+Utils.h"

@interface STDataMonitoringLineDetailListViewController ()

@end

@implementation STDataMonitoringLineDetailListViewController

- (id)initWithData:(NSDictionary *) data
{
    self = [super init];
    if (self) {
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.data=data;
        
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
    if([self.dataItemArray count] > row){
        NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
        cell.textLabel.text=[NSString stringWithFormat:@"第:%ld,%@",row+1,[dictionary objectForKey:@"METER_NAME"]];
    }else{
        cell.textLabel.text=@"更多";
    }
    return cell;
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
	_reloading = YES;
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"SJ20" forKey:@"GNID"];
    [p setObject:[self.data objectForKey:@"CP_ID"] forKey:@"QTCP"];
    [p setObject:@"" forKey:@"QTKEY"];
    [p setObject:@"" forKey:@"QTKEY1"];
    [p setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"QTPINDEX"];
    [p setObject:[NSString stringWithFormat: @"%d",PAGESIZE] forKey:@"QTPSIZE"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URL params:p];
    
}

@end
