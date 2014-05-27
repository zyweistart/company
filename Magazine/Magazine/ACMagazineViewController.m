#import "ACMagazineViewController.h"
#import "ACShelf1Cell.h"

@interface ACMagazineViewController ()

@end

@implementation ACMagazineViewController

- (id)init
{
    self=[super init];
    if(self){
        [self.collectionView registerClass:[ACShelf1Cell class] forCellWithReuseIdentifier:CELLIDENTIFIER];
        self.pageCount=1;
        //验证数据是否已经加载完毕
        self.endReached = NO;
        [self.dataItemArray removeAllObjects];
        for(int i=1;i<=PAGESIZE;i++){
            [self.dataItemArray addObject:[NSString stringWithFormat:@"%d",i]];
        }
    }
    return self;
}

- (void)doRefresh
{
    double delayInSeconds = 2.0;
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        [self.dataItemArray removeAllObjects];
        for(int i=1;i<=PAGESIZE;i++){
            [self.dataItemArray addObject:[NSString stringWithFormat:@"%d",i]];
        }
        self.pageCount=1;
        NSLog(@"下拉刷新了,第%d页",self.pageCount);
        //验证数据是否已经加载完毕
        self.endReached = NO;
        [self setLoading:NO];
    });
}

- (void)loadMore
{
    double delayInSeconds = 2.0;
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        self.pageCount++;
        NSLog(@"加载更多了,第%d页",self.pageCount);
        for(int i=1;i<=PAGESIZE;i++){
            [self.dataItemArray addObject:[NSString stringWithFormat:@"%d",i*self.pageCount]];
        }
        //验证数据是否已经加载完毕
        if(self.pageCount == 4){
            self.endReached = YES;
        }else{
            self.endReached = NO;
        }
        [self.collectionView reloadData];
    });
}


//每个UICollectionView展示的内容
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    UICollectionViewCell *cell=[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
    if(cell!=nil){
        return cell;
    }
    ACShelf1Cell *shelfCell = (ACShelf1Cell *)[collectionView dequeueReusableCellWithReuseIdentifier:CELLIDENTIFIER forIndexPath:indexPath];
    return shelfCell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.row == [self.dataItemArray count])  {
        return CGSizeMake(320, 40);
    }else{
        return CGSizeMake(100, 137);
    }
}

@end
