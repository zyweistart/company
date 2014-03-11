//
//  STStudyViewController.m
//  ElectricianRun
//  我要学习
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STStudyViewController.h"
#import "STNavigationWebPageViewController.h"

#define REQUESTCODESUBMIT 43281794

@interface STStudyViewController () {
    NSArray *titleArr;
}

@end

@implementation STStudyViewController

- (id)init {
    self=[super init];
    if(self) {
        self.title=@"我要学习";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"我要学习"
                          message:@"手机号码"
                          delegate:self
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:@"取消",nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    //设置输入框的键盘类型
    UITextField *tf = [alert textFieldAtIndex:0];
    tf.keyboardType = UIKeyboardTypeNumberPad;
    [alert show];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 45;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [titleArr count];
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath{
    static NSString *CellIdentifier=@"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSInteger row=[indexPath row];
    
    cell.textLabel.font=[UIFont systemFontOfSize:13];
    cell.textLabel.text=[titleArr objectAtIndex:row];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row=[indexPath row];
    STNavigationWebPageViewController *navigationWebPageViewController=[[STNavigationWebPageViewController alloc]initWithNavigationTitle:[titleArr objectAtIndex:row] resourcePath:[titleArr objectAtIndex:row]];
    [self.navigationController pushViewController:navigationWebPageViewController animated:YES];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex==0){
        NSString *phone=[[alertView textFieldAtIndex:0]text];
        if(![@"" isEqualToString:phone]){
            NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
            [p setObject:phone forKey:@"telNum"];
            [p setObject:@"" forKey:@"identityNo"];
            [p setObject:@"2" forKey:@"operateType"];
            self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:REQUESTCODESUBMIT];
            [self.hRequest setIsShowMessage:YES];
            [self.hRequest start:URLelecRegister params:p];
        }else{
            self.tabBarController.selectedIndex=0;
        }
    }else{
        self.tabBarController.selectedIndex=0;
    }
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode
{
    if(repCode==REQUESTCODESUBMIT){
        NSLog(@"%@",[response responseString]);
        [self loadData];
    }
}

- (void)loadData
{
    titleArr=[[NSArray alloc]initWithObjects:
              @"TRMS系统变电站失电报警装置安装调试教程",
              @"TRMS系统变电站远程监测服务安装调试教程",
              @"TRMS系统巡检工作服务规范(e电工版)",
              @"TRMS系统现场安装调试问题处理指南",nil];
    [self.tableView reloadData];
}

@end
