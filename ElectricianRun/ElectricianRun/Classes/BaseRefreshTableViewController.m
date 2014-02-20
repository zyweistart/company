#import "BaseRefreshTableViewController.h"

@interface BaseRefreshTableViewController ()

@end

@implementation BaseRefreshTableViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        self.edgesForExtendedLayout = UIRectEdgeNone;
        
        if (_refreshHeaderView == nil) {
            
            EGORefreshTableHeaderView *view = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.bounds.size.height, self.view.frame.size.width, self.tableView.bounds.size.height)];
            view.delegate = self;
            [self.tableView addSubview:view];
            _refreshHeaderView = view;
        }
        
        [_refreshHeaderView refreshLastUpdatedDate];
        
        _currentPage=1;
        _pageCount=_currentPage;
        
    }
    return self;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSUInteger count=[_dataItemArray count];
    if(_pageCount==_currentPage){
        return count;
    } else {
        return count+1;
    }
}

#pragma mark -
#pragma mark UIScrollViewDelegate Methods

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
	
	[_refreshHeaderView egoRefreshScrollViewDidScroll:scrollView];
    
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
	
	[_refreshHeaderView egoRefreshScrollViewDidEndDragging:scrollView];
	
}


#pragma mark -
#pragma mark EGORefreshTableHeaderDelegate Methods

- (void)egoRefreshTableHeaderDidTriggerRefresh:(EGORefreshTableHeaderView*)view{
	
    _currentPage=1;
    
	[self reloadTableViewDataSource];
    
}

- (BOOL)egoRefreshTableHeaderDataSourceIsLoading:(EGORefreshTableHeaderView*)view{
	
	return _reloading; // should return if data source model is reloading
	
}

- (NSDate*)egoRefreshTableHeaderDataSourceLastUpdated:(EGORefreshTableHeaderView*)view{
	
	return [NSDate date]; // should return date data source was last changed
	
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
	
	//  should be calling your tableviews data source model to reload
	//  put here just for demo
	_reloading = YES;
}

- (void)doneLoadingTableViewData{
	
	//  model should call this when its done loading
	_reloading = NO;
	[_refreshHeaderView egoRefreshScrollViewDataSourceDidFinishedLoading:self.tableView];
	
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{

    NSMutableDictionary *pageinfo=[[response resultJSON] objectForKey:@"Rows"];
    
    _pageCount=[[pageinfo objectForKey:@"PageCount"] intValue];
    if(_pageCount<_currentPage) {
        _currentPage--;
    } else {
        
        NSArray *tmpData=[[response resultJSON] objectForKey:@"table1"];
        
        if(_currentPage==1){
            _dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
        } else {
            [_dataItemArray addObjectsFromArray:tmpData];
        }
        
    }
    [self doneLoadingTableViewData];
    //数据表重新加载
    [self.tableView reloadData];
    
}

- (void)requestFailed:(int)repCode didFailWithError:(NSError *)error{
    [self doneLoadingTableViewData];
}

@end
