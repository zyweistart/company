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
#import <CoreLocation/CoreLocation.h>
#import "CustomAnnotation.h"

@interface STTaskAuditMapViewController () <MKMapViewDelegate, CLLocationManagerDelegate,HttpRequestDelegate>

@end

@implementation STTaskAuditMapViewController {
    
    HttpRequest *hRequest;
    MKMapView *_mapView;
    CLLocationManager *_locationManager;
    
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
    [_mapView setShowsUserLocation:YES];
    [self.view addSubview:_mapView];
    
    _locationManager = [[CLLocationManager alloc] init];
    _locationManager.delegate = self;
    [_locationManager startUpdatingLocation];
    
    [super viewDidLoad];
}

- (void)search:(id)sender
{
//    int length=3;
//    //声明一个数组  用来存放画线的点
//    MKMapPoint coords[length];
//    coords[0]=MKMapPointForCoordinate(CLLocationCoordinate2DMake(31.484137685, 120.371875243));
//    coords[1]=MKMapPointForCoordinate(CLLocationCoordinate2DMake(31.784044745, 110.371879653));
//    coords[2]=MKMapPointForCoordinate(CLLocationCoordinate2DMake(32.484044745, 150.371879653));
//    MKPolyline *line = [MKPolyline polylineWithPoints:coords count:length];
//    [_mapView addOverlay:line];
    
    NSMutableDictionary *p=[[NSMutableDictionary alloc]init];
    [p setObject:[Account getUserName] forKey:@"imei"];
    [p setObject:[Account getPassword] forKey:@"authentication"];
    [p setObject:@"13600000000" forKey:@"phoneNum"];
    [p setObject:@"357071050721612" forKey:@"UUid"];
    
    [p setObject:@"2012-03-03 00:00" forKey:@"startDate"];
    [p setObject:@"2014-03-03 23:59" forKey:@"endDate"];
    [p setObject:@"10067" forKey:@"userId"];
    [p setObject:@"" forKey:@"rtype"];
//    [p setObject:@"" forKey:@"rvalue"];
    
    hRequest=[[HttpRequest alloc]init:self delegate:self responseCode:500];
    [hRequest setIsShowMessage:YES];
    [hRequest start:URLgetLocationInfo params:p];
}



- (void)viewuser:(id)sender
{
    STViewUserListViewController *viewUserListViewController=[[STViewUserListViewController alloc]init];
    [self.navigationController pushViewController:viewUserListViewController animated:YES];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation {
    [_locationManager stopUpdatingLocation];
    
//    NSString *strLat = [NSString stringWithFormat:@"%.4f",newLocation.coordinate.latitude];
//    NSString *strLng = [NSString stringWithFormat:@"%.4f",newLocation.coordinate.longitude];
//    NSLog(@"Lat: %@  Lng: %@", strLat, strLng);
    
    CLLocationCoordinate2D coords = CLLocationCoordinate2DMake(newLocation.coordinate.latitude,newLocation.coordinate.longitude);
	float zoomLevel = 0.02;
	MKCoordinateRegion region = MKCoordinateRegionMake(coords,MKCoordinateSpanMake(zoomLevel, zoomLevel));
	[_mapView setRegion:[_mapView regionThatFits:region] animated:YES];
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

- (void)requestFinishedByResponse:(Response*)response responseCode:(int)repCode{
    
    if([@"1" isEqualToString:[[response resultJSON]objectForKey:@"result"]]){
        NSMutableArray *data=[[response resultJSON]objectForKey:@"gpsDataInfoList"];
        
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
        
        NSDictionary *d=[data objectAtIndex:length-1];
        double latitude=[[d objectForKey:@"latitude"]doubleValue];
        double longitude=[[d objectForKey:@"longitude"]doubleValue];
        
        CLLocationCoordinate2D cll = CLLocationCoordinate2DMake(latitude,longitude);
        float zoomLevel = 0.02;
        MKCoordinateRegion region = MKCoordinateRegionMake(cll, MKCoordinateSpanMake(zoomLevel, zoomLevel));
        [_mapView setRegion:[_mapView regionThatFits:region] animated:YES];

        CustomAnnotation *annotation = [[CustomAnnotation alloc] initWithCoordinate:cll];
        annotation.title = @"标题";
        annotation.subtitle = @"子标题";
        [_mapView addAnnotation:annotation];
        
        
        [_mapView addOverlay:line];
        
        NSLog(@"%@",data);
    }else{
        [Common alert:@"查询失败"];
    }
    
}
@end
