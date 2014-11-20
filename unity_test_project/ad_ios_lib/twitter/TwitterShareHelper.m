//
//  TwitterShareHelper.m
//
//
//  //
//

#import "TwitterShareHelper.h"
#import <Twitter/Twitter.h>
#import <Social/Social.h>
#import "UnityAppController.h"

@implementation TwitterShareHelper
@synthesize sharedMessage;

//twitterに接続する
-(void)goShareMessage:(NSString *)message withConnectionState:(bool *)isConnected andSharedState:(bool *)isSuccessShared andCancelState:(bool *)isCanceled{
    [self activityStart];
    connectedPtr = isConnected;
    sharedPtr = isSuccessShared;
    cancelPtr = isCanceled;
    //
    * connectedPtr = NO;
    * sharedPtr = NO;
    * cancelPtr = NO;
    if (message) {
        self.sharedMessage = message;
    }else{
        self.sharedMessage=@"";
    }
    NSURL * url=[NSURL URLWithString:@"http://mobile.twitter.com"];
    NSURLRequest * request=[[[NSURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy  timeoutInterval:10]autorelease];
    NSURLConnection * connection=[[[NSURLConnection alloc]initWithRequest:request delegate:self]autorelease];
    if (!connection) {
        
    }
}

#pragma mark connection delegate

//twitterに接続失敗した時のコールバックイベント
-(void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    [self activityStop];
    
    NSLog(@"connection fail!");
    * connectedPtr = NO;
    
    UIAlertView * a = [[UIAlertView alloc]initWithTitle:nil message:@"Twitterに接続できません、ネットの状況をご確認ください。" delegate:@"" cancelButtonTitle:@"確定" otherButtonTitles:nil];
    [a show];
    [a release];
}

//twitterに接続完了する時のコールバックイベント
-(void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    NSLog(@"connection　Did　Finish　Loading!");
    * connectedPtr = YES;
    //
    [self activityStop];
    
    //端末のOSバージョンは6.0以下の場合
    if ([[[UIDevice currentDevice]systemVersion]floatValue] < 6.0) {
        if ([TWTweetComposeViewController canSendTweet]) {
            NSLog(@"canSendTweet ");
        } else {
            NSLog(@"can not SendTweet ");
            UIAlertView * a = [[UIAlertView alloc]initWithTitle:nil message:@"Twitterに接続できません、ネットの状況をご確認ください。" delegate:@"" cancelButtonTitle:@"確定" otherButtonTitles:nil];
            [a show];
        }
        //__block bool *tempIsShared=isSuccessShared;
        TWTweetComposeViewController * tweetViewController = [[TWTweetComposeViewController alloc] init];//This class has been  deprecated in iOS 6
        
        [tweetViewController setInitialText:self.sharedMessage];
        
        // Create the completion handler block.
        [tweetViewController setCompletionHandler:^(TWTweetComposeViewControllerResult result) {
            NSString * output;
            
            switch (result) {
                case TWTweetComposeViewControllerResultCancelled:
                    // The cancel button was tapped.
                    output = @"Tweet cancelled.";
                    //sharedPtr=NO;
                    * cancelPtr = YES;
                    break;
                case TWTweetComposeViewControllerResultDone:
                    // The tweet was sent.
                    output = @"Tweet done.";
                    * sharedPtr = YES;
                    break;
                default:
                    break;
            }
            
            [tweetViewController dismissModalViewControllerAnimated:YES];
            [tweetViewController release];
        }];
        
        UnityAppController * mainDelegate = (UnityAppController *)[[UIApplication sharedApplication]delegate];
        [mainDelegate.rootViewController presentModalViewController:tweetViewController animated:YES];
    }
    
    //端末のOSバージョンは6.0以降の場合
    else {
        SLComposeViewController * tweetViewController = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
        [tweetViewController setInitialText:self.sharedMessage];
        [tweetViewController setCompletionHandler:^(SLComposeViewControllerResult result) {
            NSString * output;
            switch (result) {
                case SLComposeViewControllerResultCancelled:
                    // The cancel button was tapped.
                    output = @"Tweet cancelled.";
                    //*sharedPtr=NO;
                    * cancelPtr = YES;
                    break;
                case SLComposeViewControllerResultDone:
                    // The tweet was sent.
                    output = @"Tweet done.";
                    * sharedPtr = YES;
                    break;
                default:
                    break;
            }
            NSLog(@"output state:%@", output);
            // Dismiss the tweet composition view controller.
            [tweetViewController dismissModalViewControllerAnimated:YES];
        }];
        
        UnityAppController * mainDelegate = (UnityAppController *)[[UIApplication sharedApplication]delegate];
        [mainDelegate.rootViewController presentModalViewController:tweetViewController animated:YES];
        
    }
}

#pragma mark ---- Activity  Start and Stop
/*
 * Activity  animation  start
 */
-(void) activityStart{
    loadingAlert = [[UIAlertView alloc] initWithTitle:nil
                                              message: @"少々お待ちください"
                                             delegate: nil
                                    cancelButtonTitle: nil
                                    otherButtonTitles: nil];
    UIActivityIndicatorView * activityView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
    activityView.frame = CGRectMake(120.f, 48.0f, 37.0f, 37.0f);
    [activityView setColor:[UIColor greenColor]];
    [loadingAlert addSubview:activityView];
    [activityView startAnimating];
    [loadingAlert show];
}

/*
 * Activity  animation  stop
 */
-(void) activityStop{
    [loadingAlert dismissWithClickedButtonIndex:0 animated:YES];
}

@end
