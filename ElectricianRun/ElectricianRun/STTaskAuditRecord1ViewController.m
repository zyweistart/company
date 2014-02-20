//
//  STTaskAuditRecord1ViewController.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditRecord1ViewController.h"
#import "STTaskAuditRecord2ViewController.h"

@interface STTaskAuditRecord1ViewController ()

@end

@implementation STTaskAuditRecord1ViewController

- (id)initWithData:(NSDictionary *) data
{
    self = [super init];
    if (self) {
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.data=data;
        
        
        NSDictionary *dic1=[[NSDictionary alloc]initWithObjectsAndKeys:@"站点电耗量信息",@"1", nil];
        NSDictionary *dic2=[[NSDictionary alloc]initWithObjectsAndKeys:@"运行设备外观、温度检查",@"2", nil];
        NSDictionary *dic3=[[NSDictionary alloc]initWithObjectsAndKeys:@"受总柜运行情况",@"3", nil];
        NSDictionary *dic4=[[NSDictionary alloc]initWithObjectsAndKeys:@"TRMS系统巡视检查",@"4", nil];
        self.dataItemArray=[[NSMutableArray alloc]init];
        [self.dataItemArray addObject:dic1];
        [self.dataItemArray addObject:dic2];
        [self.dataItemArray addObject:dic3];
        [self.dataItemArray addObject:dic4];
        
    }
    return self;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSUInteger row=[indexPath row];
    NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
    cell.textLabel.text=[NSString stringWithFormat:@"%@",[dictionary objectForKey:[NSString stringWithFormat:@"%ld",row+1]]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {

    NSUInteger row=[indexPath row];
    STTaskAuditRecord2ViewController *taskAuditRecord2ViewController=[[STTaskAuditRecord2ViewController alloc]initWithData:[self data] type:row+1];
    [self.navigationController pushViewController:taskAuditRecord2ViewController animated:YES];
    
}

@end
