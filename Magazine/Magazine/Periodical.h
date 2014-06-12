//
//  Periodical.h
//  Magazine
//
//  Created by Start on 6/12/14.
//  Copyright (c) 2014 Ancun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Periodical : NSManagedObject

@property (nonatomic, retain) NSString * bigTitle;
@property (nonatomic, retain) NSString * contenturl;
@property (nonatomic, retain) NSString * downloadUrl;
@property (nonatomic, retain) NSString * smallTitle;
@property (nonatomic, retain) NSString * periods;

@end
