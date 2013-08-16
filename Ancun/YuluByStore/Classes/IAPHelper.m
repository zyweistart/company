//
//  IAPHelper.m
//  ACyulu
//
//  Created by Start on 13-4-8.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "IAPHelper.h"

@implementation IAPHelper {
    HttpRequest *_http;
    SKProductsRequest *_productsRequest;
}

static IAPHelper * _sharedHelper;

+ (IAPHelper *)sharedHelper {
    if (_sharedHelper != nil) {
        return _sharedHelper;
    } else {
        _sharedHelper = [[IAPHelper alloc] init];
    }
    return _sharedHelper;
}

- (NSMutableDictionary*)getProductDetail:(NSString *)identifier {
    for (NSMutableDictionary * product in _productlist){
        if([identifier isEqualToString:[product objectForKey:@"procode"]]){
            return product;
        }
    }
    return nil;
}

//加载AppStore中的产品信息
- (void)requestProducts {
    NSMutableSet * products = [NSMutableSet set];
    for (NSMutableDictionary * product in _productlist) {
        [products addObject:[product objectForKey:@"procode"]];
    }
    _productsRequest = [[SKProductsRequest alloc] initWithProductIdentifiers:products];
    _productsRequest.delegate = self;
    [_productsRequest start];
}

//产品购买
- (void)buyProductIdentifier:(SKProduct *)product {
//    NSLog(@"Buying %@...", product.productIdentifier);
    SKPayment* payment=[SKPayment paymentWithProduct:product];
    [[SKPaymentQueue defaultQueue] addPayment:payment];
}

- (void)completeTransaction:(SKPaymentTransaction *)transaction productIdentifier:(NSString *)productIdentifier {
    NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
    [requestParams setObject:ACCESSID forKey:@"accessid"];
    [requestParams setObject:_recordno forKey:@"recordno"];
//    [requestParams setObject:[Crypto base64Encode:transaction.transactionReceipt] forKey:@"receiptdata"];
    [requestParams setObject:[[NSString alloc] initWithData:transaction.transactionReceipt encoding:NSUTF8StringEncoding] forKey:@"receiptdata"];
    _http=[[HttpRequest alloc]init];
    NSMutableDictionary* propertyes=[[NSMutableDictionary alloc]init];
    [propertyes setObject:productIdentifier forKey:@"productIdentifier"];
    [_http setPropertys:propertyes];
    [_http setDelegate:self];
    [_http setController:_controller];
    [_http setRequestCode:REQUESTCODE_BUY_VERIFYING];
    [_http handle:@"v4ephoneapppayCon" signKey:ACCESSKEY headParams:nil requestParams:requestParams];
}

#pragma mark delegate

//AppStore返回产品信息
- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response {
    [_products addObjectsFromArray:response.products];
    _productsRequest=nil;
    //通知产品列表已经加载完成
    [[NSNotificationCenter defaultCenter] postNotificationName:kProductsLoadedNotification object:_products];
}

- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions {
    for (SKPaymentTransaction *transaction in transactions) {
        switch (transaction.transactionState) {
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

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode {
    if([response successFlag]) {
        if(reqCode==REQUESTCODE_BUY_VERIFYING) {
            [[NSNotificationCenter defaultCenter] postNotificationName:kProductPurchasedNotification object:[[response propertys] objectForKey:@"productIdentifier"]];
        }
    }
}

@end
