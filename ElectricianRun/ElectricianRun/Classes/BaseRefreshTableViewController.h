#import <UIKit/UIKit.h>
#import "EGORefreshTableHeaderView.h"

@interface BaseRefreshTableViewController:UITableViewController <EGORefreshTableHeaderDelegate,HttpRequestDelegate>{
    //当前页
    int _currentPage;
    //总页数
    int _pageCount;
    //是否正在加载数据中
	BOOL _reloading;
    
	EGORefreshTableHeaderView *_refreshHeaderView;
    
}

- (void)reloadTableViewDataSource;
- (void)doneLoadingTableViewData;

@property (strong,nonatomic) HttpRequest *hRequest;

@property (strong,nonatomic) NSMutableArray *dataItemArray;

@end