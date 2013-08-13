//
//  ACRechargeNav.m
//  Ancun
//
//  Created by Start on 13-8-13.
//
//

#import "ACRechargeNav.h"

@implementation ACRechargeNav

//CGRectMake(x,y,320,40);
- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _lblTip1=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 40)];
        [_lblTip1 setFont:[UIFont systemFontOfSize:15]];
        [_lblTip1 setTextAlignment:NSTextAlignmentCenter];
        [_lblTip1 setBackgroundColor:[UIColor redColor]];
        [_lblTip1 setTextColor:[UIColor grayColor]];
        [_lblTip1 setText:@"选择套餐"];
        [self addSubview:_lblTip1];
        
        _lblTip2=[[UILabel alloc]initWithFrame:CGRectMake(80, 0, 80, 40)];
        [_lblTip2 setFont:[UIFont systemFontOfSize:15]];
        [_lblTip2 setTextAlignment:NSTextAlignmentCenter];
        [_lblTip2 setBackgroundColor:[UIColor redColor]];
        [_lblTip2 setTextColor:[UIColor grayColor]];
        [_lblTip2 setText:@"确认信息"];
        [self addSubview:_lblTip2];
        
        _lblTip3=[[UILabel alloc]initWithFrame:CGRectMake(160, 0, 80, 40)];
        [_lblTip3 setFont:[UIFont systemFontOfSize:15]];
        [_lblTip3 setTextAlignment:NSTextAlignmentCenter];
        [_lblTip3 setTextColor:[UIColor grayColor]];
        [_lblTip3 setText:@"支付"];
        [self addSubview:_lblTip3];
        
        _lblTip4=[[UILabel alloc]initWithFrame:CGRectMake(240, 0, 80, 40)];
        [_lblTip4 setFont:[UIFont systemFontOfSize:15]];
        [_lblTip4 setTextAlignment:NSTextAlignmentCenter];
        [_lblTip4 setTextColor:[UIColor grayColor]];
        [_lblTip4 setText:@"成功"];
        [self addSubview:_lblTip4];
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
