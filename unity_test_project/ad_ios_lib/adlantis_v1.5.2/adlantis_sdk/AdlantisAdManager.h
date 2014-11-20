//
//  AdlantisAdManager.h
//  AdLantis iOS SDK
//
//  Copyright 2009-2013 Atlantis. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

///////////////////////////////////////////////////////////////////////////////////////////////////
@class AdNetworkConnection;

// used when assets are updated via network
extern NSString * const AdlantisAdsUpdatedNotification;
extern NSString * const AdlantisAdsUpdatedNotificationAdCount;          // NSNumber(int) count of ads received
extern NSString * const AdlantisAdsUpdatedNotificationCached;           // NSValue(bool) were the ads loaded from the cache?
extern NSString * const AdlantisAdsUpdatedNotificationError;            // NSValue(bool) was there an error?
extern NSString * const AdlantisAdsUpdatedNotificationNSError;          // NSError error that occurred (not always available)
extern NSString * const AdlantisAdsUpdatedNotificationErrorDescription; // NSString describing error (not always available)

extern NSString * const AdlantisAdManagerAssetUpdatedNotification;

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
@interface AdlantisAdManager : NSObject

+ (instancetype)sharedManager;

+ (NSString*)versionString;                        // AdLantis SDK version
+ (NSString*)build;                                // AdLantis SDK build
- (NSString*)fullVersionString;                    // Complete version information in one line

- (NSString*)byline;

@property (nonatomic,retain) NSString *publisherID;

@property (nonatomic,copy) NSString *host;

@property (nonatomic,assign) NSTimeInterval adFetchInterval;      // (default) amount of time (in seconds) before fetching next set of ads, set to zero to stop ad fetch
@property (nonatomic,assign) NSTimeInterval adDisplayInterval;    // (default) amount of time (in seconds) before showing the next ad

@property (nonatomic,retain) AdNetworkConnection *adNetworkConnection;
@property (nonatomic,copy) NSArray *testAdRequestURLs;

@property (nonatomic,retain) NSString *isoCountryCode; // ISO 3166-1 country code representation

// deprecated - use the classes in AdLantisConversion.h
- (void)sendConversionTag:(NSString*)inConversionTag              __attribute__((deprecated));
- (void)sendConversionTagTest:(NSString*)inConversionTag          __attribute__((deprecated));

// clear the contents of the cache
// キャッシュをクリアするための関数群
- (void)clearDiskCache;
- (void)clearMemoryCache;
- (void)clearCache;

@end
