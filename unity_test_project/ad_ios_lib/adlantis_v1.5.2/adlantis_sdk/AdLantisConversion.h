//
//  AdLantisConversion.h
//  adlantis_iphone_sdk
//
//  Created on 10/3/12.
//
//

#import <Foundation/Foundation.h>

@interface AdLantisConversion : NSObject

+ (instancetype)conversionWithTag:(NSString*)tag;

+ (void)sendConversionWithTag:(NSString*)tag;

- (instancetype)initWithTag:(NSString*)tag;

- (void)send;

- (NSString*)tag;

@end

@interface AdLantisConversionTest : AdLantisConversion

@end
