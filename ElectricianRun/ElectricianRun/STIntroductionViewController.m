//
//  STIntroductionViewController.m
//  ElectricianRun
//  新能量介绍
//  Created by Start on 3/3/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STIntroductionViewController.h"

@interface STIntroductionViewController ()

@end

@implementation STIntroductionViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"新能量介绍";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

@end
