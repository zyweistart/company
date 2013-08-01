//
//  XML.h
//  ACyulu
//  XML基础操作类
//  Created by Start on 12-11-17.
//  Copyright (c) 2012年 Start. All rights reserved.
//

@interface XML : NSObject

+ (NSString*)generate:(NSString*)action requestParams:(NSMutableDictionary*)requestParams;

+ (Response*)analysis:(NSString*)xmlContent;

@end
