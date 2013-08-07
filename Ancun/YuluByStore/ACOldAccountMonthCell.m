//
//  ACOldAccountMonthCell.m
//  Ancun
//
//  Created by Start on 13-8-6.
//
//

#import "ACOldAccountMonthCell.h"

@implementation ACOldAccountMonthCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        _lblMonth=[[UILabel alloc]initWithFrame:CGRectMake(5, 5, 65, 50)];
        [_lblMonth setFont:[UIFont systemFontOfSize:30]];
        [_lblMonth setTextColor:[UIColor grayColor]];
        [_lblMonth setText:@"08月"];
        [self addSubview:_lblMonth];
        
        UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(70, 0, 1, 65)];
        [lbl1 setBackgroundColor:[UIColor grayColor]];
        [self addSubview:lbl1];
        
        UILabel *lbl2=[[UILabel alloc]initWithFrame:CGRectMake(90, 5, 90, 25)];
        [lbl2 setFont:[UIFont systemFontOfSize:15]];
        [lbl1 setBackgroundColor:[UIColor grayColor]];
        [lbl2 setText:@"充值总时长"];
        [self addSubview:lbl2];
        
        _lblPayTime=[[UILabel alloc]initWithFrame:CGRectMake(90, 35, 90, 25)];
        [_lblPayTime setFont:[UIFont systemFontOfSize:15]];
        [_lblPayTime setTextColor:[UIColor blueColor]];
        [_lblPayTime setText:@"6017分钟"];
        [self addSubview:_lblPayTime];
        
        UILabel *lbl3=[[UILabel alloc]initWithFrame:CGRectMake(200, 5, 90, 25)];
        [lbl3 setFont:[UIFont systemFontOfSize:15]];
        [lbl1 setBackgroundColor:[UIColor grayColor]];
        [lbl3 setText:@"使用总时长"];
        [self addSubview:lbl3];
        
        _lblUseTime=[[UILabel alloc]initWithFrame:CGRectMake(200, 35, 90, 25)];
        [_lblUseTime setFont:[UIFont systemFontOfSize:15]];
        [_lblUseTime setTextColor:[UIColor redColor]];
        [_lblUseTime setText:@"60分钟"];
        [self addSubview:_lblUseTime];
        
        [self setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    }
    return self;
}

@end