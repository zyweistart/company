//
//  ACNotaryViewController.m
//  ACyulu
//
//  Created by Start on 13-1-11.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "ACNotaryViewController.h"
#import "ACRecordingDetailViewController.h"
#import "ACRecordingDetailCell.h"
#import "DataSingleton.h"
#import "ACRecordingItemCell.h"

@interface ACNotaryViewController ()

@end

@implementation ACNotaryViewController{
    HttpRequest *_loadHttp;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self){
        self.navigationItem.title=@"出证列表";
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        id content=[dictioanry objectForKey:CACHE_NOTARYLIST];
        if(content){
            self.dataItemArray=[[XML analysis:content] dataItemArray];
            if([self.dataItemArray count]>0){
                [self.tableView reloadData];
            }
        }
    }
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    if(!self.dataItemArray||[self.dataItemArray count]==0||[[Config Instance] isRefreshNotaryList]){
        [self autoRefresh];
        [[Config Instance]setIsRefreshNotaryList:NO];
    }
    [[BaiduMobStat defaultStat] pageviewStartWithName:@"ACNotaryViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewEndWithName:@"ACNotaryViewController"];
}

#pragma mark -
#pragma mark Delegate Methods

- (CGFloat)tableView:(UITableView*)tableView heightForRowAtIndexPath:(NSIndexPath*)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        return 60;
    }else{
        return 50;
    }
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        
        static NSString *cellReuseIdentifier=@"ACRecordingItemCellIdentifier";
        ACRecordingItemCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        if(!cell){
            UINib *nib=[UINib nibWithNibName:@"ACRecordingItemCell" bundle:nil];
            [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
            cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        }
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        
        NSString* name=[[[Config Instance]contact] objectForKey:[dictionary objectForKey:@"oppno"]];
        if(name==nil){
            name=[dictionary objectForKey:@"oppno"];
        }
        cell.lbl_name.text=name;
        cell.lbl_date.text=[[dictionary objectForKey:@"begintime"] substringWithRange:NSMakeRange(0, 10)];
        NSString *remark=[[dictionary objectForKey:@"remark"] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        
        if([remark isEqualToString:@""]){
            cell.lbl_remark.textColor=[UIColor grayColor];
            cell.lbl_remark.text=@"添加备注";
        }else{
            cell.lbl_remark.textColor=[UIColor blackColor];
            cell.lbl_remark.text=remark;
        }
        return cell;
        
    }else{
        return [[DataSingleton Instance] getLoadMoreCell:tableView andIsLoadOver:_loadOver andLoadOverString:@"数据加载完毕" andLoadingString:(_reloading ? @"正在加载 . . ." : @"下面 8 项 . . .") andIsLoading:_reloading];
    }
}

- (void)tableView:(UITableView*)tableView didSelectRowAtIndexPath:(NSIndexPath*)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        ACRecordingDetailViewController *detailViewController=[[ACRecordingDetailViewController alloc]init];
        [detailViewController setFileno:[dictionary objectForKey:@"fileno"]];
        [detailViewController setResultDelegate:self];
        detailViewController.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailViewController animated:YES];
    }else{
        _currentPage++;
        [self reloadTableViewDataSource];
    }
}

- (void)actionSheet:(UIActionSheet*)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==0){
        [Common resultLoginViewController:self resultCode:RESULTCODE_ACLoginViewController_1 requestCode:0 data:nil];
    }
}

- (void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSMutableDictionary*)result{
    if(resultCode==RESULTCODE_ACRecordingDetailViewController_back){
        if(result) {
            for(NSMutableDictionary *dic in self.dataItemArray){
                if([[dic objectForKey:@"fileno"] isEqualToString:[result objectForKey:@"fileno"]]){
                    [dic setObject:[result objectForKey:@"cerflag"] forKey:@"cerflag"];
                    [dic setObject:[result objectForKey:@"accstatus"] forKey:@"accstatus"];
                    [dic setObject:[result objectForKey:@"remark"] forKey:@"remark"];
                    break;
                }
            }
            [self.tableView reloadData];
        }
    }
}

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    [super requestFinishedByResponse:response requestCode:reqCode];
    if([response successFlag]){
        if([[response code]isEqualToString:@"110042"]||_currentPage==1){
            NSMutableDictionary *dictionary=[NSMutableDictionary dictionaryWithDictionary:[Common getCache:[Config Instance].cacheKey]];
            [dictionary setObject:[response responseString] forKey:CACHE_NOTARYLIST];
            //缓存数据
            [Common setCache:[Config Instance].cacheKey data:dictionary];
        }
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)reloadTableViewDataSource{
	if([[Config Instance]isLogin]){
        _reloading = YES;
        [self.tableView reloadData];
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:@"1" forKey:@"status"];
        [requestParams setObject:@"" forKey:@"calltype"];
        [requestParams setObject:@"" forKey:@"oppno"];
        [requestParams setObject:@"" forKey:@"callerno"];
        [requestParams setObject:@"" forKey:@"calledno"];
        [requestParams setObject:@"" forKey:@"begintime"];
        [requestParams setObject:@"" forKey:@"endtime"];
        [requestParams setObject:@"" forKey:@"durmin"];
        [requestParams setObject:@"" forKey:@"durmax"];
        [requestParams setObject:@"" forKey:@"licno"];
        [requestParams setObject:@"" forKey:@"accstatus"];
        [requestParams setObject:@"2" forKey:@"cerflag"];
        [requestParams setObject:@"" forKey:@"remark"];
        [requestParams setObject:@"1" forKey:@"phoneflag"];
        [requestParams setObject:@"" forKey:@"userno"];
        [requestParams setObject:@"" forKey:@"phone"];
        [requestParams setObject:@"" forKey:@"grouprecordno"];
        [requestParams setObject:@"" forKey:@"groupflag"];
        [requestParams setObject:@"" forKey:@"ordersort"];
        [requestParams setObject:[NSString stringWithFormat: @"%d",_pageSize]  forKey:@"pagesize"];
        [requestParams setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"currentpage"];
        if(_loadHttp==nil) {
            _loadHttp=[[HttpRequest alloc]init];
        }
        [_loadHttp setDelegate:self];
        [_loadHttp setController:self];
        [_loadHttp loginhandle:@"v4recQry" requestParams:requestParams];
    }else{
        [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:0];
        [Common noLoginAlert:self];
    }
}

@end
