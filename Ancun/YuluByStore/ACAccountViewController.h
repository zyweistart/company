//
//  ACAccountViewController.h
//  ACyulu
//
//  Created by Start on 12/26/12.
//  Copyright (c) 2012 ancun. All rights reserved.
//

#import "BaseTableViewController.h"

@class ACAccountHeaderCell;
@class ACAccountHeaderCountCell;
@interface ACAccountViewController : BaseTableViewController<UIActionSheetDelegate,HttpViewDelegate>{
    
    NSArray *_cellArray;
    ACAccountHeaderCell *_headerCell;
    ACAccountHeaderCountCell *_headerCountCell;
    
}
@end
