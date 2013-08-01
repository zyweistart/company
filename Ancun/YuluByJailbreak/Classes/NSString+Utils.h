//
//  NSString+Utils.h
//  Ancun
//
//  Created by Start on 13-7-24.
//
//

#import <Foundation/Foundation.h>

@interface NSString (Utils)

- (NSString*)md5;
+ (NSData*)base64Decode:(NSString*)string;
+ (NSString*)base64Encode:(NSData*)data;

@end
