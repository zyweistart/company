//
//  STMapViewController.m
//  ElectricianRun
//
//  Created by Start on 2/20/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STMapViewController.h"
#import <MapKit/MapKit.h>

@interface STMapViewController () <MKMapViewDelegate>

@end

@implementation STMapViewController {
    
    MKMapView *_mapView;
    CLLocationCoordinate2D _coordinate;
    
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title=@"工作人员位置";
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        _mapView=[[MKMapView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        [_mapView setDelegate:self];
        [self.view addSubview:_mapView];
        
        self.navigationItem.rightBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"定位"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(goLocation:)];
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _mapView.showsUserLocation = YES;
}

- (void)goLocation:(id)sender {
    CLLocationCoordinate2D fromCoordinate = _coordinate;
    
    
    CLLocationCoordinate2D toCoordinate   = CLLocationCoordinate2DMake(32.010241,
                                                                       118.719635);
    
    MKPlacemark *fromPlacemark = [[MKPlacemark alloc] initWithCoordinate:fromCoordinate
                                                       addressDictionary:nil];
    
    MKPlacemark *toPlacemark   = [[MKPlacemark alloc] initWithCoordinate:toCoordinate
                                                       addressDictionary:nil];
    
    MKMapItem *fromItem = [[MKMapItem alloc] initWithPlacemark:fromPlacemark];
    
    MKMapItem *toItem   = [[MKMapItem alloc] initWithPlacemark:toPlacemark];
    
    [self findDirectionsFrom:fromItem
                          to:toItem];
    
}

#pragma mark - Private

- (void)findDirectionsFrom:(MKMapItem *)source
                        to:(MKMapItem *)destination
{
    MKDirectionsRequest *request = [[MKDirectionsRequest alloc] init];
    request.source = source;
    request.destination = destination;
    request.requestsAlternateRoutes = YES;
    
    MKDirections *directions = [[MKDirections alloc] initWithRequest:request];
    
    [directions calculateDirectionsWithCompletionHandler:
     ^(MKDirectionsResponse *response, NSError *error) {
         
         if (error) {
             
             NSLog(@"error:%@", error);
         }
         else {
             
             MKRoute *route = response.routes[0];
             
             [_mapView addOverlay:route.polyline];
         }
     }];
}

#pragma mark - MKMapViewDelegate

- (MKOverlayRenderer *)mapView:(MKMapView *)mapView
            rendererForOverlay:(id<MKOverlay>)overlay
{
    MKPolylineRenderer *renderer = [[MKPolylineRenderer alloc] initWithOverlay:overlay];
    renderer.lineWidth = 5.0;
    renderer.strokeColor = [UIColor purpleColor];
    return renderer;
}

- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    _coordinate.latitude = userLocation.location.coordinate.latitude;
    _coordinate.longitude = userLocation.location.coordinate.longitude;
    
    [self setMapRegionWithCoordinate:_coordinate];
}

- (void)setMapRegionWithCoordinate:(CLLocationCoordinate2D)coordinate
{
    MKCoordinateRegion region;
    
    region = MKCoordinateRegionMake(coordinate, MKCoordinateSpanMake(.1, .1));
    MKCoordinateRegion adjustedRegion = [_mapView regionThatFits:region];
    [_mapView setRegion:adjustedRegion animated:YES];
}

@end
