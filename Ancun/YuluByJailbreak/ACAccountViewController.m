//
//  ACAccountViewController.m
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//

#import "ACAccountViewController.h"
#import "ACAccountHeaderCell.h"
#import "ACAccountHeaderCountCell.h"
#import "ACMoreCell.h"
#import "ACMore.h"
#import "ACExtractionListViewController.h"
#import "ACNotaryViewController.h"
#import "ACBuyViewController.h"
#import "IAPHelper.h"

@interface ACAccountViewController ()

- (void)refresh:(id)sender;
- (void)updateUserInfo;

@end

@implementation ACAccountViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if(self){
        self.tabBarItem.image = [UIImage imageNamed:@"nav_icon_account"];
        self.tabBarItem.title = @"我的账户";
        self.navigationItem.title=@"我的账户";
        
        self.tableView=[[UITableView alloc]initWithFrame:
                    CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height) style:UITableViewStyleGrouped];
        [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
        [self.view addSubview:self.tableView];
        
        _cellArray=[[NSArray alloc] initWithObjects:
                    [[ACMore alloc]initWith:@"出证列表" andImg:@"more_icon_notary" andTag:1],
                    [[ACMore alloc]initWith:@"提取列表" andImg:@"more_icon_extract" andTag:2],
                    nil];
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                                 initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh
                                                 target:self
                                                 action:@selector(refresh:)];
    }
    return self;
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self updateUserInfo];
    [[BaiduMobStat defaultStat] pageviewStartWithName:@"ACAccountViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewEndWithName:@"ACAccountViewController"];
}

#pragma mark -
#pragma mark Delegate Methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if(section==0){
        if([SKPaymentQueue canMakePayments]){
            return 3;
        }else{
            return 2;
        }
        //TODO如果不支持IAP付款方式则在此返回2
        //        return 2;
    }else{
        return 2;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if([indexPath section]==0){
        if([indexPath row]==0){
            return 90;
        }else if([indexPath row]==1){
            return 60;
        }
    }
    return 45;
}

- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if([indexPath section]==0){
        if([[Config Instance] isOldUser]){
            //老用户
            if([indexPath row]==0){
                static NSString *cellReuseIdentifier=@"ACAccountHeaderCellIdentifier";
                _headerCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                if(!_headerCell){
                    UINib *nib=[UINib nibWithNibName:@"ACAccountHeaderCell" bundle:nil];
                    [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
                    _headerCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                }
                _headerCell.selectionStyle = UITableViewCellSelectionStyleNone;
                return _headerCell;
            }else if([indexPath row]==1){
                static NSString *cellReuseIdentifier=@"ACAccountHeaderCountCellIdentifier";
                _headerCountCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                if(!_headerCountCell){
                    UINib *nib=[UINib nibWithNibName:@"ACAccountHeaderCountCell" bundle:nil];
                    [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
                    _headerCountCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                }
                _headerCountCell.selectionStyle = UITableViewCellSelectionStyleNone;
                return _headerCountCell;
            }
        }else{
            //新用户
            if([indexPath row]==0){
                static NSString *cellReuseIdentifier=@"ACAccountHeaderCellIdentifier";
                _headerCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                if(!_headerCell){
                    UINib *nib=[UINib nibWithNibName:@"ACAccountHeaderCell" bundle:nil];
                    [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
                    _headerCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                }
                _headerCell.selectionStyle = UITableViewCellSelectionStyleNone;
                return _headerCell;
            }else if([indexPath row]==1){
                static NSString *cellReuseIdentifier=@"ACAccountHeaderCountCellIdentifier";
                _headerCountCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                if(!_headerCountCell){
                    UINib *nib=[UINib nibWithNibName:@"ACAccountHeaderCountCell" bundle:nil];
                    [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
                    _headerCountCell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
                }
                _headerCountCell.selectionStyle = UITableViewCellSelectionStyleNone;
                return _headerCountCell;
            }
        }
        
    }
    static NSString *cellReuseIdentifier=@"ACMoreCellIdentifier";
    ACMoreCell *cell=[self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
    if(!cell){
        UINib *nib=[UINib nibWithNibName:@"ACMoreCell" bundle:nil];
        [self.tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
        cell = [self.tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
    }
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    if([indexPath section]==0){
        cell.lbl_name.text=@"账户充值";
        cell.img_view.image=[UIImage imageNamed:@"more_icon_payment"];
        return cell;
    }else{
        ACMore* more=[_cellArray objectAtIndex:[indexPath row]];
        cell.lbl_name.text=[more name];
        cell.img_view.image=[UIImage imageNamed:[more img]];
        return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if([indexPath section]==0){
        [self.navigationController pushViewController:[[ACBuyViewController alloc]init] animated:YES];
    }else if([indexPath section]==1){
        ACMore *more=[_cellArray objectAtIndex:[indexPath row]];
        if(more.tag==1){
            [self.navigationController pushViewController:[[ACNotaryViewController alloc]init] animated:YES];
        }else if(more.tag==2){
            [self.navigationController pushViewController:[[ACExtractionListViewController alloc]init] animated:YES];
        }
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==0){
        [Common resultLoginViewController:self resultCode:RESULTCODE_ACLoginViewController_1 requestCode:0 data:nil];
    }
}

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if([response successFlag]){
        NSMutableDictionary *dics=[[response mainData] objectForKey:@"v4info"];
        //更新旧的用户信息
        for(NSString *key in dics){
            [[[Config Instance] userInfo]setValue:[dics objectForKey:key] forKey:key];
        }
        [self updateUserInfo];
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)refresh:(id)sender{
    NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
    [requestParams setObject:@"1" forKey:@"raflag"];
    HttpRequest *http=[[HttpRequest alloc]init];
    [http setDelegate:self];
    [http setController:self];
    [http loginhandle:@"v4infoGet" requestParams:requestParams];
}

- (void)updateUserInfo{
    if([[Config Instance]isLogin]){
        NSMutableDictionary *userInfo=[[Config Instance] userInfo];
        if(_headerCell){
            _headerCell.lbl_phone.text=[userInfo objectForKey:@"phone"];
        }
        if(_headerCountCell){
            _headerCountCell.lbl_recording_count.text=[NSString stringWithFormat:@"%@个",[userInfo objectForKey:@"nrtcount"]];
            int rttime=[[userInfo objectForKey:@"ruttime"] intValue];
            if(rttime%60>0){
                rttime++;
            }
            _headerCountCell.lbl_recording_timelong.text=[NSString stringWithFormat:@"%d分",rttime/60];
            int retimelong=[[userInfo objectForKey:@"rectime"] intValue];
            if(retimelong%60>0){
                retimelong++;
            }
            _headerCountCell.lbl_recording_retimelong.text=[NSString stringWithFormat:@"%d分",retimelong/60];
        }
    }else{
        [Common noLoginAlert:self];
    }
}

@end
