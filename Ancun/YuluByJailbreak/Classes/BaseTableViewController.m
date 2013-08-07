//
//  ACBaseTableViewController.m
//  ACyulu
//
//  Created by Start on 12-12-8.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "BaseTableViewController.h"

@interface BaseTableViewController ()

@end

@implementation BaseTableViewController

#pragma mark -
#pragma mark DelegateMethod

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
     return [self.dataItemArray count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 62;
}

#pragma mark -
#pragma mark CustomMethod

- (UITableView *)buildTableView{
    if(self.tableView==nil){
        self.tableView=[[UITableView alloc]initWithFrame:
                    CGRectMake(0, 0,
                               self.view.frame.size.width,
                               self.view.frame.size.height)];
        [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
        [self.view addSubview:self.tableView];
    }
    return self.tableView;
}

@end