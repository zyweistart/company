//
//  ACLoginViewController.m
//  ACyulu
//
//  Created by Start on 12-12-5.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "ACLoginViewController.h"
#import "ACDialViewController.h"
#import "ACContactViewController.h"
#import "ACAccountViewController.h"
#import "ACOldAccountViewController.h"
#import "ACRecordingManagerViewController.h"
#import "ACMoreViewController.h"
#import "ACRegisterViewController.h"
#import "ACForgetPwdViewController.h"

@interface ACLoginViewController ()

- (void)autoLogin;

@end

@implementation ACLoginViewController{
    int m_lastTabIndex;
    SSCheckBoxView *_checkbox;
    HttpRequest *_loginHttp;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    if(iPhone5){
        nibNameOrNil=@"ACLoginViewController@iPhone5";
    }else{
        nibNameOrNil=@"ACLoginViewController";
    }
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        if(iPhone5){
            if(![Common getCacheByBool:DEFAULTDATA_FIRSTLOGIN]){
                _checkbox = [[SSCheckBoxView alloc] initWithFrame:CGRectMake(20, 304, 100, 30)
                                                            style:kSSCheckBoxViewStyleGlossy
                                                          checked:YES];
            }else{
                _checkbox = [[SSCheckBoxView alloc] initWithFrame:CGRectMake(20, 304, 100, 30)
                                                            style:kSSCheckBoxViewStyleGlossy
                                                          checked:[Common getCacheByBool:DEFAULTDATA_AUTOLOGIN]];
            }
        }else{
            if(![Common getCacheByBool:DEFAULTDATA_FIRSTLOGIN]){
                _checkbox = [[SSCheckBoxView alloc] initWithFrame:CGRectMake(20, 259, 100, 30)
                                                            style:kSSCheckBoxViewStyleGlossy
                                                          checked:YES];
            }else{
                _checkbox = [[SSCheckBoxView alloc] initWithFrame:CGRectMake(20, 259, 100, 30)
                                                            style:kSSCheckBoxViewStyleGlossy
                                                          checked:[Common getCacheByBool:DEFAULTDATA_AUTOLOGIN]];
            }
        }
        [_checkbox setText:@""];
        [self.view addSubview:_checkbox];
        UIImage *bgImg = [UIImage imageNamed:@"login_gb"];
        UIColor *color = [[UIColor alloc] initWithPatternImage:bgImg];
        [self.view setBackgroundColor:color];
        //设置登录代理
        [[Config Instance] setLoginResultDelegate:self];
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    [self autoLogin];
}

#pragma mark -
#pragma mark Delegate Methods

- (void)requestFinishedByResponse:(Response*)response requestCode:(int)reqCode{
    if([response successFlag]){
        NSString *phone=_txtPhone.text;
        [[Config Instance] setIsLogin:YES];
        [[Config Instance] setIsCalculateTotal:YES];
        [[Config Instance] setUserInfo:[[response mainData] objectForKey:@"v4info"]];
        [[Config Instance] setCacheKey:[NSString stringWithFormat:@"cache_%@",phone]];
        if(![Common getCacheByBool:DEFAULTDATA_FIRSTLOGIN]){
            //TODO:第一次登录
        }
        [Common setCacheByBool:DEFAULTDATA_FIRSTLOGIN data:YES];
        [Common setCache:DEFAULTDATA_PHONE data:phone];
        if([Common getCacheByBool:DEFAULTDATA_AUTOLOGIN]){
            [Common setCache:DEFAULTDATA_PASSWORD data:_txtPassword.text];
        }else{
            [Common setCache:DEFAULTDATA_PASSWORD data:@""];
        }
        //拔号盘
        UINavigationController *dialViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACDialViewController alloc]init]];
        //是否隐藏导航条
        dialViewControllerNav.navigationBarHidden = YES;
        //联系人
        UINavigationController *contactViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACContactViewController alloc]init]];
        contactViewControllerNav.navigationBar.tintColor=NAVCOLOR;
        //我的账户
        UINavigationController *accountViewControllerNav;
        if([[Config Instance]isOldUser]) {
            accountViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACOldAccountViewController alloc]init]];
        } else {
            accountViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACAccountViewController alloc]init]];
        }
        accountViewControllerNav.navigationBar.tintColor=NAVCOLOR;
        //录音管理
        UINavigationController *recordingManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACRecordingManagerViewController alloc]init]];
        recordingManagerViewControllerNav.navigationBar.tintColor=NAVCOLOR;
        //更多
        UINavigationController *moreViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACMoreViewController alloc]init]];
        moreViewControllerNav.navigationBar.tintColor=NAVCOLOR;
        
        //添加标签控制器
        UITabBarController *_tabBarController = [[UITabBarController alloc] init];
        _tabBarController.delegate = self;
        _tabBarController.viewControllers = [NSArray arrayWithObjects:
                                             dialViewControllerNav,
                                             contactViewControllerNav,
                                             accountViewControllerNav,
                                             recordingManagerViewControllerNav,
                                             moreViewControllerNav,
                                             nil];
        [self presentViewController:_tabBarController animated:YES completion:nil];
    }else if([[response code] isEqualToString:@"120020"]){
        //用户不存在
        [Common alert:@"用户名或密码不正确"];
    }
}

