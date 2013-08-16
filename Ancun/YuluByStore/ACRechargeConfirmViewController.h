//
//  ACRechargeConfirmViewController.h
//  Ancun
//
//  Created by Start on 13-8-7.
//
//

#import <UIKit/UIKit.h>

@class ACRechargeNav;
@interface ACRechargeConfirmViewController : ACBaseViewController<HttpViewDelegate,UIActionSheetDelegate>{
    
    HttpRequest *_alipayHttp;
    
}

@property (assign,nonatomic) int currentType;
@property (strong,nonatomic) NSMutableDictionary *data;
@property (strong,nonatomic) ACRechargeNav *rechargeNav;

- (void)layoutSuccessPage;

@end
