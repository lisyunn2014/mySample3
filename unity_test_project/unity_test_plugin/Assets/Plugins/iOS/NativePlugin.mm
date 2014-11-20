#import "NativePlugin.h"
#import "FacebookHelper.h"
#import "TwitterShareHelper.h"
#import "AppsViewController.h"
#import "Reachability.h"

#import "CrossRoad.h"
#import "YAPGif.h"


extern UIViewController* UnityGetGLViewController();

@implementation NativePlugin

#define TWITTER 0
#define FACEBOOK 1
#define LINE 2

#define ADBANNER_VIEW_SIZE_320x50 CGSizeMake( 320.0, 50.0 )

#define kMEDIA_CODE_AID @"id815523895"

#define BUNDLE_VERSION [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleVersionKey]

#define SHIBUSAN_URL_CHECK @"http://shibusan.jp/api/status_check.php?appid=SG0036&v=%@&device=0"
#define MY_IOS_URL @"https://itunes.apple.com/jp/app/id815523895?l=ja&ls=1&mt=8"

static NativePlugin *instance = nil;

bool isConnected;
bool isShared;
bool isCanceled;

UIButton *buttonTrigger1 = nil;
UIButton *buttonTrigger2 = nil;

CrossRoad *_crossroad;

@synthesize webview_url = _webview_url;

+ (void) initAd {
    if(instance != nil) return;
	
    // Init
    NativePlugin *adViewController = [[NativePlugin alloc] init];
    instance = adViewController;
    
}

- (void) initBannerAd1:(const char*)url {
    
    [self StatusCheck];
    
    instance.webview_url = [[[NSString alloc] initWithUTF8String:url] autorelease];
    
    //AID
    AidAdAgent * aidAd = [AidAd agentForMedia:kMEDIA_CODE_AID];
    //テキスト型広告
    [aidAd setPreferredCreativeStyle:kAidAdCreativeStyle_PLAIN_TEXT];
    [aidAd setDialogBlocker:self];
    [aidAd startLoading];
    [NSTimer scheduledTimerWithTimeInterval:3.0f target:self selector:@selector(waidAid:) userInfo:nil repeats:NO];
    
    _crossroad = [[CrossRoad alloc] initWithAppId:@"EB46FD89-B1A3-8150-EE4A-7D16D83721AF"];
    [_crossroad sendConversion];
    
}

-(void) initGA:(const char*)ga_tracking_id {

	NSString *ga_id = [[[NSString alloc] initWithUTF8String:ga_tracking_id] autorelease];

    //GoogleAnalytics初期化
    // V2
    //[GAI sharedInstance].trackUncaughtExceptions = YES;
    //[GAI sharedInstance].dispatchInterval = 20;
    //    [GAI sharedInstance].debug = NO;
    //[[GAI sharedInstance] trackerWithTrackingId:ga_id];
    //    [GAI sharedInstance].defaultTracker.appVersion = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
    
    // V3
    [GAI sharedInstance].trackUncaughtExceptions = YES;
    [GAI sharedInstance].dispatchInterval = 20;
    [[GAI sharedInstance] trackerWithTrackingId:ga_id];

}

-(void) showShibusanAd:(const char*)listurl {
    
    //ネットワーク接続確認
    Reachability *curReach = [Reachability reachabilityForInternetConnection];
    NetworkStatus status = [curReach currentReachabilityStatus];
    if (status==ReachableViaWiFi || status==ReachableViaWWAN) {
        AppsViewController *viewController = [[AppsViewController alloc] init];
        viewController.apps = [[[NSString alloc] initWithUTF8String:listurl] autorelease];
        viewController.store = @"itms-apps://itunes.com/apps/transcosmosinc";
        [UnityGetGLViewController() presentViewController:viewController animated:YES completion:nil];
        [viewController release];
    }else if (status == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"接続できません"
                                                        message:@"渋三あっぷすに接続できません。ネットワークの状態を確認してください。"
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    }
}

