//
//  STDataMonitoringSearchViewController.h
//  ElectricianRun
//
//  Created by Start on 2/21/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "STDataMonitoringViewController.h"

@interface STDataMonitoringSearchViewController : UIViewController

@property (strong,nonatomic) STDataMonitoringViewController<SearchDelegate> *delegate;

@end
