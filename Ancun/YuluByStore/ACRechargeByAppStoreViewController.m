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
        
        _leftDataItemArray=[[NSMutableArray alloc]init];
        [_leftDataItemArray addObject:@"com_ancun_luyin_rns60"];
        _centerDataItemArray=[[NSMutableArray alloc]init];
        _rightDataItemArray=[[NSMutableArray alloc]init];
        
        self.navigationItem.title=@"账户充值(AppStore)";
        
        _leftTopTab=[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 106, 40)];
        [_leftTopTab.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_leftTopTab setTitle:@"基础包月套餐" forState:UIControlStateNormal];
        [_leftTopTab setBackgroundColor:[UIColor colorWithRed:(44/255.0) green:(140/255.0) blue:(207/255.0) alpha:1]];
        [self.view addSubview:_leftTopTab];
        
        _centerTopTab=[[UIButton alloc]initWithFrame:CGRectMake(107, 0, 106, 40)];
        [_centerTopTab.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_centerTopTab setTitle:@"增值时长套餐" forState:UIControlStateNormal];
        [_centerTopTab setBackgroundColor:[UIColor colorWithRed:(44/255.0) green:(140/255.0) blue:(207/255.0) alpha:1]];
        [self.view addSubview:_centerTopTab];
        
        _rightTopTab=[[UIButton alloc]initWithFrame:CGRectMake(214, 0, 106, 40)];
        [_rightTopTab.titleLabel setFont:[UIFont systemFontOfSize:15]];
        [_rightTopTab setTitle:@"增值存储套餐" forState:UIControlStateNormal];
        [_rightTopTab setBackgroundColor:[UIColor colorWithRed:(44/255.0) green:(140/255.0) blue:(207/255.0) alpha:1]];
        [self.view addSubview:_rightTopTab];
        
        _lblSlid=[[UILabel alloc]initWithFrame:CGRectMake(0, 36, 106, 4)];
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
                        CGRectMake(0, 40,
                                   self.view.frame.size.width,
                                   self.view.frame.size.height-40)];
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
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [_leftTopTab sendActionsForControlEvents:UIControlEventTouchUpInside];
}

#pragma mark-
#pragma mark 界面按钮事件

- (void)leftTopButtonAction {
    currentTab=1;
    
    [self loadAppStoreProduct:_leftDataItemArray];
    
    [_leftTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_centerTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(0, 36, 106, 4);
    
    [UIView commitAnimations];
}

- (void)centerTopButtonAction {
    currentTab=2;
 
    [self loadAppStoreProduct:_centerDataItemArray];
    
    [_centerTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_leftTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_rightTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(107, 36, 106, 4);
    
    [UIView commitAnimations];
}

- (void)rightTopButtonAction {
    currentTab=3;
    
    [self loadAppStoreProduct:_rightDataItemArray];
    
    [_rightTopTab setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];//此时选中
    [_leftTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    [_centerTopTab setTitleColor:[UIColor colorWithRed:(220/255.0) green:(220/255.0) blue:(220/255.0) alpha:1] forState:UIControlStateNormal];//此时未被选中
    
    [UIView beginAnimations:nil context:nil];//动画开始
    [UIView setAnimationDuration:0.3];
    
    _lblSlid.frame = CGRectMake(214, 36, 106, 4);
    
    [UIView commitAnimations];
}

#pragma mark -
#pragma mark Delegate Methods

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if([response successFlag]){
        if(reqCode==REQUESTCODE_BUY_LOADPRODUCT){
//            [[IAPHelper sharedHelper] setProductlist:[response dataItemArray]];
//            if ([IAPHelper sharedHelper].products == nil) {
//                [[IAPHelper sharedHelper] requestProducts];
//                _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
//                [self performSelector:@selector(timeout:) withObject:nil afterDelay:30.0];
//            }
            NSLog(@"充值产品获取");
        }else if(reqCode==REQUESTCODE_BUY_BUILD){
            SKProduct *product = [[response propertys] objectForKey:@"product"];
            [[IAPHelper sharedHelper] setController:self];
            [[IAPHelper sharedHelper] setRecordno:[[[response mainData] objectForKey:@"payinfo"] objectForKey:@"recordno"]];
            [[IAPHelper sharedHelper] buyProductIdentifier:product];
        }
    }
}

- (void)loadAppStoreProduct:(NSMutableArray *)dataItemArray {
//    if ([dataItemArray count]>0) {
//        NSMutableDictionary *dic=[[IAPHelper sharedHelper] productlistDic];
//        if(dic==nil) {
//            dic=[[NSMutableDictionary alloc]init];
//        }
//        [dic setObject:dataItemArray forKey:[self returnCurrentTab]];
//        [[IAPHelper sharedHelper] setProductlistDic:dic];
//        NSMutableArray *dataItema=[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]];
//        if(dataItema==nil) {
//            [[IAPHelper sharedHelper] requestProducts:[self returnCurrentTab]];
//            _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
//            [self performSelector:@selector(timeout:) withObject:nil afterDelay:30.0];
//        } else {
//            //通知产品列表已经加载完成
//            [[NSNotificationCenter defaultCenter] postNotificationName:kProductsLoadedNotification object:dataItema];
//        }
//    } else {
//        //通知产品列表已经加载完成
//        [[NSNotificationCenter defaultCenter] postNotificationName:kProductsLoadedNotification object:nil];
//    }
}

- (NSString *)returnCurrentTab {
    if(currentTab == 2) {
        return CACHE_ACCOUNT_PAY2;
    } else if(currentTab == 3) {
        return CACHE_ACCOUNT_PAY3;
    }
    return CACHE_ACCOUNT_PAY1;
}

//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
//    return [[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] count];
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
//    return 60;
//}
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//    NSInteger row=[indexPath row];
//    static NSString *cellReuseIdentifier = @"ACPaymentCell";
//    ACPaymentCell *cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
//    if(!cell){
//        UINib *nib=[UINib nibWithNibName:@"ACPaymentCell" bundle:nil];
//        [tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
//        cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
//    }
////    SKProduct *product =[[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] objectAtIndex:row];
////    cell.lbl_description.text=product.localizedDescription;
////    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
//    return cell;
//}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if([SKPaymentQueue canMakePayments]) {
//        SKProduct *product = [[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] objectAtIndex:[indexPath row]];
//        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
//        NSString *recprod=[[[IAPHelper sharedHelper]getProductDetail:product.productIdentifier tag:[self returnCurrentTab]] objectForKey:@"recordno"];
//        [requestParams setObject:recprod forKey:@"recprod"];
//        _loadHttp=[[HttpRequest alloc]init];
//        [_loadHttp setDelegate:self];
//        [_loadHttp setController:[self parentViewController]];
//        NSMutableDictionary* propertyes=[[NSMutableDictionary alloc]init];
//        [propertyes setObject:product forKey:@"product"];
//        [_loadHttp setPropertys:propertyes];
//        [_loadHttp setRequestCode:REQUESTCODE_BUY_BUILD];
//        [_loadHttp loginhandle:@"v4phoneapppayReq" requestParams:requestParams];
    }
}

#pragma mark -
#pragma mark Custom Methods

//产品加载
- (void)productsLoaded:(NSNotification *)notification{
    [NSObject cancelPreviousPerformRequestsWithTarget:self];
    [self dismissHUD:nil];
//    [self.tableView reloadData];
//    if([[[[IAPHelper sharedHelper] productsDic]objectForKey:[self returnCurrentTab]] count]==0) {
//        [Common alert:@"暂无记录"];
//    }
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