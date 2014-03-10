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
        
        NSMutableDictionary *data1=[[NSMutableDictionary alloc]init];
        [data1 setObject:@"TRMS系统变电站远程监测服务安装高度教程" forKey:@"title"];
        [data1 setObject:@"elec_telnet_test" forKey:@"url"];
        NSMutableDictionary *data2=[[NSMutableDictionary alloc]init];
        [data2 setObject:@"TRMS系统变电站失电报警装置安装调试教程" forKey:@"title"];
        [data2 setObject:@"loss_power_Setup" forKey:@"url"];
        NSMutableDictionary *data3=[[NSMutableDictionary alloc]init];
        [data3 setObject:@"TRMS系统巡检工作服务规范" forKey:@"title"];
        [data3 setObject:@"sys_service_specification" forKey:@"url"];
        NSMutableDictionary *data4=[[NSMutableDictionary alloc]init];
        [data4 setObject:@"TRMS系统安装调试问题处理指南" forKey:@"title"];
        [data4 setObject:@"install_debug_problems" forKey:@"url"];
        
        titleArr=[[NSArray alloc]initWithObjects:
                  data1,
                  data2,
                  data3,
                  data4,nil];
        
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
    
    NSMutableDictionary *d=[titleArr objectAtIndex:row];
    
    cell.textLabel.font=[UIFont systemFontOfSize:13];
    cell.textLabel.text=[d objectForKey:@"title"];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row=[indexPath row];
    NSMutableDictionary *d=[titleArr objectAtIndex:row];
    NSString *title=[d objectForKey:@"title"];
    NSString *url=[d objectForKey:@"url"];
    STNavigationWebPageViewController *navigationWebPageViewController=[[STNavigationWebPageViewController alloc]initWithNavigationTitle:title resourcePath:url];
    [self.navigationController pushViewController:navigationWebPageViewController animated:YES];
}

@end
