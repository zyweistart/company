//
//  STTaskAuditRecord3ViewController.h
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseTableViewController.h"

@interface STTaskAuditRecord3ViewController : BaseTableViewController<HttpRequestDelegate>

@property (strong,nonatomic) NSDictionary *data;
@property (strong,nonatomic) NSDictionary *dic;

- (id)initWithData:(NSDictionary *)data dic:(NSDictionary *)dic;

@property (strong,nonatomic) HttpRequest *hRequest;

@end
