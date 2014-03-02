//
//  DownloadIcon.h
//  ElectricianRun
//
//  Created by Start on 3/2/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DownloadIcon : NSObject<NSURLConnectionDataDelegate>

@property NSMutableData *resultData;

- (void)startWithUrl:(NSString*)url;

@end
