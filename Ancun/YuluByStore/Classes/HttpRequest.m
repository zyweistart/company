//
//  HttpRequest.m
//  ACyulu
//
//  Created by Start on 12-12-6.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "HttpRequest.h"
#import "Reachability.h"
#import "GHNSString+HMAC.h"
#import "GCDiscreetNotificationView.h"
#import "NSString+OAURLEncodingAdditions.h"
#import "BaseRefreshTableViewController.h"

@implementation HttpRequest

+ (BOOL) isNetworkConnection{
    Reachability *reach = [Reachability reachabilityForInternetConnection];
    NetworkStatus netStatus = [reach currentReachabilityStatus];
    if(netStatus==ReachableViaWWAN||netStatus==ReachableViaWiFi){
        return YES;
    }else{
        return NO;
    }
}

- (void)loginhandle:(NSString*)url requestParams:(NSMutableDictionary*)request {
    if([[Config Instance]isLogin]){
        [request setObject:[[[Config Instance] userInfo] objectForKey:@"accessid"] forKey:@"accessid"];
        [self handle:url signKey:[[[Config Instance] userInfo] objectForKey:@"accesskey"]  headParams:nil requestParams:request];
    }else{
        [Common noLoginAlert:self];
    }
}

- (void)handle:(NSString*)url signKey:(NSString*)signKey requestParams:(NSMutableDictionary*)request {
    [self handle:url signKey:signKey headParams:nil requestParams:request];
}

- (void)handle:(NSString*)action signKey:(NSString*)signKey headParams:(NSMutableDictionary*)head requestParams:(NSMutableDictionary*)request {
    if ([HttpRequest isNetworkConnection]) {
        _action=action;
        _signKey=signKey;
        _head=head;
        _request=request;
        if(_isFileDownload){
            //如果为下载是否使用的是3G移动网络
            Reachability *reach = [Reachability reachabilityForInternetConnection];
            if([reach currentReachabilityStatus]==ReachableViaWWAN){
                [Common actionSheet:self message:@"即将通过移动网络加载数据，为了节约流量，推荐您使用WIFI无线网络!" tag:1];
            }else{
                [self handle];
            }
        }else{
            [self handle];
        }
    }else{
        if(self.controller){
            [Common notificationMessage:@"网络连接出错，请检测网络设置" inView:self.controller.view];
            if( [_delegate respondsToSelector: @selector(requestFailed:requestCode:)]){
                [_delegate requestFailed:nil requestCode:self.requestCode];
            }
        }
    }
}

- (void)handle{
    NSString *requestBodyContent=[XML generate:_action requestParams:_request];
    
    if(!_head){
        _head=[[NSMutableDictionary alloc]init];
    }
    if(![_head objectForKey:@"sign"]){
        //签名
        if(_signKey){
            [_head setObject:[[requestBodyContent md5] gh_HMACSHA1:_signKey] forKey:@"sign"];
        }else{
            [_head setObject:[requestBodyContent md5] forKey:@"sign"];
        }
    }
    //请求长度
    [_head setObject:[NSString stringWithFormat:@"%d",[[requestBodyContent dataUsingEncoding:NSUTF8StringEncoding] length]] forKey:@"reqlength"];
    ASIHTTPRequest *asihttp = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:ANCUN_HTTP_URL]];
    
    //设置请求头参数
    for(NSString *key in _head){
        //URL编码
        [asihttp addRequestHeader:key value:[[_head objectForKey:key] URLEncodedString]];
    }
    
    [asihttp setRequestMethod:@"POST"];
    [asihttp appendPostData:[requestBodyContent dataUsingEncoding:NSUTF8StringEncoding]];
    [asihttp setDelegate:self];
    if(_isFileDownload){
        proHud=[[ATMHud alloc]init];
        [self.controller.view addSubview:proHud.view];
        [asihttp setDownloadProgressDelegate:proHud];
        [asihttp setShowAccurateProgress:YES];
        [proHud setCaption:@"下载中..."];
        [proHud setProgress:1];
        [proHud show];
        [[Config Instance]setIsCalculateTotal:YES];
    }else{
        if(self.controller){
            asihttp.hud = [[MBProgressHUD alloc] initWithView:self.controller.view];
            [self.controller.view addSubview:asihttp.hud];
            if(self.message){
                asihttp.hud.labelText = _message;
            }
            asihttp.hud.dimBackground = YES;
            asihttp.hud.square = YES;
            [asihttp.hud show:YES];
        }
    }
    [asihttp setTimeOutSeconds:30];
    [asihttp startAsynchronous];
}

- (void)requestFinished:(ASIHTTPRequest *)request {
    if( [_delegate respondsToSelector: @selector(requestFinished:requestCode:)]){
        [_delegate requestFinished:request requestCode:self.requestCode];
    }else if( [_delegate respondsToSelector: @selector(requestFinishedByResponse:requestCode:)]){
        Response *response=[[Response alloc]init];
        [response setRequest:request];
        if(!_isFileDownload){
            NSString *responseString = [request responseString];
            response=[XML analysis:responseString];
            [response setPropertys:_propertys];
            [response setResponseString:responseString];
            if(!_isVerify){
//                NSLog(@"%@---%@",[response code],[response msg]);
                [response setSuccessFlag:NO];
                if([[response code] isEqualToString:@"100000"]){
                    [response setSuccessFlag:YES];
                }else if([[response code] isEqualToString:@"110042"]){
                    //暂无记录
                    if([self.controller isKindOfClass:[BaseRefreshTableViewController class]]){
                        [[((BaseRefreshTableViewController*)self.controller) dataItemArray]removeAllObjects];
                        [[((BaseRefreshTableViewController*)self.controller) tableView]reloadData];
                    }
                    [response setSuccessFlag:YES];
                    if(self.controller){
                        [Common notificationMessage:[response msg] inView:self.controller.view];
                    }
                }else if([[response code] isEqualToString:@"110026"]){
                    //通行证编号错误或未登录
                    [[Config Instance]setIsLogin:NO];
                    [Common noLoginAlert:self];
                }else if([[response code] isEqualToString:@"110036"]){
                    //签名不匹配或密码不正确
                    [Common alert:@"用户名或密码不正确"];
                }else if([[response code] isEqualToString:@"120020"]){
                    //用户不存在
                }else if([[response code] isEqualToString:@"120169"]){
                    //该手机号码已被注册
                }else{
                    [Common alert:[response msg]];
                }
            }
        }else{
            [response setPropertys:_propertys];
        }
        [_delegate requestFinishedByResponse:response requestCode:self.requestCode];
    }
    if (request.hud) {
        [request.hud hide:YES];
    }
    if(proHud){
        [proHud hide];
    }
}

- (void) requestFailed:(ASIHTTPRequest *)request {
    if( [_delegate respondsToSelector: @selector(requestFailed:requestCode:)]){
        [_delegate requestFailed:request requestCode:self.requestCode];
    }else{
        [Common alert:@"请求异常，请重试！"];
        NSLog(@"%@",[[request error]description]);
    }
    if (request.hud) {
        [request.hud hide:YES];
    }
    if(proHud){
        [proHud hide];
    }
}

- (void) actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(actionSheet.tag==1){
        //移动流量下载行为
        if(buttonIndex==0){
            [self handle];
        }
    }else{
        //未登陆行为
        if(buttonIndex==0){
            [Common resultLoginViewController:self.controller resultCode:RESULTCODE_ACLoginViewController_1 requestCode:0 data:nil];
        }
    }
}

@end