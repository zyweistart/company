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

@interface IAPHelper:NSObject<SKProductsRequestDelegate,SKPaymentTransactionObserver,HttpViewDelegate>{
    NSArray* _products;
    SKProductsRequest* _request;
    NSMutableArray* _productlist;
}

@property (retain) NSArray* products;
@property (retain) SKProductsRequest* request;
@property (retain) NSMutableArray* productlist;

@property (retain,nonatomic) UIViewController *controller;
@property (retain,nonatomic) NSString* recordno;

+ (IAPHelper *) sharedHelper;

- (void)requestProducts;
- (void)setProductList:(NSMutableArray*)productIdentifiers;
- (NSMutableDictionary*)getProductDetail:(NSString*)identifier;
- (void)buyProductIdentifier:(SKProduct*)product;
@end