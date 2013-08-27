//
//  IAPHelper.h
//  ACyulu
//
//  Created by Start on 13-4-8.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "StoreKit/StoreKit.h"

#define kProductsLoadedNotification       @"ProductsLoaded"
#define kProductPurchasedNotification      @"ProductPurchased"
#define kProductPurchaseFailedNotification  @"ProductPurchaseFailed"

@interface IAPHelper:NSObject<SKProductsRequestDelegate,SKPaymentTransactionObserver,HttpViewDelegate>

@property (strong,nonatomic) NSString* recordno;
@property (strong,nonatomic) UIViewController *controller;

@property (strong,nonatomic) NSArray* products;
@property (strong,nonatomic) NSMutableArray* productlist;

@property (strong,nonatomic) SKProductsRequest* productsRequest;

+ (IAPHelper *)sharedHelper;

- (SKProduct *)product:(NSString *)identifier;
- (NSMutableDictionary *)productDetail:(NSString*)identifier;
- (void)requestProducts;
- (void)buyProductIdentifier:(SKProduct*)product;

@end