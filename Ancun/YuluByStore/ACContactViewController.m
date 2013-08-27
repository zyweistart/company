//
//  ACContactViewController.m
//  ACyulu
//
//  Created by Start on 12-12-6.
//  Copyright (c) 2012年 ancun. All rights reserved.
//

#import "ACContactViewController.h"
#import "NSDictionary+MutableDeepCopy.h"
#import "ChineseToPinyin.h"
#import "ACContactCell.h"

@interface ACContactViewController ()

- (void)resetSearch;
- (void)handleSearchForTerm:(NSString *)searchTerm;
- (void) dial:(NSString*)phone;

@end

@implementation ACContactViewController{
    BOOL _searching;
    
    NSMutableArray *_keys;
    NSMutableDictionary *_names;
    NSMutableDictionary *_allNames;
    
    HttpRequest *_contactHttp;
}

@synthesize table;
@synthesize search;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.tabBarItem.image = [UIImage imageNamed:@"nav_icon_contact"];
        self.tabBarItem.title = @"通讯录";
        self.navigationItem.title=@"通讯录";
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                                 initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh
                                                 target:self
                                                 action:@selector(refresh:)];
        [self refresh:nil];
        
    }
    return self;
}

#pragma mark -
#pragma mark DelegateMethod

- (void)requestFinishedByResponse:(Response *)response requestCode:(int)reqCode{
    if([response successFlag]){
#ifdef TEST
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[[NSString alloc] initWithFormat:@"tel://%@",PHONENUMBER]]];
#else
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[[NSString alloc] initWithFormat:@"tel://%@",[[[response mainData] objectForKey:@"serverinfo"] objectForKey:@"serverno"]]]];
#endif
        [[Config Instance]setIsRefreshUserInfo:YES];
        [[Config Instance]setIsRefreshRecordingList:YES];
    }
}

#pragma mark Table View Data Source Methods
- (NSString*)tableView:(UITableView*)tableView titleForHeaderInSection:(NSInteger)section{
    if ([_keys count] == 0){
        return nil;
    }
    NSString *key = [_keys objectAtIndex:section];
    if (key == UITableViewIndexSearch){
        return nil;
    }
    return key;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView*)tableView{
    return ([_keys count] > 0) ? [_keys count] : 1;
}

- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section{
    if ([_keys count] == 0)
        return 0;
    NSString *key = [_keys objectAtIndex:section];
    NSArray *nameSection = [_names objectForKey:key];
    return [nameSection count];
}

- (CGFloat)tableView:(UITableView*)tableView heightForRowAtIndexPath:(NSIndexPath*)indexPath{
    NSString *key = [_keys objectAtIndex:[indexPath section]];
    NSArray *nameSection = [_names objectForKey:key];
    NSArray *namePhones=[nameSection objectAtIndex:[indexPath row]];
    if([[namePhones objectAtIndex:2] isEqualToString:@"1"]){
        //单无姓名
        return 40;
    }else{
        //有姓名的号码
        return 60; 
    }
}

static NSString *SectionsTableIdentifier1 = @"SectionsTableIdentifier1";
static NSString *SectionsTableIdentifier2 = @"ACContactCell";

- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath{
    NSString *key = [_keys objectAtIndex:[indexPath section]];
    NSArray *nameSection = [_names objectForKey:key];
    
    NSArray *namePhones=[nameSection objectAtIndex:[indexPath row]];
    if([[namePhones objectAtIndex:2] isEqualToString:@"1"]){
        //单无姓名
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:
                                 SectionsTableIdentifier1];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]
                    initWithStyle:UITableViewCellStyleDefault
                    reuseIdentifier:SectionsTableIdentifier1];
        }
        cell.textLabel.text=[namePhones objectAtIndex:0];
        return cell;
    }else{
        //双
        ACContactCell *cell = [tableView dequeueReusableCellWithIdentifier:SectionsTableIdentifier2];
        if(!cell){
            cell = [[ACContactCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:SectionsTableIdentifier2];
        }
        cell.lblName.text=[namePhones objectAtIndex:0];
        cell.lblPhone.text=[namePhones objectAtIndex:1];
        return cell;
    }
}

- (void)tableView:(UITableView*)tableView didSelectRowAtIndexPath:(NSIndexPath*)indexPath{
    NSString *key = [_keys objectAtIndex:[indexPath section]];
    NSArray *nameSection = [_names objectForKey:key];
    NSArray *namePhones=[nameSection objectAtIndex:[indexPath row]];
    [self dial:[namePhones objectAtIndex:1]];
}

- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index {
    NSString *key = [_keys objectAtIndex:index];
    if (key == UITableViewIndexSearch) {
        [tableView setContentOffset:CGPointZero animated:NO];
        return NSNotFound;
    } else return index;
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    if (_searching)
        return nil;
    return _keys;
}

