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
#import "STMyeViewController.h"

#import "STDataMonitoringViewController.h"
#import "STAlarmManagerViewController.h"
#import "STTaskManagerViewController.h"
#import "STTaskAuditViewController.h"

#import "STNewsDetailViewController.h"
#import "STNewsListViewController.h"

#import "ETFoursquareImages.h"
#import "NSString+Utils.h"
#import "SQLiteOperate.h"
#import "DownloadIcon.h"

//#define IMAGEHEIGHT 90
#define IMAGEHEIGHT 180

@interface STIndexViewController () <UITabBarControllerDelegate>

@end

@implementation STIndexViewController{
    HttpRequest *_hRequest;
    
    DownloadIcon *downloadIcon;
    
    SQLiteOperate *db;
    
    NSDictionary *newData;
}

- (void)viewDidLoad {
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
    
    
    // 设置按钮内部图片间距
    UIEdgeInsets insets;
    insets.top = insets.bottom = insets.right = insets.left = 3;
    //用户体验
    UIButton *btnF1=[[UIButton alloc]initWithFrame:CGRectMake(5,5+IMAGEHEIGHT, 152.5, 100)];
    btnF1.titleLabel.font=[UIFont systemFontOfSize: 15.0];
    [btnF1 setTitle:@"用户体验" forState:UIControlStateNormal];
    [btnF1 setBackgroundColor:[UIColor colorWithRed:(72/255.0) green:(74/255.0) blue:(174/255.0) alpha:1]];
    [btnF1 addTarget:self action:@selector(onClickUserExperience:) forControlEvents:UIControlEventTouchUpInside];
    [btnF1 setContentVerticalAlignment:UIControlContentVerticalAlignmentBottom];
    [btnF1 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
    btnF1.contentEdgeInsets = insets;
    [foursquareImages.scrollView addSubview:btnF1];
    //我管辖的变电站
    UIButton *btnF2=[[UIButton alloc]initWithFrame:CGRectMake(162.5,5+IMAGEHEIGHT, 152.5, 100)];
    btnF2.titleLabel.font=[UIFont systemFontOfSize: 15.0];
    [btnF2 setTitle:@"我管辖的变电站" forState:UIControlStateNormal];
    [btnF2 setBackgroundColor:[UIColor colorWithRed:(9/255.0) green:(104/255.0) blue:(220/255.0) alpha:1]];
    [btnF2 addTarget:self action:@selector(onClickJurisdiction:) forControlEvents:UIControlEventTouchUpInside];
    [btnF2 setContentVerticalAlignment:UIControlContentVerticalAlignmentBottom];
    [btnF2 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
    btnF2.contentEdgeInsets = insets;
    [foursquareImages.scrollView addSubview:btnF2];
    //工程建站
    UIButton *btnF4=[[UIButton alloc]initWithFrame:CGRectMake(5,110+IMAGEHEIGHT, 100, 100)];
    btnF4.titleLabel.font=[UIFont systemFontOfSize: 15.0];
    [btnF4 setTitle:@"工程建站" forState:UIControlStateNormal];
    [btnF4 setBackgroundColor:[UIColor colorWithRed:(215/255.0) green:(131/255.0) blue:(7/255.0) alpha:1]];
    [btnF4 addTarget:self action:@selector(onClickSite:) forControlEvents:UIControlEventTouchUpInside];
    [btnF4 setContentVerticalAlignment:UIControlContentVerticalAlignmentBottom];
    [btnF4 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
    btnF4.contentEdgeInsets = insets;
    [foursquareImages.scrollView addSubview:btnF4];
    //扫描操作
    UIButton *btnF5=[[UIButton alloc]initWithFrame:CGRectMake(110,110+IMAGEHEIGHT, 100, 100)];
    btnF5.titleLabel.font=[UIFont systemFontOfSize: 15.0];
    [btnF5 setTitle:@"扫描操作" forState:UIControlStateNormal];
    [btnF5 setBackgroundColor:[UIColor colorWithRed:(170/255.0) green:(44/255.0) blue:(1/255.0) alpha:1]];
    [btnF5 addTarget:self action:@selector(onClickOperating:) forControlEvents:UIControlEventTouchUpInside];
    [btnF5 setContentVerticalAlignment:UIControlContentVerticalAlignmentBottom];
    [btnF5 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
    btnF5.contentEdgeInsets = insets;
    [foursquareImages.scrollView addSubview:btnF5];
    //在线计算
    UIButton *btnF6=[[UIButton alloc]initWithFrame:CGRectMake(215,110+IMAGEHEIGHT, 100, 100)];
    btnF6.titleLabel.font=[UIFont systemFontOfSize: 15.0];
    [btnF6 setTitle:@"在线计算" forState:UIControlStateNormal];
    [btnF6 setBackgroundColor:[UIColor colorWithRed:(1/255.0) green:(75/255.0) blue:(164/255.0) alpha:1]];
    [btnF6 addTarget:self action:@selector(onClickCalculation:) forControlEvents:UIControlEventTouchUpInside];
    [btnF6 setContentVerticalAlignment:UIControlContentVerticalAlignmentBottom];
    [btnF6 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
    btnF6.contentEdgeInsets = insets;
    [foursquareImages.scrollView addSubview:btnF6];
    
    UIView *newView=[[UIView alloc]initWithFrame:CGRectMake(5, 215+IMAGEHEIGHT, 310, 100)];
    [newView setUserInteractionEnabled:YES];
    [newView setBackgroundColor:[UIColor colorWithRed:(208/255.0) green:(206/255.0) blue:(193/255.0) alpha:1]];
    [newView addGestureRecognizer:[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onClickNewList:)]];
    
    UIImageView *img=[[UIImageView alloc]initWithFrame:CGRectMake(5, 15, 80, 60)];
    [newView addSubview:img];
    
    UILabel *lblTitle=[[UILabel alloc]initWithFrame:CGRectMake(90, 7, 215, 15)];
    [lblTitle setFont:[UIFont systemFontOfSize:12]];
    [lblTitle setTextColor:[UIColor colorWithRed:(102/255.0) green:(102/255.0) blue:(102/255.0) alpha:1]];
    [lblTitle setTextAlignment:NSTextAlignmentLeft];
    [newView addSubview:lblTitle];
    
    UILabel *lblContent=[[UILabel alloc]initWithFrame:CGRectMake(95, 25, 210, 55)];
    [lblContent setFont:[UIFont systemFontOfSize:10]];
    [lblContent setTextColor:[UIColor colorWithRed:(102/255.0) green:(102/255.0) blue:(102/255.0) alpha:1]];
    [lblContent setTextAlignment:NSTextAlignmentLeft];
    [lblContent setNumberOfLines:0];
    [newView addSubview:lblContent];
    
    [foursquareImages.scrollView addSubview:newView];
    
    foursquareImages.scrollView.contentSize = CGSizeMake(320, 95+80+IMAGEHEIGHT);
    
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
            
//            NSString *url=[data objectForKey:@"url"];
            NSString *icon_name=[newData objectForKey:@"icon_name"];
//            NSString *file_name=[data objectForKey:@"file_name"];
            
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

//用户体验
- (void)onClickUserExperience:(id)sender {
    UINavigationController *userExperienceSelectViewController = [[UINavigationController alloc] initWithRootViewController:[[STUserExperienceSelectViewController alloc]init]];
    [self presentViewController:userExperienceSelectViewController animated:YES completion:nil];
}

//我管辖的变电站
- (void)onClickJurisdiction:(id)sender {
    //数据监测
     UINavigationController *dtaMonitoringViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STDataMonitoringViewController alloc]init]];
//    dtaMonitoringViewControllerNav.navigationBarHidden=YES;
    dtaMonitoringViewControllerNav.tabBarItem.title=@"数据监测";
    //报警管理
    UINavigationController *alarmManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STAlarmManagerViewController alloc]init]];
    alarmManagerViewControllerNav.tabBarItem.title=@"报警管理";
    //任务管理
    UINavigationController *taskManagerViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STTaskManagerViewController alloc]init]];
    taskManagerViewControllerNav.title=@"任务管理";
    taskManagerViewControllerNav.tabBarItem.title=@"任务管理";
    //任务稽核
    UINavigationController *taskAuditViewControllerNav = [[UINavigationController alloc] initWithRootViewController:[[STTaskAuditViewController alloc]init]];
    taskAuditViewControllerNav.title=@"任务稽核";
    taskAuditViewControllerNav.tabBarItem.title=@"任务稽核";
    
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
