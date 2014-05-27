#import "BaseCollectionRefreshViewController.h"
#import "CollectionViewLoadingCell.h"

@interface BaseCollectionRefreshViewController ()

@property (nonatomic, strong) EGORefreshTableHeaderView *refreshHeaderView;

@end

@implementation BaseCollectionRefreshViewController

- (id)init
{
    self=[super init];
    if(self){
        self.refreshHeaderView = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.collectionView.bounds.size.height, self.collectionView.bounds.size.width, self.collectionView.bounds.size.height)];
        self.refreshHeaderView.keyNameForDataStore = [NSString stringWithFormat:@"%@_LastRefresh", [self class]];
        [self.collectionView addSubview:self.refreshHeaderView];
        [self.collectionView registerClass:[CollectionViewLoadingCell class] forCellWithReuseIdentifier:CELLIDENTIFIERLOADINGCELL];
    }
    return self;
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if(self.searchDisplayController.searchResultsTableView == scrollView){
        return;
    }
    if (self.refreshHeaderView.state == EGOOPullRefreshLoading) {
        CGFloat offset = MAX(scrollView.contentOffset.y * -1, 0);
        offset = MIN(offset, 60);
        scrollView.contentInset = UIEdgeInsetsMake(offset, 0.0f, 0.0f, 0.0f);
    } else if (scrollView.isDragging) {
        if (self.refreshHeaderView.state == EGOOPullRefreshPulling && scrollView.contentOffset.y > -65.0f && scrollView.contentOffset.y < 0.0f && !self.loading) {
            [self.refreshHeaderView setState:EGOOPullRefreshNormal];
        } else if (self.refreshHeaderView.state == EGOOPullRefreshNormal && scrollView.contentOffset.y < -65.0f && !self.loading) {
            [self.refreshHeaderView setState:EGOOPullRefreshPulling];
        }
        if (scrollView.contentInset.top != 0) {
            scrollView.contentInset = UIEdgeInsetsZero;
        }
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    if(self.searchDisplayController.searchResultsTableView == scrollView){
        return;
    }
    if(!self.loading){
        if (scrollView.contentOffset.y <= - 65.0f) {
            [self setLoading:YES];
        }
    }
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    if(self.endReached){
        return [self.dataItemArray count];
    }else{
        return [self.dataItemArray count]+1;
    }
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.row == [self.dataItemArray count])  {
		CollectionViewLoadingCell *cell = (CollectionViewLoadingCell *)[collectionView dequeueReusableCellWithReuseIdentifier:CELLIDENTIFIERLOADINGCELL forIndexPath:indexPath];
        //开始执行加载更多方法
        [self loadMore];
		return cell;
    }
    return nil;
}

- (BOOL)loading
{
    return _loading;
}

- (void)setLoading:(BOOL)loading
{
    _loading = loading;
    [UIView beginAnimations:nil context:nil];
    if(loading) {
        [self.refreshHeaderView setState:EGOOPullRefreshLoading];
        [UIView setAnimationDuration:0.2];
		self.collectionView.contentInset = UIEdgeInsetsMake(60.0f, 0.0f, 0.0f, 0.0f);
        //开始执行下拉刷新
        [self doRefresh];
    } else {
        [self.refreshHeaderView setState:EGOOPullRefreshNormal];
        [self.refreshHeaderView setCurrentDate];
        [UIView setAnimationDuration:.3];
        [self.collectionView setContentInset:UIEdgeInsetsMake(0.0f, 0.0f, 0.0f, 0.0f)];
        //数据刷新完毕重新加载表的数据
        [self.collectionView reloadData];
    }
    [UIView commitAnimations];
}

- (void)doRefresh
{
    NSLog(@"子类必须覆盖该方法，该语句不得出现在控制台上");
}

- (void)loadMore
{
    NSLog(@"子类必须覆盖该方法，该语句不得出现在控制台上");
}

@end