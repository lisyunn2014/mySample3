//
//  AdstirInterstitial.h
//
//  Copyright (c) 2013-2014 UNITED, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class AdstirInterstitial;

//
// Common delegate protocol
// 共通のデリゲート
////////////////////////////////////////////////////////////////////////

@protocol AdstirInterstitialDelegate <NSObject>
@optional
// Performed when recive ad setting. / 広告情報が取得できた際に呼び出されます。
- (void)adstirInterstitialDidReceiveSetting:(AdstirInterstitial *)inter;
// Performed when fail to recive ad setting. / 広告情報が取得できなかった際に呼び出されます。
- (void)adstirInterstitialDidFailedToReceiveSetting:(AdstirInterstitial *)inter;
@end

//
// Delegate protocol for dialog ad
// ダイアログ用のデリゲート
////////////////////////////////////////////////////////////////////////

@protocol AdstirInterstitialDialogDelegate <NSObject>
// Performed when positive button is tapped. // 肯定的なボタンがタップされた際に呼び出されます。
- (void)adstirInterstitialDialogPositiveButtonClick:(AdstirInterstitial *)inter;
// Performed when positive button is tapped. // 肯定的なボタンがタップされた際に呼び出されます。
- (void)adstirInterstitialDialogNegativeButtonClick:(AdstirInterstitial *)inter;
// Performed when positive button is tapped. // 肯定的なボタンがタップされた際に呼び出されます。
- (void)adstirInterstitialDialogCancel:(AdstirInterstitial *)inter;
@end


@interface AdstirInterstitial : NSObject

//
// Spot settings / 広告スポットの設定
////////////////////////////////////////////////////////////////////////

// Set media ID. / メディアIDを設定します。
@property (copy, nonatomic) NSString *media;
// Set spot number. / 枠Noを設定します。
@property (assign, nonatomic) NSUInteger spot;


//
// Dialog text settings / ダイアログの設定
////////////////////////////////////////////////////////////////////////

// Text to display on dialog. / ダイアログに表示されるテキストを設定します。
@property (copy, nonatomic) NSString *dialogText;
// Color of dialog text. / テキスト色を設定します。
@property (strong, nonatomic) UIColor *dialogTextColor;
// Background color of dialog. / 背景色を設定します。
@property (strong, nonatomic) UIColor *dialogBackgroundColor;
// Border color of dialog. / 枠線の色を設定します。
@property (strong, nonatomic) UIColor *dialogBorderColor;

// Positive button text. / 肯定的なボタンのテキストを設定します。
@property (copy, nonatomic) NSString *positiveButtonText;
// Color of positive button text. / テキスト色を設定します。
@property (strong, nonatomic) UIColor *positiveButtonTextColor;
// Background color of positive button text. / 背景色を設定します。
@property (strong, nonatomic) UIColor *positiveButtonBackgroundColor;
// Border color of positive button text. / 枠線の色を設定します。
@property (strong, nonatomic) UIColor *positiveButtonBorderColor;

// Negative button text. / 否定的なボタンのテキストを設定します。
@property (copy, nonatomic) NSString *negativeButtonText;
// Color of negative button text. / テキスト色を設定します。
@property (strong, nonatomic) UIColor *negativeButtonTextColor;
// Background color of negative button text. / 背景色を設定します。
@property (strong, nonatomic) UIColor *negativeButtonBackgroundColor;
// Border color of negative button text. / 枠線の色を設定します。
@property (strong, nonatomic) UIColor *negativeButtonBorderColor;


//
// Dialog text settings / ダイアログの設定
////////////////////////////////////////////////////////////////////////

// Set delegate object for interstitial ad. / インタースティシャル広告のデリゲートを設定します。
@property (weak, nonatomic) id<AdstirInterstitialDelegate> delegate;
// Set delegate object for dialog ad. / インタースティシャルダイアログのデリゲートを設定します。
@property (weak, nonatomic) id<AdstirInterstitialDialogDelegate> dialogDelegate;


//
// Dialog text settings / ダイアログの設定
////////////////////////////////////////////////////////////////////////

// Preload ad information. / 広告情報を事前に読み込みます。
- (void)load;

// Show interstitial ad. / インタースティシャル広告を表示します。
// Returns NO, if no ad to display. / 広告が表示されない場合、NOを返します。
- (BOOL)showTypeA:(UIViewController *)viewController;

// Show dialog ad. Dialog is always shown if no ad to display.
// インタースティシャルダイアログを表示します。ダイアログは広告が表示されない場合にも表示されます。
- (void)showTypeB:(UIViewController *)viewController;

// Show fullscreen ad. / フルスクリーン広告を表示します。
// Returns NO, if no ad to display. / 広告が表示されない場合、NOを返します。
- (BOOL)showTypeC:(UIViewController *)viewController;


//
// Following methods are deprecated! Please use new method. / 下記は互換性のために残されていますが、非推奨です。
////////////////////////////////////////////////////////////////////////

// Replaced with showTypeA:viewController . / showTypeA:viewController を利用してください。
- (BOOL)showInterstitial:(UIViewController *)viewController DEPRECATED_ATTRIBUTE;
// Replaced with showTypeB:viewController . / showTypeB:viewController を利用してください。
- (void)showDialog:(UIViewController *)viewController DEPRECATED_ATTRIBUTE;

@end
