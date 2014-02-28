//
//  STDataMonitoringViewController.m
//  ElectricianRun
//  数据监测
//  Created by Start on 1/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STDataMonitoringViewController.h"
#import "STDataMonitoringLineViewController.h"
#import "STDataMonitoringSearchViewController.h"
#import "STDataMonitoringCell.h"
#import "NSString+Utils.h"

@interface STDataMonitoringViewController ()

@end

@implementation STDataMonitoringViewController {
    NSMutableDictionary *searchData;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    [self setIsLoadCache:YES];
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"数据监测";
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"查询"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(search:)];
        
        
        searchData=[[NSMutableDictionary alloc]init];
        [searchData setObject:@"" forKey:@"name"];
    }
    return self;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

//查询
- (void)search:(id)sender{
//    [self.header beginRefreshing];
    STDataMonitoringSearchViewController *dataMonitoringSearchViewController=[[STDataMonitoringSearchViewController alloc]init];
    [dataMonitoringSearchViewController setDelegate:self];
    [self.navigationController pushViewController:dataMonitoringSearchViewController animated:YES];
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
    
    [cell.lbl1 setText:[Common NSNullConvertEmptyString:[dictionary objectForKey:@"CP_NAME"]]];
    [cell.lbl2 setText:[Common NSNullConvertEmptyString:[dictionary objectForKey:@"TRANS_COUNT"]]];
    [cell.lbl3 setText:[Common NSNullConvertEmptyString:[dictionary objectForKey:@"MAX_DATE"]]];
    [cell.lbl4 setText:[Common NSNullConvertEmptyString:[dictionary objectForKey:@"MAX_LOAD"]]];
    return cell;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
    STDataMonitoringLineViewController *dataMonitoringLineViewController=[[STDataMonitoringLineViewController alloc]initWithData:dictionary];
    [self.navigationController pushViewController:dataMonitoringLineViewController animated:YES];
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";

    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"SJ10" forKey:@"GNID"];
    [p setObject:[searchData objectForKey:@"name"] forKey:@"QTKEY"];
    [p setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"QTPINDEX"];
    [p setObject:[NSString stringWithFormat: @"%d",PAGESIZE] forKey:@"QTPSIZE"];

    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URL params:p];
    
}

- (void)startSearch:(NSMutableDictionary *)data {
    
    searchData=data;
    [self autoRefresh];
}

@end
