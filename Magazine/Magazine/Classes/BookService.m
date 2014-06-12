//
//  BookService.m
//  Magazine
//
//  Created by Start on 6/12/14.
//  Copyright (c) 2014 Ancun. All rights reserved.
//

#import "BookService.h"

@implementation BookService

- (Book*)get:(NSString*)periods
{
    ACAppDelegate *delegate=(ACAppDelegate*)[[UIApplication sharedApplication]delegate];
    NSManagedObjectContext *objectContext=delegate.managedObjectContext;
    
    NSFetchRequest *request=[[NSFetchRequest alloc]init];
    NSEntityDescription *entity=[NSEntityDescription entityForName:@"Book" inManagedObjectContext:objectContext];
    [request setEntity:entity];
    NSPredicate *query=[NSPredicate predicateWithFormat:@"periods == %@",periods];
    [request setPredicate:query];
    
    NSError *error=nil;
    NSArray *results=[objectContext executeFetchRequest:request error:&error];
    if([results count]>0){
        Book *book=(Book *)[results objectAtIndex:0];
        return book;
    }
    
    return nil;
}

- (BOOL)save:(NSDictionary*)data
{
    NSString *periods=[data objectForKey:@"periods"];
    Book *book=[self get:periods];
    ACAppDelegate *delegate=(ACAppDelegate*)[[UIApplication sharedApplication]delegate];
    NSManagedObjectContext *objectContext=delegate.managedObjectContext;
    //判断是否已经存在已经存在则更新期刊信息
    if(book==nil){
        //添加
        book=(Book*)[NSEntityDescription insertNewObjectForEntityForName:@"Book" inManagedObjectContext:objectContext];
    }
    [book setBookAuthor:[data objectForKey:@"bookAuthor"]];
    [book setBookId:[data objectForKey:@"bookId"]];
    [book setBookType:[data objectForKey:@"bookType"]];
    [book setCollect:[data objectForKey:@"collect"]];
    [book setDescriptions:[data objectForKey:@"description"]];
    [book setEndPageUrl:[data objectForKey:@"endPageUrl"]];
    [book setFrontPageUrl:[data objectForKey:@"frontPageUrl"]];
    [book setIntroduction:[data objectForKey:@"introduction"]];
    [book setPeriods:[data objectForKey:@"periods"]];
    [book setPrice:[data objectForKey:@"price"]];
    [book setRecommmend:[data objectForKey:@"recommmend"]];
    [book setTotalPage:[data objectForKey:@"totalPage"]];
    NSString *index=[data objectForKey:@"index"];
    if(index){
        [book setIndex:index];
    }else{
        [book setIndex:@"0"];
    }
    NSString *readpoint=[data objectForKey:@"readpoint"];
    if(readpoint){
        [book setReadpotin:readpoint];
    }else{
        [book setReadpotin:@"0"];
    }
    return [objectContext save:nil];
}

- (BOOL)saveByBook:(Book *)book
{
    ACAppDelegate *delegate=(ACAppDelegate*)[[UIApplication sharedApplication]delegate];
    NSManagedObjectContext *objectContext=delegate.managedObjectContext;
    return [objectContext save:nil];
}

@end
