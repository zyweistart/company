//
//  STTaskManagerConsumptionViewController.m
//  ElectricianRun
//
//  Created by Start on 2/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskManagerConsumptionViewController.h"

@interface STTaskManagerConsumptionViewController ()

@end

@implementation STTaskManagerConsumptionViewController {
    NSDictionary *_data;
    NSString *_taskId;
    NSString *_gnid;
    NSInteger _type;
}

- (id)initWithData:(NSDictionary *)data taskId:(NSString *)taskId gnid:(NSString *)g type:(NSInteger)t {
    self=[super init];
    if(self){
        [self.view setBackgroundColor:[UIColor whiteColor]];
        _data=data;
        _taskId=taskId;
        _gnid=g;
        _type=t;
    }
    return self;
}

@end
