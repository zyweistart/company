#import "BaseTableViewController.h"

@interface BaseTableViewController ()

@end

@implementation BaseTableViewController

- (id)init
{
    self=[self initWithUITableViewStyle:UITableViewStylePlain];
    if(self){
        
    }
    return self;
}

- (id)initWithUITableViewStyle:(UITableViewStyle)style
{
    self=[super init];
    if(self){
        if(self.tableView==nil){
            self.tableView=[[UITableView alloc]initWithFrame:self.view.bounds style:style];
            [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
            [self.tableView setDelegate:self];
            [self.tableView setDataSource:self];
            [self.view addSubview:self.tableView];
            self.dataItemArray=[[NSMutableArray alloc]init];
        }
    }
    return self;
}

#pragma mark -
#pragma mark DelegateMethod

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
     return [self.dataItemArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"子类必须覆盖该方法，该语句不得出现在控制台上");
    return nil;
}

@end
