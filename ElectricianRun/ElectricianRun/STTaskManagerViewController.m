//
//  STTaskManagerViewController.m
//  ElectricianRun
//  任务管理
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskManagerViewController.h"
#import "STTaskManagerExpandingCellIdentifierCell.h"
#import "NSString+Utils.h"
#import "STTaskManagerHandleViewController.h"

@interface STTaskManagerViewController ()

@end


@implementation STTaskManagerViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"任务管理";
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
    }
    return self;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - UITableView delegate
static NSString *cellIdentifier = @"ExpandingCellIdentifier";
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	STTaskManagerExpandingCellIdentifierCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if(!cell) {
        cell = [[STTaskManagerExpandingCellIdentifierCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    NSInteger row=[indexPath row];
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
    [cell.lblName setText:[dictionary objectForKey:@"CP_NAME"]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UIActionSheet *sheet = [[UIActionSheet alloc]
                            initWithTitle:@""
                            delegate:self
                            cancelButtonTitle:@"取消"
                            destructiveButtonTitle:@"提交"
                            otherButtonTitles:
                            @"站点电耗量信息",
                            @"运行设备外观、温度检查",
                            @"受总柜运行情况",
                            @"TRMS系统巡视检查",nil];
    sheet.tag=[indexPath row];
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:@"RW10" forKey:@"GNID"];
    [p setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"QTPINDEX"];
    [p setObject:[NSString stringWithFormat: @"%d",PAGESIZE] forKey:@"QTPSIZE"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URLAppMonitoringAlarm params:p];
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    NSInteger row=actionSheet.tag;
    NSDictionary *data=[self.dataItemArray objectAtIndex:row];
    NSString *taskId=[data objectForKey:@"TASK_ID"];
    if(buttonIndex==0){
        //提交
    } else if(buttonIndex==1){
        //站点电耗量信息
        STTaskManagerHandleViewController *taskManagerHandleViewController=[[STTaskManagerHandleViewController alloc]initWithTaskId:taskId gnid:@"RW16" type:1];
        [self.navigationController pushViewController:taskManagerHandleViewController animated:YES];
    } else if(buttonIndex==2){
        //受总柜运行情况
        STTaskManagerHandleViewController *taskManagerHandleViewController=[[STTaskManagerHandleViewController alloc]initWithTaskId:taskId gnid:@"RW17" type:3];
        [self.navigationController pushViewController:taskManagerHandleViewController animated:YES];
    } else if(buttonIndex==3){
        //运行设备外观、温度检查
        STTaskManagerHandleViewController *taskManagerHandleViewController=[[STTaskManagerHandleViewController alloc]initWithTaskId:taskId gnid:@"RW15" type:2];
        [self.navigationController pushViewController:taskManagerHandleViewController animated:YES];
    } else if(buttonIndex==4){
        //TRMS系统巡视检查
        STTaskManagerHandleViewController *taskManagerHandleViewController=[[STTaskManagerHandleViewController alloc]initWithTaskId:taskId gnid:@"RW17" type:4];
        [self.navigationController pushViewController:taskManagerHandleViewController animated:YES];
    }
}

@end
