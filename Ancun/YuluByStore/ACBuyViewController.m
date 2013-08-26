//
//  ACBuyViewController.m
//  ACyulu
//
//  Created by Start on 13-4-8.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "ACBuyViewController.h"
#import "ACPaymentCell.h"
#import "IAPHelper.h"

@interface ACBuyViewController ()

@end

@implementation ACBuyViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.navigationItem.title=@"账户充值";
        
        self.tableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height) style:UITableViewStyleGrouped];
        [self.tableView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.tableView setDelegate:self];
        [self.tableView setDataSource:self];
        [self.view addSubview:self.tableView];
        
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(productsLoaded:) name:kProductsLoadedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(productPurchased:) name:kProductPurchasedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector: @selector(productPurchaseFailed:) name:kProductPurchaseFailedNotification object: nil];
    //加载产品列表
    NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
    [requestParams setObject:@"6" forKey:@"type"];
    [requestParams setObject:@"2" forKey:@"subtype"];
    [requestParams setObject:@"2" forKey:@"property"];
    HttpRequest *http=[[HttpRequest alloc]init];
    [http setDelegate:self];
    [http setController:self];
    [http setRequestCode:REQUESTCODE_BUY_LOADPRODUCT];
    [http handle:@"recprodGet" signKey:nil headParams:nil requestParams:requestParams];
}

#pragma mark -
#pragma mark Delegate Methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return [[IAPHelper sharedHelper].products count];
}

- (NSString*)tableView:(UITableView*)tableView titleForHeaderInSection:(NSInteger)section{
    SKProduct *product = [[IAPHelper sharedHelper].products objectAtIndex:section];
    NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
    [numberFormatter setFormatterBehavior:NSNumberFormatterBehavior10_4];
    [numberFormatter setNumberStyle:NSNumberFormatterCurrencyStyle];
    [numberFormatter setLocale:product.priceLocale];
    NSString *formattedString = [numberFormatter stringFromNumber:product.price];
    return [NSString stringWithFormat:@"%@ %@",product.localizedTitle,formattedString];
}

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section{
    return 1;
}

- (CGFloat)tableView:(UITableView*)tableView heightForRowAtIndexPath:(NSIndexPath*)indexPath{
    return 50;
}

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath {
    static NSString *cellReuseIdentifier = @"ACPaymentCell";
    ACPaymentCell *cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
    if(!cell){
        UINib *nib=[UINib nibWithNibName:@"ACPaymentCell" bundle:nil];
        [tableView registerNib:nib forCellReuseIdentifier:cellReuseIdentifier];
        cell = [tableView dequeueReusableCellWithIdentifier:cellReuseIdentifier];
    }
    SKProduct *product = [[IAPHelper sharedHelper].products objectAtIndex:indexPath.row];
    cell.lbl_description.text=product.localizedDescription;
    return cell;
}

- (void)tableView:(UITableView*)tableView didSelectRowAtIndexPath:(NSIndexPath*)indexPath{
    if([SKPaymentQueue canMakePayments]){
        SKProduct *product = [[IAPHelper sharedHelper].products objectAtIndex:indexPath.row];
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:[[[IAPHelper sharedHelper]productDetail:product.productIdentifier] objectForKey:@"recordno"] forKey:@"recprod"];
        HttpRequest *http=[[HttpRequest alloc]init];
        [http setDelegate:self];
        [http setController:[self parentViewController]];
        NSMutableDictionary* propertyes=[[NSMutableDictionary alloc]init];
        [propertyes setObject:product forKey:@"product"];
        [http setPropertys:propertyes];
        [http setRequestCode:REQUESTCODE_BUY_BUILD];
        [http loginhandle:@"v4phoneapppayReq" requestParams:requestParams];
    }
}

- (void)requestFinishedByResponse:(Response*)response requestCode:(int)reqCode{
    if([response successFlag]){
        if(reqCode==REQUESTCODE_BUY_LOADPRODUCT){
            [[IAPHelper sharedHelper]setProductlist:[response dataItemArray]];
            if ([IAPHelper sharedHelper].products == nil) {
                [[IAPHelper sharedHelper] requestProducts];
                _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
                [self performSelector:@selector(timeout:) withObject:nil afterDelay:30.0];
            }
        }else if(reqCode==REQUESTCODE_BUY_BUILD){
            SKProduct *product = [[response propertys] objectForKey:@"product"];
            [[IAPHelper sharedHelper] setController:self];
            [[IAPHelper sharedHelper] setRecordno:[[[response mainData] objectForKey:@"payinfo"] objectForKey:@"recordno"]];
            [[IAPHelper sharedHelper] buyProductIdentifier:product];
        }
    }
}

#pragma mark -
#pragma mark Custom Methods

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