#import "MyViewController.h"
#import "ACLoginViewController.h"

@interface MyViewController () {
    int pageNumber;
}

@end

@implementation MyViewController

- (id)initWithPageNumber:(NSUInteger)page {
    if (self = [super init]) {
        pageNumber = page;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.numberImage=[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 320, iPhone5?528:480)];
    [self.view addSubview:self.numberImage];
    if(pageNumber==4){
        int left=self.view.frame.size.width;
        int top=self.view.frame.size.height;
        UIButton *btn=[UIButton buttonWithType:UIButtonTypeCustom];
        [btn setFrame:CGRectMake(left/2-139, top/2, 278, 46)];
        [btn setImage:[UIImage imageNamed:@"extraction_button_2"] forState:UIControlStateNormal];
        [btn setBackgroundImage:[UIImage imageNamed:@"extraction_button_3"] forState:UIControlStateHighlighted];
        [btn addTarget:self action:@selector(welcomeOnClick:) forControlEvents:UIControlEventTouchDown];
        [self.view addSubview:btn];
    }
}

- (void)welcomeOnClick:(id)sender {
    [UIApplication sharedApplication].keyWindow.rootViewController=[[ACLoginViewController alloc]init];
}

@end