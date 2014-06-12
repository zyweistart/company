#import "BaseViewController.h"

@interface ACPeriodicalDataContentViewController : BaseViewController

- (id)initWithData:(NSDictionary *)data;

- (void)loadData;

@property (strong,nonatomic)NSDictionary *data;

@end
