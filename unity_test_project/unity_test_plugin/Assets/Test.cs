using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;

public class Test : MonoBehaviour {
	//[SerializeField]
	//private string[] url;

	public string web_url = "http://210.130.206.199/own/ad/housead.html?appid=SG0034";
	public string list_url = "http://210.130.206.199/own/applist/list.html?appid=SG0034";
	public string ga_tracking_id = "UA-46917210-22";

	void Start () {

		NativePlugin.initAd ();

		NativePlugin.initBannerAd1 (web_url);

		NativePlugin.initGA (ga_tracking_id);

		NativePlugin.startGAView("TOP");

	}

	void Update(){
		if (Application.platform == RuntimePlatform.Android && Input.GetKey(KeyCode.Escape)){
			Application.Quit();
		}
	}
	void OnGUI() {

		int current = 1;
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "Hide BannerAd1")){
			NativePlugin.hideBannerAd1();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Hide BannerAd1");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "Show BannerAd1")){
			NativePlugin.showBannerAd1();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Show BannerAd1");
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "Hide BannerAd2")){
			NativePlugin.hideBannerAd2();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Hide BannerAd2");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "Show BannerAd2")){
			NativePlugin.showBannerAd2();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Show BannerAd2");
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "Send Facebook")){
			NativePlugin.sendFacebook("message.facebook");
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Send Facebook");
		}

		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "Send Twitter")){
			NativePlugin.sendTwitter("message.twitter");
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Send Twitter");
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "Send Line")){
			NativePlugin.sendLine("message.Line");
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Send Line");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "showWallAd1")){
			int st = NativePlugin.chkStatus();

			if(st == 0) {
//				NativePlugin.showWallAd1();
//				NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd1");
				NativePlugin.sendFacebook("message.facebook");
				NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Send Facebook");
			}
			else if(st == 1) {
//				NativePlugin.showWallAd2();
//				NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd2");
				NativePlugin.sendTwitter("message.twitter");
				NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Send Twitter");
				
			}
			else if(st == 2) {
//				NativePlugin.showWallAd3();
//				NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd3");
				NativePlugin.sendLine("message.Line");
				NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","Send Line");
				
			}
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "showWallAd2")){
			NativePlugin.showWallAd2();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd2");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "showWallAd3")){
			NativePlugin.showWallAd3();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd3");
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "showWallAd4")){
			NativePlugin.showWallAd4();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd4");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "showWallAd5")){
			NativePlugin.showWallAd5();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showWallAd5");
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "showDialogAd1")){
			NativePlugin.showDialogAd1();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showDialogAd1");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "showDialogAd2")){
			NativePlugin.showDialogAd2();
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showDialogAd2");
		}
		if(GUI.Button(new Rect(0, (current++) * 80, Screen.width/2, 80), "showShibusanAd")){
			NativePlugin.showShibusanAd(list_url);
			NativePlugin.startGAEvent("TOP","GA_BTN_PRESS_TAG","showShibusanAd");
		}
		if(GUI.Button(new Rect(Screen.width/2, (current-1) * 80, Screen.width/2, 80), "showOwnImage")){
			NativePlugin.initOwnImage("http://shibusan.jp/own/applist/img/SG0001.png",1);
			NativePlugin.initOwnImage("http://www.gifs.net/Animation11/Everything_Else/Money/Piggie_bank.gif",2);

			NativePlugin.showOwnImage(1);
			NativePlugin.showOwnImage(2);
		}
	}
}
