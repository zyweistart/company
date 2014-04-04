#import "ACMoreCell.h"

@implementation ACMoreCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setBackgroundView:[[UIView alloc] init]];
        [self setBackgroundColor:[UIColor clearColor]];
        UIView *container=[[UIView alloc]initWithFrame:CGRectMake(15.75, 0, 288.5, 69.5)];
        [container setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"more1"]]];
        [self addSubview:container];
        
        _imgView=[[UIImageView alloc]initWithFrame:CGRectMake(20, 9, 30, 30)];
        [container addSubview:_imgView];
        _lblName=[[UILabel alloc]initWithFrame:CGRectMake(58, 13, 242, 21)];
        [_lblName setFont:[UIFont systemFontOfSize:18]];
        [_lblName setTextAlignment:NSTextAlignmentLeft];
        [_lblName setBackgroundColor:[UIColor clearColor]];
        [_lblName setTextColor:[UIColor colorWithRed:(102/255.0) green:(102/255.0) blue:(102/255.0) alpha:1]];
        [container addSubview:_lblName];
    }
    return self;
}

@end
