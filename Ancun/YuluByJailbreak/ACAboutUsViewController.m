//
//  ACAboutUsViewController.m
//  ACyulu
//
//  Created by Start on 13-1-11.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "ACAboutUsViewController.h"

@interface ACAboutUsViewController ()

@end

@implementation ACAboutUsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    if(iPhone5){
        nibNameOrNil=@"ACAboutUsViewController@iPhone5";
    }else{
        nibNameOrNil=@"ACAboutUsViewController";
    }
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.navigationItem.title=@"关于我们";
    }
    return self;
}

- (void)viewDidAppear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewStartWithName:@"ACAboutUsViewController"];
}

- (void)viewDidDisappear:(BOOL)animated{
    [[BaiduMobStat defaultStat] pageviewEndWithName:@"ACAboutUsViewController"];
}

@end
