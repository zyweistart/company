//
//  STTaskManagerViewController.m
//  ElectricianRun
//  任务管理
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskManagerViewController.h"
#import "STTaskManagerExpandedCellIdentifierCell.h"
#import "STTaskManagerExpandingCellIdentifierCell.h"
#import "NSString+Utils.h"

@interface STTaskManagerViewController ()

@property (nonatomic) NSIndexPath *expandingIndexPath;
@property (nonatomic) NSIndexPath *expandedIndexPath;

- (NSIndexPath *)actualIndexPathForTappedIndexPath:(NSIndexPath *)indexPath;

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

#pragma mark - UITableView data source
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	if (self.expandedIndexPath) {
        return [self.dataItemArray count]+1;
    } else {
        return [self.dataItemArray count];
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    if ([indexPath isEqual:self.expandedIndexPath]) {
        return 100;
    } else {
        return 40;
    }
}

#pragma mark - UITableView delegate
static NSString *cellIdentifier1 = @"ExpandedCellIdentifier";
static NSString *cellIdentifier2 = @"ExpandingCellIdentifier";
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	if ([indexPath isEqual:self.expandedIndexPath]) {
        // init expanded cell
        STTaskManagerExpandedCellIdentifierCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellIdentifier1];
        if(!cell) {
            cell = [[STTaskManagerExpandedCellIdentifierCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier1];
        }
        return cell;
	} else {
        // init expanding cell
        STTaskManagerExpandingCellIdentifierCell *cell = [self.tableView dequeueReusableCellWithIdentifier:cellIdentifier2];
        if(!cell) {
            cell = [[STTaskManagerExpandingCellIdentifierCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier2];
        }
        NSIndexPath *theIndexPath = [self actualIndexPathForTappedIndexPath:indexPath];
        NSInteger row=[theIndexPath row];
        NSDictionary *dictionary=[self.dataItemArray objectAtIndex:row];
		[cell.lblName setText:[dictionary objectForKey:@"CP_NAME"]];
        return cell;
	}
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    // disable touch on expanded cell
	UITableViewCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
	if ([[cell reuseIdentifier] isEqualToString:cellIdentifier1]) {
		return;
	}
	
    // deselect row
	[self.tableView deselectRowAtIndexPath:indexPath animated:NO];
	
    // get the actual index path
	indexPath = [self actualIndexPathForTappedIndexPath:indexPath];
	
    // save the expanded cell to delete it later
	NSIndexPath *theExpandedIndexPath = self.expandedIndexPath;
	
    // same row tapped twice - get rid of the expanded cell
	if ([indexPath isEqual:self.expandingIndexPath]) {
		self.expandingIndexPath = nil;
		self.expandedIndexPath = nil;
	} else {
        // add the expanded cell
		self.expandingIndexPath = indexPath;
		self.expandedIndexPath = [NSIndexPath indexPathForRow:[indexPath row] + 1
													inSection:[indexPath section]];
	}
	
	[self.tableView beginUpdates];
	
	if (theExpandedIndexPath) {
		[self.tableView deleteRowsAtIndexPaths:@[theExpandedIndexPath]
							 withRowAnimation:UITableViewRowAnimationNone];
	}
	if (self.expandedIndexPath) {
		[self.tableView insertRowsAtIndexPaths:@[self.expandedIndexPath]
							 withRowAnimation:UITableViewRowAnimationNone];
	}
	
	[self.tableView endUpdates];
	
    // scroll to the expanded cell
	[self.tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];
}

#pragma mark - controller methods

- (NSIndexPath *)actualIndexPathForTappedIndexPath:(NSIndexPath *)indexPath
{
    
	if (self.expandedIndexPath && [indexPath row] > [self.expandedIndexPath row]) {
		return [NSIndexPath indexPathForRow:[indexPath row] - 1 inSection:[indexPath section]];
	}
	
	return indexPath;
}

#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    
//	_reloading = YES;
    
    NSString *URL=@"http://122.224.247.221:7007/WEB/mobile/AppMonitoringAlarm.aspx";
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:@"zhangyy" forKey:@"imei"];
    [p setObject:[@"8888AA" md5] forKey:@"authentication"];
    [p setObject:@"SJ10" forKey:@"GNID"];
    [p setObject:@"" forKey:@"QTKEY"];
    [p setObject:[NSString stringWithFormat: @"%d",_currentPage] forKey:@"QTPINDEX"];
    [p setObject:[NSString stringWithFormat: @"%d",PAGESIZE] forKey:@"QTPSIZE"];
    
    self.hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [self.hRequest setIsShowMessage:NO];
    [self.hRequest start:URL params:p];
    
}

@end
