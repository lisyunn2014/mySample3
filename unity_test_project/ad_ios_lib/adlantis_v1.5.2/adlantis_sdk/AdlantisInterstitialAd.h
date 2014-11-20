//
//  AdlantisInterstitialAd.h
//  AdLantis iOS SDK
//
//  Copyright 2013 Atlantis. All rights reserved.
//
//

#import <Foundation/Foundation.h>

@protocol AdlantisInterstitialAdDelegate;

@interface AdlantisInterstitialAd : NSObject

+ (instancetype)interstitialAdWithPublisherId:(NSString*)publisherId;

- (void)show;

- (void)cancel;

@property (nonatomic,assign) id<AdlantisInterstitialAdDelegate> delegate;

@end


@protocol AdlantisInterstitialAdDelegate <NSObject>

@optional

- (void)interstitialAdRequestComplete:(AdlantisInterstitialAd*)interstitialAd;

- (void)interstitialAdRequestFailed:(AdlantisInterstitialAd*)interstitialAd;

- (void)interstitialAdWillBePresented:(AdlantisInterstitialAd*)interstitialAd;

- (void)interstitialAdWasDismissed:(AdlantisInterstitialAd*)interstitialAd;

@end
