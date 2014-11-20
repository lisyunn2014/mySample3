//
//  Copyright (c) 2012-2014 UNITED, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

// Error code definition. / エラーコード
typedef enum : NSUInteger {
    // Timed out. / タイムアウト
    kAdstirAdErrorTimedOut = 100,
    // Invalid parameter. / 不正なパラメータ
    kAdstirAdErrorParameter = 200,
    // Network error. / ネットワークエラー
    kAdstirAdErrorNetwork = 300,
} AdstirAdError;

@class AdstirAdBase;

//
// Delegate protocol
// デリゲート
////////////////////////////////////////////////////////////////////////

@protocol AdstirAdDelegate <NSObject>
- (void)adstirAdDidReceived:(AdstirAdBase *)view;
- (void)adstirAdDidFailed:(AdstirAdBase *)view;
- (void)adstirAdDidError:(AdstirAdBase *)view WithCode:(AdstirAdError)code;
@end

@interface AdstirAdBase : UIView

//
// Spot settings / 広告スポットの設定
////////////////////////////////////////////////////////////////////////

// Set media ID. / メディアIDを設定します。
@property (copy) NSString *media;
// Set spot number. / 枠Noを設定します。
@property (nonatomic, assign) NSUInteger spot;


@property (nonatomic, assign) NSTimeInterval interval;
@property (nonatomic, weak) id<AdstirAdDelegate> delegate;
@property (nonatomic, assign) bool isCenter;
@end

@interface AdstirAdpapriView : AdstirAdBase
@end

@interface AdstirRTBView : AdstirAdBase
@end

@interface AdstirIconView : AdstirAdBase
// Icon slots(number of icon on view). / アイコンのスロット数(表示数)を設定します。
@property (nonatomic, assign) NSUInteger slot;
@end

@interface AdstirAdserverView : AdstirAdBase
@end


