#import "ACMagazineViewController.h"
#import "ACShelf2Cell.h"

@interface ACMagazineViewController ()

@end

@implementation ACMagazineViewController

- (id)init
{
    self=[super init];
    if(self){
        [self.collectionView registerClass:[ACShelf2Cell class] forCellWithReuseIdentifier:CELLIDENTIFIER];
    }
    return self;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(!self.endReached){
        if(indexPath.row == [self.dataItemArray count])  {
            return CGSizeMake(320, 40);
        }
    }
    return CGSizeMake(100, 172);
}

//每个UICollectionView展示的内容
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    UICollectionViewCell *cell=[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
    if(cell!=nil){
        return cell;
    }
    ACShelf2Cell *shelfCell = (ACShelf2Cell *)[collectionView dequeueReusableCellWithReuseIdentifier:CELLIDENTIFIER forIndexPath:indexPath];
    
    NSDictionary *data=[self.dataItemArray objectAtIndex:indexPath.row];
    [shelfCell.title setText:[data objectForKey:@"periods"]];
    [shelfCell.price setText:@"收费"];
//    [shelfCell loadImage:[data objectForKey:@"frontPageUrl"]];
    return shelfCell;
}

- (void)loadDataWithPage:(int)page
{
    NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
    [params setObject:@"getjournallist_ios" forKey:@"act"];
    [params setObject:[NSString stringWithFormat:@"%d",page] forKey:@"page"];
    [params setObject:[NSString stringWithFormat:@"%d",PAGESIZE] forKey:@"pagesize"];
    self.hRequest=[[HttpRequest alloc]init];
    [self.hRequest setDelegate:self];
    [self.hRequest setController:self];
    [self.hRequest handle:@"" headParams:nil requestParams:params];
}

@end