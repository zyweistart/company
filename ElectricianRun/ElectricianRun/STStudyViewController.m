//
//  STStudyViewController.m
//  ElectricianRun
//  我要学习
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STStudyViewController.h"

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
        titleArr=[[NSArray alloc]initWithObjects:@"新能量学习1",@"新能量学习2",@"新能量学习3",@"新能量学习4",nil];
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
    cell.textLabel.text=[titleArr objectAtIndex:row];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row=[indexPath row];
    [Common alert:[NSString stringWithFormat:@"当前学习的是第%d条",row]];
}

@end
