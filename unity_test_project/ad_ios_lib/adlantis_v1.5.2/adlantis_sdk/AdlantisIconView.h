//
//  AdlantisIconView.h
//  AdLantis iOS SDK
//
//  Copyright 2013 Atlantis. All rights reserved.
//
//

#import <UIKit/UIKit.h>

@class AdlantisIconAdSet;

extern const NSUInteger kAdlantisIconWidth;
extern const NSUInteger kAdlantisIconHeight;

@interface AdlantisIconView : UIView

@property (nonatomic,readonly) AdlantisIconAdSet *set;

@end
