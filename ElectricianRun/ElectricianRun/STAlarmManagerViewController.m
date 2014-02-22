//
//  STAlarmManagerViewController.m
//  ElectricianRun
//  报警管理
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STAlarmManagerViewController.h"
#import "STAlarmManagerSearchViewController.h"
#import "NSString+Utils.h"

@interface STAlarmManagerViewController ()

@end

@implementation STAlarmManagerViewController {
    NSMutableArray *deleteDic;
    NSArray *handleData;
    NSMutableDictionary *searchData;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"报警管理";
        
        deleteDic=[[NSMutableArray alloc]init];
        
        handleData=[[NSArray alloc]initWithObjects:
        @"选择",@"已处理",@"处理未妥",@"试验",
        @"误报",@"不需要处理",@"正在处理",@"原因不明",
        @"计划处理",@"人工分闸", nil];
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
        self.navigationItem.rightBarButtonItems=[[NSArray alloc]initWithObjects:
                                                 [[UIBarButtonItem alloc]
                                                                                 initWithTitle:@"查询"
                                                                                 style:UIBarButtonItemStyleBordered
                                                                                 target:self
                                                  action:@selector(search:)],
                                                 [[UIBarButtonItem alloc]
                                                                              initWithTitle:@"批量处理"
                                                                              style:UIBarButtonItemStyleBordered
                                                                              target:self
                                                                              action:@selector(edit:)], nil];
        
        searchData=[[NSMutableDictionary alloc]init];
        [searchData setObject:@"" forKey:@"QTKEY"];
        [searchData setObject:@"" forKey:@"QTKEY1"];
        [searchData setObject:@"" forKey:@"QTKEY2"];
        
        
        
        
    }
    return self;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)edit:(id)sender {
    
    UIBarButtonItem *bi=[self.navigationItem.rightBarButtonItems objectAtIndex:1];
    
    if ([bi.title isEqualToString: @"批量处理"]) {
		bi.title = @"确定";
		[self.tableView setEditing:YES animated:YES];
        [deleteDic removeAllObjects];
	} else {
//		bi.title = @"批量处理";
//		[self.tableView setEditing:NO animated:YES];
        RMPickerViewController *pickerVC = [RMPickerViewController pickerController];
        pickerVC.delegate = self;
        [pickerVC show];
	}
}

- (void)search:(id)sender {
    STAlarmManagerSearchViewController *alarmManagerSearchViewController=[[STAlarmManagerSearchViewController alloc]init];
    [alarmManagerSearchViewController setDelegate:self];
    [self.navigationController pushViewController:alarmManagerSearchViewController animated:YES];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSUInteger row=[indexPath row];
    if([self.dataItemArray count] > row){
        NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
        cell.textLabel.text=[NSString stringWithFormat:@"第:%ld,%@",row+1,[dictionary objectForKey:@"SITE_NAME"]];
    }else{
        cell.textLabel.text=@"更多";
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    UIBarButtonItem *bi=[self.navigationItem.rightBarButtonItems objectAtIndex:1];
    
    if ([bi.title isEqualToString: @"批量处理"]) {
        RMPickerViewController *pickerVC = [RMPickerViewController pickerController];
        pickerVC.delegate = self;
        [pickerVC show];
    } else {
        [deleteDic addObject:[self.dataItemArray objectAtIndex:[indexPath row]]];
    }
    
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete | UITableViewCellEditingStyleInsert;
}

- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
    if ([self.navigationItem.rightBarButtonItem.title isEqualToString: @"确定"]) {
        [deleteDic removeObject:[self.dataItemArray objectAtIndex:[indexPath row]]];
    }
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"SJ30" forKey:@"GNID"];
    [p setObject:[searchData objectForKey:@"QTKEY"] forKey:@"QTKEY"];
    [p setObject:[searchData objectForKey:@"QTKEY1"] forKey:@"QTKEY1"];
    [p setObject:[searchData objectForKey:@"QTKEY2"] forKey:@"QTKEY2"];
    [p setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"QTPINDEX"];
    [p setObject:[NSString stringWithFormat: @"%d",PAGESIZE] forKey:@"QTPSIZE"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URL params:p];
    
}

- (void)startSearch:(NSMutableDictionary *)data {
    
    searchData=data;
    [self autoRefresh];
    
}

#pragma mark - RMPickerViewController Delegates
- (void)pickerViewController:(RMPickerViewController *)vc didSelectRows:(NSArray *)selectedRows {
    NSLog(@"Successfully selected rows: %@", selectedRows);
    UIBarButtonItem *bi=[self.navigationItem.rightBarButtonItems objectAtIndex:1];
    if ([bi.title isEqualToString: @"确定"]) {
		bi.title = @"批量处理";
		[self.tableView setEditing:NO animated:YES];
	}
}

- (void)pickerViewControllerDidCancel:(RMPickerViewController *)vc {
    NSLog(@"Selection was canceled");
    UIBarButtonItem *bi=[self.navigationItem.rightBarButtonItems objectAtIndex:1];
    if ([bi.title isEqualToString: @"确定"]) {
		bi.title = @"批量处理";
		[self.tableView setEditing:NO animated:YES];
	}
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return [handleData count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    
    return [handleData objectAtIndex:row];
}
@end
