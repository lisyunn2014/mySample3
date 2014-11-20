//
//  AdlantisIconAdSet.h
//  AdLantis iOS SDK
//
//  Copyright 2013 Atlantis. All rights reserved.
//
//

#import <Foundation/Foundation.h>

@class AdlantisIconView;
@protocol AdlantisIconAdSetDelegate;

@interface AdlantisIconAdSet : NSObject

+ (instancetype)iconSetWithPublisherId:(NSString*)publisherId;

- (void)add:(AdlantisIconView*)iconView;

- (void)remove:(AdlantisIconView*)iconView;

- (void)load;

- (NSUInteger)count;

@property (nonatomic,readonly) NSString *publisherId;

@property (nonatomic,assign) id<AdlantisIconAdSetDelegate> delegate;

@end


@protocol AdlantisIconAdSetDelegate <NSObject>

- (void)iconAdRequestComplete:(AdlantisIconAdSet*)iconSet count:(NSUInteger)count;

- (void)iconAdRequestFailed:(AdlantisIconAdSet*)iconSet;

@end

