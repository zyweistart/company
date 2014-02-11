//
//  STUserExperienceViewController.m
//  ElectricianRun
//  用户体验
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STUserExperienceViewController.h"

@interface STUserExperienceViewController ()

@end

@implementation STUserExperienceViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.view.backgroundColor=[UIColor whiteColor];
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
    }
    return self;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(10, 64, 150, 20)];
    [lbl1 setFont:[UIFont systemFontOfSize:12.0]];
    [lbl1 setText:@"当前负荷：1265.62kW"];
    [lbl1 setTextColor:[UIColor blackColor]];
    [self.view addSubview:lbl1];
    
    UILabel *lbl2=[[UILabel alloc]initWithFrame:CGRectMake(160, 64, 150, 20)];
    [lbl2 setFont:[UIFont systemFontOfSize:12.0]];
    [lbl2 setText:@"当前总电量：5kWh"];
    [lbl2 setTextColor:[UIColor blackColor]];
    [self.view addSubview:lbl2];
    
    UILabel *lbl3=[[UILabel alloc]initWithFrame:CGRectMake(30, 90, 80, 60)];
    [lbl3 setNumberOfLines:0];
    [lbl3 setFont:[UIFont systemFontOfSize:12.0]];
    [lbl3 setText:@"Ia=;\nIb=;\nIc=;"];
    [lbl3 setTextColor:[UIColor blackColor]];
    [self.view addSubview:lbl3];
    
    UIButton *btnF1=[[UIButton alloc]initWithFrame:CGRectMake(5,64, 100, 40)];
    btnF1.titleLabel.font=[UIFont systemFontOfSize: 10.0];
    [btnF1 setTitle:@"进线A" forState:UIControlStateNormal];
    [btnF1 setBackgroundColor:[UIColor purpleColor]];
    [btnF1 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnF1];
    
    UILabel *lbl4=[[UILabel alloc]initWithFrame:CGRectMake(150, 90, 100, 60)];
    [lbl4 setNumberOfLines:0];
    [lbl4 setFont:[UIFont systemFontOfSize:12.0]];
    [lbl4 setText:@"Ia=;\nIb=;\nIc=;"];
    [lbl4 setTextColor:[UIColor blackColor]];
    [self.view addSubview:lbl4];


//    UIButton *btnF2=[[UIButton alloc]initWithFrame:CGRectMake(5,109, 100, 40)];
//    btnF2.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF2 setTitle:@"出线A-1" forState:UIControlStateNormal];
//    [btnF2 setBackgroundColor:[UIColor purpleColor]];
//    [btnF2 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF2];
//    
//    UIButton *btnF3=[[UIButton alloc]initWithFrame:CGRectMake(5,154, 100, 40)];
//    btnF3.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF3 setTitle:@"出线A-2" forState:UIControlStateNormal];
//    [btnF3 setBackgroundColor:[UIColor purpleColor]];
//    [btnF3 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF3];
//    
//    UIButton *btnF4=[[UIButton alloc]initWithFrame:CGRectMake(5,209, 100, 40)];
//    btnF4.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF4 setTitle:@"出线A-3" forState:UIControlStateNormal];
//    [btnF4 setBackgroundColor:[UIColor purpleColor]];
//    [btnF4 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF4];
//    
//    
//    UIButton *btnF5=[[UIButton alloc]initWithFrame:CGRectMake(205,64, 100, 40)];
//    btnF5.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF5 setTitle:@"进线B" forState:UIControlStateNormal];
//    [btnF5 setBackgroundColor:[UIColor purpleColor]];
//    [btnF5 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF5];
//    
//    UIButton *btnF6=[[UIButton alloc]initWithFrame:CGRectMake(205,109, 100, 40)];
//    btnF6.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF6 setTitle:@"出线B-1" forState:UIControlStateNormal];
//    [btnF6 setBackgroundColor:[UIColor purpleColor]];
//    [btnF6 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF6];
//    
//    UIButton *btnF7=[[UIButton alloc]initWithFrame:CGRectMake(205,154, 100, 40)];
//    btnF7.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF7 setTitle:@"出线B-2" forState:UIControlStateNormal];
//    [btnF7 setBackgroundColor:[UIColor purpleColor]];
//    [btnF7 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF7];
//    
//    UIButton *btnF8=[[UIButton alloc]initWithFrame:CGRectMake(205,209, 100, 40)];
//    btnF8.titleLabel.font=[UIFont systemFontOfSize: 10.0];
//    [btnF8 setTitle:@"出线B-3" forState:UIControlStateNormal];
//    [btnF8 setBackgroundColor:[UIColor purpleColor]];
//    [btnF8 addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:btnF8];
    
}

- (void)onClick:(id)sender{
    
}

@end
