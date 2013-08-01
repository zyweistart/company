//
//  NSString+OAURLEncodingAdditions.h
//  ACyulu
//
//  Created by Start on 13-1-15.
//  Copyright (c) 2013年 ancun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (OAURLEncodingAdditions)

- (NSString *) URLEncodedString;

- (NSString*) URLDecodedString;

@end
