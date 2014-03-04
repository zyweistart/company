//
//  STViewUserListViewController.m
//  ElectricianRun
//
//  Created by Start on 3/4/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STViewUserListViewController.h"
#import "STDataMonitoringCell.h"
#import "NSString+Utils.h"

@interface STViewUserListViewController ()

@end

@implementation STViewUserListViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    [self setIsLoadCache:YES];
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"数据监测";
    }
    return self;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 75;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellReuseIdentifier=@"Cell";
    
    STDataMonitoringCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
    if(!cell) {
        cell = [[STDataMonitoringCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellReuseIdentifier];
    }
    
    NSUInteger row=[indexPath row];
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
    NSLog(@"%@",dictionary);
    return cell;
    
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/getLocationInfo.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"3" forKey:@"rtype"];
    [p setObject:@"2012-03-02 00:00" forKey:@"startDate"];
    [p setObject:@"2014-03-02 23:59" forKey:@"endDate"];
//    [p setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"QTPINDEX"];
//    [p setObject:[NSString stringWithFormat: @"%d",PAGESIZE] forKey:@"QTPSIZE"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URL params:p];
    
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    
    NSArray *tmpData=[[response resultJSON] objectForKey:@"gpsUserList"];
    
    self.dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
    
    // 刷新表格
    [self.tableView reloadData];
    
    [self doneLoadingTableViewData];
    
}

@end
