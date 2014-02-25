//
//  STAboutUsViewController.m
//  ElectricianRun
//  关于我们
//  Created by Start on 2/22/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STAboutUsViewController.h"

@interface STAboutUsViewController ()

@end

@implementation STAboutUsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"关于我们";
        [self.view setBackgroundColor:[UIColor whiteColor]];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
