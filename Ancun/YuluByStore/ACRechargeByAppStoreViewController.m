//
//  ACAccountViewController.m
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//

#import "ACRechargeByAppStoreViewController.h"
#import "ACRechargeNav.h"
#import "ACAccountRechargeCell.h"
#import "DataSingleton.h"
#import "NSString+Date.h"
#import "IAPHelper.h"
#import "ACPaymentCell.h"

#ifdef  TEST
    #define PRODUCTRECORDNO_STRING @"1dc3838c3c2c9ce5a1fe54f9b6cf5bb9"
#else
    #define PRODUCTRECORDNO_STRING @"204ed4c44295be11e76887ea05a959ff"
#endif

#define CACHE_ACCOUNT_PAY1 CACHE_CONSTANT(@"CACHE_ACCOUNT_PAY1")
#define CACHE_ACCOUNT_PAY2 CACHE_CONSTANT(@"CACHE_ACCOUNT_PAY2")
#define CACHE_ACCOUNT_PAY3 CACHE_CONSTANT(@"CACHE_ACCOUNT_PAY3")

@interface ACRechargeByAppStoreViewController ()

//界面按钮事件
- (void)leftTopButtonAction;
- (void)centerTopButtonAction;
- (void)rightTopButtonAction;

@end

@implementation ACRechargeByAppStoreViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    
    if (self) {
        
        self.navigationItem.title=@"账户充值(AppStore)";
        
        _rechargeNav=[[ACRechargeNav alloc]initWithFrame:CGRectMake(0, 0, 320, 40)];
        [_rechargeNav firstStep];
        [self.view addSubview:_rechargeNav];
        
        _leftTopTab=[[UIButton alloc]initWithFrame:CGRectMake(0, 40, 106, 40)];
        [_leftTopTab.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_leftTopTab setTitle:@"基础包月套餐" forState:UIControlStateNormal];
        [_leftTopTab setBackgroundColor:[UIColor colorWithRed:(44/255.0) green:(140/255.0) blue:(207/255.0) alpha:1]];
        [self.view addSubview:_leftTopTab];
        
        _centerTopTab=[[UIButton alloc]initWithFrame:CGRectMake(107, 40, 106, 40)];
        [_centerTopTab.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_centerTopTab setTitle:@"增值时长套餐" forState:UIControlStateNormal];
        [_centerTopTab setBackgroundColor:[UIColor colorWithRed:(44/255.0) green:(140/255.0) blue:(207/255.0) alpha:1]];
        [self.view addSubview:_centerTopTab];
        
        _rightTopTab=[[UIButton alloc]initWithFrame:CGRectMake(214, 40, 106, 40)];
        [_rightTopTab.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_rightTopTab setTitle:@"增值存储套餐" forState:UIControlStateNormal];
        [_rightTopTab setBackgroundColor:[UIColor colorWithRed:(44/255.0) green:(140/255.0) blue:(207/255.0) alpha:1]];
        [self.view addSubview:_rightTopTab];
        
        _lblSlid=[[UILabel alloc]initWithFrame:CGRectMake(0, 76, 106, 4)];
        [_lblSlid setBackgroundColor:[UIColor colorWithRed:(76/255.0) green:(86/255.0) blue:(108/255.0) alpha:1]];
        [self.view addSubview:_lblSlid];
        
        _leftTopTab.showsTouchWhenHighlighted = YES;//指定按钮被按下时发光
        [_leftTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
        [_leftTopTab addTarget:self action:@selector(leftTopButtonAction) forControlEvents:UIControlEventTouchUpInside];
        
        _centerTopTab.showsTouchWhenHighlighted = YES;//指定按钮被按下时发光
        [_centerTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
        [_centerTopTab addTarget:self action:@selector(centerTopButtonAction) forControlEvents:UIControlEventTouchUpInside];
        
        _rightTopTab.showsTouchWhenHighlighted = YES;//指定按钮被按下时发光
        [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
        [_rightTopTab addTarget:self action:@selector(rightTopButtonAction) forControlEvents:UIControlEventTouchUpInside];
        
        self.tableView=[[UITableView alloc]initWithFrame:
                        CGRectMake(0, 83,
                                   self.view.frame.size.width,
                                   self.view.frame.size.height-83)];
        [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
        [self.view addSubview:self.tableView];
        
    }
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    
    //初始化数据
    currentTab=1;
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(productsLoaded:) name:kProductsLoadedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(productPurchased:) name:kProductPurchasedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector: @selector(productPurchaseFailed:) name:kProductPurchaseFailedNotification object: nil];

    //读取使用记录缓存信息
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        id content=[dictioanry objectForKey:CACHE_ACCOUNT_PAY1];
        if(content){
            _leftDataItemArray=[[XML analysis:content] dataItemArray];
            if([_leftDataItemArray count]>0){
                self.dataItemArray=_leftDataItemArray;
                [self.tableView reloadData];
            }
        }
        content=[dictioanry objectForKey:CACHE_ACCOUNT_PAY2];
        if(content){
            _centerDataItemArray=[[XML analysis:content] dataItemArray];
        }
        content=[dictioanry objectForKey:CACHE_ACCOUNT_PAY3];
        if(content){
            _rightDataItemArray=[[XML analysis:content] dataItemArray];
        }
    }
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [_leftTopTab sendActionsForControlEvents:UIControlEventTouchUpInside];
}

#pragma mark-
#pragma mark 界面按钮事件

- (void)leftTopButtonAction {
    currentTab=1;

    self.dataItemArray=_leftDataItemArray;
    if([self.dataItemArray count]>0) {
        [self loadAppStoreProduct:self.dataItemArray];
    } else {
        [self reloadTableViewDataSource];
    }
    
    [_leftTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_centerTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(0, 76, 106, 4);
    
    [UIView commitAnimations];
}

- (void)centerTopButtonAction {
    currentTab=2;
 
    self.dataItemArray=_centerDataItemArray;
    if([self.dataItemArray count]>0){
        [self loadAppStoreProduct:self.dataItemArray];
    }else{
        [self reloadTableViewDataSource];
    }
    
    [_centerTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_leftTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(107, 76, 106, 4);
    
    [UIView commitAnimations];
}

- (void)rightTopButtonAction {
    currentTab=3;
    
    self.dataItemArray=_rightDataItemArray;
    if([self.dataItemArray count]>0){
        [self loadAppStoreProduct:self.dataItemArray];
    }else{
        [self reloadTableViewDataSource];
    }
    
    [_rightTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_leftTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_centerTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(214, 76, 106, 4);
    
    [UIView commitAnimations];
}

#pragma mark -
#pragma mark Delegate Methods

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    [Common setCacheXmlByList:[response responseString] tag:[self returnCurrentTab]];
    if(currentTab == 1) {
        _leftDataItemArray=[response dataItemArray];
    } else if(currentTab == 2) {
        _centerDataItemArray=[response dataItemArray];
    } else if(currentTab == 3) {
        _rightDataItemArray=[response dataItemArray];
    }
    if ([[response dataItemArray] count]>0) {
        [self loadAppStoreProduct:[response dataItemArray]];
    } else {
        [self.tableView reloadData];
    }    
}

- (void)loadAppStoreProduct:(NSMutableArray *)dataItemArray {
    if ([dataItemArray count]>0) {
        NSMutableDictionary *dic=[[IAPHelper sharedHelper] productlistDic];
        if(dic==nil) {
            dic=[[NSMutableDictionary alloc]init];
        }
        [dic setObject:dataItemArray forKey:[self returnCurrentTab]];
        [[IAPHelper sharedHelper] setProductlistDic:dic];
        NSMutableArray *dataItema=[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]];
        if(dataItema==nil) {
            [[IAPHelper sharedHelper] requestProducts:[self returnCurrentTab]];
            _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
            [self performSelector:@selector(timeout:) withObject:nil afterDelay:30.0];
        } else {
            //通知产品列表已经加载完成
            [[NSNotificationCenter defaultCenter] postNotificationName:kProductsLoadedNotification object:dataItema];
        }
    }
}

- (NSString *)returnCurrentTab {
    if(currentTab == 2) {
        return CACHE_ACCOUNT_PAY2;
    } else if(currentTab == 3) {
        return CACHE_ACCOUNT_PAY3;
    }
    return CACHE_ACCOUNT_PAY1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    int count=[[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] count];
    return count+1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    int count=[[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] count];
    if(count>[indexPath row]){
        return 60;
    }else{
        return 50;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row=[indexPath row];
    int count=[[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] count];
    if(count>[indexPath row]){
        static NSString *cellReuseIdentifier = @"ACPaymentCell";
        ACPaymentCell *cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        if(!cell){
            UINib *nib=[UINib nibWithNibName:@"ACPaymentCell" bundle:nil];
            [tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
            cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        }
        SKProduct *product =[[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] objectAtIndex:row];
        cell.lbl_description.text=product.localizedDescription;
        return cell;
    } else {
        static NSString *cellReuseIdentifier = @"ACPaymentCellUITableViewCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
        if(!cell){
            cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellReuseIdentifier];
        }
        [cell.textLabel setTextAlignment:NSTextAlignmentCenter];
        [cell.textLabel setText:@"重新加载"];
        return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if([self.dataItemArray count]>[indexPath indexAtPosition:1]) {
        NSLog(@"购买了哦");
    } else {
        [self reloadTableViewDataSource];
    }
}

#pragma mark -
#pragma mark Custom Methods

- (void)reloadTableViewDataSource {
    NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
    if(currentTab == 1) {
        [requestParams setObject:@"3"  forKey:@"type"];
    } else if (currentTab == 2){
        [requestParams setObject:@"2"  forKey:@"type"];
    } else if (currentTab == 3){
        [requestParams setObject:@"1"  forKey:@"type"];
    }
    [requestParams setObject:PRODUCTRECORDNO_STRING  forKey:@"productrecordno"];
    [requestParams setObject:@"1"  forKey:@"status"];
    _loadHttp=[[HttpRequest alloc]init];
    [_loadHttp setDelegate:self];
    [_loadHttp setController:self];
    [_loadHttp loginhandle:@"v4QrycomboList" requestParams:requestParams];
}

//产品加载
- (void)productsLoaded:(NSNotification *)notification{
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    [self dismissHUD:nil];
    [self.tableView reloadData];
}

//购买产品
- (void)productPurchased:(NSNotification *)notification{
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    [self dismissHUD:nil];
    [Common alert:@"支付成功"];
}

//购买失败
- (void)productPurchaseFailed:(NSNotification *)notification {
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    [self dismissHUD:nil];
    SKPaymentTransaction * transaction = (SKPaymentTransaction *) notification.object;
    if (transaction.error.code != SKErrorPaymentCancelled) {
        [Common alert:transaction.error.localizedDescription];
    }
}

//超时提示
- (void)timeout:(id)arg {
    _hud.labelText = @"超时!";
    _hud.detailsLabelText = @"请稍候在试.";
    _hud.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Checkmark.png"]];
	_hud.mode = MBProgressHUDModeCustomView;
    [self performSelector:@selector(dismissHUD:) withObject:nil afterDelay:3.0];
}

- (void)dismissHUD:(id)arg{
    [MBProgressHUD hideHUDForView:self.navigationController.view animated:YES];
    _hud = nil;
}

@end