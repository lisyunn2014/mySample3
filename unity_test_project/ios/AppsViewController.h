//
//  AppsViewController.h
//  PenstaProject
//
//  Created by Iwao Suzuki on 2013/12/26.
//
//

#import <UIKit/UIKit.h>

@interface AppsViewController : UIViewController
{
    UIWebView *wv;
    NSString *apps;
    NSString *store;
}

@property (nonatomic, retain) IBOutlet UIWebView *wv;
@property (nonatomic, retain) NSString *apps;
@property (nonatomic, retain) NSString *store;

- (IBAction)close:(id)sender;
- (IBAction)store:(id)sender;

@end
