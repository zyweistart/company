#import <AVFoundation/AVFoundation.h>

@interface ACPlayerView : UIView<AVAudioPlayerDelegate,UIActionSheetDelegate,ResultDelegate,HttpViewDelegate>

@property (strong,nonatomic) NSString *path;
@property (strong,nonatomic) NSMutableDictionary *dictionary;
@property (strong,nonatomic) UIViewController *controller;

@property (strong, nonatomic) UIButton *btn_notary;
@property (strong, nonatomic) UIButton *btn_extraction;
@property (strong, nonatomic) UIButton *btn_player;
@property (strong, nonatomic) UISlider *sider_player;
@property (strong, nonatomic) UILabel *lbl_playertimerlong;
@property (strong, nonatomic) UILabel *lbl_playertimertotallong;


- (id)initWithController:(UIViewController*)controller;

- (void)player:(NSString *)playerPath dictionary:(NSDictionary*)dic;
- (void)stop;

@end