- (void) showBannerAd1{
    
    if(self.uiWebView != nil) {
        self.uiWebView.hidden= YES;
        self.uiWebView  = nil;
    }
    
    UIViewController* parent = UnityGetGLViewController();

    self.uiWebView = [[UIWebView alloc] init];
    self.uiWebView.delegate = self;
    self.uiWebView.frame = CGRectMake( 0.0, 0.0, ADBANNER_VIEW_SIZE_320x50.width, ADBANNER_VIEW_SIZE_320x50.height );
    self.uiWebView.scalesPageToFit = YES;
    self.uiWebView.hidden= NO;
    [parent.view addSubview:self.uiWebView];
    NSURL *url = [NSURL URLWithString:self.webview_url];
    //キャッシュを全て消去
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    //NSURLRequest作成時にポリシーを設定
    NSURLRequest *req = [NSURLRequest requestWithURL:url
                                         cachePolicy:NSURLRequestReturnCacheDataElseLoad
                                     timeoutInterval:30.0];
    [self.uiWebView loadRequest:req];
}

- (void) hideBannerAd1{
    if(self.uiWebView != nil) {
        self.uiWebView.hidden= YES;
        self.uiWebView  = nil;
    }
}

- (void) showBannerAd2{
    
}

- (void) hideBannerAd2{
    
}

-(BOOL) webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType {
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    
    return YES;
}

- (void)webView:(UIWebView *)wv didFailLoadWithError:(NSError *)error {
    self.uiWebView.hidden= YES;
    self.uiWebView  = nil;
}

- (void)startGAView:(const char*)viewName {
    NSString *nssViewName = [[[NSString alloc] initWithUTF8String:viewName] autorelease];
    
    // V2
    //[[GAI sharedInstance].defaultTracker trackView:nssViewName];
    
    // V3
    [[GAI sharedInstance].defaultTracker set:kGAIScreenName value:nssViewName];
    [[GAI sharedInstance].defaultTracker send:[[GAIDictionaryBuilder createAppView]  build]];
}

- (void)startGAEvent:(const char*)category AtAction:(const char*)action AtLabel:(const char*) label {
    NSString *nssCategory = [[[NSString alloc] initWithUTF8String:category] autorelease];
    NSString *nssAction = [[[NSString alloc] initWithUTF8String:action] autorelease];
    NSString *nssLabel = [[[NSString alloc] initWithUTF8String:label] autorelease];
    
    // V2
    //[[GAI sharedInstance].defaultTracker trackEventWithCategory:nssCategory
    //                                                 withAction:nssAction
    //                                                  withLabel:nssLabel
    //                                                  withValue:nil];
    
    // V3
    [[GAI sharedInstance].defaultTracker send:[[GAIDictionaryBuilder createEventWithCategory:nssCategory
                                                                                      action:nssAction
                                                                                       label:nssLabel
                                                                                       value:nil] build]];
}

- (void)sendFacebook:(const char*)message {
    
    float iOSVersion = [[[UIDevice currentDevice] systemVersion] floatValue];
    
    if(iOSVersion >= 6.0) {
	
		/* social frmae work */
    
        int type = FACEBOOK;
        SLComposeViewController *composeViewController = [SLComposeViewController composeViewControllerForServiceType:type == TWITTER ? SLServiceTypeTwitter : SLServiceTypeFacebook];
    
        composeViewController.completionHandler = ^(SLComposeViewControllerResult res) {
            if (res == SLComposeViewControllerResultCancelled) {
            // Cancel
            // UnitySendMessage(<#(char const *)obj#>, <#(char const *)method#>, <#(char const *)msg#>)
            }
            else if (res == SLComposeViewControllerResultDone) {
            // done!
            // UnitySendMessage(<#(char const *)obj#>, <#(char const *)method#>, <#(char const *)msg#>)
            }
        [composeViewController dismissViewControllerAnimated:YES completion:nil];
    };
    
    NSString *_text = [[[NSString alloc] initWithUTF8String:message] autorelease];
    [composeViewController setInitialText:_text];

    
    [UnityGetGLViewController() presentViewController:composeViewController animated:YES completion:nil];
    
    }
    else {
	
		/* facebook SDK */
    
        FacebookHelper *facebookHelper  = [[[FacebookHelper alloc] init] autorelease];
        [facebookHelper share:message];
    }
    
}

