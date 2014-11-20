package jp.co.transcosmos.nativeplugin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppsviewActivity extends Activity {

    private static Activity me = null;
	private String apps;
	private String store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    // ステータスバー非表示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // タイトルバー非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		me = this;		
		apps = getIntent().getExtras().getString("appsString");
		store = getIntent().getExtras().getString("storeString");
		showView();
	}


	private float screenHeight() {
	    WindowManager windowmanager = (WindowManager)getSystemService(WINDOW_SERVICE);
	    Display display = windowmanager.getDefaultDisplay();
	    return display.getHeight();
	}

	private void showView() {
		final LinearLayout appsView = new LinearLayout(this);
		Activity context = me;
		LinearLayout.LayoutParams appsViewLayout = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		int headerHeight = (int)(50 * context.getResources().getDisplayMetrics().density);
		appsView.setBackgroundColor(0xff000000);
		appsView.setLayoutParams(appsViewLayout);
		appsView.setOrientation(LinearLayout.VERTICAL);
		
		RelativeLayout headerView = new RelativeLayout(this);
		RelativeLayout.LayoutParams headerViewLayout = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, headerHeight);
		headerView.setLayoutParams(headerViewLayout);
		headerView.setBackgroundColor(0xffe8e8e8);
		appsView.addView(headerView);
		
		final WebView webView = new WebView(this);
		LinearLayout.LayoutParams webViewLayout = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		webView.setLayoutParams(webViewLayout);
		webView.setBackgroundColor(0xffffffff);
		webView.setPadding(0, 0, 0, 0);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);
		webView.getSettings().setAppCacheEnabled(false);
		webView.setWebViewClient(new WebViewClient(){
			public boolean isError = false;
            @Override
			public void onPageFinished(WebView view, String url) {
	            super.onPageFinished(view, url);
	            view.clearCache(true);
	            if (isError) {
	            	isError = false;
	            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(me);
	            	alertDialog.setTitle("接続できません");
	            	alertDialog.setMessage("渋三あっぷすに接続できません。ネットワークの状態を確認してください。");
	            	alertDialog.setCancelable(true);
	                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    	dialog.cancel();
	                    	finish();
	                    }
	                });
	                alertDialog.create();
	                alertDialog.show();
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
                me.startActivity(intent);
            	return true;
            }
        });
		appsView.addView(webView);

		Button closeButton = new Button(this);
		closeButton.setId(1);
		RelativeLayout.LayoutParams closeButtonLayout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		closeButtonLayout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		closeButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		closeButton.setLayoutParams(closeButtonLayout);
        closeButton.setText("閉じる");
        closeButton.setTextColor(0xFF00BFFF);
        closeButton.setTextSize(16);
        closeButton.setBackgroundColor(0x00000000);
        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        	    final View webFrame = appsView;

        	    Animation animation = new TranslateAnimation(0, 0, 0, screenHeight());
        	    animation.setDuration(500);
        	    animation.setAnimationListener(new Animation.AnimationListener() {
        	        @Override
        	        public void onAnimationStart(Animation animation) {}

        	        @Override
        	        public void onAnimationEnd(Animation animation) {
        	            webFrame.setVisibility(View.GONE);
        	            finish();
        	        }

        	        @Override
        	        public void onAnimationRepeat(Animation animation) {}
        	    });
        	    webFrame.startAnimation(animation);
            }
        });
        headerView.addView(closeButton);
        
        TextView title = new TextView(this);
        title.setId(2);
        RelativeLayout.LayoutParams titleLayout = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleLayout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        titleLayout.addRule(RelativeLayout.LEFT_OF, 3);
        titleLayout.addRule(RelativeLayout.RIGHT_OF, 1);
        title.setGravity(Gravity.CENTER);
        title.setLayoutParams(titleLayout);
        title.setText("渋三あっぷす");
        title.setTextColor(0xff000000);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setTextSize(18);
        headerView.addView(title);

		Button storeButton = new Button(this);
		storeButton.setId(3);
		RelativeLayout.LayoutParams storeButtonLayout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		storeButtonLayout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		storeButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		storeButton.setLayoutParams(storeButtonLayout);
		storeButton.setText("ストア");
		storeButton.setTextColor(0xFF00BFFF);
		storeButton.setTextSize(16);
		storeButton.setBackgroundColor(0x00000000);
		storeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(store)));
            }
        });
		headerView.addView(storeButton);

		setContentView(appsView, appsViewLayout);
		
	    final View webFrame = appsView;
	    webFrame.setVisibility(View.VISIBLE);

	    Animation animation = new TranslateAnimation(0, 0, screenHeight(), 0);
	    animation.setDuration(500);
	    animation.setFillAfter(true);
	    animation.setFillEnabled(true);
	    animation.setAnimationListener(new Animation.AnimationListener() {
	        @Override
	        public void onAnimationStart(Animation animation) {}

	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	webView.loadUrl(apps);
	        }

	        @Override
	        public void onAnimationRepeat(Animation animation) {}
	    });

	    webFrame.startAnimation(animation);
	}

}