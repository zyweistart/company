//
//  ACForgetPasswordViewController.m
//  Ancun
//
//  Created by Start on 4/3/14.
//
//

#import "ACForgetPasswordViewController.h"

@interface ACForgetPasswordViewController ()

@end

@implementation ACForgetPasswordViewController

- (id)init
{
    self=[super init];
    if(self){
        _type=2;
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    [_lblTitlte setText:@"忘记密码"];
    [_lblSuccessInfo setText:@"恭喜您，找回密码成功"];
    [_btnReadAgreementCheck setHidden:YES];
    [_btnReadAgreement setHidden:YES];
}

- (void)setPassword:(id)sender{
    [self backgroundDoneEditing:nil];
    _password=_regInputPassword.text;
    NSString *rePassword=_regInputRePassword.text;
    if([@"" isEqualToString:_password]){
        [Common alert:@"密码不能为空"];
    }else if([@"" isEqualToString:rePassword]){
        [Common alert:@"请再输入一次密码"];
    }else if(![_password isEqualToString:rePassword]){
        [Common alert:@"两次密码不相同"];
    }else{
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:_phone forKey:@"phone"];
        [requestParams setObject:[_password md5] forKey:@"password"];
        [requestParams setObject:_verificationCode forKey:@"authcode"];
        [requestParams setObject:@"c" forKey:@"operatesource"];
        [requestParams setObject:@"" forKey:@"ip"];
        [requestParams setObject:@"" forKey:@"mac"];
        [requestParams setObject:@"" forKey:@"raflag"];
        _hRequest=[[HttpRequest alloc]init];
        [_hRequest setDelegate:self];
        [_hRequest setController:self];
        [_hRequest setIsShowMessage:YES];
        [_hRequest setRequestCode:REQUESTCODE_SIGNUP];
        [_hRequest handle:@"v4pwdReset" signKey:nil headParams:nil requestParams:requestParams];
    }
}

@end
