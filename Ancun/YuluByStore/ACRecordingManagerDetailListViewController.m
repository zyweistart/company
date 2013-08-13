//
//  ACRecordingManagerDetailListViewController.m
//  ACyulu
//
//  Created by Start on 12-12-7.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "ACRecordingManagerDetailListViewController.h"
#import "ACRecordingDetailViewController.h"
#import "ACRecordingDetailCell.h"
#import "DataSingleton.h"

#define CACHE_RECORDINGMANAGERLIST CACHE_CONSTANT(@"CACHE_RECORDINGMANAGERLIST")

@interface ACRecordingManagerDetailListViewController ()

@end

@implementation ACRecordingManagerDetailListViewController{
    HttpRequest *_managerHttp;
}

- (id)initWithOppno:(NSString *)oppno{
    _oppno=oppno;
    if(self){
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                                initWithTitle:@"返回"
                                                style:UIBarButtonItemStyleBordered
                                                target:self
                                                action:@selector(back:)];

        NSString* navTitle=[[[Config Instance]contact] objectForKey:_oppno];
        if(navTitle==nil){
            navTitle=_oppno;
        }
        self.navigationItem.title=navTitle;
        
        _playerView=[ACPlayerView instance:self];
        //self.view.frame.size.height:主视图高度
        //[playerView frame].size.height:播放视图的高度
        //44:为导航栏的高度
        [_playerView setFrame:CGRectMake(0,
                                        self.view.frame.size.height-[_playerView frame].size.height-44,
                                        self.view.frame.size.width,
                                        [_playerView frame].size.height)];
        [self.view addSubview:_playerView];
        self.tableView=[[UITableView alloc]initWithFrame:
                    CGRectMake(0, 0,
                               self.view.frame.size.width,
                               self.view.frame.size.height-_playerView.frame.size.height)];
        [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
        [self.view addSubview:self.tableView];
        if (_refreshHeaderView == nil) {
            EGORefreshTableHeaderView *view = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.bounds.size.height, self.view.frame.size.width, self.tableView.bounds.size.height)];
            view.delegate = self;
            [self.tableView addSubview:view];
            _refreshHeaderView = view;
        }
    }
    self=[super init];
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        NSMutableDictionary *opponodic=[dictioanry objectForKey:CACHE_RECORDINGMANAGERLIST];
        if(opponodic){
            id content=[opponodic objectForKey:_oppno];
            if(content){
                self.dataItemArray=[[XML analysis:content] dataItemArray];
                if([self.dataItemArray count]>0){
                    [self.tableView reloadData];
                }
            }
        }
    }
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    //如果为nil或者集合长度为零则自动刷新
    if(!self.dataItemArray||[self.dataItemArray count]==0){
        [self autoRefresh];
    }
}

#pragma mark -
#pragma mark Delegate Methods

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if(reqCode==REQUESTCODE_FILE_DOWNLOAD){
        NSString* fileno=[[response propertys] objectForKey:@"fileno"];
        //创建文件管理器
        NSFileManager* fileManager = [NSFileManager defaultManager];
        //获取临时目录
        NSString* tmpDir=NSTemporaryDirectory();
        //更改到待操作的临时目录
        [fileManager changeCurrentDirectoryPath:[tmpDir stringByExpandingTildeInPath]];
        [fileManager removeItemAtPath:fileno error:nil];
        NSString *tmpPath = [tmpDir stringByAppendingPathComponent:fileno];
        
        //创建数据缓冲区
        NSMutableData* writer = [[NSMutableData alloc] init];
        //将字符串添加到缓冲中
        [writer appendData: [[response request]responseData]];
        //将其他数据添加到缓冲中
        //将缓冲的数据写入到临时文件中
        [writer writeToFile:tmpPath atomically:YES];
        
        //获取Documents主目录
        NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        //得到相应的Documents的路径
        NSString* docDir = [paths objectAtIndex:0];
        //更改到待操作的目录下
        [fileManager changeCurrentDirectoryPath:[docDir stringByExpandingTildeInPath]];
        [fileManager removeItemAtPath:fileno error:nil];
        NSString *path = [docDir stringByAppendingPathComponent:fileno];
        //把临时下载好的文件移动到主文档目录下
        [fileManager moveItemAtPath:tmpPath toPath:path error:nil];
        
        [_playerView player:path dictionary:[response propertys]];
        [self.tableView reloadData];
    }else if(reqCode==REQUESTCODE_REMOVE){
        if([response successFlag]){
            [Common notificationMessage:@"删除成功" inView:self.view];
            [self autoRefresh];
            [[Config Instance] setIsRefreshRecordingList:YES];
//            [_dataItemArray removeObjectAtIndex:indexPath.row];
//            [_tableView reloadData];
        }
    }else{
        [super requestFinishedByResponse:response requestCode:reqCode];
        if([response successFlag]){
            if([[response code]isEqualToString:@"110042"]||_currentPage==1){
                //缓存数据
                NSMutableDictionary *dictionary=[NSMutableDictionary dictionaryWithDictionary:[Common getCache:[Config Instance].cacheKey]];
                NSMutableDictionary *oppnoDic=[NSMutableDictionary dictionaryWithDictionary:[dictionary objectForKey:CACHE_RECORDINGMANAGERLIST]];
                [oppnoDic setObject:[response responseString] forKey:_oppno];
                [dictionary setObject:oppnoDic forKey:CACHE_RECORDINGMANAGERLIST];
                [Common setCache:[Config Instance].cacheKey data:dictionary];
            }
        }
    }
}

