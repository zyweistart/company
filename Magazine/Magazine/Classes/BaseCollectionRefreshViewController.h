#import <UIKit/UIKit.h>
#import "BaseCollectionViewController.h"
#import "EGORefreshTableHeaderView.h"

@interface BaseCollectionRefreshViewController : BaseCollectionViewController
{
    BOOL _loading;
}
//总页面数
@property (assign, nonatomic) int pageCount;
//是否正在加载中
@property (nonatomic, assign) BOOL loading;
//是否已经加载完毕
@property (nonatomic, assign) BOOL endReached;
//下拉刷新
- (void)doRefresh;
//加载更多
- (void)loadMore;

@end