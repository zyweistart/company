#import <UIKit/UIKit.h>

@interface MyViewController : UIViewController

@property (nonatomic, strong) IBOutlet UIImageView *numberImage;

- (id)initWithPageNumber:(NSUInteger)page;

@end
