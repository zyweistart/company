#import "ACLoginViewController.h"

@interface ACLoginViewController ()

@end

@implementation ACLoginViewController

- (id)init
{
    self = [super init];
    if (self) {
        UIControl *control=[[UIControl alloc]initWithFrame:self.view.bounds];
        [self.view addSubview:control];
        
        UITextField *txtUserName=[[UITextField alloc]initWithFrame:CGRectMake(0, 0, 200, 50)];
        [control addSubview:txtUserName];
        UITextField *txtPassword=[[UITextField alloc]initWithFrame:CGRectMake(0, 80, 200, 50)];
        [control addSubview:txtPassword];
        
    }
    return self;
}

@end
