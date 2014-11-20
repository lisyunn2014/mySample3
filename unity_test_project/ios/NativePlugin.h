#import <UIKit/UIKit.h>
#import "GAI.h"
#import "GAIDictionaryBuilder.h"
#import "GAIFields.h"
#import "Social/Social.h"
#import <AidAd/AidAd.h>

@interface NativePlugin: UIViewController  <UIWebViewDelegate, AidAdDialogBlocker>{

    NSString* _webview_url;
    NSString *st_chk_str;
    UIAlertView *alert2;
    UIAlertView *alert3;
    UIAlertView *alert9;
}


+ (void)initAd;
- (void)initBannerAd1:(const char*) url;
- (void)hideBannerAd1;
- (void)showBannerAd1;
- (void)hideBannerAd2;
- (void)showBannerAd2;
- (void)initGA:(const char*)ga_tracking_id;
- (void)startGAView:(const char*)viewName;
- (void)startGAEvent:(const char*)category AtAction:(const char*)action AtLabel:(const char*) label;
- (void)sendFacebook:(const char*)message;
- (void)sendTwitter:(const char*)message;
- (void)sendLine:(const char*)message;
- (void)showWallAd1;
- (void)showWallAd2;
- (void)showWallAd3;
- (void)showWallAd4;
- (void)showWallAd5;
- (void)showDialogAd1;
- (void)showDialogAd2;
- (int) initOwnImage:(const char*)path AtType:(int)type;
- (void) showOwnImage:(int)pos;
- (void) hideOwnImage:(int)pos;
- (int) chkStatus;


@property (nonatomic, retain) UIWebView *uiWebView;

@property (nonatomic, retain) NSString *webview_url;

@end

