//
//  IAPHelper.h
//  ACyulu
//
//  Created by Start on 13-4-8.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "StoreKit/StoreKit.h"

//加载完成
#define kProductsLoadedNotification       @"ProductsLoaded"
//支付成功
#define kProductPurchasedNotification      @"ProductPurchased"
//支付失败
#define kProductPurchaseFailedNotification  @"ProductPurchaseFailed"

@interface IAPHelper:NSObject<SKProductsRequestDelegate,SKPaymentTransactionObserver,HttpViewDelegate>

@property (strong,nonatomic) NSString *recordno;
@property (strong,nonatomic) UIViewController *controller;

//@property (strong,nonatomic) NSMutableArray *products;
//@property (strong,nonatomic) NSMutableArray *productlist;

@property (strong,nonatomic) NSMutableDictionary *productsDic;
@property (strong,nonatomic) NSMutableDictionary *productlistDic;

+ (IAPHelper *)sharedHelper;

- (void)requestProducts:(NSString *)tag;
- (NSMutableDictionary*)getProductDetail:(NSString *)identifier tag:(NSString *)tag;
- (void)buyProductIdentifier:(SKProduct*)product;

@end