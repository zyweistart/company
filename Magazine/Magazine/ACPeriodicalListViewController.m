#import "ACPeriodicalListViewController.h"
#import "ACPeriodicalContentViewController.h"

@interface ACPeriodicalListViewController ()

@property (strong,nonatomic)NSDictionary *data;

@end

@implementation ACPeriodicalListViewController

- (id)initWithData:(NSDictionary *)data
{
    self = [super init];
    if (self) {
        self.data=data;
        self.title=[NSString stringWithFormat:@"期刊号%@",[self.data objectForKey:@"periods"]];
    }
    return self;
}

- (void)loadData
{
    NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
    [params setObject:@"gettitlebyjid" forKey:@"act"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.data objectForKey:@"periods"]] forKey:@"jid"];
    self.hRequest=[[HttpRequest alloc]init];
    [self.hRequest setDelegate:self];
    [self.hRequest setController:self];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest handle:@"" headParams:nil requestParams:params];
}

- (void)requestFinishedByResponse:(Response*)response requestCode:(int)reqCode
{
    if([response successFlag]){
        [self.dataItemArray addObjectsFromArray:[[response resultJSON]objectForKey:@"data"]];
        [self.tableView reloadData];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier=@"CellIdentifier";
    UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSDictionary *d=[self.dataItemArray objectAtIndex:[indexPath row]];
    [cell.textLabel setText:[d objectForKey:@"bigTitle"]];
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *d=[self.dataItemArray objectAtIndex:[indexPath row]];
    ACPeriodicalContentViewController *periodicalContentViewController=[[ACPeriodicalContentViewController alloc]initWithData:d];
    [self.navigationController pushViewController:periodicalContentViewController animated:YES];
    [periodicalContentViewController loadData];
}

@end