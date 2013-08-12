//
//  ACRechargeConfirmViewController.h
//  Ancun
//
//  Created by Start on 13-8-7.
//
//

#import <UIKit/UIKit.h>

@interface ACRechargeConfirmViewController : UIViewController<HttpViewDelegate,UIActionSheetDelegate>{
    
    UILabel *_lblTip2;
    UILabel *_lblTip3;
    UILabel *_lblTip4;
    
    HttpRequest *_alipayHttp;
}

@property (assign,nonatomic) int currentType;
@property (strong,nonatomic) NSMutableDictionary *data;

@end
