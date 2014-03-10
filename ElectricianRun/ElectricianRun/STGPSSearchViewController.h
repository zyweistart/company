//
//  STGPSSearchViewController.h
//  ElectricianRun
//
//  Created by Start on 3/9/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "STTaskAuditMapViewController.h"

@interface STGPSSearchViewController : UIViewController


- (id)initWitData:(NSDictionary*)data;

@property (strong,nonatomic) NSMutableDictionary *searchData;

@property (strong,nonatomic) STTaskAuditMapViewController<SearchDelegate> *delegate;

@end
