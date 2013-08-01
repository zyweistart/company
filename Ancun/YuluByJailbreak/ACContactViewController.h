//
//  ACContactViewController.h
//  ACyulu
//
//  Created by Start on 12-12-6.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "BaseTableViewController.h"

@interface ACContactViewController : BaseTableViewController<HttpViewDelegate,UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate>

@property (retain, nonatomic) IBOutlet UIView *viewinfo;
@property (retain, nonatomic) IBOutlet UITableView *table;
@property (retain, nonatomic) IBOutlet UISearchBar *search;

@end