- (void)sendTwitter:(const char*)message {
    
    float iOSVersion = [[[UIDevice currentDevice] systemVersion] floatValue];
    
    if(iOSVersion >= 6.0) {
	
		/* social frmae work */
	
        int type = TWITTER;
        SLComposeViewController *composeViewController = [SLComposeViewController composeViewControllerForServiceType:type == TWITTER ? SLServiceTypeTwitter : SLServiceTypeFacebook];
    
        composeViewController.completionHandler = ^(SLComposeViewControllerResult res) {
            if (res == SLComposeViewControllerResultCancelled) {
            // Cancel
            // UnitySendMessage(<#(char const *)obj#>, <#(char const *)method#>, <#(char const *)msg#>)
            }
            else if (res == SLComposeViewControllerResultDone) {
            // done!
            // UnitySendMessage(<#(char const *)obj#>, <#(char const *)method#>, <#(char const *)msg#>)
            }
        [composeViewController dismissViewControllerAnimated:YES completion:nil];
    };
    
        NSString *_text = [[[NSString alloc] initWithUTF8String:message] autorelease];
        [composeViewController setInitialText:_text];

    
        [UnityGetGLViewController() presentViewController:composeViewController animated:YES completion:nil];
    }
    else {
    
		/* twitter frame work */
	
        NSString *msg = [[[NSString alloc] initWithUTF8String:message] autorelease];
        TwitterShareHelper *twitter  = [[[TwitterShareHelper alloc] init] autorelease];
        [twitter goShareMessage:msg withConnectionState:&isConnected andSharedState:&isShared andCancelState:&isCanceled];
    }
    
}

- (void)sendLine:(const char*)message {

    //　送りたいテキスト情報をtextStringに格納します。
    NSString *shareMessageStr = [[NSString alloc] initWithUTF8String:message];
    //テキストをURLエンコードする
    shareMessageStr = [shareMessageStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    //　LINEを開くためのパスを指定
    NSString *LINEUrlString = [NSString stringWithFormat:@"http://line.naver.jp/R/msg/text/%@",shareMessageStr];

    // URLスキームを利用してLINEのアプリケーションを起動する
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:LINEUrlString]];
    
}


- (void) showWallAd1{
    [_crossroad showWall:UnityGetGLViewController()];
}

- (void) showWallAd2{
    [_crossroad showInterstitial:UnityGetGLViewController()];
}

- (void) showWallAd3{
    
}

- (void) showWallAd4{
    
}

- (void) showWallAd5{
    
}

- (void) showDialogAd1{
    [[AidAd agentForMedia:kMEDIA_CODE_AID] showDialog];
}

- (void) showDialogAd2{
    
}

- (int)initOwnImage:(const char*)path AtType:(int)type {
    
    if(type == 1 && buttonTrigger1 != nil)
        return 0;
    if(type == 2 && buttonTrigger2 != nil)
        return 0;
    
    NSString *imgPath = [[NSString alloc] initWithUTF8String:path];
    
    CGSize mainBounds = [[UIScreen mainScreen] bounds].size;
    
    // type: 1. png image 2. gif image
    
    if(type == 1) {
        buttonTrigger1 = [UIButton buttonWithType:UIButtonTypeCustom];
        //[buttonTrigger setTitle:@"" forState:UIControlStateNormal];
        [buttonTrigger1 addTarget:self
                           action:@selector(buttonPushTrigger1)
                 forControlEvents:UIControlEventTouchUpInside];
        
        NSURL *myURL1 = [NSURL URLWithString:imgPath];
        NSData *data1 = [NSData dataWithContentsOfURL:myURL1];
        UIImage *img = [UIImage imageWithData:data1];
        
        buttonTrigger1.frame = CGRectMake(0,mainBounds.height-100, 40, 40);
        [buttonTrigger1 setBackgroundImage:img forState:UIControlStateNormal];
        
        [UnityGetGLViewController().view addSubview: buttonTrigger1];
        
        buttonTrigger1.hidden = YES;
        
        return type;
        
    }
    else if(type == 2) {
        buttonTrigger2 = [UIButton buttonWithType:UIButtonTypeCustom];
        //[buttonTrigger setTitle:@"" forState:UIControlStateNormal];
        [buttonTrigger2 addTarget:self
                           action:@selector(buttonPushTrigger2)
                 forControlEvents:UIControlEventTouchUpInside];
        
        NSURL *myURL2 = [NSURL URLWithString:imgPath];
        NSData *data2 = [NSData dataWithContentsOfURL:myURL2];
        YAPGif *gif = [[YAPGif alloc] initWithGifData:data2];
        
        buttonTrigger2.frame = CGRectMake(mainBounds.width-80,mainBounds.height-100, 40, 40);
        [buttonTrigger2 setBackgroundImage:gif.image forState:UIControlStateNormal];
        
        [UnityGetGLViewController().view addSubview: buttonTrigger2];
        
        buttonTrigger2.hidden = YES;
        
        return type;
    }

    
    return 0;
}

