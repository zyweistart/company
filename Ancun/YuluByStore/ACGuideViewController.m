#import "ACGuideViewController.h"
#import "ACLoginViewController.h"

@interface ACGuideViewController ()

@end

#pragma mark -

@implementation ACGuideViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    EAIntroPage *page1 = [EAIntroPage page];
    page1.bgImage = [UIImage imageNamed:@"guide1"];
    
    EAIntroPage *page2 = [EAIntroPage page];
    page2.bgImage = [UIImage imageNamed:@"guide2"];
    
    EAIntroPage *page3 = [EAIntroPage page];
    page3.bgImage = [UIImage imageNamed:@"guide3"];
    
    EAIntroPage *page4 = [EAIntroPage page];
    page4.bgImage = [UIImage imageNamed:@"guide4"];
    
    EAIntroPage *page5 = [EAIntroPage page];
    page5.bgImage = [UIImage imageNamed:@"guide5"];
    
    EAIntroView *intro = [[EAIntroView alloc] initWithFrame:self.view.bounds andPages:@[page1,page2,page3,page4,page5]];
    
    [intro setDelegate:self];
    [intro showInView:self.view animateDuration:0.0];
}

- (void)introDidFinish {
    //进入应用之后才进行存储
    NSDictionary* infoDict =[[NSBundle mainBundle] infoDictionary];
    [Common setCache:DEFAULTDATA_LASTVERSIONNO data:[infoDict objectForKey:@"CFBundleShortVersionString"]];
    
    [UIApplication sharedApplication].keyWindow.rootViewController=[[ACLoginViewController alloc]init];
}

@end
