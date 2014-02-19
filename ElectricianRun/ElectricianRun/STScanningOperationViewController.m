//
//  STScanningOperationViewController.m
//  ElectricianRun
//  扫描操作
//  Created by Start on 2/19/14.
//  Copyright (c) 2014 Start. All rights reserved.
//

#import "STScanningOperationViewController.h"
#import "STScanningViewController.h"

@interface STScanningOperationViewController()<ScanningDelegate>

@end

@implementation STScanningOperationViewController {
    NSArray *selectData;
    UITextField *txtValue1;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        self.title=@"扫描操作";
        
        [self.view setBackgroundColor:[UIColor whiteColor]];
        
        self.navigationItem.leftBarButtonItem=[[UIBarButtonItem alloc]
                                               initWithTitle:@"返回"
                                               style:UIBarButtonItemStyleBordered
                                               target:self
                                               action:@selector(back:)];
        
        selectData=[[NSArray alloc]initWithObjects:@"请选择",@"线路实时信息",@"更换采集器",nil];
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIControl *control=[[UIControl alloc]initWithFrame:CGRectMake(0, 64, 320, 300)];
    [control addTarget:self action:@selector(backgroundDoneEditing:) forControlEvents:UIControlEventTouchDown];
    [self.view addSubview:control];
    
    UILabel *lblValue1=[[UILabel alloc]initWithFrame:CGRectMake(10, 100, 60, 30)];
    lblValue1.font=[UIFont systemFontOfSize:12.0];
    [lblValue1 setText:@"二维码"];
    [lblValue1 setTextColor:[UIColor blackColor]];
    [lblValue1 setBackgroundColor:[UIColor clearColor]];
    [lblValue1 setTextAlignment:NSTextAlignmentRight];
    [control addSubview:lblValue1];
    
    txtValue1=[[UITextField alloc]initWithFrame:CGRectMake(80, 100, 150, 30)];
    [txtValue1 setFont:[UIFont systemFontOfSize: 12.0]];
    [txtValue1 setClearButtonMode:UITextFieldViewModeWhileEditing];
    [txtValue1 setBorderStyle:UITextBorderStyleRoundedRect];
    [txtValue1 setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [txtValue1 setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [txtValue1 setKeyboardType:UIKeyboardTypePhonePad];
    [control addSubview:txtValue1];
    
    UIButton *btnCalculate=[[UIButton alloc]initWithFrame:CGRectMake(235, 100, 30, 30)];
    [btnCalculate setTitle:@"..." forState:UIControlStateNormal];
    [btnCalculate setBackgroundColor:[UIColor blueColor]];
//    [btnCalculate setBackgroundImage:[UIImage imageNamed:@"button_gb"] forState:UIControlStateNormal];
    [btnCalculate addTarget:self action:@selector(scanning:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnCalculate];
    
    UIButton *btnSubmit=[[UIButton alloc]initWithFrame:CGRectMake(110, 160, 100, 30)];
    [btnSubmit setTitle:@"提交" forState:UIControlStateNormal];
    [btnSubmit setBackgroundColor:[UIColor blueColor]];
//    [btnSubmit setBackgroundImage:[UIImage imageNamed:@"button_gb"] forState:UIControlStateNormal];
    [btnSubmit addTarget:self action:@selector(submit:) forControlEvents:UIControlEventTouchUpInside];
    [control addSubview:btnSubmit];
    
    UIPickerView* pickerView = [ [ UIPickerView alloc] initWithFrame:CGRectMake(0.0,352.0,320.0,216.0)];
    pickerView.delegate = self;
    pickerView.dataSource =  self;
    [self.view addSubview:pickerView];
    
}

- (void)submit:(id)sender {
    NSLog(@"SUBMIT");
}

- (void)scanning:(id)sender {
    STScanningViewController *scanningViewController=[[STScanningViewController alloc]init];
    [scanningViewController setDelegate:self];
    [self presentViewController:scanningViewController animated:YES completion:nil];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    
}

-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
{
    if (!view) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 100, 30)];
        label.text = [selectData objectAtIndex:row];
        label.textColor = [UIColor blueColor];
        label.font=[UIFont systemFontOfSize:14];
        view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 100, 30)];
        [view addSubview:label];
    }
    return view ;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [selectData count];
}

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

-(CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
    return 30.0f;
}

- (void)back:(id)sender{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)backgroundDoneEditing:(id)sender {
    [txtValue1 resignFirstResponder];
}

- (void)success:(NSString*)value {
    [txtValue1 setText:value];
    [self backgroundDoneEditing:nil];
}

@end
