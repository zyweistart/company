//
//  STStudyViewController.m
//  ElectricianRun
//  我要学习
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STStudyViewController.h"
#import "STNavigationWebPageViewController.h"

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
        
        titleArr=[[NSArray alloc]initWithObjects:
                  @"TRMS系统变电站失电报警装置安装调试教程",
                  @"TRMS系统变电站远程监测服务安装调试教程",
                  @"TRMS系统巡检工作服务规范(e电工版)",
                  @"TRMS系统现场安装调试问题处理指南",nil];
        
    }
    return self;
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

@end
