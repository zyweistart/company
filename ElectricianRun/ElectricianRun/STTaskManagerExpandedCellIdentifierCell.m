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
        _lblName=[[UILabel alloc]initWithFrame:CGRectMake(10, 5, 320, 30)];
        [_lblName setFont:[UIFont systemFontOfSize:15]];
        [_lblName setTextAlignment:NSTextAlignmentCenter];
        [_lblName setTextColor:[UIColor colorWithRed:(102/255.0) green:(102/255.0) blue:(102/255.0) alpha:1]];
        [_lblName setText:@"******功能按钮******"];
        [self addSubview:_lblName];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
