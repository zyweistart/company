//
//  ACOldAccountDayCell.m
//  Ancun
//
//  Created by Start on 13-8-6.
//
//

#import "ACOldAccountDayCell.h"

@implementation ACOldAccountDayCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(25, 10, 50, 30)];
        [lbl1 setFont:[UIFont systemFontOfSize:17]];
        [lbl1 setTextColor:[UIColor redColor]];
        [lbl1 setText:@"05号"];
        [self addSubview:lbl1];
        
        lbl1=[[UILabel alloc]initWithFrame:CGRectMake(150, 10, 35, 30)];
        [lbl1 setFont:[UIFont systemFontOfSize:15]];
        [lbl1 setBackgroundColor:[UIColor blackColor]];
        [lbl1 setTextColor:[UIColor redColor]];
        [lbl1 setText:@"充值"];
        [self addSubview:lbl1];
        
        lbl1=[[UILabel alloc]initWithFrame:CGRectMake(215, 10, 80, 30)];
        [lbl1 setFont:[UIFont systemFontOfSize:15]];
        
        [lbl1 setTextColor:[UIColor redColor]];
        [lbl1 setText:@"+6012分钟"];
        [self addSubview:lbl1];
        
    }
    return self;
}

@end
