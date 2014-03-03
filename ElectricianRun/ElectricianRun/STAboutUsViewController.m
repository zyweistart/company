//
//  STAboutUsViewController.m
//  ElectricianRun
//  关于
//  Created by Start on 2/22/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STAboutUsViewController.h"
#import "STFeedbackViewController.h"
#import "STIntroductionViewController.h"
#import "STAgreementContentViewController.h"

@interface STAboutUsViewController () {
    NSArray *titleArr;
}

@end

@implementation STAboutUsViewController


- (id)init {
    self=[super init];
    if(self) {
        self.title=@"关于";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        titleArr=[[NSArray alloc]initWithObjects:@"检测更新",@"用户反馈",@"新能量介绍",@"e电工协议内容",nil];
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
    if(row>0){
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row=[indexPath row];
    if(row==1){
        STFeedbackViewController *feedbackViewController=[[STFeedbackViewController alloc]init];
        [self.navigationController pushViewController:feedbackViewController animated:YES];
    }else if(row==2){
        STIntroductionViewController *introductionViewController=[[STIntroductionViewController alloc]init];
        [self.navigationController pushViewController:introductionViewController animated:YES];
    }else if(row==3){
        STAgreementContentViewController *agreementContentViewController=[[STAgreementContentViewController alloc]init];
        [self.navigationController pushViewController:agreementContentViewController animated:YES];
    }else if(row==0){
        [Common alert:@"当前版本已经为最新版本"];
    }
}

@end