- (void)buttonPushTrigger1 {
    buttonTrigger1.hidden = YES;
}

- (void)buttonPushTrigger2 {
    buttonTrigger2.hidden = YES;
}

- (void)showOwnImage:(int)pos {
    
    if(pos == 1)
        buttonTrigger1.hidden = NO;
    else if(pos == 2)
        buttonTrigger2.hidden = NO;
}

- (void)hideOwnImage:(int)pos {
    
    if(pos == 1)
        buttonTrigger1.hidden = YES;
    else if(pos == 2)
        buttonTrigger2.hidden = YES;
}

-(int) chkStatus {
    int status  = [st_chk_str intValue];
    
    return status;
}

-(void)StatusCheck {
    
    NSString * check_URL = [NSString stringWithFormat:SHIBUSAN_URL_CHECK,BUNDLE_VERSION];
    NSURL *url = [NSURL URLWithString:check_URL];
    NSURLRequest *request = [[NSURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10];
    NSData *received = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    
    st_chk_str = [[NSString alloc]initWithData:received encoding:NSUTF8StringEncoding];
    
    int status  = [st_chk_str intValue];
    
    switch (status) {
        case 0:
            break;
        case 1:
            break;
        case 2:
        {
            alert2 =
            [[UIAlertView alloc] initWithTitle:nil message:@"このアプリを更新しますか？"
                                      delegate:self cancelButtonTitle:@"いいえ" otherButtonTitles:@"はい",nil];
            [alert2 show];
        }
            break;
        case 3:
        {
            UIView *uv = [[UIView alloc] initWithFrame:CGRectMake(0,0, (float)UnityGetGLViewController().view.bounds.size.width,
                                                                  (float)UnityGetGLViewController().view.bounds.size.height)];
            uv.backgroundColor = [UIColor blackColor];
            [UnityGetGLViewController().view addSubview:uv];
            
            alert3 =
            [[UIAlertView alloc] initWithTitle:nil message:@"このアプリを更新してくだい！"
                                      delegate:self cancelButtonTitle:@"はい" otherButtonTitles:nil];
            [alert3 show];
        }
            break;
        case 9:
        {
            UIView *uv = [[UIView alloc] initWithFrame:CGRectMake(0,0, (float)UnityGetGLViewController().view.bounds.size.width,
                                                                  (float)UnityGetGLViewController().view.bounds.size.height)];
            uv.backgroundColor = [UIColor blackColor];
            [UnityGetGLViewController().view addSubview:uv];
            alert9 =
            [[UIAlertView alloc] initWithTitle:nil message:@"現在メンテナンス中のため、しばらくの間ご利用いただけません。"
                                      delegate:self cancelButtonTitle:@"アプリ終了" otherButtonTitles:nil];
            [alert9 show];
        }
            break;
        case 99:
            break;
            
        default:
            break;
    }
    
}

-(void)alertView:(UIAlertView*)actionSheet
clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(actionSheet== alert2) {
        if (buttonIndex == 0){
            
        }else if(buttonIndex==1){
            NSURL *url = [NSURL URLWithString:MY_IOS_URL];
            NSURLRequest *req = [NSURLRequest requestWithURL:url];
            [[UIApplication sharedApplication] openURL:[req URL]];
        }
    }else if (actionSheet==alert3) {
        if (buttonIndex==0) {
            NSURL *url = [NSURL URLWithString:MY_IOS_URL];
            NSURLRequest *req = [NSURLRequest requestWithURL:url];
            [[UIApplication sharedApplication] openURL:[req URL]];
            exit(0);
        }
        
    }else if (actionSheet==alert9) {
        if (buttonIndex==0) {
            exit(0);
        }
    }
    
}

- (BOOL)shouldBlockDialog:(AidAdAgent*)agent {
    // ダイアログ表示試行回数を3で割った余が0ではない(1,2, 4,5,..回目)とき
    if ([agent countAttemptsToShowDialog] % 3 != 1) {
        //ダイアログ表示をブロック
        return YES;
    }
    return NO;
}

