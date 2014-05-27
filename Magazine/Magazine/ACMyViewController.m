#import "ACMyViewController.h"
#import "Config.h"

#define SKEY @"key"
#define SVALUE @"value"
#define SNEXT @"isnext"

@implementation ACMyViewController

- (id)init
{
    self=[super initWithUITableViewStyle:UITableViewStyleGrouped];
    if(self){
        [[Config Instance]setIsLogin:YES];
        NSDictionary *d1= @{SKEY: @"我的订阅",SVALUE:@"1",SNEXT:@"1"};
        NSDictionary *d2= @{SKEY: @"我的收藏",SVALUE:@"2",SNEXT:@"1"};
        NSDictionary *d3= @{SKEY: @"消息",SVALUE:@"3",SNEXT:@"1"};
        NSDictionary *d4= @{SKEY: @"登录",SVALUE:@"4",SNEXT:@"1"};
        NSDictionary *d5= @{SKEY: @"退出",SVALUE:@"5",SNEXT:@"0"};
        if([[Config Instance]isLogin]){
            //登录状态
            self.dataItemArray=[[NSMutableArray alloc]initWithObjects:@[d1,d2],@[d3],@[d5], nil];
        }else{
            //未登录状态
            self.dataItemArray=[[NSMutableArray alloc]initWithObjects:@[d4],@[d3], nil];
        }
    }
    return self;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [self.dataItemArray count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [[self.dataItemArray objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSDictionary *data=[[self.dataItemArray objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
    UITableViewCell *cell=[[UITableViewCell alloc]init];
    [cell.textLabel setText:[data objectForKey:SKEY]];
    if([@"1" isEqualToString:[data objectForKey:SNEXT]]){
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    }
    return cell;
}

@end