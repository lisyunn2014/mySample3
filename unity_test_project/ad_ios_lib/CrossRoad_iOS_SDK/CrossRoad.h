//
// Copyright (c) transcosmos inc. All Rights Reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, CRMessageId) {
    CRMessageIdNotConnected
};

@interface CrossRoad : NSObject

- (id)initWithAppId:(NSString*)appId;
- (void)showWall:(UIViewController*)owner;
- (void)showInterstitial:(UIViewController*)owner;
- (void)sendConversion;
- (void)setMessage:(NSString *)message forId:(CRMessageId)id;

@end
