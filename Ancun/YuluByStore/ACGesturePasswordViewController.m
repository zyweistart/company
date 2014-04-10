//
//  ACGesturePasswordViewController.m
//  Ancun
//
//  Created by Start on 4/10/14.
//
//

#import "ACGesturePasswordViewController.h"
#import "ACLoginViewController.h"
#import "ACDialsViewController.h"
#import "ACContactsViewController.h"
#import "ACAccountViewController.h"
#import "ACRecordingManagerViewController.h"
#import "ACMoreViewController.h"

@interface ACGesturePasswordViewController () 

@end

@implementation ACGesturePasswordViewController{
    int errorCount;
    
    unsigned long m_lastTabIndex;
    
}

- (id)init
{
    self = [super init];
    if (self) {
        self.view.backgroundColor = [UIColor whiteColor];
        self.lockView=[[KKGestureLockView alloc]initWithFrame:self.view.bounds];
        [self.view addSubview:self.lockView];
        self.lockView.normalGestureNodeImage = [UIImage imageNamed:@"gesture_node_normal"];
        self.lockView.selectedGestureNodeImage = [UIImage imageNamed:@"gesture_node_selected"];
        self.lockView.lineColor = [[UIColor orangeColor] colorWithAlphaComponent:0.3];
        self.lockView.lineWidth = 12;
        self.lockView.delegate = self;
        self.lockView.contentInsets = UIEdgeInsetsMake(150, 20, 100, 20);
        errorCount=0;
    }
    return self;
}

- (void)gestureLockView:(KKGestureLockView *)gestureLockView didBeginWithPasscode:(NSString *)passcode{
}

- (void)gestureLockView:(KKGestureLockView *)gestureLockView didEndWithPasscode:(NSString *)passcode{
    NSLog(@"%@",passcode);
    if([passcode length]>6){
        NSString *value=[Common getCache:DEFAULTDATA_GESTUREPWD];
        if([passcode isEqualToString:value]){
            [self dismissViewControllerAnimated:YES completion:nil];
            return;
        }
    }
    errorCount++;
    if(errorCount>2){
        [Common alert:@"超过限制请重新登录"];
        //清除账户
        [Common setCache:DEFAULTDATA_PHONE data:@""];
        //清除登录密码
        [Common setCache:DEFAULTDATA_PASSWORD data:@""];
        [self goLoginPage];
    }else{
        [Common alert:@"手势密码出错，请重试!"];
    }
}

