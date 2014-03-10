//
//  STTaskAuditMapViewController.m
//  ElectricianRun
//  任务稽核-工作人员位置
//  Created by Start on 1/25/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STTaskAuditMapViewController.h"
#import "STViewUserListViewController.h"
#import <MapKit/MapKit.h>
#import "CustomAnnotation.h"
#import "STGPSSearchViewController.h"

#define ZOOMLEVEL 0.02

//按日
#define REUQESTCODEDAY 1
//按月
#define REQUESTCODEMONTH 2
//按轨迹
#define REQUESTCODELOCUS 3
//按位置
#define REQUESTCODELOCATION 4

@interface STTaskAuditMapViewController () <MKMapViewDelegate,HttpRequestDelegate>

@end

@implementation STTaskAuditMapViewController {
    
    HttpRequest *hRequest;
    MKMapView *_mapView;
    CLLocationManager *_locationManager;
    NSMutableArray *overlays;
    
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"工作人员位置";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.navigationItem.rightBarButtonItems=[[NSArray alloc]initWithObjects:
                                                 [[UIBarButtonItem alloc]
                                                  initWithTitle:@"用户"
                                                  style:UIBarButtonItemStyleBordered
                                                  target:self
                                                  action:@selector(viewuser:)],
                                                 [[UIBarButtonItem alloc]
                                                  initWithTitle:@"搜索"
                                                  style:UIBarButtonItemStyleBordered
                                                  target:self
                                                  action:@selector(search:)], nil];
        overlays=[[NSMutableArray alloc]init];
    }
    return self;
}

- (void)viewDidLoad
{
    _mapView=[[MKMapView alloc]initWithFrame:[self.view bounds]];
    [_mapView setDelegate:self];
    [_mapView setMapType:MKMapTypeStandard];
    [_mapView setScrollEnabled:YES];
    [_mapView setZoomEnabled:YES];
    [self.view addSubview:_mapView];
  
    //显示默认的位置
    CLLocationCoordinate2D coords = CLLocationCoordinate2DMake(30.287786360161632,
                                                               120.15082687139511);
	MKCoordinateRegion region = MKCoordinateRegionMake(coords,MKCoordinateSpanMake(ZOOMLEVEL, ZOOMLEVEL));
	[_mapView setRegion:[_mapView regionThatFits:region] animated:YES];
    
    [super viewDidLoad];
}

- (void)search:(id)sender
{
    STGPSSearchViewController *gpsSearchViewController=[[STGPSSearchViewController alloc]init];
    [gpsSearchViewController setDelegate:self];
    [gpsSearchViewController setSearchData:nil];
    [self.navigationController pushViewController:gpsSearchViewController animated:YES];
}

- (void)viewuser:(id)sender
{
    STViewUserListViewController *viewUserListViewController=[[STViewUserListViewController alloc]init];
    [viewUserListViewController setDelegate:self];
    [self.navigationController pushViewController:viewUserListViewController animated:YES];
    [viewUserListViewController autoRefresh];
}

- (MKOverlayView *)mapView:(MKMapView *)mapView viewForOverlay:(id<MKOverlay>)overlay
{
    if ([overlay isKindOfClass:[MKPolyline class]])
    {
        MKPolylineView *lineview=[[MKPolylineView alloc] initWithOverlay:overlay];
        lineview.strokeColor=[[UIColor blueColor] colorWithAlphaComponent:0.5];
        lineview.lineWidth=3.0;
        return lineview;
    }
    return nil;
}

- (void)startSearch:(NSMutableDictionary *)data {
    //清除地图上的标记
    [_mapView removeAnnotations:[_mapView annotations]];
    //清除地点上的线路
    [_mapView removeOverlays:overlays];
    [overlays removeAllObjects];
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:[data objectForKey:@"phoneNum"] forKey:@"phoneNum"];
    [p setObject:[data objectForKey:@"UUID"] forKey:@"UUid"];
    [p setObject:[data objectForKey:@"userId"] forKey:@"userId"];
    
    [p setObject:@"2012-03-03 00:00" forKey:@"startDate"];
    [p setObject:@"2014-03-03 23:59" forKey:@"endDate"];
    [p setObject:@"2" forKey:@"rtype"];
    [p setObject:@"2" forKey:@"rvalue"];
    
    hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [hRequest setIsShowMessage:YES];
    [hRequest start:URLgetLocationInfo params:p];
}

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    NSLog(@"%@",[response responseString]);
    
    if(repCode==REUQESTCODEDAY){
        //按日
    }else if(repCode==REQUESTCODEMONTH){
        //按月
    }else if(repCode==REQUESTCODELOCATION){
        //按位置
    }else if(repCode==REQUESTCODELOCUS){
        //按轨迹
    }
    
    NSMutableArray *data=[[response resultJSON]objectForKey:@"gpsUserList"];
    int length=[data count];
    //声明一个数组  用来存放画线的点
    MKMapPoint coords[length];
    for(int i=0;i<length;i++){
        NSDictionary *d=[data objectAtIndex:i];
        double latitude=[[d objectForKey:@"latitude"]doubleValue];
        double longitude=[[d objectForKey:@"longitude"]doubleValue];
        coords[i]=MKMapPointForCoordinate(CLLocationCoordinate2DMake(latitude, longitude));
    }
    MKPolyline *line = [MKPolyline polylineWithPoints:coords count:length];
    [overlays addObject:line];
    [_mapView addOverlay:line];
    //开始的位置点
    if(length>=0){
        NSDictionary *d=[data objectAtIndex:0];
        double latitude=[[d objectForKey:@"latitude"]doubleValue];
        double longitude=[[d objectForKey:@"longitude"]doubleValue];
        
        CLLocationCoordinate2D cll = CLLocationCoordinate2DMake(latitude,longitude);
        MKCoordinateRegion region = MKCoordinateRegionMake(cll, MKCoordinateSpanMake(ZOOMLEVEL, ZOOMLEVEL));
        [_mapView setRegion:[_mapView regionThatFits:region] animated:YES];
        
        CustomAnnotation *annotation = [[CustomAnnotation alloc] initWithCoordinate:cll];
        annotation.title = [d objectForKey:@"userName"];
        annotation.subtitle = [d objectForKey:@"gpsTime"];
        
        [_mapView addAnnotation:annotation];
    }
    //结束的位置点
    if(length>1){
        NSDictionary *d=[data objectAtIndex:length-1];
        double latitude=[[d objectForKey:@"latitude"]doubleValue];
        double longitude=[[d objectForKey:@"longitude"]doubleValue];
        
        CLLocationCoordinate2D cll = CLLocationCoordinate2DMake(latitude,longitude);
        MKCoordinateRegion region = MKCoordinateRegionMake(cll, MKCoordinateSpanMake(ZOOMLEVEL, ZOOMLEVEL));
        [_mapView setRegion:[_mapView regionThatFits:region] animated:YES];
        
        CustomAnnotation *annotation = [[CustomAnnotation alloc] initWithCoordinate:cll];
        annotation.title = [d objectForKey:@"userName"];
        annotation.subtitle = [d objectForKey:@"gpsTime"];
        
        [_mapView addAnnotation:annotation];
    }
}

@end
