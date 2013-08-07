//
//  ACAccountViewController.m
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//

#import "ACOldAccountDayViewController.h"
#import "ACOldAccountDayCell.h"
#import "DataSingleton.h"
#import "NSString+Date.h"

@interface ACOldAccountDayViewController ()

@end

@implementation ACOldAccountDayViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    
    if (self) {
        
        self.navigationItem.title=@"2013年08月消费明细";
        
        self.tableView=[[UITableView alloc]initWithFrame:
                        CGRectMake(0, 50,
                                   self.view.frame.size.width,
                                   self.view.frame.size.height-50)];
        [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
        [self.view addSubview:self.tableView];
        if(_refreshHeaderView==nil){
            EGORefreshTableHeaderView *view=[[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.bounds.size.height, self.view.frame.size.width, self.tableView.bounds.size.height)];
            view.delegate = self;
            [self.tableView addSubview:view];
            _refreshHeaderView = view;
        }
        [_refreshHeaderView refreshLastUpdatedDate];
        
    }
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    //设置Navigation Bar颜色
    self.navigationController.navigationBar.tintColor = NAVCOLOR;
    
    UIView *view=[[UIView alloc]initWithFrame:CGRectMake(0, 0, 320, 50)];
    [view setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(50, 10, 35, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    [lbl1 setText:@"时间"];
    [view addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(150, 10, 35, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    [lbl1 setText:@"类别"];
    [view addSubview:lbl1];
    
    lbl1=[[UILabel alloc]initWithFrame:CGRectMake(235, 10, 35, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    [lbl1 setText:@"时长"];
    [view addSubview:lbl1];
    
    [self.view addSubview:view];
    
    //读取使用记录缓存信息
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        id content=[dictioanry objectForKey:CACHE_OLDACCOUNT_MONTH];
        if(content){
            //            self.dataItemArray=[[XML analysis:content] dataItemArray];
        }
    }
    
}

//表视图委托
#pragma mark -
#pragma mark table view data source methods

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [[BaiduMobStat defaultStat] pageviewStartWithName:@"ACOldAccountViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [[BaiduMobStat defaultStat] pageviewEndWithName:@"ACOldAccountViewController"];
}

#pragma mark -
#pragma mark Delegate Methods

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==0){
        [Common resultLoginViewController:self resultCode:RESULTCODE_ACLoginViewController_1 requestCode:0 data:nil];
    }
}

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    [super requestFinishedByResponse:response requestCode:reqCode];
    if([response successFlag]) {
        
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if([self.dataItemArray count]>0){
        if(_pageSize>[self.dataItemArray count]){
            return [self.dataItemArray count];
        }else{
            return [self.dataItemArray count]+1;
        }
    }else{
        return 1;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        return 65;
    }else{
        return 50;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *oldAccountCell=@"oldAccountCell";
    //获取当前的行
    NSInteger row=[indexPath row];
    if([self.dataItemArray count]>row){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
        ACOldAccountDayCell *cell = [self.tableView dequeueReusableCellWithIdentifier:oldAccountCell];
        if(!cell) {
            cell = [[ACOldAccountDayCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:oldAccountCell];
        }
        
        return cell;
    }else{
        return [[DataSingleton Instance] getLoadMoreCell:tableView andIsLoadOver:_loadOver andLoadOverString:@"数据加载完毕" andLoadingString:(_reloading ? @"正在加载 . . ." : @"下面 8 项 . . .") andIsLoading:_reloading];
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.dataItemArray count]>[indexPath indexAtPosition:1]){
        //不处理单击事件
    }else{
        //加载更多
        _currentPage++;
        [self reloadTableViewDataSource];
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)reloadTableViewDataSource{
	if([[Config Instance]isLogin]){
        _reloading = YES;
        [self.tableView reloadData];
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:@"" forKey:@"changetimeb"];
        [requestParams setObject:@"" forKey:@"changetimee"];
        [requestParams setObject:@"" forKey:@"ordersort"];
        _loadHttp=[[HttpRequest alloc]init];
        [_loadHttp setDelegate:self];
        [_loadHttp setController:self];
        [_loadHttp loginhandle:@"v4accStat" requestParams:requestParams];
    }else{
        [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:0];
        [Common noLoginAlert:self];
    }
}

@end