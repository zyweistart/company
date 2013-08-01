#import "MyViewController.h"
#import "ACLoginViewController.h"

@interface MyViewController () {
    int pageNumber;
}

@end

@implementation MyViewController

- (id)initWithPageNumber:(NSUInteger)page {
    if (self = [super initWithNibName:@"MyView" bundle:nil]) {
        pageNumber = page;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    if(pageNumber==3){
        int left=self.view.frame.size.width;
        int top=self.view.frame.size.height;
        UIButton *btn=[UIButton buttonWithType:UIButtonTypeCustom];
        [btn setFrame:CGRectMake(left/2-139, top/2+(top/2-46*2), 278, 46)];
        [btn setImage:[UIImage imageNamed:@"extraction_button_2"] forState:UIControlStateNormal];
        [btn setBackgroundImage:[UIImage imageNamed:@"extraction_button_3"] forState:UIControlStateHighlighted];
        [btn addTarget:self action:@selector(welcomeOnClick:) forControlEvents:UIControlEventTouchDown];
        [self.view addSubview:btn];
    }
}

- (void) welcomeOnClick:(id)sender{
    [self presentViewController:[[ACLoginViewController alloc]init] animated:YES completion:nil];
}

@end