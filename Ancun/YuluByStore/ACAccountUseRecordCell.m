//
//  ACAccountUseRecordCell.m
//  Ancun
//
//  Created by Start on 13-8-6.
//
//

#import "ACAccountUseRecordCell.h"

@implementation ACAccountUseRecordCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        _lblDate=[[UILabel alloc]initWithFrame:CGRectMake(5, 5, 80, 60)];
        [_lblDate setFont:[UIFont systemFontOfSize:15]];
        [_lblDate setTextAlignment:NSTextAlignmentLeft];
        [_lblDate setLineBreakMode:UILineBreakModeCharacterWrap];
        [_lblDate setNumberOfLines:0];
        [self addSubview:_lblDate];
        
        _lblContent=[[UILabel alloc]initWithFrame:CGRectMake(85, 5, 160, 60)];
        [_lblContent setFont:[UIFont systemFontOfSize:15]];
        [_lblContent setTextAlignment:NSTextAlignmentCenter];
        [_lblContent setLineBreakMode:UILineBreakModeCharacterWrap];
        [_lblContent setNumberOfLines:0];
        [self addSubview:_lblContent];
        
        _lblRemark=[[UILabel alloc]initWithFrame:CGRectMake(245, 5, 70, 60)];
        [_lblRemark setFont:[UIFont systemFontOfSize:15]];
        [_lblRemark setTextAlignment:NSTextAlignmentRight];
        [_lblRemark setLineBreakMode:UILineBreakModeCharacterWrap];
        [_lblRemark setNumberOfLines:0];
        [_lblRemark setTextColor:[UIColor redColor]];
        [self addSubview:_lblRemark];
        
        [self setSelectionStyle:UITableViewCellSelectionStyleNone];
        
    }
    return self;
}
@end
