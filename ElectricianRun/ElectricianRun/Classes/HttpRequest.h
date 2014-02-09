//
//  HttpRequest.h
//  ElectricianRun
//
//  Created by Start on 2/9/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HttpRequest : NSObject<NSURLConnectionDataDelegate>

- (void)start:(NSString*)URL params:(NSMutableDictionary*)params;

@end
