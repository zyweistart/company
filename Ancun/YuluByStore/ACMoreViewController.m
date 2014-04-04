#import "ACMoreViewController.h"
#import "ACNavigationWebPageViewController.h"
#import "ACAboutUsViewController.h"
#import "ACFeedBackViewController.h"
#import "ACModifyPwdViewController.h"
#import "FileUtils.h"

@interface ACMoreViewController () <UIActionSheetDelegate>

@end

@implementation ACMoreViewController{
    UILabel *lblCachName;
    long long cachesize;
}

- (id)init{
    self = [super init];
    if(self){
        self.title=@"更多";
        UIScrollView *container=nil;
        if(IOS7){
            container=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height-STATUSHEIGHT-TOPNAVIGATIONHEIGHT-BOTTOMTABBARHEIGHT)];
        }else{
            container=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height-TOPNAVIGATIONHEIGHT-BOTTOMTABBARHEIGHT)];
        }
        [container setContentSize:CGSizeMake(self.view.frame.size.width, 442)];
        [container setScrollEnabled:YES];
        [self.view setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"morebg"]]];
        [self.view addSubview:container];
        
        NSArray *names=[[NSArray alloc]initWithObjects:@"小贴士",@"意见反馈",@"关于我们",@"修改密码",@"正在计算缓存大小",@"重新登录", nil];
        
        for(int i=0;i<6;i++){
            UIButton *btnBg=[[UIButton alloc]initWithFrame:CGRectMake(15.75, 10+i*69.5+1*i,288.5,69.5)];
            
            UIImageView *icon=[[UIImageView alloc]initWithImage:[UIImage imageNamed:[NSString stringWithFormat:@"more_icon_%d",i+1]]];
            [icon setFrame:CGRectMake(20, 21, 27.5, 27.5)];
            [btnBg addSubview:icon];
            if(i==4){
                lblCachName=[[UILabel alloc]initWithFrame:CGRectMake(60, 21, 200, 27.5)];
                [lblCachName setText:[names objectAtIndex:i]];
                [lblCachName setFont:[UIFont systemFontOfSize:22]];
                [lblCachName setTextColor:[UIColor whiteColor]];
                [lblCachName setBackgroundColor:[UIColor clearColor]];
                [btnBg addSubview:lblCachName];
            }else{
                UILabel *lbl=[[UILabel alloc]initWithFrame:CGRectMake(60, 21, 200, 27.5)];
                [lbl setText:[names objectAtIndex:i]];
                [lbl setFont:[UIFont systemFontOfSize:22]];
                [lbl setTextColor:[UIColor whiteColor]];
                [lbl setBackgroundColor:[UIColor clearColor]];
                [btnBg addSubview:lbl];
            }
            
            [btnBg setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
            [btnBg setBackgroundImage:[UIImage imageNamed:[NSString stringWithFormat:@"more%d",i+1]] forState:UIControlStateNormal];
            [btnBg setTag:i];
            [btnBg addTarget:self action:@selector(onClickAction:) forControlEvents:UIControlEventTouchUpInside];
            
            [container addSubview:btnBg];
        }
        
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if([[Config Instance]isCalculateTotal]){
        [NSThread detachNewThreadSelector:@selector(calculateTotal) toTarget:self withObject:nil];
    }
}

- (void)actionSheet:(UIActionSheet*)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(actionSheet.tag==1){
        if(buttonIndex==0){
            [Common resultLoginViewController:self resultCode:RESULTCODE_ACLoginViewController_2 requestCode:0 data:nil];
        }
    }else if(actionSheet.tag==2){
        if(buttonIndex==0){
            [FileUtils removeCacheFile];
//            [Common setCache:[Config Instance].cacheKey data:nil];
            cachesize=0;
            [Common alert:@"清除完成"];
            [NSThread detachNewThreadSelector:@selector(calculateTotal) toTarget:self withObject:nil];
        }
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)onClickAction:(UIButton *)sender
{
    ACNavigationWebPageViewController *navigationWebPageViewController=[[ACNavigationWebPageViewController alloc]initWithNavigationTitle:@"小贴士" resourcePath:@"TipContent"];
    ACFeedBackViewController *feedBackViewController=[[ACFeedBackViewController alloc]init];
    ACAboutUsViewController *aboutUsViewController=[[ACAboutUsViewController alloc]init];
    ACModifyPwdViewController *modifyPwdViewController=[[ACModifyPwdViewController alloc]init];
    switch(sender.tag){
        case 0:
            //小贴士
            navigationWebPageViewController.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:navigationWebPageViewController animated:YES];
            break;
        case 1:
            //意见反馈
            feedBackViewController.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:feedBackViewController animated:YES];
            break;
        case 2:
            //关于我们
            aboutUsViewController.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:aboutUsViewController animated:YES];
            break;
        case 3:
            //修改密码
            modifyPwdViewController.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:modifyPwdViewController animated:YES];
            break;
        case 4:
            //清理缓存
            if([[Config Instance]isCalculateTotal]){
                [Common alert:@"正在计算缓存大小"];
            }else{
                if(cachesize>0){
                    [Common actionSheet:self message:@"确定要清除所有缓存文件吗？" tag:2];
                }else{
                    [Common alert:@"当前的缓存文件为0KB，无须清除"];
                }
            }
            break;
        case 5:
            //重新登录
            [Common actionSheet:self message:@"确定要重新登录吗？" tag:1];
            break;
    }
}

//计算空间大小
- (void)calculateTotal{
    NSString *cacheName=nil;
    cachesize=[FileUtils getCacheSize];
    if(cachesize/1024/1024>1024){//GB
        float totla=(float)cachesize/1024/1024;
        cacheName=[NSString stringWithFormat:@"%.2fGB",totla/1024];
    }else if(cachesize/1024>1024){//MB
        float totla=(float)cachesize/1024;
        cacheName=[NSString stringWithFormat:@"%.2fMB",totla/1024];
    }else{//KB
        cacheName=[NSString stringWithFormat:@"%lldKB",cachesize/1024];
    }
    [lblCachName setText:[NSString stringWithFormat:@"缓存大小:%@",cacheName]];
    [[Config Instance]setIsCalculateTotal:NO];
}

@end
