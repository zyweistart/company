//
//  IAPHelper.m
//  ACyulu
//
//  Created by Start on 13-4-8.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import "IAPHelper.h"

@implementation IAPHelper

@synthesize products = _products;
@synthesize request = _request;
@synthesize productlist=_productlist;

static IAPHelper * _sharedHelper;

+ (IAPHelper *)sharedHelper{
    if (_sharedHelper != nil) {
        return _sharedHelper;
    }
    _sharedHelper = [[IAPHelper alloc] init];
    return _sharedHelper;
}

- (void)setProductList:(NSMutableArray*)productlists{
    self.productlist=productlists;
}

- (NSMutableDictionary*)getProductDetail:(NSString*)identifier{
    for (NSMutableDictionary * product in self.productlist){
        if([identifier isEqualToString:[product objectForKey:@"procode"]]){
            return product;
        }
    }
    return nil;
}
//请求加载AppStore中的产品信息
- (void)requestProducts {
    NSMutableSet * products = [NSMutableSet set];
    for (NSMutableDictionary * product in self.productlist){
        [products addObject:[product objectForKey:@"procode"]];
    }
    self.request = [[[SKProductsRequest alloc] initWithProductIdentifiers:products] autorelease];
    _request.delegate = self;
    [_request start];
}
//AppStore返回产品信息
- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response {
    self.products = response.products;
    self.request = nil;
    [[NSNotificationCenter defaultCenter] postNotificationName:kProductsLoadedNotification object:_products];
}
//产品购买
- (void)buyProductIdentifier:(SKProduct *)product{
//    NSLog(@"Buying %@...", product.productIdentifier);
    SKPayment* payment=[SKPayment paymentWithProduct:product];
    [[SKPaymentQueue defaultQueue] addPayment:payment];
}

- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions{
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

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if([response successFlag]){
        if(reqCode==REQUESTCODE_BUY_VERIFYING){
            [[NSNotificationCenter defaultCenter] postNotificationName:kProductPurchasedNotification object:[[response propertys] objectForKey:@"productIdentifier"]];
        }
    }
}

- (void)dealloc{
    [_controller release];
    _controller=nil;
    [_recordno release];
    _recordno=nil;
    [_products release];
    _products = nil;
    [_request release];
    _request = nil;
    [_productlist release];
    _productlist=nil;
    [super dealloc];
}

@end
