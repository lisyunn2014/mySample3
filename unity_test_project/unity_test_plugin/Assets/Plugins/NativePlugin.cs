using UnityEngine;
using System.Runtime.InteropServices;

public class NativePlugin { 

#if UNITY_ANDROID
	private static AndroidJavaObject nativePlugin = null;
#endif

	[DllImport("__Internal")]
	private static extern void initAdIOS_();
	public static void initAd() {
		#if UNITY_IPHONE
		initAdIOS_();
		#elif UNITY_ANDROID 
		nativePlugin = new AndroidJavaObject("jp.co.transcosmos.nativeplugin.NativePlugin");
		nativePlugin.Call("initAd");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void initBannerAd1IOS_(string url);
	public static void initBannerAd1(string url) {
#if UNITY_IPHONE
		initBannerAd1IOS_(url);
#elif UNITY_ANDROID 
		nativePlugin.Call("initBannerAd1",url);
#endif
	}

	[DllImport("__Internal")]
	private static extern void hideBannerAd1IOS_();
	public static void hideBannerAd1() {
#if UNITY_IPHONE
		hideBannerAd1IOS_();
#elif UNITY_ANDROID
		nativePlugin.Call("hideBannerAd1");
#endif
	}
	

	[DllImport("__Internal")]
	private static extern void showBannerAd2IOS_();
	public static void showBannerAd2() {
#if UNITY_IPHONE
		showBannerAd2IOS_();
#elif UNITY_ANDROID
		nativePlugin.Call("showBannerAd2");
#endif
	}

	[DllImport("__Internal")]
	private static extern void hideBannerAd2IOS_();
	public static void hideBannerAd2() {
		#if UNITY_IPHONE
		hideBannerAd2IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("hideBannerAd2");
		#endif
	}
	
	
	[DllImport("__Internal")]
	private static extern void showBannerAd1IOS_();
	public static void showBannerAd1() {
		#if UNITY_IPHONE
		showBannerAd1IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showBannerAd1");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showShibusanAdIOS_(string listurl);
	public static void showShibusanAd(string listurl) {
		#if UNITY_IPHONE
		showShibusanAdIOS_(listurl);
		#elif UNITY_ANDROID 
		nativePlugin.Call("showShibusanAd",listurl);
		#endif
	}


	[DllImport("__Internal")]
	private static extern void initGAIOS_( string ga_tracking_id);
	public static void initGA( string ga_tracking_id) {
		#if UNITY_IPHONE
		initGAIOS_(ga_tracking_id);
		#elif UNITY_ANDROID
		nativePlugin.Call("initGA",ga_tracking_id);
		#endif
	}
	[DllImport("__Internal")]
	private static extern void startGAViewIOS_( string viewName);
	public static void startGAView( string viewName) {
		#if UNITY_IPHONE
		startGAViewIOS_(viewName);
		#elif UNITY_ANDROID
		nativePlugin.Call("startGAView",viewName);
		#endif
	}
	[DllImport("__Internal")]
	private static extern void startGAEventIOS_(string category, string action, string label);
	public static void startGAEvent(string category, string action, string label) {
		#if UNITY_IPHONE
		startGAEventIOS_(category,  action,  label);
		#elif UNITY_ANDROID
		nativePlugin.Call("startGAEvent",category,action,label);
		#endif
	}
	[DllImport("__Internal")]
	private static extern void sendFacebookIOS_(string message);
	public static void sendFacebook(string message) {
		#if UNITY_IPHONE
		sendFacebookIOS_(message);
		#elif UNITY_ANDROID
		string facebook_app_id ="221297844723256";
		nativePlugin.Call("sendFacebook",facebook_app_id,message);
		#endif
	}
	[DllImport("__Internal")]
	private static extern void sendTwitterIOS_(string message);
	public static void sendTwitter(string message) {
		#if UNITY_IPHONE
		sendTwitterIOS_(message);
		#elif UNITY_ANDROID
		string twitter_consumer_key = "JJohkqJg8VskySFmiaIbRg";
		string twitter_secret_key = "Uoz5XMZ9bvY9aYeC95Zapa1ihQTX0rvMLyw1uryD6E";
		nativePlugin.Call("sendTwitter",twitter_consumer_key,twitter_secret_key,message);
		#endif
	}
	[DllImport("__Internal")]
	private static extern void sendLineIOS_(string message);
	public static void sendLine(string message) {
		#if UNITY_IPHONE
		sendLineIOS_(message);
		#elif UNITY_ANDROID
		nativePlugin.Call("sendLine",message);
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showWallAd1IOS_();
	public static void showWallAd1() {
		#if UNITY_IPHONE
		showWallAd1IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showWallAd1");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showWallAd2IOS_();
	public static void showWallAd2() {
		#if UNITY_IPHONE
		showWallAd2IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showWallAd2");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showWallAd3IOS_();
	public static void showWallAd3() {
		#if UNITY_IPHONE
		showWallAd3IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showWallAd3");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showWallAd4IOS_();
	public static void showWallAd4() {
		#if UNITY_IPHONE
		showWallAd4IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showWallAd4");
		#endif
	}


	[DllImport("__Internal")]
	private static extern void showWallAd5IOS_();
	public static void showWallAd5() {
		#if UNITY_IPHONE
		showWallAd5IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showWallAd5");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showDialogAd1IOS_();
	public static void showDialogAd1() {
		#if UNITY_IPHONE
		showDialogAd1IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showDialogAd1");
		#endif
	}

	[DllImport("__Internal")]
	private static extern void showDialogAd2IOS_();
	public static void showDialogAd2() {
		#if UNITY_IPHONE
		showDialogAd2IOS_();
		#elif UNITY_ANDROID
		nativePlugin.Call("showDialogAd2");
		#endif
	}

	[DllImport("__Internal")]
	private static extern int initOwnImageIOS_(string path, int type);
	public static int initOwnImage(string path, int type) {
		int r = 0;
		#if UNITY_IPHONE
		r = initOwnImageIOS_(path, type);
		#elif UNITY_ANDROID
		r = nativePlugin.Call<int>("initOwnImage",path, type);
		#endif

		return r;
	}

	[DllImport("__Internal")]
	private static extern void showOwnImageIOS_(int pos);
	public static void showOwnImage(int pos) {
		#if UNITY_IPHONE
		showOwnImageIOS_(pos);
		#elif UNITY_ANDROID
		nativePlugin.Call("showOwnImage",pos);
		#endif
	}

	[DllImport("__Internal")]
	private static extern void hideOwnImageIOS_(int pos);
	public static void hideOwnImage(int pos) {
		#if UNITY_IPHONE
		hideOwnImageIOS_(pos);
		#elif UNITY_ANDROID
		nativePlugin.Call("hideOwnImage",pos);
		#endif
	}
	

	[DllImport("__Internal")]
	private static extern int chkStatusIOS_();
	public static int chkStatus() {
		int r = 0;
		#if UNITY_IPHONE
		r = chkStatusIOS_();
		#elif UNITY_ANDROID
		r = nativePlugin.Call<int>("chkStatus");
		#endif

		return r;
	}

}

