//
//  STSignupPhotographViewController.h
//  ElectricianRun
//
//  Created by Start on 3/6/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface STSignupPhotographViewController : UIViewController<HttpRequestDelegate>

@property (strong,nonatomic) NSString *name;
@property (strong,nonatomic) NSString *phone;
@property (strong,nonatomic) NSString *card;
@property (strong,nonatomic) NSString *address;

@property (strong,nonatomic) HttpRequest *hRequest;

@end
