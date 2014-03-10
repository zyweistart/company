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
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"用户列表";
    }
    return self;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 45;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellReuseIdentifier=@"Cell";
    UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
    if(!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellReuseIdentifier];
    }
    NSUInteger row=[indexPath row];
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
    [cell.textLabel setText:[dictionary objectForKey:@"userName"]];
    [cell.detailTextLabel setText:[dictionary objectForKey:@"userName"]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
    [_delegate startSearch:dictionary];
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:@"3" forKey:@"rtype"];
    [p setObject:@"2012-03-02 00:00" forKey:@"startDate"];
    [p setObject:@"2014-03-02 23:59" forKey:@"endDate"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URLgetLocationInfo params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    NSArray *tmpData=[[response resultJSON] objectForKey:@"gpsUserList"];
    
    self.dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
    
    // 刷新表格
    [self.tableView reloadData];
    
    [self doneLoadingTableViewData];
    
}

@end
