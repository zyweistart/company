//
//  BaseMJRefreshViewController.h
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseTableViewController.h"
#import "MJRefresh.h"

@interface BaseMJRefreshViewController : BaseTableViewController<HttpRequestDelegate> {
    //当前页
    int _currentPage;
    //总页数
    int _pageCount;
}

@property MJRefreshHeaderView *header;
@property MJRefreshFooterView *footer;

@property BOOL isLoadCache;
@property (strong,nonatomic) HttpRequest *hRequest;

- (void)reloadTableViewDataSource;

- (void)doneLoadingTableViewData;

- (void)autoRefresh;

@end