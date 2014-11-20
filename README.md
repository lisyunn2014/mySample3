mySample3
=========

unity test project for native sdk plugin


mac os X10.9.3

unity v4.3.4f1
環境設定では、Unity_test_projectフォルダ下のinstall.sh実行する



Android

Unity->file->Build Setting
Switch Platform を　Adnroidに指定
buildボタン押す"Utest_android"で出力
Utest_android.apk生成される


Xcode5.1.1 ios7.1
iOS

Unity->file->Build Setting
Switch Platform を　iOSに指定
buildボタン押す"Utest_ios_build"で出力

Utest_ios_build生成される。
Utest_ios_build内Unity-iPhone.xcodeprojをクリック
Xcodeで下記の手順で設定を行う

１、Unity_test_projectフォルダ下のad_ios_lib、frmae_work
をUtest_ios_buildにaddする

２、Build Phases->Link Binary With Libraries
以下のframework追加

Twitter.framework
Social.framework
CoreData.framework

// gif
MobileCoreServices.framework
ImageIO.framework
AdSupport.framework (Optional)

// gamefeat
CoreTelephony.framework
StoreKit.framework

// admob
AudioToolbox.framework
MessageUI.framework

// adcrops
Security.framework

// Adlantise
QuartzCore.framework

Build Setting->Other Linker Flags
ObjC追加

Build Settings > Apple LLVM Compiler 3.1 - Language > Enable Objective-C Exceptioons
「NO」を「YES」に設定