- (void)waidAid:(NSTimer*)timer {
    AidAdAgent * aidAd = [AidAd agentForMedia:kMEDIA_CODE_AID];
    [aidAd setPreferredCreativeStyle:kAidAdCreativeStyle_POPUP_IMAGE];
    [aidAd showDialog];
}

@end

extern "C" {
    void initAdIOS_();
    void initBannerAd1IOS_(const char* url);
    void hideBannerAd1IOS_();
    void showBannerAd1IOS_();
    void hideBannerAd2IOS_();
    void showBannerAd2IOS_();
    void showShibusanAdIOS_(const char* listurl);
	void initGAIOS_(const char* ga_tracking_id);
    void startGAViewIOS_(const char* viewName);
    void startGAEventIOS_(const char* category, const char* action, const char* label);
    void sendFacebookIOS_(const char* message);
    void sendTwitterIOS_(const char* message);
	void sendLineIOS_(const char* message);
    void showWallAd1IOS_();
    void showWallAd2IOS_();
    void showWallAd3IOS_();
    void showWallAd4IOS_();
    void showWallAd5IOS_();
    void showDialogAd1IOS_();
    void showDialogAd2IOS_();
     int initOwnImageIOS_(const char* path, int type);
    void showOwnImageIOS_(int pos);
    void hideOwnImageIOS_(int pos);
     int chkStatusIOS_();
}

void initAdIOS_() {
    [NativePlugin initAd];
}

void initBannerAd1IOS_(const char* url) {
    if(instance != nil){
        [instance initBannerAd1:(const char*)url];
    }
}

void hideBannerAd1IOS_(){
    if(instance != nil){
        [instance hideBannerAd1];
    }
}

void showBannerAd1IOS_(){
    if(instance != nil){
        [instance showBannerAd1];
    }
}

void hideBannerAd2IOS_(){
    if(instance != nil){
        [instance hideBannerAd2];
    }
}

void showBannerAd2IOS_(){
    if(instance != nil){
        [instance showBannerAd2];
    }
}

void showShibusanAdIOS_(const char* listurl){
    if(instance != nil){
        [instance showShibusanAd:(const char*)listurl];
    }
}

void initGAIOS_(const char* ga_tracking_id){
    if(instance != nil){
        [instance initGA:(const char*)ga_tracking_id];
    }
}

void startGAViewIOS_(const char* viewName){
    if(instance != nil){
        [instance startGAView:(const char*)viewName];
    }
}

void startGAEventIOS_(const char* category, const char* action, const char* label){
    if(instance != nil){
        [instance startGAEvent:(const char*)category AtAction:(const char*)action AtLabel:(const char*) label];
    }
}

void sendFacebookIOS_(const char* message){
    if(instance != nil){
        [instance sendFacebook:(const char*)message];
    }
}
void sendTwitterIOS_(const char* message){
    if(instance != nil){
        [instance sendTwitter:(const char*)message];
    }
}
void sendLineIOS_(const char* message){
    if(instance != nil){
        [instance sendLine:(const char*)message];
    }
}

void showWallAd1IOS_(){
    if(instance != nil){
        [instance showWallAd1];
    }
}

void showWallAd2IOS_(){
    if(instance != nil){
        [instance showWallAd2];
    }
}

void showWallAd3IOS_(){
    if(instance != nil){
        [instance showWallAd3];
    }
}

void showWallAd4IOS_(){
    if(instance != nil){
        [instance showWallAd4];
    }
}

void showWallAd5IOS_(){
    if(instance != nil){
        [instance showWallAd5];
    }
}

void showDialogAd1IOS_(){
    if(instance != nil){
        [instance showDialogAd1];
    }
}

void showDialogAd2IOS_(){
    if(instance != nil){
        [instance showDialogAd2];
    }
}

int initOwnImageIOS_(const char* path, int type){
    int r = 0;
    if(instance != nil){
        r = [instance initOwnImage:(const char*)path AtType: (int) type];
    }
    return r;
}

void showOwnImageIOS_(int pos){
    if(instance != nil){
        [instance showOwnImage:(int)pos];
    }
}

void hideOwnImageIOS_(int pos){
    if(instance != nil){
        [instance hideOwnImage:(int)pos];
    }
}

int chkStatusIOS_(){
    int r = 0;
    if(instance != nil){
       r = [instance chkStatus];
    }
    return r;
}


