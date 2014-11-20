package jp.co.transcosmos.adcontrolManager;

import net.adcrops.sdk.AdcController;
import net.adcrops.sdk.activity.AdcViewListActivity;
//import net.adcrops.sdk.config.AdcConfig;
import net.adcrops.sdk.exception.AdcSecurityException;
import net.app_c.cloud.sdk.AppCCloud;

import com.amoad.amoadsdk.AMoAdSdkWallActivity;

import jp.basicinc.gamefeat.android.sdk.controller.GameFeatAppController;
import jp.co.transcosmos.adcontrolinterface.R;
import jp.co.transcosmos.crossroad.CrossRoad;
import jp.co.transcosmos.crossroad.cocos2dx.CrossRoadCocos2dxHelper;
import jp.live_aid.aid.AdController;
import jp.maru.mrd.astawall.MrdAstaWallActivity;
import jp.tjkapp.adfurikunsdk.AdfurikunLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

@SuppressLint("SetJavaScriptEnabled")
public class AdControlManager{
	
	private static WebView webView = null;
	private static Activity mActivity = null; 
	private static String mUrl1 = null;
	private static int gravity1;
	private static int gravity2;
	static final float[] DIMENSIONS_LANDSCAPE = { 320, 50 };
	static final float[] DIMENSIONS_PORTRAIT = { 320, 50 };
	//private static LinearLayout mLinearLayout1 = null;
	private static FrameLayout mFrameLayout1 = null;
	private static LinearLayout mLinearLayout2 = null;
	
	private FrameLayout mFramelayout;
	private ViewGroup adLayout;
	
	private static AlertDialog alert;
	private static AppCCloud appCCloud;
	private static GameFeatAppController gfAppController;
	private static AdfurikunLayout adfurikunView;
	
	private static AdController  _adController;
	private static String _MEDIA_CODE_AID;
	
	private static CrossRoad controller;
 
    public AdControlManager(Activity activity) {  
        
        mActivity = activity;
        gravity1 = Gravity.CENTER|Gravity.TOP;
        gravity2 = Gravity.CENTER|Gravity.BOTTOM;
        
        
        mFramelayout = (FrameLayout)mActivity.findViewById(android.R.id.content);
        adLayout = (ViewGroup)mActivity.getLayoutInflater().inflate(R.layout.ads_layout, null);
        mFramelayout.addView(adLayout);
       
    }  
    
	static void bannerAdInit1(int gravity) {
	 	
	 	//mLinearLayout1 = new LinearLayout(mActivity);
		mFrameLayout1 = new FrameLayout(mActivity);
	 	
	 	webView = new WebView(mActivity);
	 	
	 	webView.setBackgroundColor(0);
	 	webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	 	webView.setVerticalScrollbarOverlay(true);
	 	webView.getSettings().setJavaScriptEnabled(true);
	 	webView.getSettings().setBuiltInZoomControls(true);
	 	webView.getSettings().setLoadWithOverviewMode(true);
	 	webView.getSettings().setUseWideViewPort(true);
	 	webView.getSettings().setAppCacheEnabled(false);
    	webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
    	webView.setHorizontalScrollBarEnabled(false);
    	webView.setVerticalScrollBarEnabled(false);
	 	
	 	webView.loadUrl(mUrl1);
	 	
	 	webView.setWebViewClient(new WebViewClient(){
	 		public boolean isError = false;
	 	        @Override
	 		public void onPageFinished(WebView view, String url) {
	             super.onPageFinished(view, url);
	             view.clearCache(true);
	             if (isError) {
	             	view.setVisibility(View.GONE);
	             }
	             else {
	                String pageTitle = webView.getTitle();
	                if(pageTitle != null) {
	             	   if(pageTitle.equals("404 Not Found")) {
	             		  view.setVisibility(View.GONE);
	             	   }
	                }
	             }
	 	        }
	 	        @Override
	 	        public void onReceivedError(WebView view, int errorCode,
	 	                String description, String failingUrl) {
	 	        	isError = true;
	 	        }
	 	        @Override
	 	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	 	        	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	 	        	mActivity.startActivity(intent);
	 	        	return true;
	 	        }
	 	    });

	 	//mLinearLayout1.addView(webView);
	 	mFrameLayout1.addView(webView);
	 	
