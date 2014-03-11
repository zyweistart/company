//
//  STIndexViewController.m
//  ElectricianRun
//  首页
//  Created by Start on 1/24/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STIndexViewController.h"

#import "STUserExperienceSelectViewController.h"
#import "STProjectSiteViewController.h"
#import "STScanningOperationViewController.h"
#import "STCalculateViewController.h"
#import "STDataMonitoringViewController.h"
#import "STAlarmManagerViewController.h"
#import "STTaskManagerViewController.h"
#import "STTaskAuditViewController.h"
#import "STNewsListViewController.h"

#import "ETFoursquareImages.h"
#import "SQLiteOperate.h"

@interface STIndexViewController () <UITabBarControllerDelegate>

@end

@implementation STIndexViewController{
    HttpRequest *_hRequest;
    
    NSDictionary *newData;
    
    SQLiteOperate *db;
    UIImageView *img;
    UILabel *lblTitle;
    UILabel *lblContent;
}

- (void)viewDidLoad {
    
    int IMAGEHEIGHT=200;
    
    [super viewDidLoad];
    ETFoursquareImages *foursquareImages = [[ETFoursquareImages alloc] initWithFrame:CGRectMake(0, 0, 320, self.view.frame.size.height-0)];
    [foursquareImages setImagesHeight:IMAGEHEIGHT];
    
    NSArray *images  = [NSArray arrayWithObjects:
                        [UIImage imageNamed:@"image1"],
                        [UIImage imageNamed:@"image2"],
                        [UIImage imageNamed:@"image3"],
                        [UIImage imageNamed:@"image4"], nil];
    
    [foursquareImages setImages:images];
    [foursquareImages setBackgroundColor:[UIColor redColor]];
    
    [self.view addSubview:foursquareImages];
    
    self.automaticallyAdjustsScrollViewInsets=NO;
    UIScrollView *scroll=[[UIScrollView alloc]initWithFrame:CGRectMake(0, IMAGEHEIGHT, 320, inch4?320:250)];
    [scroll setBackgroundColor:[UIColor whiteColor]];
    scroll.contentSize = CGSizeMake(320,320);
    [scroll setScrollEnabled:YES];
    [foursquareImages.scrollView addSubview:scroll];
    
    //用户体验
    UIButton *btnF1=[[UIButton alloc]initWithFrame:CGRectMake(5,5, 152.5, 100)];
    [btnF1 setBackgroundImage:[UIImage imageNamed:@"yh1"] forState:UIControlStateNormal];
    [btnF1 addTarget:self action:@selector(onClickUserExperience:) forControlEvents:UIControlEventTouchUpInside];
    [scroll addSubview:btnF1];
    //我管辖的变电站
    UIButton *btnF2=[[UIButton alloc]initWithFrame:CGRectMake(162.5,5, 152.5, 100)];
    [btnF2 setBackgroundImage:[UIImage imageNamed:@"bdz1"] forState:UIControlStateNormal];
    [btnF2 addTarget:self action:@selector(onClickJurisdiction:) forControlEvents:UIControlEventTouchUpInside];
    [scroll addSubview:btnF2];
    //工程建站
    UIButton *btnF4=[[UIButton alloc]initWithFrame:CGRectMake(5,110, 100, 100)];
    [btnF4 setBackgroundImage:[UIImage imageNamed:@"gcjz"] forState:UIControlStateNormal];
    [btnF4 addTarget:self action:@selector(onClickSite:) forControlEvents:UIControlEventTouchUpInside];
    [scroll addSubview:btnF4];
    //扫描操作
    UIButton *btnF5=[[UIButton alloc]initWithFrame:CGRectMake(110,110, 100, 100)];
    [btnF5 setBackgroundImage:[UIImage imageNamed:@"sm"] forState:UIControlStateNormal];
    [btnF5 addTarget:self action:@selector(onClickOperating:) forControlEvents:UIControlEventTouchUpInside];
    [scroll addSubview:btnF5];
    //在线计算
    UIButton *btnF6=[[UIButton alloc]initWithFrame:CGRectMake(215,110, 100, 100)];
    [btnF6 setBackgroundImage:[UIImage imageNamed:@"zxjs"] forState:UIControlStateNormal];
    [btnF6 addTarget:self action:@selector(onClickCalculation:) forControlEvents:UIControlEventTouchUpInside];
    [scroll addSubview:btnF6];
    
    UIView *newView=[[UIView alloc]initWithFrame:CGRectMake(5, 215, 310, 100)];
    [newView setUserInteractionEnabled:YES];
    [newView setBackgroundColor:[UIColor colorWithRed:(208/255.0) green:(206/255.0) blue:(193/255.0) alpha:1]];
    [newView addGestureRecognizer:[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onClickNewList:)]];
    
    img=[[UIImageView alloc]initWithFrame:CGRectMake(5, 15, 80, 60)];
    [newView addSubview:img];
    lblTitle=[[UILabel alloc]initWithFrame:CGRectMake(90, 7, 215, 15)];
    [lblTitle setFont:[UIFont systemFontOfSize:12]];
    [lblTitle setTextColor:[UIColor colorWithRed:(102/255.0) green:(102/255.0) blue:(102/255.0) alpha:1]];
    [lblTitle setTextAlignment:NSTextAlignmentLeft];
    [newView addSubview:lblTitle];
    lblContent=[[UILabel alloc]initWithFrame:CGRectMake(95, 25, 210, 55)];
    [lblContent setFont:[UIFont systemFontOfSize:10]];
    [lblContent setTextColor:[UIColor colorWithRed:(102/255.0) green:(102/255.0) blue:(102/255.0) alpha:1]];
    [lblContent setTextAlignment:NSTextAlignmentLeft];
    [lblContent setNumberOfLines:0];
    [newView addSubview:lblContent];
    
    [scroll addSubview:newView];
    
    foursquareImages.scrollView.contentSize = CGSizeMake(320, 320+IMAGEHEIGHT);
    
    [foursquareImages.pageControl setCurrentPageIndicatorTintColor:[UIColor colorWithRed:(28/255.f) green:(189/255.f) blue:(141/255.f) alpha:1.0]];
    db=[[SQLiteOperate alloc]init];
    if([db openDB]){
        NSString *sqlQuery = @"SELECT * FROM NEW";
        NSMutableArray *indata=[db query:sqlQuery];
        if(indata!=nil&&[indata count]>0){
            int r=arc4random()%[indata count];
            newData=[indata objectAtIndex:r];
            [lblTitle setText:[newData objectForKey:@"name"]];
            [lblContent setText:[newData objectForKey:@"content"]];
            NSString *icon_name=[newData objectForKey:@"icon_name"];
            //创建文件管理器
            NSFileManager* fileManager = [NSFileManager defaultManager];
            //获取Documents主目录
            NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
            //得到相应的Documents的路径
            NSString* docDir = [paths objectAtIndex:0];
            //更改到待操作的目录下
            [fileManager changeCurrentDirectoryPath:[docDir stringByExpandingTildeInPath]];
            NSString *path = [docDir stringByAppendingPathComponent:icon_name];
            //如果图标文件已经存在则进行显示否则进行下载
            if([fileManager fileExistsAtPath:path]){
                [img setImage:[UIImage imageWithContentsOfFile:path]];
            }
        }
    }
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