#pragma mark Table View Delegate Methods
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [search resignFirstResponder];
    _searching = NO;
    search.text = @"";
    [tableView reloadData];
    return indexPath;
}

#pragma mark Search Bar Delegate Methods

//点击键盘上的search按钮时调用
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    NSString *searchTerm = [searchBar text];
    [self handleSearchForTerm:searchTerm];
    [search resignFirstResponder];
}

//输入文本实时更新时调用
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchTerm {
    if ([searchTerm length] == 0) {
        [self resetSearch];
        [table reloadData];
        return;
    }
    [self handleSearchForTerm:searchTerm];
}

//cancel按钮点击时调用
- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    _searching = NO;
    search.text = @"";
    [self resetSearch];
    [table reloadData];
    [searchBar resignFirstResponder];
}

//点击搜索框时调用
- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    _searching = YES;
    [table reloadData];
}

#pragma mark -
#pragma mark CustomMethod
- (void)resetSearch {
    _names = [_allNames mutableDeepCopy];
    NSMutableArray *keyArray = [[NSMutableArray alloc] init];
    //    [keyArray addObject:UITableViewIndexSearch];
    [keyArray addObjectsFromArray:[[_allNames allKeys]
                                   sortedArrayUsingSelector:@selector(compare:)]];
    _keys = keyArray;
}

- (void)dial:(NSString*)phone{
    if([[[Config Instance]noDialPhoneNumber] containsObject:[Common formatPhone:phone]]){
        [Common alert:@"禁止通过安存语录拨打该号码"];
    }else if([HttpRequest isNetworkConnection]){
        //网络连接时用网络拔号
        NSMutableDictionary *requestParams = [[NSMutableDictionary alloc] init];
        [requestParams setObject:@"1" forKey:@"calltype"];
        [requestParams setObject:[Common formatPhone:phone] forKey:@"oppno"];
        _contactHttp=[[HttpRequest alloc]init];
        [_contactHttp setDelegate:self];
        [_contactHttp setController:self];
        [_contactHttp loginhandle:@"v4Call" requestParams:requestParams];
    }else{
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[[NSString alloc] initWithFormat:@"tel://%@,%@#",PHONENUMBER,phone]]];
    }
}

- (void)handleSearchForTerm:(NSString *)searchTerm {
    NSMutableArray *sectionsToRemove = [[NSMutableArray alloc] init];
    [self resetSearch];
    
    for (NSString *key in _keys) {
        NSMutableArray *array = [_names valueForKey:key];
        NSMutableArray *toRemove = [[NSMutableArray alloc] init];
        for (NSMutableArray *name in array) {
            //基本查找法索引0为当前的显示姓名
            if ([[name objectAtIndex:0] rangeOfString:searchTerm
                                              options:NSCaseInsensitiveSearch].location == NSNotFound){
                //转拼音查找法
                if ([[ChineseToPinyin pinyinFromChiniseString:[name objectAtIndex:0]] rangeOfString:[searchTerm uppercaseString]
                                                                                            options:NSCaseInsensitiveSearch].location == NSNotFound){
                    //号码查找法
                    if ([[Common formatPhone:[name objectAtIndex:1]] rangeOfString:[searchTerm uppercaseString] options:NSCaseInsensitiveSearch].location == NSNotFound){
                        [toRemove addObject:name];
                    }
                }
            }
        }
        if ([array count] == [toRemove count])
            [sectionsToRemove addObject:key];
        
        [array removeObjectsInArray:toRemove];
    }
    [_keys removeObjectsInArray:sectionsToRemove];
    [table reloadData];
}

