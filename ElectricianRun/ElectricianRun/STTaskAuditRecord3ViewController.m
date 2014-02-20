//
//  STTaskAuditRecord3ViewController.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditRecord3ViewController.h"

@interface STTaskAuditRecord3ViewController ()

@end

@implementation STTaskAuditRecord3ViewController

- (id)initWithData:(NSDictionary *) data
{
    self = [super init];
    if (self) {
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.data=data;
        
    }
    return self;
}

@end
