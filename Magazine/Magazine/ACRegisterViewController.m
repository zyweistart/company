#import "ACRegisterViewController.h"

@interface ACRegisterViewController ()
//介绍
@property (strong,nonatomic)UIView *setpView1;
//开通类型
@property (strong,nonatomic)UIView *setpView2;
//信息输入
@property (strong,nonatomic)UIView *setpView3;
//验证码
@property (strong,nonatomic)UIView *setpView4;

@end

@implementation ACRegisterViewController

- (id)init
{
    self = [super init];
    if (self) {
        self.title=@"注册";
    }
    return self;
}


- (void)goStep2:(id)sender
{
    
}

- (void)goStep3:(id)sender
{
    
}

- (void)goStep4:(id)sender
{
    
}

@end
