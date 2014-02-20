
#import "MJRefresh.h"


@interface BaseTableViewController : UITableViewController<HttpRequestDelegate> {
    //当前页
    long _currentPage;
    //总页数
    long _pageCount;
}

@property MJRefreshHeaderView *header;
@property MJRefreshFooterView *footer;

@property (strong,nonatomic) HttpRequest *hRequest;

@property (strong,nonatomic) NSMutableArray *dataItemArray;

- (void)reloadTableViewDataSource;

- (void)doneLoadingTableViewData;

@end
