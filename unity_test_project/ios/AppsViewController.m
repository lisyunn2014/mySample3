//
//  AppsViewController.m
//  PenstaProject
//
//  Created by Iwao Suzuki on 2013/12/26.
//
//

#import "AppsViewController.h"

@interface AppsViewController ()

@end

@implementation AppsViewController

@synthesize wv;
@synthesize apps;
@synthesize store;

- (void)dealloc {
    [wv release];
    [super dealloc];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    //[UIApplication sharedApplication].statusBarHidden = YES;
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    [[NSURLCache sharedURLCache] setDiskCapacity:0];
    [[NSURLCache sharedURLCache] setMemoryCapacity:0];
    NSURLRequest *req = [NSURLRequest requestWithURL:[NSURL URLWithString:apps]];
    [wv loadRequest:req];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)close:(id)sender {
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)store:(id)sender {
    
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:store]];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
    //NSLog(@"error %@", error);
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"接続できません"
                                                    message:@"渋三あっぷすに接続できません。ネットワークの状態を確認してください。"
                                                   delegate:self
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    if (navigationType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL: [request URL]];
        return NO;
    }
    return YES;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
