#import "ACPeriodicalDetailViewController.h"
#import "ACPeriodicalListViewController.h"
#import "Common.h"
#import "BookService.h"

@interface ACPeriodicalDetailViewController ()

@property (strong,nonatomic)NSDictionary *data;
@property (strong,nonatomic)NSArray *dataItemArray;

@end

@implementation ACPeriodicalDetailViewController

- (id)initWithData:(NSDictionary *)data;
{
    self = [super init];
    if (self) {
        self.title=@"期刊详情";
        self.data=data;
        
        BookService *bookService=[[BookService alloc]init];
        [bookService save:self.data];
        
        //初始化UI
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                                initWithTitle:@"关闭"
                                                style:UIBarButtonItemStyleBordered
                                                target:self
                                                action:@selector(close:)];
        UIControl *control=[[UIControl alloc]initWithFrame:self.view.bounds];
        [self.view addSubview:control];
        UIImageView *ivPic=[[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 120, 150)];
        [control addSubview:ivPic];
        UILabel *lbl1=[[UILabel alloc]initWithFrame:CGRectMake(140, 20, 170, 20)];
        lbl1.font=[UIFont systemFontOfSize:14.0];
        [lbl1 setTextColor:[UIColor blackColor]];
        [lbl1 setBackgroundColor:[UIColor clearColor]];
        [lbl1 setTextAlignment:NSTextAlignmentLeft];
        [self.view addSubview:lbl1];
        UILabel *lbl2=[[UILabel alloc]initWithFrame:CGRectMake(140, 50, 170, 20)];
        lbl2.font=[UIFont systemFontOfSize:14.0];
        [lbl2 setTextColor:[UIColor blackColor]];
        [lbl2 setBackgroundColor:[UIColor clearColor]];
        [lbl2 setTextAlignment:NSTextAlignmentLeft];
        [self.view addSubview:lbl2];
        UILabel *lbl3=[[UILabel alloc]initWithFrame:CGRectMake(140, 70, 170, 20)];
        lbl3.font=[UIFont systemFontOfSize:14.0];
        [lbl3 setTextColor:[UIColor blackColor]];
        [lbl3 setBackgroundColor:[UIColor clearColor]];
        [lbl3 setTextAlignment:NSTextAlignmentLeft];
        [self.view addSubview:lbl3];
        UILabel *lbl4=[[UILabel alloc]initWithFrame:CGRectMake(10, 160, 300, 50)];
        lbl4.font=[UIFont systemFontOfSize:14.0];
        [lbl4 setTextColor:[UIColor blackColor]];
        [lbl4 setBackgroundColor:[UIColor clearColor]];
        [lbl4 setTextAlignment:NSTextAlignmentLeft];
        [self.view addSubview:lbl4];
        
        UIView *bottomBtn=[[UIView alloc]initWithFrame:CGRectMake(0, control.frame.size.height-108, 320, 44)];
        [control addSubview:bottomBtn];
        
        UIButton *btn1=[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 105, 44)];
        [btn1 setTitle:@"订阅" forState:UIControlStateNormal];
        [btn1 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [btn1 addTarget:self action:@selector(subscription:) forControlEvents:UIControlEventTouchUpInside];
        [bottomBtn addSubview:btn1];
        UIButton *btn2=[[UIButton alloc]initWithFrame:CGRectMake(105, 0, 110, 44)];
        [btn2 setTitle:@"阅读" forState:UIControlStateNormal];
        [btn2 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [btn2 addTarget:self action:@selector(read:) forControlEvents:UIControlEventTouchUpInside];
        [bottomBtn addSubview:btn2];
        UIButton *btn3=[[UIButton alloc]initWithFrame:CGRectMake(215, 0, 105, 44)];
        [btn3 setTitle:@"已下载" forState:UIControlStateNormal];
        [btn3 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [btn3 addTarget:self action:@selector(download:) forControlEvents:UIControlEventTouchUpInside];
        [bottomBtn addSubview:btn3];
        
        //加载数据
        NSString *frontPageUrl=[data objectForKey:@"frontPageUrl"];
        NSString *fileName=[frontPageUrl substringWithRange:NSMakeRange(33,25)];
        [Common loadImageWithImageView:ivPic url:frontPageUrl fileName:fileName];
        [lbl1 setText:[NSString stringWithFormat:@"期刊号:%@",[self.data objectForKey:@"periods"]]];
        [lbl2 setText:[NSString stringWithFormat:@"收藏:%@",[self.data objectForKey:@"collect"]]];
        [lbl3 setText:[NSString stringWithFormat:@"推荐:%@",[self.data objectForKey:@"recommmend"]]];
        [lbl4 setText:[NSString stringWithFormat:@"%@",[self.data objectForKey:@"description"]]];
    }
    return self;
}
//订阅
- (void)subscription:(id)sender
{
    NSLog(@"subscription");
}
//阅读
- (void)read:(id)sender
{
    ACPeriodicalListViewController *periodicalListViewController=[[ACPeriodicalListViewController alloc]initWithData:self.data];
    [self.navigationController pushViewController:periodicalListViewController animated:YES];
    [periodicalListViewController loadData:self.dataItemArray];
}
//下载
- (void)download:(id)sender
{
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_group_t group = dispatch_group_create();
    //创建文件管理器
    NSFileManager* fileManager = [NSFileManager defaultManager];
    //获取Documents主目录
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    //得到相应的Documents的路径
    NSString* docDir = [paths objectAtIndex:0];
    NSString *documentPath=[docDir stringByAppendingPathComponent:@"documents"];
    //文件夹不存在则创建目录
    if(![fileManager fileExistsAtPath:documentPath]){
        [fileManager createDirectoryAtPath:documentPath withIntermediateDirectories:YES attributes:nil error:nil];
    }
    //更改到待操作的目录下
    [fileManager changeCurrentDirectoryPath:[documentPath stringByExpandingTildeInPath]];
    for(NSDictionary *dv in self.dataItemArray){
        NSString *url=[dv objectForKey:@"downloadUrl"];
        NSString *fileName=[url substringFromIndex:30];
        NSString *path = [documentPath stringByAppendingPathComponent:fileName];
        if(![fileManager fileExistsAtPath:path]){
            dispatch_group_async(group, queue, ^{
                NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]];
                if (data) {
                    //创建数据缓冲区
                    NSMutableData* writer = [[NSMutableData alloc] init];
                    //将字符串添加到缓冲中
                    [writer appendData: data];
                    //将缓冲的数据写入到文件中
                    [writer writeToFile:path atomically:YES];
                }
            });
        }
    }
    dispatch_group_notify(group, dispatch_get_main_queue(), ^{
        [Common alert:@"下载完成了"];
    });
}

- (void)loadDataDataItemArray
{
    NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
    [params setObject:@"gettitlebyjid" forKey:@"act"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.data objectForKey:@"periods"]] forKey:@"jid"];
    self.hRequest=[[HttpRequest alloc]init];
    [self.hRequest setDelegate:self];
    [self.hRequest setController:self];
    [self.hRequest setIsShowMessage:YES];
    [self.hRequest handle:@"" headParams:nil requestParams:params];
}

- (void)requestFinishedByResponse:(Response*)response requestCode:(int)reqCode
{
    if([response successFlag]){
        self.dataItemArray=[[response resultJSON]objectForKey:@"data"];
    }
}

@end