//用户体验
- (void)onClickUserExperience:(id)sender {
    UINavigationController *userExperienceSelectViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceSelectViewController alloc]init]];
    [self presentViewController:userExperienceSelectViewControllerNav animated:YES completion:nil];
}

//我管辖的变电站
- (void)onClickJurisdiction:(id)sender {
    //数据监测
     UINavigationController *dtaMonitoringViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STDataMonitoringViewController alloc]init]];
//    dtaMonitoringViewControllerNav.navigationBarHidden=YES;
    dtaMonitoringViewControllerNav.tabBarItem.title=@"数据监测";
    dtaMonitoringViewControllerNav.tabBarItem.image=[UIImage imageNamed:@"sj"];
    //报警管理
    UINavigationController *alarmManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STAlarmManagerViewController alloc]init]];
    alarmManagerViewControllerNav.tabBarItem.title=@"报警管理";
    alarmManagerViewControllerNav.tabBarItem.image=[UIImage imageNamed:@"bj"];
    //任务管理
    UINavigationController *taskManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STTaskManagerViewController alloc]init]];
    taskManagerViewControllerNav.title=@"任务管理";
    taskManagerViewControllerNav.tabBarItem.title=@"任务管理";
    taskManagerViewControllerNav.tabBarItem.image=[UIImage imageNamed:@"gl"];
    //任务稽核
    UINavigationController *taskAuditViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STTaskAuditViewController alloc]init]];
    taskAuditViewControllerNav.title=@"任务稽核";
    taskAuditViewControllerNav.tabBarItem.title=@"任务稽核";
    taskAuditViewControllerNav.tabBarItem.image=[UIImage imageNamed:@"rw"];
    
    UITabBarController *_tabBarController = [[UITabBarController alloc] init];
    [_tabBarController.view setBackgroundColor:[UIColor whiteColor]];
    _tabBarController.delegate = self;
    _tabBarController.viewControllers = [NSArray arrayWithObjects:
                                             dtaMonitoringViewControllerNav,
                                             alarmManagerViewControllerNav,
                                             taskManagerViewControllerNav,
                                             taskAuditViewControllerNav,nil];
    [self presentViewController:_tabBarController animated:YES completion:nil];
}

//工程建站
- (void)onClickSite:(id)sender {
    UINavigationController *projectSiteViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STProjectSiteViewController alloc]init]];
    [self presentViewController:projectSiteViewControllerNav animated:YES completion:nil];
}

//扫描操作
- (void)onClickOperating:(id)sender {
    UINavigationController *scanningOperationViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STScanningOperationViewController alloc]init]];
    [self presentViewController:scanningOperationViewControllerNav animated:YES completion:nil];
}

//在线计算
- (void)onClickCalculation:(id)sender {
    UINavigationController *calculateViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STCalculateViewController alloc]init]];
    [self presentViewController:calculateViewControllerNav animated:YES completion:nil];
}

//新闻详细
- (void)onClickNewList:(id)sender {
    UINavigationController *newsDetailViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STNewsListViewController alloc]initWithData:newData]];
    [self presentViewController:newsDetailViewControllerNav animated:YES completion:nil];
}

@end
