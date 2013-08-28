//
//  ACContactsViewController.h
//  Ancun
//
//  Created by Start on 13-8-28.
//
//

#import <UIKit/UIKit.h>

@interface ACContactsViewController : UIViewController<UITableViewDelegate,UITableViewDataSource,UISearchDisplayDelegate, UISearchBarDelegate,HttpViewDelegate>

@property (strong,nonatomic) NSMutableArray *dataKeys;
@property (strong,nonatomic) NSMutableDictionary *dataResults;

@property (strong,nonatomic) NSMutableArray *searchKeys;
@property (strong,nonatomic) NSMutableDictionary *searchResults;

@property (strong,nonatomic) IBOutlet UIView *message;
@property (strong,nonatomic) IBOutlet UITableView *tableView;

- (void)loadContact;

@end
