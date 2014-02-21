//
//  STTaskManagerExpandedCellIdentifierCell.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskManagerExpandedCellIdentifierCell.h"

@implementation STTaskManagerExpandedCellIdentifierCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        _btn1=[[UIButton alloc]initWithFrame:CGRectMake(10, 5, 200, 30)];
        _btn1.titleLabel.font=[UIFont systemFontOfSize:12];
        [_btn1 setTitle:@"站点电耗量信息" forState:UIControlStateNormal];
        [_btn1 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_btn1 addTarget:self action:@selector(btn1:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_btn1];
        
        _btn2=[[UIButton alloc]initWithFrame:CGRectMake(10, 40, 200, 30)];
        _btn2.titleLabel.font=[UIFont systemFontOfSize:12];
        [_btn2 setTitle:@"运行设备外观、温度检查" forState:UIControlStateNormal];
        [_btn2 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_btn2 addTarget:self action:@selector(btn2:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_btn2];
        
        _btn3=[[UIButton alloc]initWithFrame:CGRectMake(10, 75, 200, 30)];
        _btn3.titleLabel.font=[UIFont systemFontOfSize:12];
        [_btn3 setTitle:@"受总柜运行情况" forState:UIControlStateNormal];
        [_btn3 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_btn3 addTarget:self action:@selector(btn3:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_btn3];
        
        _btn4=[[UIButton alloc]initWithFrame:CGRectMake(10, 110, 200, 30)];
        _btn4.titleLabel.font=[UIFont systemFontOfSize:12];
        [_btn4 setTitle:@"TRMS系统巡视检查" forState:UIControlStateNormal];
        [_btn4 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [_btn4 addTarget:self action:@selector(btn4:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_btn4];
        
    }
    return self;
}

- (void)btn1:(id)sender{
    
}

- (void)btn2:(id)sender{
    
}

- (void)btn3:(id)sender{
    
}

- (void)btn4:(id)sender{
    
}

@end