- (void)refresh:(id)sender{
    BOOL ISCREATEACYULUPHONENUMBER=YES;
    //自iOS6.0后获取通讯录列表需要询问用户，经过用户同意后才可以获取通讯录用户列表。而且ABAddressBookRef的初始化工作也由ABAddressBookCreate函数转变为ABAddressBookCreateWithOptions函数。下面代码是兼容之前版本的获取通讯录用户列表方法。
    ABAddressBookRef addressBook=[Common getAbAddressBook];
    [[Config Instance] setContact:nil];
    if(_allNames==nil){
        _allNames=[[NSMutableDictionary alloc]init];
    }else{
        [_allNames removeAllObjects];
    }
    if (addressBook){
        table.hidden=NO;
        _viewinfo.hidden=YES;
        NSMutableDictionary *refreshFlag=[[NSMutableDictionary alloc]init];
        CFArrayRef results =ABAddressBookCopyArrayOfAllPeople(addressBook);
        for(int i = 0; i < CFArrayGetCount(results); i++){
            ABRecordRef person = CFArrayGetValueAtIndex(results, i);
            
            NSString *firstName = (__bridge NSString*)ABRecordCopyValue(person, kABPersonFirstNameProperty);
            if(firstName==nil){
                firstName=@"";
            }
            NSString *lastname = (__bridge NSString*)ABRecordCopyValue(person, kABPersonLastNameProperty);
            if(lastname==nil){
                lastname=@"";
            }
            NSString *name=[[NSString alloc]initWithFormat:@"%@%@",lastname,firstName];
            
            NSString *hName=nil;
            NSMutableArray *mutable=nil;
            BOOL flag=YES;
            NSString* nameFlag=@"0";
            ABMultiValueRef phoneRef = ABRecordCopyValue(person, kABPersonPhoneProperty);
            
            for (int k = 0; k<ABMultiValueGetCount(phoneRef); k++){
                //的电话值
                NSString * personPhone = (__bridge NSString*)ABMultiValueCopyValueAtIndex(phoneRef, k);
                if(flag) {
                    if([name isEqualToString:@""]){
                        name=personPhone;
                        nameFlag=@"1";
                    }
                    unichar c=[[[[ChineseToPinyin pinyinFromChiniseString:name] substringWithRange:NSMakeRange(0,1)] lowercaseString] characterAtIndex:0];
                    if(c>=97&&c<=122){
                        hName=[NSString stringWithFormat:@"%c",c];
                    }else{
                        hName=@"#";
                    }
                    mutable=[_allNames objectForKey:hName];
                    if(!mutable){
                        mutable=[[NSMutableArray alloc]init];
                    }
                    flag=NO;
                }
                NSArray *nameDic=[[NSArray alloc]initWithObjects:name,personPhone,nameFlag,nil];
                
                NSString *personName=nil;
                NSString *phoneNumber=[Common formatPhone:personPhone];
                //跑过不能拨打的号码
                if([[[Config Instance]noDialPhoneNumber] containsObject:[Common formatPhone:phoneNumber]]){
                    if([[Common formatPhone:phoneNumber] isEqualToString:PHONENUMBER]){
                        ISCREATEACYULUPHONENUMBER=NO;
                    }
                    continue;
//                }else if([phoneNumber length]<7){
//                    continue;
                }
                //是否已经创建了安存语录音电话号码
                if(ISCREATEACYULUPHONENUMBER) {
                    if([name isEqualToString:APPNAME]) {
                        ISCREATEACYULUPHONENUMBER=NO;
                    }
                }
                
                NSMutableDictionary* contact=[[Config Instance]contact];
                if(contact==nil){
                    contact=[[NSMutableDictionary alloc]init];
                }else{
                    if([refreshFlag objectForKey:personPhone]==nil){
                        personName=[contact objectForKey:phoneNumber];
                        if(personName){
                            [contact setObject:@"" forKey:phoneNumber];
                            [refreshFlag setObject:@"1" forKey:personPhone];
                        }
                    }
                }
                if(personName){
                    [contact setObject:[NSString stringWithFormat:@"%@,%@",personName,name] forKey:phoneNumber];
                }else{
                    if([nameFlag isEqualToString:@"1"]){
                        [contact setObject:phoneNumber forKey:phoneNumber];
                    }else{
                        [contact setObject:name forKey:phoneNumber];
                    }
                }
                [[Config Instance]setContact:contact];
                
                [mutable addObject:nameDic];
            }
            if(!flag){
                [_allNames setObject:mutable forKey:hName];
            }
        }
        CFRelease(results);
        if(ISCREATEACYULUPHONENUMBER) {
            //添加安存语录官方电话
            ABRecordRef newPerson = ABPersonCreate();
            CFErrorRef error = NULL;
            ABRecordSetValue(newPerson, kABPersonFirstNameProperty, APPNAME, &error);
            //phone number
            ABMutableMultiValueRef multiPhone = ABMultiValueCreateMutable(kABMultiStringPropertyType);
            ABMultiValueAddValueAndLabel(multiPhone, PHONENUMBER, kABPersonPhoneIPhoneLabel, NULL);
            ABRecordSetValue(newPerson, kABPersonPhoneProperty, multiPhone, &error);
            CFRelease(multiPhone);
            
            ABAddressBookAddRecord(addressBook, newPerson, &error);
            ABAddressBookSave(addressBook, &error);
            CFRelease(newPerson);
        }
        CFRelease(addressBook);
        [self resetSearch];
        [table reloadData];
        [table setContentOffset:CGPointMake(0.0, 44.0) animated:NO];
    }else{
        table.hidden=YES;
        _viewinfo.hidden=NO;
    }
}

- (void)viewDidUnload {
    [self setViewinfo:nil];
    [super viewDidUnload];
}
@end