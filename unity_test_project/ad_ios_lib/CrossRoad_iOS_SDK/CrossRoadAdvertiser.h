//
// Copyright (c) transcosmos inc. All Rights Reserved.
//

#import <Foundation/Foundation.h>

@interface CrossRoadAdvertiser : NSObject

- (id)initWithAppId:(NSString*)appId;
- (void)sendConversionWithURL:(NSString*)url;

@end
