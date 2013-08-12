//
//  ACOldAccountDayCell.m
//  Ancun
//
//  Created by Start on 13-8-6.
//
//

#import "ACOldAccountDayCell.h"

@implementation ACOldAccountDayCell
//高为60
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        _lblMonth=[[UILabel alloc]initWithFrame:CGRectMake(40, 5, 50, 25)];
        [_lblMonth setFont:[UIFont systemFontOfSize:20]];
        [_lblMonth setTextAlignment:NSTextAlignmentCenter];
        [_lblMonth setTextColor:[UIColor grayColor]];
        [self addSubview:_lblMonth];
        
        _lblTime=[[UILabel alloc]initWithFrame:CGRectMake(40, 30, 50, 25)];
        [_lblTime setFont:[UIFont systemFontOfSize:15]];
        [_lblTime setTextAlignment:NSTextAlignmentCenter];
        [_lblTime setTextColor:[UIColor grayColor]];
        [self addSubview:_lblTime];
        
        _lblRemark=[[UILabel alloc]initWithFrame:CGRectMake(150, 10, 35, 30)];
        [_lblRemark setFont:[UIFont systemFontOfSize:15]];
        [_lblRemark setTextColor:[UIColor redColor]];
        [self addSubview:_lblRemark];
        
        _lblValue=[[UILabel alloc]initWithFrame:CGRectMake(215, 10, 80, 30)];
        [_lblValue setFont:[UIFont systemFontOfSize:15]];
        [_lblValue setTextColor:[UIColor redColor]];
        [_lblValue setTextAlignment:NSTextAlignmentCenter];
        [self addSubview:_lblValue];
        
        [self setSelectionStyle:UITableViewCellSelectionStyleNone];
    }
    return self;
}

@end
