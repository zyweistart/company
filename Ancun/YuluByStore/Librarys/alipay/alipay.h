/**NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
[requestParams setObject:@"3" forKey:@"payuse"];
[requestParams setObject:@"864bdaeface151e6a95b4f4827551f5d" forKey:@"recprod"];
[requestParams setObject:@"1" forKey:@"quantity"];
[requestParams setObject:@"1" forKey:@"actflag"];
HttpRequest *http=[[HttpRequest alloc]init];
[http setDelegate:self];
[http setController:self];
[http setRequestCode:10000000];
[http loginhandle:@"v4alipayReq" requestParams:requestParams];

if(reqCode==10000000){
    if([response successFlag]){
        NSString *orderString=[[[response mainData] objectForKey:@"alipayinfo"] objectForKey:@"reqcontent"];
        
        orderString=[orderString stringByReplacingOccurrencesOfString:@"&amp;" withString:@"&"];
        
        AlixPay * alixpay = [AlixPay shared];
        int ret = [alixpay pay:orderString applicationScheme:@"iOSAlipay"];
        
        if (ret == kSPErrorAlipayClientNotInstalled) {
            UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                 message:@"您还没有安装支付宝快捷支付，请先安装。"
                                                                delegate:self
                                                       cancelButtonTitle:@"确定"
                                                       otherButtonTitles:nil];
            [alertView setTag:123];
            [alertView show];
            [alertView release];
        }
    } else {
        NSLog(@"充值产品不存在或暂不支持");
    }
    return;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	if (alertView.tag == 123) {
		NSString * URLString = @"http://itunes.apple.com/cn/app/id535715926?mt=8";
		[[UIApplication sharedApplication] openURL:[NSURL URLWithString:URLString]];
	}
}*/