//
//  IAPHelper.m
//  ACyulu
//
//  Created by Start on 13-4-8.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "IAPHelper.h"

@implementation IAPHelper

static IAPHelper * _sharedHelper;

+ (IAPHelper *)sharedHelper {
    if (_sharedHelper == nil) {
        _sharedHelper = [[IAPHelper alloc] init];
    }
    return _sharedHelper;
}

//根据唯一标识获取数据的详细集合
- (NSMutableDictionary *)productDetail:(NSString *)identifier {
    if(![@"" isEqualToString:identifier]) {
        for (NSMutableDictionary * product in self.productlist){
            if([identifier isEqualToString:[product objectForKey:@"appstorerecordno"]]){
                return product;
            }
        }
    }
    return nil;
}

- (SKProduct *)product:(NSString *)identifier {
    for(SKProduct *product in self.products) {
        if( [identifier isEqualToString:product.productIdentifier]) {
            return product;
        }
    }
    return nil;
}

#pragma mark 请求加载AppStore中的产品信息
- (void)requestProducts {
    if(self.productsRequest==nil) {
        NSMutableSet * products = [NSMutableSet set];
        for (NSMutableDictionary * product in self.productlist) {
            NSString *productId=[product objectForKey:@"appstorerecordno"];
            if(![@"" isEqualToString:productId]) {
                [products addObject:productId];
            }
        }
        self.productsRequest = [[SKProductsRequest alloc] initWithProductIdentifiers:products];
        self.productsRequest.delegate = self;
        [self.productsRequest start];
    }
}

#pragma mark 产品购买
- (void)buyProductIdentifier:(SKProduct *)product {
//    NSLog(@"Buying %@...", product.productIdentifier);
    SKPayment* payment=[SKPayment paymentWithProduct:product];
    [[SKPaymentQueue defaultQueue] addPayment:payment];
}

#pragma mark AppStore返回产品信息
- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response {
    self.products = response.products;
    [[NSNotificationCenter defaultCenter] postNotificationName:kProductsLoadedNotification object:_products];
    self.productsRequest = nil;
}

- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions {
    for (SKPaymentTransaction *transaction in transactions){
        switch (transaction.transactionState){
            case SKPaymentTransactionStatePurchased:
//                NSLog(@"completeTransaction...");
                [self completeTransaction:transaction productIdentifier:transaction.payment.productIdentifier];
                [[SKPaymentQueue defaultQueue] finishTransaction: transaction];
                break;
            case SKPaymentTransactionStateFailed:
//                if (transaction.error.code != SKErrorPaymentCancelled){
//                    NSLog(@"Transaction error: %@", transaction.error.localizedDescription);
//                }
                [[NSNotificationCenter defaultCenter] postNotificationName:kProductPurchaseFailedNotification object:transaction];
                [[SKPaymentQueue defaultQueue] finishTransaction: transaction];
                break;
            case SKPaymentTransactionStateRestored:
//                NSLog(@"restoreTransaction...");
                [self completeTransaction:transaction productIdentifier:transaction.originalTransaction.payment.productIdentifier];
                [[SKPaymentQueue defaultQueue] finishTransaction: transaction];
            default:
                break;
        }
    }
}

- (void)completeTransaction:(SKPaymentTransaction *)transaction productIdentifier:(NSString *)productIdentifier {
    NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
    [requestParams setObject:ACCESSID forKey:@"accessid"];
    [requestParams setObject:self.recordno forKey:@"recordno"];
//    [requestParams setObject:[Crypto base64Encode:transaction.transactionReceipt] forKey:@"receiptdata"];
    [requestParams setObject:[[NSString alloc] initWithData:transaction.transactionReceipt encoding:NSUTF8StringEncoding] forKey:@"receiptdata"];
    HttpRequest *http=[[HttpRequest alloc]init];
    NSMutableDictionary* propertyes=[[NSMutableDictionary alloc]init];
    [propertyes setObject:productIdentifier forKey:@"productIdentifier"];
    [http setPropertys:propertyes];
    [http setDelegate:self];
    [http setController:self.controller];
    [http setRequestCode:REQUESTCODE_BUY_VERIFYING];
    [http handle:@"v4ephoneapppayCon" signKey:ACCESSKEY headParams:nil requestParams:requestParams];
}

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode {
    if([response successFlag]){
        if(reqCode==REQUESTCODE_BUY_VERIFYING){
            [[NSNotificationCenter defaultCenter] postNotificationName:kProductPurchasedNotification object:[[response propertys] objectForKey:@"productIdentifier"]];
        }
    }
}

@end
