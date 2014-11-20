//
//  TwitterShareHelper.h
//
//
//
//
//

#import <Foundation/Foundation.h>

@interface TwitterShareHelper : NSObject{
    bool * connectedPtr;
    bool * sharedPtr;
    bool * cancelPtr;
    UIAlertView * loadingAlert;
}

@property (nonatomic,retain)NSString *sharedMessage;

-(void)goShareMessage:(NSString *)message withConnectionState:(bool *)isConnected andSharedState:(bool *)isSuccessShared andCancelState:(bool *)isCanceled;

@end
