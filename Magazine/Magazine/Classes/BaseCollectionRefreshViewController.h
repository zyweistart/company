#import <UIKit/UIKit.h>
#import "BaseCollectionViewController.h"
#import "EGORefreshTableHeaderView.h"
#import "HttpRequest.h"

@interface BaseCollectionRefreshViewController : BaseCollectionViewController<HttpViewDelegate>
{
    BOOL _loading;
}

//总页面数
@property (assign, nonatomic) int currentPage;
//是否正在加载中
@property (nonatomic, assign) BOOL loading;
//是否已经加载完毕
@property (nonatomic, assign) BOOL endReached;

@property (nonatomic,strong) HttpRequest *hRequest;

- (void)loadDataWithPage:(int)page;

@end