//
//  ACBaseRefreshTableViewController.m
//  ACyulu
//
//  Created by Start on 12-12-8.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "BaseRefreshTableViewController.h"

@interface BaseRefreshTableViewController ()

@end

@implementation BaseRefreshTableViewController

- (id) initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        //每页大小
        _pageSize=8;
        //初始化页为第一页
        _currentPage=1;
        [super buildTableView];
        if(_refreshHeaderView==nil){
            EGORefreshTableHeaderView *view=[[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.bounds.size.height, self.view.frame.size.width, self.tableView.bounds.size.height)];
            view.delegate = self;
            [self.tableView addSubview:view];
            _refreshHeaderView = view;
        }
        [_refreshHeaderView refreshLastUpdatedDate];
    }
    return self;
}

#pragma mark -
#pragma mark DelegateMethod

- (void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSMutableArray *)result{
    [self.tableView setContentOffset:CGPointMake(0, -75) animated:YES];
    _currentPage=1;
    [self doneManualRefresh];
}

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if([response successFlag]){
        //判断是否已经全部加载完毕
        NSInteger totalpage=[[[response pageInfo]objectForKey:@"totalpage"]intValue];
        if(totalpage>=_currentPage){
            if(totalpage==_currentPage){
                _loadOver=YES;
            }else{
                _loadOver=NO;
            }
            if(_currentPage==1){
                //如果当前为第一页则全部加入
                if(self.dataItemArray){
                    [self.dataItemArray removeAllObjects];
                    [self.dataItemArray addObjectsFromArray:[[NSMutableArray alloc]initWithArray:[response dataItemArray]]];
                   
                }else{
                   self.dataItemArray=[[NSMutableArray alloc]initWithArray:[response dataItemArray]];
                }
            }else{
                //追加到最后面
                [self.dataItemArray addObjectsFromArray:[[NSMutableArray alloc]initWithArray:[response dataItemArray]]];
            }
        }else{
            _currentPage=totalpage;
            _loadOver=YES;
        }
        if(!self.dataItemArray){
            self.dataItemArray=[[NSMutableArray alloc]init];
        }
        [self.tableView reloadData];
    }
    [self doneLoadingTableViewData];
}

- (void)requestFailed:(ASIHTTPRequest *)request requestCode:(int)reqCode{
    [self doneLoadingTableViewData];
}

#pragma mark TableView

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if([self.dataItemArray count]>0){
        if(_pageSize>[self.dataItemArray count]){
            return [self.dataItemArray count];
        }else{
            return [self.dataItemArray count]+1;
        }
    }else{
        return 1;
    }
}

#pragma mark UIScrollViewDelegate Methods

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
	[_refreshHeaderView egoRefreshScrollViewDidScroll:scrollView];
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
	[_refreshHeaderView egoRefreshScrollViewDidEndDragging:scrollView];
}

#pragma mark EGORefreshTableHeaderDelegate Methods

- (void)egoRefreshTableHeaderDidTriggerRefresh:(EGORefreshTableHeaderView *)view{
    _currentPage=1;
	[self reloadTableViewDataSource];
}

- (BOOL)egoRefreshTableHeaderDataSourceIsLoading:(EGORefreshTableHeaderView *)view{
	return _reloading; // should return if data source model is reloading
}

- (NSDate *)egoRefreshTableHeaderDataSourceLastUpdated:(EGORefreshTableHeaderView *)view{
	return [NSDate date]; // should return date data source was last changed
}

#pragma mark -
#pragma mark CustomMethod

- (void)autoRefresh{
    [self.tableView setContentOffset:CGPointMake(0, -75) animated:YES];
    [self performSelector:@selector(doneManualRefresh) withObject:nil afterDelay:0.4];
}

- (void)doneManualRefresh{
    [_refreshHeaderView egoRefreshScrollViewDidScroll:self.tableView];
    [_refreshHeaderView egoRefreshScrollViewDidEndDragging:self.tableView];
}

- (void)reloadTableViewDataSource{
	_reloading = YES;
    [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:3.0];
}

- (void)doneLoadingTableViewData{
	_reloading = NO;
    [_refreshHeaderView egoRefreshScrollViewDataSourceDidFinishedLoading:self.tableView];
}

@end
