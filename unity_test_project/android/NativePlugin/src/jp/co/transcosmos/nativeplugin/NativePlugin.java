package jp.co.transcosmos.nativeplugin;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import jp.co.transcosmos.adcontrolManager.AdControlManager;
import cn.com.transcosmos.GifView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.faceBook.FaceBookActivity;
import com.unity3d.player.twitter.TwitterActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class NativePlugin {
	
	private Handler handler;
	private Activity activity;
	private static final AtomicReference<Tracker> gaTracker = new AtomicReference<Tracker>();
	private static StatusCheck ck;
	private ImageView imgv = null;
	private GifView gfv = null;
	
	public NativePlugin() {
		activity = UnityPlayer.currentActivity;
		handler = new Handler(Looper.getMainLooper());

		ck = new StatusCheck(activity,"SG0036");
		
		//Log.d("NativePlugin","NativePlugin");
	}
	
	public void initAd() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	new AdControlManager(activity);
	        	
	        	AdControlManager.initAppCWall();
	        	AdControlManager.initGFWall();
        
	        	AdControlManager.initBannerAd2(Gravity.CENTER|Gravity.BOTTOM);
        
	        	AdControlManager.initAid("jp.co.transcosmoTJOi");	
	        	
	        	//AdControlManager.initCrossRoad("test_key");

	        }
		});
	}
	
	public void initBannerAd1(final String url){
		//Log.d("NativePlugin","initAd");
		handler.post(new Runnable(){
	        @Override
	        public void run(){

		        
		        AdControlManager.initBannerAd1(url,Gravity.CENTER|Gravity.TOP);
	        

	        }
		});
	}
	
	public void initGA(final String ga_tracking_id) {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	gaTracker.set(GoogleAnalytics.getInstance(activity).getTracker(ga_tracking_id));
	        }
		});
	}	
	
	public void startGAView(final String viewName) {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){
//				Toast.makeText(
//						activity,
//						viewName,
//						Toast.LENGTH_LONG).show();
	        	//gaTracker.get().sendView(viewName);
	        	gaTracker.get().set(Fields.SCREEN_NAME, viewName);
	        	gaTracker.get().send(MapBuilder
	        			  .createAppView()
	        			  .build()
	        	);
	        }
		});
	}
	
	public void startGAEvent(final String category,  final String action,  final String label) {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	//gaTracker.get().sendEvent(category, action, label, null);
	        	gaTracker.get().send(MapBuilder
	        		      .createEvent(category, action, label, null)
	        		      .build()
	        			);
	        }
		});
	}
	
	public void sendFacebook(final String facebook_app_id, final String message) {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){	        	
				Intent faceBookIntent = new Intent(activity, FaceBookActivity.class);
				faceBookIntent.putExtra("facebook_app_id", facebook_app_id);
				faceBookIntent.putExtra("shareMessage", message);
				activity.startActivity(faceBookIntent);
	        }
		});
	}
	
	public void sendTwitter(final String twitter_consumer_key, final String twitter_secret_key, final String message) {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){	        	
				Intent twitterIntent = new Intent(activity, TwitterActivity.class);
				twitterIntent.putExtra("twitter_consumer_key", twitter_consumer_key);
				twitterIntent.putExtra("twitter_secret_key", twitter_secret_key);
				twitterIntent.putExtra("shareMessage", message);
				activity.startActivity(twitterIntent);
	        }
		});
	}
	
	public void sendLine(final String message) {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){	        	
				Uri uri = Uri.parse("http://line.naver.jp/R/msg/text/?" + message);
                activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
	        }
		});
	}
	
	
	/* show banner ad */
	public void showBannerAd1() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	AdControlManager.showBannerAd1();
	        }
		});		
	}
	
	public void hideBannerAd1() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){

	        	AdControlManager.hideBannerAd1();
	        }
		});		
	}
	
	public void showBannerAd2() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	AdControlManager.showBannerAd2();
	        }
		});		
	}

	public void hideBannerAd2() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	AdControlManager.hideBannerAd2();
	        }
		});		
	}
	
	/* show shibusan list  webview */
	public void showShibusanAd(final String listurl) {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	    		
	    		ConnectionDetector cd = new ConnectionDetector(activity);
	    		if(!cd.isConnectingToInternet()){
	    			Dialog alertDialog = new AlertDialog.Builder(activity)
	    			.setTitle("接続できません")
	    			.setMessage("渋三あっぷすに接続できません。ネ\nットワークの状態を確認してください。")
	    			.setNegativeButton("OK",
	    					new DialogInterface.OnClickListener() {
	    						@Override
	    						public void onClick(DialogInterface dialog,
	    								int which) {
	    						}
	    					}).create();
	    			alertDialog.setCancelable(true);
	    			alertDialog.setCanceledOnTouchOutside(false);
	    			alertDialog.show();
	    			return;
	    		}	    		
	        	Intent appsviewIntent = new Intent(activity, AppsviewActivity.class);
	    		appsviewIntent.putExtra("appsString", listurl);
	    		appsviewIntent.putExtra("storeString", "https://play.google.com/store/apps/developer?id=transcosmos+inc.&amp;hl=ja");
	    		activity.startActivity(appsviewIntent);
	        }
		});
	}
	
	/* show offer wall ad */
	public void showWallAd1() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	AdControlManager.showAdcropsWall();
	        }
		});		
	}
	
	public void showWallAd2() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	AdControlManager.showAmoadGamesWall();
	        }
		});		
	}
	
	public void showWallAd3() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	AdControlManager.showAppCWall();
	        	
	        }
		});		
	}
	
	public void showWallAd4() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	AdControlManager.showGFWall();

	        }
		});		
	}
	
	public void showWallAd5() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	AdControlManager.showAstaWall("ast00909vlrypcjycji3");

	        }
		});		
	}
	
	
	public void showDialogAd1() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	AdControlManager.showAidImage();
	        }
		});		
	}
	
	public void showDialogAd2() {
		handler.post(new Runnable(){
	        @Override
	        public void run(){
	        	
	        	AdControlManager.exitAidImage();
	        }
		});		
	}
	
	public int initOwnImage(final String path, final int type) {
		
		if(imgv != null && type == 1) {
			return 0;
		}
		
		if(gfv != null && type == 2) {
			return 0;
		}
	
		handler.post(new Runnable(){
	        @Override
	        public void run(){	    

	    		if(type == 1) {
	    			getImageView(path);
	    		}
	    		else if(type == 2) {
	    			getGifView(path);
	    		}
	        }
		});
		
		if(type == 1 || type == 2)
			return type;
		
		return 0;
	}
	
	public void showOwnImage(final int pos) {	
		handler.post(new Runnable(){
	        @Override
	        public void run(){	        	
	    		if(pos == 1 && imgv != null)
	    			imgv.setVisibility(View.VISIBLE);
	    		else if(pos == 2 && gfv != null)
	    			gfv.setVisibility(View.VISIBLE);
	        }
		});
	}
	
	public void hideOwnImage(final int pos) {
		handler.post(new Runnable(){
	        @Override
	        public void run(){	        	
	    		if(pos == 1 && imgv != null)
	    			imgv.setVisibility(View.INVISIBLE);
	    		else if(pos == 2 && gfv != null)
	    			gfv.setVisibility(View.INVISIBLE);
	        }
		});
	}
	
	public int chkStatus() {
		
		handler.post(new Runnable(){
	        @Override
	        public void run(){	        	
	        	ck.SetStatus();
	        }
		});
		
		return ck.GetStatus();
	}
	
	  public void getImageView(String path)
	  {

		   HttpImageView image = new HttpImageView(activity,path);

		   try {
			   imgv = image.execute().get();
		   } catch (InterruptedException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			   return;
		   } catch (ExecutionException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			   return;
		   }

		   activity.addContentView(imgv,  new FrameLayout.LayoutParams(100,100,Gravity.BOTTOM|Gravity.LEFT));
		   
		   imgv.setOnClickListener(iconButtonOnClickListener1);
		   
		   imgv.setVisibility(View.INVISIBLE);
	  }
	  
	  
	  public void getGifView(String path) {
		  HttpInputStream tsk = new HttpInputStream(path);
		  InputStream ipt = null;
		  try {
			  ipt = tsk.execute().get();
		  } catch (InterruptedException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			  return;
		  } catch (ExecutionException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			  return;
		  }
	  	  gfv = new GifView(activity);
	  	  gfv.setGifImage(ipt);
	  	  //addContentView(gf1,new FrameLayout.LayoutParams(WC,WC,Gravity.BOTTOM));
	  	  activity.addContentView(gfv,new FrameLayout.LayoutParams(100,100,Gravity.BOTTOM|Gravity.RIGHT));
	  	  gfv.setOnClickListener(iconButtonOnClickListener2);
	  	  gfv.setVisibility(View.INVISIBLE);
	  }
	  
	  private Button.OnClickListener iconButtonOnClickListener1 = new Button.OnClickListener() {
		public void onClick(View v) {
			v.setVisibility(View.INVISIBLE);
		   }
	  };
	  
	  private Button.OnClickListener iconButtonOnClickListener2 = new Button.OnClickListener() {
		public void onClick(View v) {
			v.setVisibility(View.INVISIBLE);
		   }
	  };
}