- (void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSMutableDictionary*)result{
    if(resultCode==RESULTCODE_ACRecordingDetailViewController_back){
        if(result) {
            //从详细页跳转回来
            if(_playerView){
                if([_playerView dictionary]){
                    //公证
                    if([[result objectForKey:@"cerflag"] isEqualToString:@"1"]){
                        [[_playerView btn_notary] setTitle:@"申办公证" forState:UIControlStateNormal];
                    }else if([[result objectForKey:@"cerflag"] isEqualToString:@"2"]){
                        [[_playerView btn_notary] setTitle:@"取消公证" forState:UIControlStateNormal];
                    }
                    //提取码
                    if([[result objectForKey:@"accstatus"] isEqualToString:@"1"]){
                        [[_playerView btn_extraction] setTitle:@"查看提取码" forState:UIControlStateNormal];
                    }else if([[result objectForKey:@"accstatus"] isEqualToString:@"2"]){
                        [[_playerView btn_extraction] setTitle:@"申请提取码" forState:UIControlStateNormal];
                    }
                    [_playerView setDictionary:result];
                }
            }
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

- (void)actionSheet:(UIActionSheet*)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(actionSheet.tag==4){
        
    }else{
        if(buttonIndex==0){
            [Common resultLoginViewController:self resultCode:RESULTCODE_ACLoginViewController_1 requestCode:0 data:nil];
        }
    }
}

- (void)alertView:(UIAlertView*)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag==1){
        if(buttonIndex==0){
            NSString *password=[[alertView textFieldAtIndex:0]text];
            if([password isEqualToString:@""]){
                [Common notificationMessage:@"密码不能为空" inView:self.view];
            }else{
                NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
                [requestParams setObject:[_currentDictionary objectForKey:@"fileno"] forKey:@"fileno"];
                [requestParams setObject:@"1" forKey:@"alteract"];
                [requestParams setObject:[password md5] forKey:@"password"];
                _managerHttp=[[HttpRequest alloc]init];
                [_managerHttp setDelegate:self];
                [_managerHttp setController:self];
                [_managerHttp setRequestCode:REQUESTCODE_REMOVE];
                [_managerHttp loginhandle:@"v4recAlter" requestParams:requestParams];
            }
        }
    }
}

- (CGFloat)tableView:(UITableView*)tableView heightForRowAtIndexPath:(NSIndexPath*)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        return 60;
    }else{
        return 50;
    }
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        static NSString *cellReuseIdentifier=@"ACRecordingDetailCellIdentifier";
        ACRecordingDetailCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        if(!cell){
            UINib *nib=[UINib nibWithNibName:@"ACRecordingDetailCell" bundle:nil];
            [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
            cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        }
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        NSString *fileno=[dictionary objectForKey:@"fileno"];
        cell.lbl_date.text=[dictionary objectForKey:@"begintime"];
        //创建文件管理器
        NSFileManager *fileManager = [NSFileManager defaultManager];
        //获取路径
        //1、参数NSDocumentDirectory要获取的那种路径
        NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        //2、得到相应的Documents的路径
        NSString* documentDirectory = [paths objectAtIndex:0];
        //3、更改到待操作的目录下
        [fileManager changeCurrentDirectoryPath:[documentDirectory stringByExpandingTildeInPath]];
        NSString *path = [documentDirectory stringByAppendingPathComponent:fileno];
        //如果录音文件存在都直接播放
        if([fileManager fileExistsAtPath:path]){
            cell.lbl_downloadflag.text=[Common secondConvertFormatTimerByCn:[dictionary objectForKey:@"duration"]];
        }else{
            cell.lbl_downloadflag.text=@"未下载";
        }
        NSString *remark=[dictionary objectForKey:@"remark"];
        if([remark isEqualToString:@""]){
            cell.lbl_remark.textColor=[UIColor lightGrayColor];
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
    //选择录音前先暂停
    [_playerView stop];
    if([self.dataItemArray count]>[indexPath row]){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        NSString *fileno=[dictionary objectForKey:@"fileno"];
        
        //创建文件管理器
        NSFileManager *fileManager = [NSFileManager defaultManager];
        //获取路径
        //1、参数NSDocumentDirectory要获取的那种路径
        NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        //2、得到相应的Documents的路径
        NSString* documentDirectory = [paths objectAtIndex:0];
        //3、更改到待操作的目录下
        [fileManager changeCurrentDirectoryPath:[documentDirectory stringByExpandingTildeInPath]];
        NSString *path = [documentDirectory stringByAppendingPathComponent:fileno];
        
        //如果录音文件存在都直接播放
        if([fileManager fileExistsAtPath:path]){
            [_playerView player:path dictionary:dictionary];
        }else{
            if([[Config Instance]isLogin]){
                NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
                [requestParams setObject:@"1" forKey:@"status"];
                [requestParams setObject:fileno forKey:@"fileno"];
                _managerHttp=[[HttpRequest alloc]init];
                [_managerHttp setDelegate:self];
                [_managerHttp setIsFileDownload:YES];
                [_managerHttp setPropertys:dictionary];
                [_managerHttp setRequestCode:REQUESTCODE_FILE_DOWNLOAD];
                [_managerHttp setController:self];
                [_managerHttp loginhandle:@"v4recDown" requestParams:requestParams];
            }else{
                [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:0];
                [Common noLoginAlert:self];
            }
        }
    }else{
        _currentPage++;
        [self reloadTableViewDataSource];
    }
}

- (BOOL)tableView:(UITableView*)tableView canEditRowAtIndexPath:(NSIndexPath*)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        return YES;
    }else{
        return NO;
    }
}

