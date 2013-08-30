@interface XML : NSObject

+ (NSString*)generate:(NSString*)action requestParams:(NSMutableDictionary*)requestParams;

+ (Response*)analysis:(NSString*)xmlContent;

@end