	 	Display display = mActivity.getWindow().getWindowManager().getDefaultDisplay();
	 	final float scale = mActivity.getResources().getDisplayMetrics().density;
	 	float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
	 			: DIMENSIONS_LANDSCAPE;
	 	
//	 	mActivity.addContentView(mLinearLayout1, new FrameLayout.LayoutParams(
//	 			(int) (dimensions[0] * scale), (int) (dimensions[1]
//	 					* scale ),gravity));
	 	
	 	
		if(android.os.Build.VERSION.SDK_INT <=10){
				webView.setInitialScale(1000);
		}
	 	mActivity.addContentView(mFrameLayout1, new FrameLayout.LayoutParams(
	 			(int) (dimensions[0] * scale), (int) (dimensions[1]
	 					* scale ),gravity));
	 }
	
	
	/**
	 * 告初期化
	 */

	static void bannerAdInit2(int gravity) {
	    	
//	    FrameLayout bannerFrame = (FrameLayout) mActivity.findViewById(R.id.bottom_banner_frame);
//		
//	    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//	            ViewGroup.LayoutParams.WRAP_CONTENT,
//	            ViewGroup.LayoutParams.WRAP_CONTENT);
//	    params.gravity = Gravity.CENTER_HORIZONTAL;
//		
//		
//		
//		// Add the adView to it
//		bannerFrame.addView(null, params);
		
		mLinearLayout2 = new LinearLayout(mActivity);
		
		adfurikunView = new AdfurikunLayout(mActivity);
		
	 	mLinearLayout2.addView(adfurikunView);
	 	
	 	Display display = mActivity.getWindow().getWindowManager().getDefaultDisplay();
	 	final float scale = mActivity.getResources().getDisplayMetrics().density;
		float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
	 			: DIMENSIONS_LANDSCAPE;
	 	
	 	mActivity.addContentView(mLinearLayout2, new FrameLayout.LayoutParams(
	 			(int) (dimensions[0] * scale), (int) (dimensions[1]
	 					* scale ),gravity));
		adfurikunView.startRotateAd(); // 広告表示開始	
		adfurikunView.onResume();
	}
	
    /**
     * セキュリティエラーダイアログを初期化する。
     * 
     */
    private static void initAlertDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
		 
        dialog.setTitle(mActivity.getResources().getText(R.string.adcrops_list_view_error_dialog_title));
        dialog.setMessage(mActivity.getResources().getText(R.string.adcrops_list_view_error_dialog_msg_security));

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
        	}
        });
        alert = dialog.create();
    }
    
	/**
	 *  aid の 広告の制御用の AdController を設定します
	 */
	protected static void setUpAidAdController()
	{
		if (_adController != null) return;
		try {
			// Create instance of IconLoader with mediaCode and Context
			_adController = new AdController(_MEDIA_CODE_AID, mActivity) {
				protected void dialogDidCreated(android.app.Dialog d) {
					d.setCancelable(false);
					super.dialogDidCreated(d);
				}
			};
						
		    // ダイアログ表示抑制(DialogBlocker)を設定
			_adController.setDialogBlocker(new AdController.DialogBlocker() {
		        public boolean shouldBlock(AdController ctrl, AdController.DialogType dialogType) {
		
		            // 終了時広告なら常にブロックしない
		            if (dialogType == AdController.DialogType.ON_EXIT) return false;
		
		            // ダイアログ表示試行回数を確認
		            int count = ctrl.countAttemptsToShowDialogOfType(dialogType);
		            // 3で割った余が1 ( 1, 4, 7...回目) は広告ブロックしない
		            if (count % 3 == 1) return false;
		
		            return true; // 他の時はブロックする
		        }
		
		    });
		
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	}

 
    /**************** interface parts *********************/ 
    
    public static void initAid(String media_id) {
    	_MEDIA_CODE_AID = media_id;
		setUpAidAdController();
		_adController.setCreativeStyle(AdController.CreativeStyle.PLAIN_TEXT);// テキスト型広告 の場合
		_adController.startPreloading(); // 読み込み開始
		
		
		Handler handlerTimer = new Handler();
		handlerTimer.postDelayed(new Runnable(){
		        public void run() {
					_adController.showDialog(AdController.DialogType.ON_DEMAND);
					_adController.setCreativeStyle(AdController.CreativeStyle.POPUP_IMAGE);
		
		}}, 3000);    	
    }
    
    public static void showAidImage() {
    	_adController.showDialog(AdController.DialogType.ON_DEMAND);    	
    }
    
    public static void exitAidImage() {
    	_adController.showDialog(AdController.DialogType.ON_EXIT);
    }
		
	public static void initBannerAd1(String url,int gravity) {
		mUrl1 = url;
        if(mActivity != null && mUrl1 != null) {
            
        	if(webView != null) {
        		webView.setVisibility(View.GONE);
        		webView = null;
        	}
        	if(gravity != 0)
        		gravity1 = gravity;
        	bannerAdInit1(gravity1);
        	webView.setVisibility(View.GONE);
        }
	}

 	public static void showBannerAd1() {
 		if(webView != null) {
 			if(webView.getVisibility() == View.GONE)
		    	webView.loadUrl(mUrl1);
 				webView.setVisibility(View.VISIBLE);
 		}
 	}
 	
 	public static void hideBannerAd1() {
 		if(webView != null) {
 			if(webView.getVisibility() == View.VISIBLE)
 				webView.setVisibility(View.GONE);
 		}

 	} 
 	
 	public static void initBannerAd2(int gravity) {
    	if(adfurikunView != null) {
    		adfurikunView.setVisibility(View.GONE);
    		adfurikunView = null;
    	}
    	if(gravity != 0)
    		gravity2 = gravity;
 		bannerAdInit2(gravity2);
 		adfurikunView.setVisibility(View.GONE);
 	}
 	
 	public static void showBannerAd2() {
 		if(adfurikunView != null) {
 			if(adfurikunView.getVisibility() == View.GONE)
 				adfurikunView.setVisibility(View.VISIBLE);
 		} 		
 	}
 	
 	public static void hideBannerAd2() {
 		if(adfurikunView != null) {
 			if(adfurikunView.getVisibility() == View.VISIBLE)
 				adfurikunView.setVisibility(View.GONE);
 		} 		
 	}
 	
 	public static void showAstaWall(String media_id) {
 		/* Ast Wall */
		Intent intent = new Intent( mActivity, MrdAstaWallActivity.class);
		//__MEDIA_CODE__には管理画面から取得したコードを記述してください。
		intent.putExtra("id", media_id);
		//開始
		mActivity.startActivity(intent);
 	}
 
 	public static void showAmoadGamesWall() {
    	/* AmoAd Games */
		Intent intent = new Intent(mActivity, AMoAdSdkWallActivity.class);
		mActivity.startActivity(intent); 
 	}
 	
 	public static void initGFWall() {
		// GFコントローラ
		gfAppController = new GameFeatAppController();
		// GAMEFEATE広告設定初期化
		gfAppController.activateGF(mActivity, false, false, false); 		
 	}
 	
 	public static void showGFWall() {
    	/* game feat */
    	gfAppController.show(mActivity);
 	}
 	
 	
 	public static void initAppCWall() {
    	// appc cloud 初期化
    	appCCloud = new AppCCloud(mActivity).start(); 		
 	}
 	public static void showAppCWall() {
    	/* appc cloud */
    	
    	appCCloud.Ad.callWebActivity();
 	}
 	
 	public static void showAdcropsWall() {
    	/* adcrops */
    	
        // TODO:セキュリティアラート用のダイアログ準備
        initAlertDialog();
		try {
			// TODO:adcropsControllerのイニシャライズ
			AdcController.initialize(mActivity);
		} catch (AdcSecurityException e) {
			e.printStackTrace();
		}
		
    	if( AdcController.isInstance()) {
            Intent intent = new Intent( mActivity, AdcViewListActivity.class );
            mActivity.startActivity( intent );
//            AdcConfig co = new AdcConfig();
    	}else{
    		// TODO:アラート表示
    		alert.show();
    	}
 	}

 	
 	public static void initCrossRoad(String media_id) {
    	// CrossRoad 初期化
 		controller = new CrossRoad(mActivity,media_id);
 		controller.setMessage(jp.co.transcosmos.crossroad.MessageId.NOT_CONNECTED, "メッセージカスタマイズ　テスト");
 		controller.sendConversion(mActivity);
 		
// 		CrossRoadCocos2dxHelper.setActivity(mActivity);
// 		CrossRoadCocos2dxHelper.init(media_id);
// 		CrossRoadCocos2dxHelper.sendConversion();
 	}
 	public static void showCrossRoadWall() {
    	/* CorssRoad */
 		controller.showWall(mActivity);
// 		CrossRoadCocos2dxHelper.showWall();
 	}
 	
 	public static void showCrossRoadInterstitial() {
    	/* CorssRoad */
 		controller.showInterstitial(mActivity); 
// 		CrossRoadCocos2dxHelper.showInterstitial();
 	}
 	
 	public static void showCrossRoadQuitInterstitial() {
    	/* CorssRoad */
 		controller.showInterstitialAndQuit(mActivity); 
// 		CrossRoadCocos2dxHelper.showInterstitialAndQuit();
 	}
 	
}