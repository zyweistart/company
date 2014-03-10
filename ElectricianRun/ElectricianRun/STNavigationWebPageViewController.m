#import "STNavigationWebPageViewController.h"

@interface STNavigationWebPageViewController ()

@end

@implementation STNavigationWebPageViewController

- (id)initWithNavigationTitle:(NSString *)navigationTitle resourcePath:(NSString *)resourcePath {
    self = [super init];
    if (self) {
        self.navigationItem.title=navigationTitle;
        UIWebView *webView=[[UIWebView alloc]initWithFrame:
                            CGRectMake(0, 0,
                                       self.view.frame.size.width,
                                       self.view.frame.size.height)];
        [webView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        NSString *html= [NSString stringWithContentsOfFile:[[NSBundle mainBundle] pathForResource:resourcePath ofType:@"html"] encoding:NSUTF8StringEncoding error:nil];
        [webView loadHTMLString:html baseURL:nil];
        [self.view addSubview:webView];
    }
    return self;
}

@end