- (void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSMutableArray*)result{
    [Config initData];
    if(resultCode==RESULTCODE_ACLoginViewController_1){
        //登录
        [_txtPassword setText:@""];
        [Common setCache:DEFAULTDATA_PASSWORD data:@""];
    }else if(resultCode==RESULTCODE_ACLoginViewController_2){
        //重新登录
        [_txtPhone setText:@""];
        [_txtPassword setText:@""];
        [Common setCache:DEFAULTDATA_PHONE data:@""];
        [Common setCache:DEFAULTDATA_PASSWORD data:@""];
    }else if(resultCode==RESULTCODE_ACLoginViewController_3){
        //自动登陆
        [self autoLogin];
    }
}

- (void)tabBarController:(UITabBarController*)tabBarController didSelectViewController:(UIViewController*)viewController{
    int newTabIndex = tabBarController.selectedIndex;
    if (newTabIndex == m_lastTabIndex) {
        [[NSNotificationCenter defaultCenter] postNotificationName:Notification_TabClick_ACRecordingManagerViewController object:@"load"];
    }else{
        m_lastTabIndex = newTabIndex;
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)autoLogin{
    if([Common getCache:DEFAULTDATA_PHONE]&&![[Common getCache:DEFAULTDATA_PHONE] isEqualToString:@""]){
        [_txtPhone setText:[Common getCache:DEFAULTDATA_PHONE]];
        if([Common getCache:DEFAULTDATA_PASSWORD]&&![[Common getCache:DEFAULTDATA_PASSWORD] isEqualToString:@""]){
            [_txtPassword setText:[Common getCache:DEFAULTDATA_PASSWORD]];
            if((BOOL)[Common getCacheByBool:DEFAULTDATA_AUTOLOGIN]){
                //延时0.5秒后执行防止：unbalanced calls to begin/end appearance transitions for uiviewcontroller异常
                [self performSelector:@selector(ibLogin:) withObject:self afterDelay:0];
            }
        }
    }
}

//登陆
- (IBAction)ibLogin:(id)sender{
    
    [Common setCache:DEFAULTDATA_PHONE data:@""];
    [Common setCache:DEFAULTDATA_PASSWORD data:@""];
    [Common setCacheByBool:DEFAULTDATA_AUTOLOGIN data:[_checkbox checked]];
    
    NSString *phone=_txtPhone.text;
    NSString *password=_txtPassword.text;
    if([phone isEqualToString:@""]){
        [Common notificationMessage:@"账号不能为空" inView:self.view];
    }else if([password isEqualToString:@""]){
        [Common notificationMessage:@"密码不能为空" inView:self.view];
    }else{
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:_txtPhone.text forKey:@"username"];
        [requestParams setObject:@"c" forKey:@"loginsource"];
        [requestParams setObject:@"" forKey:@"mac"];
        [requestParams setObject:@"" forKey:@"ip"];
        [requestParams setObject:@"1" forKey:@"raflag"];
        _loginHttp=[[HttpRequest alloc]init];
        [_loginHttp setDelegate:self];
        [_loginHttp setController:self];
        [_loginHttp handle:@"v4Login" signKey:[_txtPassword.text md5] requestParams:requestParams];
    }
}

//注册
- (IBAction)ibRegister:(id)sender{
    ACRegisterViewController *registerViewController=[[ACRegisterViewController alloc]init];
    UINavigationController *registerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:registerViewController];
    registerViewControllerNav.navigationBar.tintColor=NAVCOLOR;
    [self presentViewController:registerViewControllerNav animated:YES completion:nil];
}

//忘记密码
- (IBAction)ibForgetPwd:(id)sender{
    ACForgetPwdViewController *forgetPwdViewController=[[ACForgetPwdViewController alloc]init];
    UINavigationController *forgetPwdViewControllerNav = [[UINavigationController alloc] initWithRootViewController:forgetPwdViewController];
    forgetPwdViewControllerNav.navigationBar.tintColor=NAVCOLOR;
    [self presentViewController:forgetPwdViewControllerNav animated:YES completion:nil];
}

- (IBAction)backgroundDoneEditing:(id)sender{
    [_txtPhone resignFirstResponder];
    [_txtPassword resignFirstResponder];
}

@end