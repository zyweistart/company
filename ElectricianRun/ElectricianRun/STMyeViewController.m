//
//  STMyeViewController.m
//  ElectricianRun
//  我的e电工
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STMyeViewController.h"
#import "STLoginViewController.h"
#import "STSetupViewController.h"
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

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 45;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 3;
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
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    NSInteger row=[indexPath row];
    NSInteger section=[indexPath section];
    if(section==0){
        if(row==0){
            if([Account isLogin]){
                cell.textLabel.text=@"切换账户";
            }else{
                cell.textLabel.text=@"我的账户";
            }
        }else if(row==1){
            cell.textLabel.text=@"推荐给好友";
        }else{
            cell.textLabel.text=@"联系新能量";
        }
    }else if(section==1){
        cell.textLabel.text=@"设置";
    }else{
        cell.textLabel.text=@"关于e电工";
    }
    
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
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
        STSetupViewController *setupViewController=[[STSetupViewController alloc]init];
        [self.navigationController pushViewController:setupViewController animated:YES];
    }else{
        STAboutUsViewController *aboutUsViewController=[[STAboutUsViewController alloc]init];
        [self.navigationController pushViewController:aboutUsViewController animated:YES];
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(actionSheet.tag==1) {
        if(buttonIndex==0){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[[NSString alloc] initWithFormat:@"tel://%@",@"4008263365"]]];
        }
    }
}

@end
