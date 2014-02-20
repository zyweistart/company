#import "BaseTableViewController.h"


//NSString *const MJTableViewCellIdentifier = @"Cell";

@interface BaseTableViewController ()

@end

@implementation BaseTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
 
    _currentPage=0;
    
    // 集成刷新控件
    // 下拉刷新
    [self addHeader];
    // 上拉加载更多
    [self addFooter];
}

- (void)addHeader
{
    MJRefreshHeaderView *header = [MJRefreshHeaderView header];
    header.scrollView = self.tableView;
    header.beginRefreshingBlock = ^(MJRefreshBaseView *refreshView) {
        // 进入刷新状态就会回调这个Block
        _currentPage=1;
        
//        NSLog(@"%@----开始进入刷新状态", refreshView.class);
        
        [self reloadTableViewDataSource];
        
    };
    header.endStateChangeBlock = ^(MJRefreshBaseView *refreshView) {
        // 刷新完毕就会回调这个Block
//        NSLog(@"%@----刷新完毕", refreshView.class);
    };
    header.refreshStateChangeBlock = ^(MJRefreshBaseView *refreshView, MJRefreshState state) {
        // 控件的刷新状态切换了就会调用这个block
        switch (state) {
            case MJRefreshStateNormal:
//                NSLog(@"%@----切换到：普通状态", refreshView.class);
                break;
                
            case MJRefreshStatePulling:
//                NSLog(@"%@----切换到：松开即可刷新的状态", refreshView.class);
                break;
                
            case MJRefreshStateRefreshing:
//                NSLog(@"%@----切换到：正在刷新状态", refreshView.class);
                break;
            default:
                break;
        }
    };
//    [header beginRefreshing];
    _header = header;
}

- (void)addFooter
{
    MJRefreshFooterView *footer = [MJRefreshFooterView footer];
    footer.scrollView = self.tableView;
    footer.beginRefreshingBlock = ^(MJRefreshBaseView *refreshView) {
        
        long count=[self.dataItemArray count];
        _currentPage=count/PAGESIZE;
        if(count%PAGESIZE>=0){
            _currentPage++;
        }
        
//        NSLog(@"%@----开始进入刷新状态", refreshView.class);
        
        [self reloadTableViewDataSource];
        
    };
    _footer = footer;
}



/**
 为了保证内部不泄露，在dealloc中释放占用的内存
 */
- (void)dealloc
{
//    NSLog(@"MJTableViewController--dealloc---");
    [_header free];
    [_footer free];
}

#pragma mark - Table view data source
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.dataItemArray count];
}

- (void)reloadTableViewDataSource {
    
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    
    NSMutableDictionary *pageinfo=[[response resultJSON] objectForKey:@"Rows"];
    
    _pageCount=[[pageinfo objectForKey:@"PageCount"] intValue];
    if(_pageCount<_currentPage) {
        _currentPage=_pageCount;
    } else {
        
        NSArray *tmpData=[[response resultJSON] objectForKey:@"table1"];
        
        if(_currentPage==1){
            _dataItemArray=[[NSMutableArray alloc]initWithArray:tmpData];
        } else {
            [_dataItemArray addObjectsFromArray:tmpData];
        }
        
    }
    
    [self doneLoadingTableViewData];
    
}

- (void)doneLoadingTableViewData {
    // (最好在刷新表格后调用)调用endRefreshing可以结束刷新状态
    [_header endRefreshing];
    [_footer endRefreshing];
    // 刷新表格
    [self.tableView reloadData];
}

@end
