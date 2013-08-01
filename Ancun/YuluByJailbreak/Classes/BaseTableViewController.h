//
//  ACBaseTableViewController.h
//  ACyulu
//
//  Created by Start on 12-12-8.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

@interface BaseTableViewController : UIViewController<UITableViewDelegate,UITableViewDataSource>

@property (retain,nonatomic) UITableView *tableView;
@property (retain,nonatomic) NSMutableArray *dataItemArray;

- (UITableView *)buildTableView;

@end
