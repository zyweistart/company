//
//  STSignupViewController.h
//  ElectricianRun
//
//  Created by Start on 2/28/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface STSignupViewController : UIViewController <HttpRequestDelegate>

@property (strong,nonatomic) HttpRequest *hRequest;

@end
