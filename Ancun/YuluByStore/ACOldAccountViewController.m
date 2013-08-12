//
//  ACAccountViewController.m
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//

#import "ACOldAccountViewController.h"
#import "ACOldAccountMonthCell.h"
#import "ACOldAccountDayViewController.h"
#import "ACRechargeViewController.h"
#import "DataSingleton.h"
#import "NSString+Date.h"

@interface ACOldAccountViewController ()

@end

@implementation ACOldAccountViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    
    if (self) {
        
        self.navigationItem.title=@"我的账户";
        self.tabBarItem.title = @"我的账户";
        self.tabBarItem.image = [UIImage imageNamed:@"nav_icon_account"];
        
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
    
    UIView *view=[[UIView alloc]initWithFrame:CGRectMake(0, 0, 320, 70)];
    [view setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(8, 10, 100, 30)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    [lbl1 setText:@"可用录音时长:"];
    [view addSubview:lbl1];
    
    _lblTimeLong=[[UILabel alloc]initWithFrame:CGRectMake(110, 10, 90, 30)];
    [_lblTimeLong setFont:[UIFont systemFontOfSize:15]];
    [_lblTimeLong setBackgroundColor:[UIColor colorWithRed:(200/255.0) green:(200/255.0) blue:(200/255.0) alpha:1]];
    [_lblTimeLong setText:@"165分钟"];
    [view addSubview:_lblTimeLong];
    
    _btnPay=[UIButton buttonWithType:UIButtonTypeCustom];
    [_btnPay setFrame:CGRectMake(250, 15, 50, 20)];
    [_btnPay setImage:[UIImage imageNamed:@"accountpay_normal"] forState:UIControlStateNormal];
    [_btnPay setBackgroundImage:[UIImage imageNamed:@"accountpay_pressed"] forState:UIControlStateHighlighted];
    [_btnPay addTarget:self action:@selector(onPay:) forControlEvents:UIControlEventTouchDown];
    [view addSubview:_btnPay];
    
    [self.view addSubview:view];
    
    //读取使用记录缓存信息
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        id content=[dictioanry objectForKey:CACHE_OLDACCOUNT_MONTH];
        if(content){
            self.dataItemArray=[[XML analysis:content] dataItemArray];
            if([self.dataItemArray count]>0){
                [self.tableView reloadData];
            }
        }
    }
    
}

- (void)onPay:(id)sender {
    [self.navigationController pushViewController:[[ACRechargeViewController alloc] init] animated:YES];
}


//表视图委托
#pragma mark -
#pragma mark table view data source methods

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    if(!self.dataItemArray||[self.dataItemArray count]==0||[[Config Instance] isRefreshNotaryList]){
        [self autoRefresh];
        [[Config Instance]setIsRefreshNotaryList:NO];
    }
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
    if([response successFlag]){
        if([[response code]isEqualToString:@"110042"]||_currentPage==1){
            NSMutableDictionary *dictionary=[NSMutableDictionary dictionaryWithDictionary:[Common getCache:[Config Instance].cacheKey]];
            [dictionary setObject:[response responseString] forKey:CACHE_OLDACCOUNT_MONTH];
            //缓存数据
            [Common setCache:[Config Instance].cacheKey data:dictionary];
        }
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
        ACOldAccountMonthCell *cell = [self.tableView dequeueReusableCellWithIdentifier:oldAccountCell];
        if(!cell) {
            cell = [[ACOldAccountMonthCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:oldAccountCell];
        }
        
        [cell.lblMonth setText:[NSString stringWithFormat:@"%@月",[[dictionary objectForKey:@"tmonth"] substringWithRange:NSMakeRange(5,2)]]];
        
        int inamount=[[dictionary objectForKey:@"inamount"]intValue];
        [cell.lblPayTime setText:[NSString stringWithFormat:@"%d分钟",inamount/60]];
        
        int onamount=[[dictionary objectForKey:@"onamount"]intValue];
        [cell.lblUseTime setText:[NSString stringWithFormat:@"%d分钟",onamount/60]];
        
        return cell;
    }else{
        return [[DataSingleton Instance] getLoadMoreCell:tableView andIsLoadOver:_loadOver andLoadOverString:@"数据加载完毕" andLoadingString:(_reloading ? @"正在加载 . . ." : @"下面 8 项 . . .") andIsLoading:_reloading];
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSInteger row=[indexPath row];
    if([self.dataItemArray count]>row){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
        [self.navigationController pushViewController:[[ACOldAccountDayViewController alloc] initWithDate:[dictionary objectForKey:@"tmonth"]] animated:YES];
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