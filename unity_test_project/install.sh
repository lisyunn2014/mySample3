#/bin/sh
PLUGIN_DIR=./unity_test_plugin/Assets/Plugins
rm -rf $PLUGIN_DIR/iOS/*
rm -rf $PLUGIN_DIR/Android/*
cp ./ios/* $PLUGIN_DIR/iOS/
cp -rf ./android/adControlManager $PLUGIN_DIR/Android/
cp -rf ./android/adcropsView $PLUGIN_DIR/Android/
cp -rf ./android/FacebookSDK-3.5 $PLUGIN_DIR/Android/
cp ./android/NativePlugin/AndroidManifest.xml $PLUGIN_DIR/Android/
cp ./android/NativePlugin/bin/nativeplugin.jar $PLUGIN_DIR/Android/


