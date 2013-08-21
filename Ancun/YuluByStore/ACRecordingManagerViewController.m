//
//  ACRecordingManagerViewController.m
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "ACRecordingManagerViewController.h"
#import "ACRecordingManagerDetailListViewController.h"
#import "ACRecordingManagerDetailListOldViewController.h"
#import "ACRecordingCell.h"
#import "ACRecording2Cell.h"
#import "DataSingleton.h"
#import "ACContactViewController.h"

@interface ACRecordingManagerViewController ()

@end

@implementation ACRecordingManagerViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        self.tabBarItem.image = [UIImage imageNamed:@"nav_icon_recording"];
        self.tabBarItem.title = @"我的录音";
        
        self.navigationItem.title=@"我的录音";
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshed:) name:Notification_TabClick_ACRecordingManagerViewController object:nil];
        
        if([[[[Config Instance]userInfo]objectForKey:@"oldflag"]intValue]==1) {
            self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]initWithTitle:@"时长版录音" style:UIBarButtonItemStyleDone target:self action:@selector(oldRecordingManagerList:)];
        }
        
    }
    return self;
}

- (void) viewDidLoad {
    [super viewDidLoad];
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        id content=[dictioanry objectForKey:CACHE_DATA];
        if(content){
            self.dataItemArray=[[XML analysis:content] dataItemArray];
            if([self.dataItemArray count]>0){
                [self.tableView reloadData];
            }
        }
    }
}

- (void) viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    //如果为nil或者集合长度为零则自动刷新
    if(!self.dataItemArray||[self.dataItemArray count]==0||[[Config Instance]isRefreshRecordingList]){
        [self autoRefresh];
        [[Config Instance]setIsRefreshRecordingList:NO];
    }
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
            [dictionary setObject:[response responseString] forKey:CACHE_DATA];
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
            return 75;
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
                cell = [[ACRecordingCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellReuseIdentifier];
            }
            cell.lblOppno.text=[dictionary objectForKey:@"oppno"];
            cell.lblLcalltime.text=[[dictionary objectForKey:@"lcalltime"] substringWithRange:NSMakeRange(0, 10)];
            cell.lblOrttime.text=[[NSString alloc]initWithFormat:@"%@",[Common secondConvertFormatTimerByCn:[dictionary objectForKey:@"onrttime"]]];
            cell.lblRtcount.text=[[NSString alloc]initWithFormat:@"%@个录音",[dictionary objectForKey:@"onrtcount"]];
            cell.selectionStyle = UITableViewCellSelectionStyleBlue;
            return cell;
        }else{
            ACRecording2Cell *cell2 = [self.tableView dequeueReusableCellWithIdentifier:cell2ReuseIdentifier];
            if(!cell2){
                cell2 = [[ACRecording2Cell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell2ReuseIdentifier];
            }
            cell2.lblName.text=name;
            cell2.lblOppno.text=[dictionary objectForKey:@"oppno"];
            cell2.lblLcalltime.text=[[dictionary objectForKey:@"lcalltime"] substringWithRange:NSMakeRange(0, 10)];
            cell2.lblOrttime.text=[[NSString alloc]initWithFormat:@"%@",[Common secondConvertFormatTimerByCn:[dictionary objectForKey:@"onrttime"] ]];
            cell2.lblRtcount.text=[[NSString alloc]initWithFormat:@"%@个录音",[dictionary objectForKey:@"onrtcount"]];
            cell2.selectionStyle = UITableViewCellSelectionStyleBlue;
            return cell2;
        }
    }else{
        return [[DataSingleton Instance] getLoadMoreCell:tableView andIsLoadOver:_loadOver andLoadOverString:@"数据加载完毕" andLoadingString:(_reloading ? @"正在加载 . . ." : @"更多 . . .") andIsLoading:_reloading];;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.dataItemArray count]>[indexPath indexAtPosition:1]){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        if(dictionary){
            ACRecordingManagerDetailListViewController* recordingManagerDetailListViewController=[[ACRecordingManagerDetailListViewController alloc] initWithOppno:[dictionary objectForKey:@"oppno"]];
            recordingManagerDetailListViewController.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:recordingManagerDetailListViewController animated:YES];
        }
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
        _loadDataHttp=[[HttpRequest alloc]init];
        [_loadDataHttp setDelegate:self];
        [_loadDataHttp setController:self];
        [_loadDataHttp loginhandle:@"v4recStat" requestParams:requestParams];
    }else{
        [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:0];
        [Common noLoginAlert:self];
    }
}

//导航至原时长版录音数据
- (void)oldRecordingManagerList:(id)sender {
    ACRecordingManagerDetailListOldViewController* recordingManagerDetailListOldViewController=[[ACRecordingManagerDetailListOldViewController alloc] init];
    recordingManagerDetailListOldViewController.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:recordingManagerDetailListOldViewController animated:YES];
}

- (void)dealloc {
    _loadDataHttp=nil;
}

@end
