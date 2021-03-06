#import "Common.h"

@implementation Common

+ (id)getCache:(NSString *)key{
    NSUserDefaults *settings = [NSUserDefaults standardUserDefaults];
    return [settings objectForKey:key];
}
+ (void)setCache:(NSString *)key data:(id)data{
    NSUserDefaults *setting=[NSUserDefaults standardUserDefaults];
    [setting setObject:data forKey:key];
    [setting synchronize];
}
+ (BOOL)getCacheByBool:(NSString *)key{
    NSUserDefaults * settings = [NSUserDefaults standardUserDefaults];
    return [settings boolForKey:key];
}
+ (void)setCacheByBool:(NSString *)key data:(BOOL)data{
    NSUserDefaults *setting=[NSUserDefaults standardUserDefaults];
    [setting setBool:data forKey:key];
    [setting synchronize];
}

+ (void)alert:(NSString *)message cancel:(NSString *)cancelName ok:(NSString *)okName delegate:(id<UIAlertViewDelegate>)delegate{
    [Common alert:message cancel:cancelName ok:okName delegate:delegate tag:0];
}

+ (void)alert:(NSString *)message cancel:(NSString *)cancelName ok:(NSString *)okName delegate:(id<UIAlertViewDelegate>)delegate tag:(NSInteger)tag
{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"信息"
                          message:message
                          delegate:delegate
                          cancelButtonTitle:cancelName
                          otherButtonTitles:okName, nil];
    alert.tag=tag;
    [alert show];
    [[Config Instance] setAlertView:alert];
}

+ (void)alert:(NSString *)message{
    UIAlertView *alert = [[UIAlertView alloc]
                          initWithTitle:@"信息"
                          message:message
                          delegate:nil
                          cancelButtonTitle:@"确定"
                          otherButtonTitles:nil, nil];
    [alert show];
    [[Config Instance] setAlertView:alert];
}

+ (void)actionSheet:(id<UIActionSheetDelegate>)delegate message:(NSString *)message ok:(NSString *) okName tag:(NSInteger)tag{
    UIActionSheet *sheet = [[UIActionSheet alloc]
                            initWithTitle:message
                            delegate:delegate
                            cancelButtonTitle:@"取消"
                            destructiveButtonTitle:okName
                            otherButtonTitles:nil,nil];
    sheet.tag=tag;
    //UIActionSheet与UITabBarController结合使用不能使用[sheet showInView:self.view];
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
    [[Config Instance] setActionSheet:sheet];
}

+ (void)actionSheet:(id<UIActionSheetDelegate>)delegate message:(NSString *)message tag:(NSInteger)tag{
    [Common actionSheet:delegate message:message ok:@"确定" tag:tag];
}

//没有登录时提示
+ (void)noLoginAlert:(id<UIActionSheetDelegate>)delegate{
    UIActionSheet *sheet = [[UIActionSheet alloc]
                            initWithTitle:@"登录超时"
                            delegate:delegate
                            cancelButtonTitle:@"取消"
                            destructiveButtonTitle:@"重新登录"
                            otherButtonTitles:nil,nil];
    //UIActionSheet与UITabBarController结合使用不能使用[sheet showInView:self.view];
    [sheet showInView:[UIApplication sharedApplication].keyWindow];
    [[Config Instance] setActionSheet:sheet];
}

+ (NSString*)formatPhone:(NSString *)phone{
    if(phone){
        NSMutableString *phoneNumber=[[NSMutableString alloc]init];
        for(int i=0;i<[phone length];i++){
            unichar c=[phone characterAtIndex:i];
            if((c>=48&&c<=57)||c==43){
                [phoneNumber appendFormat:@"%c",c];
            }
        }
        return [NSString stringWithFormat:@"%@",phoneNumber];
    }
    return @"";
}

//把秒转换成12时12分12秒中文形式
+ (NSString*)secondConvertFormatTimerByCn:(NSString *)second{
    NSInteger sec=[second intValue];
    int day=sec/(60*60*24);
    int h=(sec%(60*60*24))/(60*60);
    int m=((sec%(60*60*24))%(60*60))/60;
    int s=(((sec%(60*60*24))%(60*60))%60)%60;
    if(day>0){
        return [NSString stringWithFormat:@"%d天%d时%d分%d秒",day,h,m,s];
    }else if(h>0){
        return [NSString stringWithFormat:@"%d时%d分%d秒",h,m,s];
    }else if(m>0){
        return [NSString stringWithFormat:@"%d分%d秒",m,s];
    }else{
        return [NSString stringWithFormat:@"%d秒",s];
    }
}

//把秒转换成12:12:12英文形式
+ (NSString*)secondConvertFormatTimerByEn:(NSString *)second{
    NSInteger sec=[second intValue];
    int day=sec/(60*60*24);
    int h=(sec%(60*60*24))/(60*60);
    int m=((sec%(60*60*24))%(60*60))/60;
    int s=(((sec%(60*60*24))%(60*60))%60)%60;
    NSString *days=nil;
    NSString *hs=nil;
    NSString *ms=nil;
    NSString *ss=nil;
    if(day>=10){
        days=[NSString stringWithFormat:@"%d",day];
    }else{
        days=[NSString stringWithFormat:@"0%d",day];
    }
    if(h>=10){
        hs=[NSString stringWithFormat:@"%d",h];
    }else{
        hs=[NSString stringWithFormat:@"0%d",h];
    }
    if(m>=10){
        ms=[NSString stringWithFormat:@"%d",m];
    }else{
        ms=[NSString stringWithFormat:@"0%d",m];
    }
    if(s>=10){
        ss=[NSString stringWithFormat:@"%d",s];
    }else{
        ss=[NSString stringWithFormat:@"0%d",s];
    }
    if(day>0){
        return [NSString stringWithFormat:@"%@:%@:%@:%@",days,hs,ms,ss];
    }else if(h>0){
        return [NSString stringWithFormat:@"%@:%@:%@",hs,ms,ss];
    }else if(m>0){
        return [NSString stringWithFormat:@"%@:%@",ms,ss];
    }else{
        return [NSString stringWithFormat:@"00:%@",ss];
    }
}

