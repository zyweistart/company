#import "ACPeriodicalContentViewController.h"

@interface ACPeriodicalContentViewController () <UIWebViewDelegate>

@property (strong,nonatomic)NSDictionary *data;

@property (strong,nonatomic) UIWebView *webView;

@end

@implementation ACPeriodicalContentViewController

- (id)initWithData:(NSDictionary *)data
{
    self = [super init];
    if (self) {
        self.data=data;
        self.webView=[[UIWebView alloc]initWithFrame:self.view.bounds];
        [self.webView setDelegate:self];
        [self.webView.scrollView setBounces:NO];
        [self.webView  setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
        [self.view addSubview:self.webView];
    }
    return self;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    
}

- (void)loadData
{
    NSString *url=[self.data objectForKey:@"downloadUrl"];
    NSString *fileName=[url substringFromIndex:30];
    [Common loadHtmlWithWebView:self.webView url:url fileName:fileName];
}

@end
