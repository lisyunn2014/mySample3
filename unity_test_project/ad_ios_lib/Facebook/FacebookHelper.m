//
//  FacebookHelper.m
//  HauteCouture
//
//  Created by 尚 光泉 on 13-9-5.
//
//

#import "FacebookHelper.h"
#import <FacebookSDK/FacebookSDK.h>

@implementation FacebookHelper

int FaceBookErrorBtnCount = 1;
const char * shareMessage="";

#pragma mark ---- Activity  Start and Stop
-(void)share:(const char *)_shareMessage{
    FaceBookErrorBtnCount = 1;
    
    [self urlConnectionTest];
    [self activityStart];
    
    shareMessage = _shareMessage;
}

//Facebookに接続する
-(void)urlConnectionTest{
    NSURL * url=[NSURL URLWithString:@"https://m.facebook.com"];
    NSURLRequest * request=[[[NSURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy  timeoutInterval:10]autorelease];
    NSURLConnection* connection=[[[NSURLConnection alloc]initWithRequest:request delegate:self]autorelease];
    if (!connection) {
        
    }
}

//Facebookに接続できない場合のコールバック
#pragma mark connection delegate
-(void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    NSLog(@"connection fail!");
    
    [self activityStop];
    
    UIAlertView * a = [[UIAlertView alloc]initWithTitle:@"Facebook" message:@"Facebookに接続できませんでした、ネットの状況をご確認ください。" delegate:@"" cancelButtonTitle:@"OK" otherButtonTitles:nil];
    [a show];
    [a release];
    
}

//Facebookに接続完了する時のイベント
-(void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    NSLog(@"connectionDidFinishLoading!");
    [self activityStop];
    
    if (!FBSession.activeSession.isOpen || FBSession.activeSession.state !=FBSessionStateOpen ) {
        if (FBSession.activeSession.state != FBSessionStateCreated) {
            // Create a new, logged out session.
            FBSession.activeSession = [[FBSession alloc] initWithPermissions:[NSArray arrayWithObject:@"publish_actions"]];
        }
        NSLog(@"ioioioo+%@",FBSession.activeSession);
        // if the session isn't open, let's open it now and present the login UX to the user
        [FBSession.activeSession openWithBehavior:FBSessionLoginBehaviorForcingWebView completionHandler:^(FBSession *session, FBSessionState status, NSError *error) {
            if (error) {
                NSLog(@"open session error:%@",error);
                [self activityStop];
                NSLog(@"open session error:%@",error);
                UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"Facebook" message:@"Facebookアカウントの使用を許可していません。\n設定画面で「オン」に設定してください。" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alert show];
                [alert release];
                
                [[FBSession activeSession] closeAndClearTokenInformation];
            } else {
                
                // send our requests if we successfully logged in
                switch (status) {
                    case FBSessionStateCreated:
                        NSLog(@"***FBSessionStateCreated");
                        break;
                    case FBSessionStateCreatedTokenLoaded:
                        NSLog(@"***FBSessionStateCreatedTokenLoaded");
                        break;
                    case FBSessionStateCreatedOpening:
                        NSLog(@"***FBSessionStateCreatedOpening");
                        break;
                    case FBSessionStateOpen:
                        NSLog(@"***FBSessionStateOpen");
                        break;
                    case FBSessionStateOpenTokenExtended:
                        NSLog(@"***FBSessionStateOpenTokenExtended");
                        break;
                    case FBSessionStateClosedLoginFailed:
                        NSLog(@"***FBSessionStateClosedLoginFailed");
                        break;
                    default:
                        NSLog(@"***FBSessionStateClosed");
                        break;
                }
                [self shareMessage:shareMessage];
            }
            
        }];
    } else {
        [self shareMessage:shareMessage];
    }

}

//メッセージアップロードのイベント
-(void)shareMessage:(const char *)shareMessage{
    
    NSString * shareMessageStr = [[NSString alloc] initWithUTF8String:shareMessage];
    
    FBRequest * myRequest = [FBRequest requestForPostStatusUpdate:shareMessageStr];
    
    //上传成功或失败时的回调方法
    [myRequest startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
        
        //アップ失敗した時のコールバックイベント
        if (error) {
            if (FaceBookErrorBtnCount == 1) {
                NSLog(@"share message error:%@",error);
                
                NSDictionary * a = (NSDictionary *)[error.userInfo objectForKey:@"com.facebook.sdk:ParsedJSONResponseKey"];
                NSDictionary * b = (NSDictionary *)[a objectForKey:@"body"];
                NSDictionary * c = (NSDictionary *)[b objectForKey:@"error"];
                int d = [[c objectForKey:@"code"] intValue];
                
                if (d == 200) {

                    [[FBSession activeSession] requestNewPublishPermissions:[NSArray arrayWithObject:@"publish_actions"] defaultAudience:FBSessionDefaultAudienceEveryone completionHandler:^(FBSession *permissionsSession,                                                                                                                                                                                                                                                                                                              NSError *permissionsError) {
                        if (permissionsError) {
                            
                        } else {
                            
                        }
                    }];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"Facebook" message:@"投稿できませんでした。" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alert show];
                    [alert release];
                }
                FaceBookErrorBtnCount = 2;
                
            }
        }
        
        //アップ成功した時のコールバックイベント
        else {
            UIAlertView * a = [[UIAlertView alloc]initWithTitle:@"Facebook" message:@"投稿しました！" delegate:@"" cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [a show];
            [a release];
        }
        [self activityStop];
    }];
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