- (void)requestFinishedByResponse:(Response*)response requestCode:(int)reqCode{
    if([response successFlag]){
        NSString *phone=[Common getCache:DEFAULTDATA_PHONE];
        [[Config Instance] setIsLogin:YES];
        [[Config Instance] setIsCalculateTotal:YES];
        [[Config Instance] setUserInfo:[[NSMutableDictionary alloc]initWithDictionary:[[response mainData] objectForKey:@"v4info"]]];
        //企业版用户无法登录
        if([@"2" isEqualToString:[[[Config Instance]userInfo]objectForKey:@"usertype"]]) {
            [Common alert:@"您的号码属于政企用户，目前尚不能使用APP登录，如需通话录音可直接拨打95105856"];
            //清除账户
            [Common setCache:DEFAULTDATA_PHONE data:@""];
            //清除登录密码
            [Common setCache:DEFAULTDATA_PASSWORD data:@""];
            [self goLoginPage];
            return;
        }
        [[Config Instance] setCacheKey:[NSString stringWithFormat:@"cache_%@",phone]];
        
        //拔号盘
        ACDialsViewController *dialViewController = [[ACDialsViewController alloc]init];
        dialViewController.tabBarItem.title = @"拨号盘";
        [[dialViewController tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"nav_icon_dial_hover"] withFinishedUnselectedImage:[UIImage imageNamed:@"nav_icon_dial"]];
        [[dialViewController tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                 dictionaryWithObjectsAndKeys: [UIColor whiteColor],
                                                                 UITextAttributeTextColor, nil] forState:UIControlStateNormal];
        [[dialViewController tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                 dictionaryWithObjectsAndKeys: TABNORMALBGCOLOR,
                                                                 UITextAttributeTextColor, nil] forState:UIControlStateSelected];
        //联系人
        UINavigationController *contactViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACContactsViewController alloc]init]];
        contactViewControllerNav.tabBarItem.title = @"通讯录";
        [[contactViewControllerNav tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"nav_icon_contact_hover"] withFinishedUnselectedImage:[UIImage imageNamed:@"nav_icon_contact"]];
        [[contactViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                       dictionaryWithObjectsAndKeys: [UIColor whiteColor],
                                                                       UITextAttributeTextColor, nil] forState:UIControlStateNormal];
        [[contactViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                       dictionaryWithObjectsAndKeys: TABNORMALBGCOLOR,
                                                                       UITextAttributeTextColor, nil] forState:UIControlStateSelected];
        if(IOS7){
            [[contactViewControllerNav navigationBar]setBarTintColor:MAINBG];
            [[contactViewControllerNav navigationBar]setBarStyle:UIBarStyleBlackTranslucent];
        }else{
            [contactViewControllerNav.navigationBar setBackgroundImage:[UIImage imageNamed:@"navigationbg"] forBarMetrics:UIBarMetricsDefault];
        }
        
        //我的账户
        UINavigationController *accountViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACAccountViewController alloc]init]];
        accountViewControllerNav.tabBarItem.title = @"我的账户";
        [[accountViewControllerNav tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"nav_icon_account_hover"] withFinishedUnselectedImage:[UIImage imageNamed:@"nav_icon_account"]];
        [[accountViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                       dictionaryWithObjectsAndKeys: [UIColor whiteColor],
                                                                       UITextAttributeTextColor, nil] forState:UIControlStateNormal];
        [[accountViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                       dictionaryWithObjectsAndKeys: TABNORMALBGCOLOR,
                                                                       UITextAttributeTextColor, nil] forState:UIControlStateSelected];
        if(IOS7){
            [[accountViewControllerNav navigationBar]setBarTintColor:MAINBG];
            [[accountViewControllerNav navigationBar]setBarStyle:UIBarStyleBlackTranslucent];
        }else{
            [accountViewControllerNav.navigationBar setBackgroundImage:[UIImage imageNamed:@"navigationbg"] forBarMetrics:UIBarMetricsDefault];
        }
        
        //录音管理
        UINavigationController *recordingManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACRecordingManagerViewController alloc]init]];
        recordingManagerViewControllerNav.tabBarItem.title = @"我的录音";
        [[recordingManagerViewControllerNav tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"nav_icon_recording_hover"] withFinishedUnselectedImage:[UIImage imageNamed:@"nav_icon_recording"]];
        [[recordingManagerViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                                dictionaryWithObjectsAndKeys: [UIColor whiteColor],
                                                                                UITextAttributeTextColor, nil] forState:UIControlStateNormal];
        [[recordingManagerViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                                dictionaryWithObjectsAndKeys: TABNORMALBGCOLOR,
                                                                                UITextAttributeTextColor, nil] forState:UIControlStateSelected];
        if(IOS7){
            [[recordingManagerViewControllerNav navigationBar]setBarTintColor:MAINBG];
            [[recordingManagerViewControllerNav navigationBar]setBarStyle:UIBarStyleBlackTranslucent];
        }else{
            [recordingManagerViewControllerNav.navigationBar setBackgroundImage:[UIImage imageNamed:@"navigationbg"] forBarMetrics:UIBarMetricsDefault];
        }
        //更多
        UINavigationController *moreViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[ACMoreViewController alloc]init]];
        moreViewControllerNav.tabBarItem.title = @"更多";
        [[moreViewControllerNav tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"nav_icon_more_hover"] withFinishedUnselectedImage:[UIImage imageNamed:@"nav_icon_more"]];
        [[moreViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                    dictionaryWithObjectsAndKeys: [UIColor whiteColor],
                                                                    UITextAttributeTextColor, nil] forState:UIControlStateNormal];
        [[moreViewControllerNav tabBarItem] setTitleTextAttributes:[NSDictionary
                                                                    dictionaryWithObjectsAndKeys: TABNORMALBGCOLOR,
                                                                    UITextAttributeTextColor, nil] forState:UIControlStateSelected];
        if(IOS7){
            [[moreViewControllerNav navigationBar]setBarTintColor:MAINBG];
            [[moreViewControllerNav navigationBar]setBarStyle:UIBarStyleBlackTranslucent];
        }else{
            [moreViewControllerNav.navigationBar setBackgroundImage:[UIImage imageNamed:@"navigationbg"] forBarMetrics:UIBarMetricsDefault];
        }
        
        //添加标签控制器
        UITabBarController *_tabBarController = [[UITabBarController alloc] init];
        [_tabBarController.view setBackgroundColor:MAINBG];
        if([[[UIDevice currentDevice] systemVersion]floatValue]>=6){
            [[_tabBarController tabBar] setShadowImage:[[UIImage alloc] init]];
        }
        [[_tabBarController tabBar] setBackgroundImage:[[UIImage alloc] init]];
        _tabBarController.delegate = self;
        _tabBarController.viewControllers = [NSArray arrayWithObjects:
                                             dialViewController,
                                             contactViewControllerNav,
                                             accountViewControllerNav,
                                             recordingManagerViewControllerNav,
                                             moreViewControllerNav,
                                             nil];
        
        [self presentViewController:_tabBarController animated:YES completion:nil];
        
    }else if([[response code] isEqualToString:@"120020"]){
        //用户不存在
        [Common alert:@"用户名或密码不正确"];
        [Common setCache:DEFAULTDATA_PASSWORD data:@""];
        [self goLoginPage];
    }
    if(![response successFlag]){
        //执行失败则清空
        [Common setCache:DEFAULTDATA_PHONE data:@""];
        [Common setCache:DEFAULTDATA_PASSWORD data:@""];
        [self goLoginPage];
    }
}

- (void)requestFailed:(int)reqCode {
    //执行失败则清空密码
    [Common setCache:DEFAULTDATA_PASSWORD data:@""];
    [self goLoginPage];
}

- (void)tabBarController:(UITabBarController*)tabBarController didSelectViewController:(UIViewController*)viewController {
    unsigned long newTabIndex = tabBarController.selectedIndex;
    if (newTabIndex == m_lastTabIndex) {
        [[NSNotificationCenter defaultCenter] postNotificationName:Notification_TabClick_ACRecordingManagerViewController object:@"load"];
    } else {
        m_lastTabIndex = newTabIndex;
    }
}

- (void)goLoginPage
{
    //清除手势密码
//        [Common setCache:DEFAULTDATA_GESTUREPWD data:@""];
    ACLoginViewController *loginViewController=[[ACLoginViewController alloc]init];
    [self presentViewController:loginViewController animated:YES completion:nil];
}

@end
