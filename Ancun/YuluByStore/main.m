//
//  main.m
//  YuluByStore
//
//  Created by Start on 13-7-24.
//
//

#import <UIKit/UIKit.h>

#import "ACAppDelegate.h"
#import "NSString+Date.h"

int main(int argc, char *argv[])
{
    @autoreleasepool {
//        NSDate *datenow=[NSDate date];
//        NSLog(@"%zi",[datenow timeIntervalSince1970]);
//        
//        
//        
//        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
//        [formatter setDateFormat:@"yyyyMMddHHmmss"];
//        [formatter setTimeZone:[NSTimeZone timeZoneWithName:@"UTC"]];
//        //[formatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
//        NSDate *dateFormatted = [formatter dateFromString:];
//        
//        
//        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
//        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
////        [dateFormatter dateFromString:@"2013-08-09 16:54:55"];
//        
//        
//        
//        
//        NSLog(@"%zi",[[dateFormatter dateFromString:@"2013-08-09"] timeIntervalSince1970]);
        
//        NSString *timeStr=@"2013-08-12 09:05:58";
//        NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
//        [formatter setDateStyle:NSDateFormatterMediumStyle];
//        [formatter setTimeStyle:NSDateFormatterShortStyle];
//        [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss"];
//        
//        NSTimeZone *timeZone=[NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
//        [formatter setTimeZone:timeZone];
//        NSDate *date=[formatter dateFromString:timeStr];
//        
//        NSString *timeSp=[NSString stringWithFormat:@"%zi",(long)[date timeIntervalSince1970]];
//        NSLog(@"timeSp:%@",timeSp);
//        
//        NSLog(@"timeSp:%@",[timeStr getDateLongTimeByYYYYMMddHHmmss]);
//         NSDate *datenow=[NSDate date];
//        NSLog(@"datenowSp:%@",[NSString stringWithFormat:@"%zi",(long)[datenow timeIntervalSince1970]]);
        
        return UIApplicationMain(argc, argv, nil, NSStringFromClass([ACAppDelegate class]));
    }
}
