//
//  DataPickerView.h
//  ElectricianRun
//
//  Created by Start on 2/22/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol DataPickerViewDelegate

@optional

- (void)pickerDidPressDoneWithRow:(NSInteger)row;

- (void)pickerDidPressCancel;

@end


@interface DataPickerView : UIView<UIPickerViewDelegate>

@property (nonatomic, strong) id<DataPickerViewDelegate> delegate;

@property (nonatomic, strong) UIPickerView *dataPicker;

- (id)initWithData:(NSArray *)data;

@end