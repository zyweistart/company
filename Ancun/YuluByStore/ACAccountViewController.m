//
//  ACAccountViewController.m
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//

#import "ACAccountViewController.h"
#import "ACRecordingCell.h"
#import "ACRecording2Cell.h"
#import "DataSingleton.h"

@interface ACAccountViewController ()

//界面按钮事件
- (void)leftTopButtonAction;
- (void)rightTopButtonAction;

@end

@implementation ACAccountViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {

    if (self) {
        
        self.navigationItem.title=@"我的账户";
        self.tabBarItem.title = @"我的账户";
        self.tabBarItem.image = [UIImage imageNamed:@"nav_icon_account"];
        
        self.tableView=[[UITableView alloc]initWithFrame:
                        CGRectMake(0, 123,
                                   self.view.frame.size.width,
                                   self.view.frame.size.height-123)];
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
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(8, 7, 300, 21)];
    [lbl1 setFont:[UIFont systemFontOfSize:15]];
    [lbl1 setText:@"基础包月套餐: 剩余7分钟，已用8分钟"];
    [self.view addSubview:lbl1];
    UILabel *lbl2=[[UILabel alloc]initWithFrame:CGRectMake(8, 28, 300, 21)];
    [lbl2 setFont:[UIFont systemFontOfSize:15]];
    [lbl2 setText:@"增值时长剩余: 5741分钟"];
    [self.view addSubview:lbl2];
    UILabel *lbl3=[[UILabel alloc]initWithFrame:CGRectMake(8, 51, 300, 21)];
    [lbl3 setFont:[UIFont systemFontOfSize:15]];
    [lbl3 setText:@"当前可用容量: 114.11MB"];
    [self.view addSubview:lbl3];
    
    _leftTopTab=[[UIButton alloc]initWithFrame:CGRectMake(0, 81, 159, 40)];
    [_leftTopTab setTitle:@"充值套餐" forState:UIControlStateNormal];
    [_leftTopTab setBackgroundColor:[UIColor blackColor]];
    [self.view addSubview:_leftTopTab];
    
    _rightTopTab=[[UIButton alloc]initWithFrame:CGRectMake(160, 81, 160, 40)];
    [_rightTopTab setTitle:@"使用记录" forState:UIControlStateNormal];
    [_rightTopTab setBackgroundColor:[UIColor blackColor]];
    [self.view addSubview:_rightTopTab];
    
    _lblSlid=[[UILabel alloc]initWithFrame:CGRectMake(0, 117, 159, 4)];
    [_lblSlid setBackgroundColor:NAVCOLOR];
    [self.view addSubview:_lblSlid];
    
    _leftTopTab.showsTouchWhenHighlighted = YES;//指定按钮被按下时发光
    [_leftTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_leftTopTab addTarget:self action:@selector(leftTopButtonAction) forControlEvents:UIControlEventTouchUpInside];
    _rightTopTab.showsTouchWhenHighlighted = YES;//指定按钮被按下时发光
    [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_rightTopTab addTarget:self action:@selector(rightTopButtonAction) forControlEvents:UIControlEventTouchUpInside];
    
}

#pragma mark-
#pragma mark 界面按钮事件

