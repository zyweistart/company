//
//  STMyeViewController.m
//  ElectricianRun
//  我的e电工
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STMyeViewController.h"
#import "STLoginViewController.h"
#import "STAlarmSetupViewController.h"
#import "STAboutUsViewController.h"
#import "WeixinSessionActivity.h"
#import "WeixinTimelineActivity.h"

@interface STMyeViewController () {
    NSArray *activity;
}

@end

@implementation STMyeViewController

- (id)init {
    self=[super init];
    if(self) {
        self.title=@"我的E电工";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        self.tableView = [[UITableView alloc] initWithFrame:self.view.bounds style: UITableViewStyleGrouped];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    activity = @[[[WeixinSessionActivity alloc] init], [[WeixinTimelineActivity alloc] init]];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.tableView reloadData];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 45;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 4;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if(section==0){
        return 3;
    }else{
        return 1;
    }
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath{
    static NSString *CellIdentifier=@"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    NSInteger row=[indexPath row];
    NSInteger section=[indexPath section];
    if(section==0){
        if(row==0){
            if([Account isLogin]){
                cell.textLabel.text=@"切换账户";
                cell.detailTextLabel.textColor=[UIColor redColor];
                cell.detailTextLabel.text=[Account getUserName];
            }else{
                cell.textLabel.text=@"登录账户";
                cell.detailTextLabel.text=@"";
            }
        }else if(row==1){
            cell.textLabel.text=@"推荐给好友";
        }else{
            cell.textLabel.text=@"联系新能量";
        }
    }else if(section==1){
        cell.textLabel.text=@"报警阀值设置";
    }else if(section==2){
        cell.textLabel.text=@"关于e电工";
    }else if(section==3){
        cell.textLabel.text=@"清除缓存数据";
    }
    if(section!=3){
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row=[indexPath row];
    NSInteger section=[indexPath section];
    if(section==0){
        if(row==0){
            STLoginViewController *loginViewController=[[STLoginViewController alloc]init];
            [self.navigationController pushViewController:loginViewController animated:YES];
        }else if(row==1){
            UIActivityViewController *activityView = [[UIActivityViewController alloc] initWithActivityItems:@[@"推荐一个很强大的APP，新能量e电工，通过集中在线监测和异常报警，高效管理众多变电站，安全、省心、省力、省钱，快下载试试吧。客户端下载地址：", [NSURL URLWithString:@"http://www.fps365.net/web/index/FPS_EDG.aspx"]] applicationActivities:activity];
            activityView.excludedActivityTypes = @[UIActivityTypeAssignToContact, UIActivityTypeCopyToPasteboard, UIActivityTypePrint];
            [self presentViewController:activityView animated:YES completion:nil];
        }else{
            [Common actionSheet:self message:@"是否拨打新能量客服电话？" tag:1];
        }
    }else if(section==1){
        if(![Account isLogin]){
            [Common alert:@"你还未登录，请先登录!"];
            STLoginViewController *loginViewController=[[STLoginViewController alloc]init];
            [self.navigationController pushViewController:loginViewController animated:YES];
            return;
        }
        STAlarmSetupViewController *alarmSetupViewController=[[STAlarmSetupViewController alloc]init];
        [self.navigationController pushViewController:alarmSetupViewController animated:YES];
    }else if(section==2){
        STAboutUsViewController *aboutUsViewController=[[STAboutUsViewController alloc]init];
        [self.navigationController pushViewController:aboutUsViewController animated:YES];
    }else{
       [Common actionSheet:self message:@"确认要删除所有缓存数据吗？" tag:2];
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(actionSheet.tag==1) {
        if(buttonIndex==0){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[[NSString alloc] initWithFormat:@"tel://%@",@"4008263365"]]];
        }
    }else if(actionSheet.tag==2){
        if(buttonIndex==0){
            //创建文件管理器
            NSFileManager* fileManager = [NSFileManager defaultManager];
            //获取Documents主目录
            NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
            //得到相应的Documents的路径
            NSString* docDir = [paths objectAtIndex:0];
            //更改到待操作的目录下
            [fileManager changeCurrentDirectoryPath:[docDir stringByExpandingTildeInPath]];
            
            //fileList便是包含有该文件夹下所有文件的文件名及文件夹名的数组
            NSArray *fileList = [fileManager contentsOfDirectoryAtPath:docDir error:nil];
            for(NSString *file in fileList){
                NSString *path = [docDir stringByAppendingPathComponent:file];
                //如果图标文件已经存在则进行显示否则进行下载
                if([fileManager fileExistsAtPath:path]){
                    [fileManager removeItemAtPath:path error:nil];
                }
            }
            [Common alert:@"已清除成功"];
        }
    }
}

@end
