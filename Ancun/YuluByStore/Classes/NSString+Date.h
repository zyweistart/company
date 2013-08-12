//
//  NSString+Date.h
//  Ancun
//
//  Created by Start on 13-8-5.
//
//

#import <Foundation/Foundation.h>

enum DateType{
    YYYYMMddHHmmss
};

@interface NSString (Date)

- (NSString *) dateStringFormat;

- (NSDate *)stringConvertDate;

- (NSString *)getDateLongTimeByYYYYMMddHHmmss;

@end