- (void)leftTopButtonAction {
    NSLog(@"leftTopButtonAction");
    currentTab=1;
    self.dataItemArray=_leftDataItemArray;
    
    [_leftTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(0, 117, 159, 4);
    
    [UIView commitAnimations];
}

- (void)rightTopButtonAction {
    NSLog(@"rightTopButtonAction");
    currentTab=2;
    self.dataItemArray=_rightDataItemArray;
    
    [_rightTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_leftTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(160, 117, 160, 4);
    
    [UIView commitAnimations];
}

//表视图委托
#pragma mark -
#pragma mark table view data source methods

- (void) viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    //如果为nil或者集合长度为零则自动刷新
    if(!self.dataItemArray||[self.dataItemArray count]==0||[[Config Instance]isRefreshRecordingList]){
        [self autoRefresh];
        [[Config Instance]setIsRefreshRecordingList:NO];
    }
    [[BaiduMobStat defaultStat] pageviewStartWithName:@"ACRecordingManagerViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewEndWithName:@"ACRecordingManagerViewController"];
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
            [dictionary setObject:[response responseString] forKey:CACHE_RECORDINGMANAGER];
            //缓存数据
            [Common setCache:[Config Instance].cacheKey data:dictionary];
        }
    }
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        NSString* name=[[[Config Instance]contact] objectForKey:[dictionary objectForKey:@"oppno"]];
        if(name==nil){
            name=[dictionary objectForKey:@"oppno"];
        }
        if([name isEqualToString:[dictionary objectForKey:@"oppno"]]){
            return 60;
        }else{
            return 70;
        }
    }else{
        return 50;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    //获取当前的行
    NSInteger row=[indexPath row];
    if([self.dataItemArray count]>row){
        static NSString *cellReuseIdentifier=@"ACRecordingCellIdentifier";
        static NSString *cell2ReuseIdentifier=@"ACRecording2CellIdentifier";
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
        NSString* name=[[[Config Instance]contact] objectForKey:[dictionary objectForKey:@"oppno"]];
        if(name==nil){
            name=[dictionary objectForKey:@"oppno"];
        }
        if([name isEqualToString:[dictionary objectForKey:@"oppno"]]){
            ACRecordingCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
            if(!cell){
                UINib *nib=[UINib nibWithNibName:@"ACRecordingCell" bundle:nil];
                [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
                cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
            }
            cell.lbl_oppno.text=[dictionary objectForKey:@"oppno"];
            cell.lbl_lcalltime.text=[[dictionary objectForKey:@"lcalltime"] substringWithRange:NSMakeRange(0, 10)];
            cell.lbl_orttime.text=[[NSString alloc]initWithFormat:@"%@",[Common secondConvertFormatTimerByCn:[dictionary objectForKey:@"onrttime"]]];
            cell.lbl_rtcount.text=[[NSString alloc]initWithFormat:@"%@个录音",[dictionary objectForKey:@"onrtcount"]];
            cell.selectionStyle = UITableViewCellSelectionStyleBlue;
            return cell;
        }else{
            ACRecording2Cell *cell2 = [self.tableView dequeueReusableCellWithIdentifier:cell2ReuseIdentifier];
            if(!cell2){
                UINib *nib=[UINib nibWithNibName:@"ACRecording2Cell" bundle:nil];
                [self.tableView registerNib:nib forCellReuseIdentifier:cell2ReuseIdentifier];
                cell2 = [self.tableView dequeueReusableCellWithIdentifier:cell2ReuseIdentifier];
            }
            cell2.lbl_name.text=name;
            cell2.lbl_oppno.text=[dictionary objectForKey:@"oppno"];
            cell2.lbl_lcalltime.text=[[dictionary objectForKey:@"lcalltime"] substringWithRange:NSMakeRange(0, 10)];
            cell2.lbl_orttime.text=[[NSString alloc]initWithFormat:@"%@",[Common secondConvertFormatTimerByCn:[dictionary objectForKey:@"onrttime"] ]];
            cell2.lbl_rtcount.text=[[NSString alloc]initWithFormat:@"%@个录音",[dictionary objectForKey:@"onrtcount"]];
            cell2.selectionStyle = UITableViewCellSelectionStyleBlue;
            return cell2;
        }
    }else{
        return [[DataSingleton Instance] getLoadMoreCell:tableView andIsLoadOver:_loadOver andLoadOverString:@"数据加载完毕" andLoadingString:(_reloading ? @"正在加载 . . ." : @"下面 8 项 . . .") andIsLoading:_reloading];
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.dataItemArray count]>[indexPath indexAtPosition:1]){
        NSLog(@"-----");
    }else{
        //加载更多
        _currentPage++;
        [self reloadTableViewDataSource];
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)refreshed:(NSNotification *)notification{
    if (notification.object){
        if ([(NSString *)notification.object isEqualToString:@"load"]) {
            [self autoRefresh];
        }
    }
}

- (void)reloadTableViewDataSource{
	if([[Config Instance]isLogin]){
        _reloading = YES;
        [self.tableView reloadData];
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:@"" forKey:@"oppno"];
        [requestParams setObject:@"" forKey:@"begintime"];
        [requestParams setObject:@"" forKey:@"endtime"];
        [requestParams setObject:@"desc" forKey:@"ordersort"];
        [requestParams setObject:[NSString stringWithFormat: @"%d",_pageSize]  forKey:@"pagesize"];
        [requestParams setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"currentpage"];
        _loadHttp=[[HttpRequest alloc]init];
        [_loadHttp setDelegate:self];
        [_loadHttp setController:self];
        [_loadHttp loginhandle:@"v4recStat" requestParams:requestParams];
    }else{
        [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:0];
        [Common noLoginAlert:self];
    }
}

@end