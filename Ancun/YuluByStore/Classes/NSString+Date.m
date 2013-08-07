//
//  NSString+Date.m
//  Ancun
//
//  Created by Start on 13-8-5.
//
//

#import "NSString+Date.h"

@implementation NSString (Date)

- (NSString *)dateStringFormat {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyyMMddHHmmss"];
    [formatter setTimeZone:[NSTimeZone timeZoneWithName:@"UTC"]];
    //[formatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    NSDate *dateFormatted = [formatter dateFromString:self];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    [formatter setTimeZone:[NSTimeZone timeZoneWithName:@"UTC"]];
    return [formatter stringFromDate:dateFormatted];
}

- (NSDate *)stringConvertDate{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    return [dateFormatter dateFromString:self];
}


@end
