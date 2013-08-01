//
//  XML.m
//  ACyulu
//
//  Created by Start on 12-11-17.
//  Copyright (c) 2012年 Start. All rights reserved.
//

#import "XML.h"
#import "TBXML.h"

@implementation XML

//生成XML
+ (NSString*)generate:(NSString*)action requestParams:(NSMutableDictionary*)requestParams{
    NSMutableString *postXML=[[NSMutableString alloc]init];
    
    [postXML appendFormat:@"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"];
    [postXML appendFormat:@"<request>"];
    [postXML appendFormat:@"<common>"];
    [postXML appendFormat:@"<action>"];
    [postXML appendFormat:@"%@",action];
    [postXML appendFormat:@"</action>"];
    [postXML appendFormat:@"<reqtime>"];
    NSDateFormatter *dateFormatter=[[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyyMMddHHmmss"];
    [postXML appendFormat:@"%@",[dateFormatter stringFromDate:[NSDate date]]];
    [postXML appendFormat:@"</reqtime>"];
    [postXML appendFormat:@"</common>"];
    [postXML appendFormat:@"<content>"];
    for (id key in requestParams){
        [postXML appendFormat:@"<%@>",key];
        [postXML appendFormat:@"%@",[requestParams objectForKey:key]];
        [postXML appendFormat:@"</%@>",key];
        
    }
    [postXML appendFormat:@"</content>"];
    [postXML appendFormat:@"</request>"];
    return [postXML description];
}

//解析XML
+ (Response*)analysis:(NSString*)xmlContent{
    Response *response=[[Response alloc]init];
    TBXML *xml = [[TBXML alloc] initWithXMLString:xmlContent error:nil];
    TBXMLElement *root = xml.rootXMLElement;
    TBXMLElement *info = [TBXML childElementNamed:@"info" parentElement:root];
    TBXMLElement *code = [TBXML childElementNamed:@"code" parentElement:info];
    TBXMLElement *msg = [TBXML childElementNamed:@"msg" parentElement:info];
    
    [response setCode:[TBXML textForElement:code]];
    [response setMsg:[TBXML textForElement:msg]];
    
    TBXMLElement *content = [TBXML childElementNamed:@"content" parentElement:root];
    if(content){
        
        //pageinfo
        TBXMLElement *pageinfo = [TBXML childElementNamed:@"pageinfo" parentElement:content];
        
        if(pageinfo){
            NSMutableDictionary *pageInfoContent=[[NSMutableDictionary alloc]init];
            
            TBXMLElement *totalcount = [TBXML childElementNamed:@"totalcount" parentElement:pageinfo];
            TBXMLElement *totalpage = [TBXML childElementNamed:@"totalpage" parentElement:pageinfo];
            TBXMLElement *pagesize = [TBXML childElementNamed:@"pagesize" parentElement:pageinfo];
            TBXMLElement *currentpage = [TBXML childElementNamed:@"currentpage" parentElement:pageinfo];
            
            [pageInfoContent setObject:[TBXML textForElement:totalcount]
                                forKey:[TBXML elementName:totalcount]];
            [pageInfoContent setObject:[TBXML textForElement:totalpage]
                                forKey:[TBXML elementName:totalpage]];
            [pageInfoContent setObject:[TBXML textForElement:pagesize]
                                forKey:[TBXML elementName:pagesize]];
            [pageInfoContent setObject:[TBXML textForElement:currentpage]
                                forKey:[TBXML elementName:currentpage]];
            
            [response setPageInfo:pageInfoContent];
            
            //mainContent
            [TBXML iterateElementsForQuery:@"*" fromElement:content withBlock:^(TBXMLElement *listItem) {
                if(![[TBXML elementName:listItem] isEqualToString:@"pageinfo"]){
                    
                    NSMutableArray *nsArr=[[NSMutableArray alloc]init];
                    [TBXML iterateElementsForQuery:@"*" fromElement:listItem withBlock:^(TBXMLElement *singItem) {
                        NSMutableDictionary *dicContent=[[NSMutableDictionary alloc]init];
                        [TBXML iterateElementsForQuery:@"*" fromElement:singItem withBlock:^(TBXMLElement *te) {
                            [dicContent setObject:[TBXML textForElement:te] forKey:[TBXML elementName:te]];
                        }];
                        [nsArr addObject:dicContent];
                    }];
                    [response setDataItemArray:nsArr];
                }
            }];
        }else{
            __block NSMutableDictionary *mainDataDics=[[NSMutableDictionary alloc]init];
            [TBXML iterateElementsForQuery:@"*" fromElement:content withBlock:^(TBXMLElement *te){
                TBXMLElement *mainData = [TBXML childElementNamed:[TBXML elementName:te] parentElement:content];
                NSMutableDictionary *dicContent=[[NSMutableDictionary alloc]init];
                [TBXML iterateElementsForQuery:@"*" fromElement:mainData withBlock:^(TBXMLElement *chte){
                    [dicContent setObject:[TBXML textForElement:chte] forKey:[TBXML elementName:chte]];
                }];
                [mainDataDics setObject:dicContent forKey:[TBXML elementName:te]];
            }];
            [response setMainData:mainDataDics];
        }
    }
    return response;
}

@end