- (NSString*)tableView:(UITableView*) tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath{
    return @"删除";
}

- (void)tableView:(UITableView*)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    if([self.dataItemArray count]>[indexPath row]){
        if(editingStyle==UITableViewCellEditingStyleDelete){
            _currentDictionary=[[NSMutableDictionary alloc]initWithDictionary:[self.dataItemArray objectAtIndex:indexPath.row]];
            UIAlertView *alert = [[UIAlertView alloc]
                                  initWithTitle:@"请输入密码"
                                  message:@"录音删除后将无法恢复，确定删除?"
                                  delegate:self
                                  cancelButtonTitle:@"确定"
                                  otherButtonTitles:@"取消",nil];
            [alert setAlertViewStyle:UIAlertViewStyleSecureTextInput];
            alert.tag=1;
            [alert show];
        }
    }
}

- (void)tableView:(UITableView*)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath*)indexPath{
    if(_playerView){
        [_playerView stop];
    }
    if([self.dataItemArray count]>[indexPath row]){
        NSMutableDictionary *dictionary=[self.dataItemArray objectAtIndex:[indexPath row]];
        ACRecordingDetailViewController *detailViewController=[[ACRecordingDetailViewController alloc]init];
        [detailViewController setFileno:[dictionary objectForKey:@"fileno"]];
        [detailViewController setResultDelegate:self];
        detailViewController.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailViewController animated:YES];
        
    }
}



#pragma mark -
#pragma mark Custom Methods

- (void)back:(id)sencder{
    if(_playerView){
        [_playerView stop];
    }
    [self.navigationController popViewControllerAnimated:YES];
}

- (void) reloadTableViewDataSource {
	if([[Config Instance]isLogin]){
        _reloading = YES;
        [self.tableView reloadData];
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:@"1" forKey:@"status"];
        [requestParams setObject:@"" forKey:@"calltype"];
        [requestParams setObject:_oppno forKey:@"oppno"];
        [requestParams setObject:@"" forKey:@"callerno"];
        [requestParams setObject:@"" forKey:@"calledno"];
        [requestParams setObject:@"" forKey:@"begintime"];
        [requestParams setObject:@"" forKey:@"endtime"];
        [requestParams setObject:@"" forKey:@"durmin"];
        [requestParams setObject:@"" forKey:@"durmax"];
        [requestParams setObject:@"" forKey:@"licno"];
        [requestParams setObject:@"" forKey:@"accstatus"];
        [requestParams setObject:@"" forKey:@"cerflag"];
        [requestParams setObject:@"" forKey:@"remark"];
        [requestParams setObject:@"1" forKey:@"phoneflag"];
        [requestParams setObject:@"" forKey:@"userno"];
        [requestParams setObject:@"" forKey:@"phone"];
        [requestParams setObject:@"" forKey:@"grouprecordno"];
        [requestParams setObject:@"" forKey:@"groupflag"];
        [requestParams setObject:@"desc" forKey:@"ordersort"];
        [requestParams setObject:[NSString stringWithFormat: @"%d",_pageSize]  forKey:@"pagesize"];
        [requestParams setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"currentpage"];
        _managerHttp=[[HttpRequest alloc]init];
        [_managerHttp setDelegate:self];
        [_managerHttp setController:self];
        [_managerHttp loginhandle:@"v4recQry" requestParams:requestParams];
    }else{
        [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:0];
        [Common noLoginAlert:self];
    }
}

@end