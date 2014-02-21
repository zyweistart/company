//
//  STDataMonitoringLineDetailListViewController.h
//  ElectricianRun
//
//  Created by Start on 1/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseRefreshTableViewController.h"

@interface STDataMonitoringLineDetailListViewController : BaseRefreshTableViewController

@property (strong,nonatomic) NSDictionary *data;

- (id)initWithData:(NSDictionary *)data;
@end