//返回登录界面
+ (void)resultLoginViewController:(UIViewController *)view resultCode:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSMutableDictionary*)result{
    if([[[Config Instance] loginResultDelegate] respondsToSelector:@selector(onControllerResult:requestCode:data:)]){
        [[[Config Instance] loginResultDelegate] onControllerResult:resultCode requestCode:requestCode data:result];
    }
    [view dismissViewControllerAnimated:YES completion:nil];
}

//返回调用
+ (void)resultNavigationViewController:(UIViewController *)view resultDelegate:(NSObject<ResultDelegate> *)resultDelegate resultCode:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSMutableDictionary *)result{
    if(resultDelegate){
        if([resultDelegate respondsToSelector:@selector(onControllerResult:requestCode:data:)]){
            [resultDelegate onControllerResult:resultCode requestCode:requestCode data:result];
        }
    }
    [view.navigationController popViewControllerAnimated:YES];
}

+ (ABAddressBookRef)getAbAddressBook{
    ABAddressBookRef addressBook = nil;
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 6.0) {
        addressBook = ABAddressBookCreateWithOptions(NULL, NULL);
        //等待同意后向下执行
        dispatch_semaphore_t sema = dispatch_semaphore_create(0);
        ABAddressBookRequestAccessWithCompletion(addressBook, ^(bool granted, CFErrorRef error)
                                                 {
                                                     dispatch_semaphore_signal(sema);
                                                 });
        dispatch_semaphore_wait(sema, DISPATCH_TIME_FOREVER);
        dispatch_release(sema);
    } else {
        addressBook = ABAddressBookCreate();
    }
    return addressBook;
}

+ (void)setCacheXmlByList:(NSString *)xml tag:(NSString *)tag {
    NSMutableDictionary *dictionary=[NSMutableDictionary dictionaryWithDictionary:[Common getCache:[Config Instance].cacheKey]];
    [dictionary setObject:xml forKey:tag];
    [Common setCache:[Config Instance].cacheKey data:dictionary];
}

+ (NSMutableArray *)getCacheXmlByList:(NSString *)tag {
    NSMutableDictionary *dictioanry=[Common getCache:[Config Instance].cacheKey];
    if(dictioanry){
        NSString *content=[dictioanry objectForKey:tag];
        if(![@"" isEqualToString:content]) {
            if(content){
                NSMutableArray *dataItemArray=[[Common toResponseData:content] dataItemArray];
                if([dataItemArray count]>0){
                    return dataItemArray;
                }
            }
        }
    }
    return nil;
}

+ (NSData *)toJSONData:(id)theData{
    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:theData
                                                       options:NSJSONWritingPrettyPrinted error:&error];
    if ([jsonData length] > 0 && error == nil){
        return jsonData;
    }else{
        return nil;
    }
}

+ (Response*)toResponseData:(NSString*)repsonseString
{
    Response *response=[[Response alloc]init];
    if(repsonseString!=nil&&![@"" isEqualToString:repsonseString]){
        [response setResponseString:repsonseString];
        [response setData:[[response responseString] dataUsingEncoding: NSUTF8StringEncoding]];
        if([response data]!=nil){
            //转换成JSON格式
            NSDictionary *resultJSON=[NSJSONSerialization JSONObjectWithData:[response data] options:NSJSONReadingMutableLeaves error:nil];
            if(resultJSON!=nil){
                [response setResultJSON:resultJSON];
                NSDictionary *responseData=[resultJSON objectForKey:@"response"];
                if(responseData!=nil){
                    //响应信息
                    NSDictionary *info=[responseData objectForKey:@"info"];
                    if(info!=nil){
                        [response setCode:[info objectForKey:@"code"]];
                        [response setMsg:[info objectForKey:@"msg"]];
                    }
                    //主体内容
                    NSDictionary *content=[responseData objectForKey:@"content"];
                    if(content!=nil){
                        //响应的分页信息
                        NSDictionary *pageinfo=[content objectForKey:@"pageinfo"];
                        if(pageinfo){
                            [response setPageInfo:pageinfo];
                            for(NSString *key in content){
                                if(![@"pageinfo" isEqualToString:key]){
                                    NSMutableArray *nsArr=[[NSMutableArray alloc]init];
                                    NSDictionary *dataItemArray=[content objectForKey:key];
                                    for(id data in dataItemArray){
                                        for(NSString *dinfo in data){
                                            [nsArr addObject:[[NSMutableDictionary alloc]initWithDictionary:[data objectForKey:dinfo]]];
                                        }
                                    }
                                    [response setDataItemArray:nsArr];
                                    break;
                                }
                            }
                        }else{
                            static NSString *combolistStr=@"combolist";
                            NSDictionary *combolist=[content objectForKey:combolistStr];
                            if(combolist!=nil){
                                NSMutableArray *nsArr=[[NSMutableArray alloc]init];
                                NSDictionary *dataItemArray=[content objectForKey:combolistStr];
                                for(id data in dataItemArray){
                                    for(NSString *dinfo in data){
                                        [nsArr addObject:[[NSMutableDictionary alloc]initWithDictionary:[data objectForKey:dinfo]]];
                                    }
                                }
                                [response setDataItemArray:nsArr];
                            }else{
                                [response setMainData:content];
                            }
                        }
                    }
                }
            }
        }
    }
    return response;
}

@end
