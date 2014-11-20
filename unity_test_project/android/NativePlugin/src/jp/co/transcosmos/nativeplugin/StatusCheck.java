package jp.co.transcosmos.nativeplugin;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class StatusCheck {
	
	protected static final String STATUS_0        = "0";	// 公開
	protected static final String STATUS_1        = "1";	// 審査中
	protected static final String STATUS_2        = "2";	// 通常アップデート
	protected static final String STATUS_3        = "3";	// 強制アップデート
	protected static final String STATUS_9        = "9";	// メンテナンス中
	protected static final String STATUS_99       = "99";	// エラー

	private static Activity mActivity;
	private static String ret;
	private static String mPkname;
	private static String mVername;
	
	public StatusCheck (Activity activity, String appid) {
		mActivity = activity;
		
		mPkname = mActivity.getPackageName();
		mVername = getVersionName(mActivity);
		
		ret = null;
		String url = "http://shibusan.jp/api/status_check.php?appid="+appid+"&v="+mVername+"&device=1";

   	 	HttpGetTask task = new HttpGetTask(url);
   	 	try {
			ret = task.execute().get();
		 } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
   	 	
	}
	
	int GetStatus() {
		
		if(ret.equals(STATUS_0)) {
			// 公開
			return 0;
		}
		else if(ret.equals(STATUS_1)) {
			// 審査中
			return 1;
		}
		else if(ret.equals(STATUS_2)) {

   	        // 通常アップデート
   	        return 2;
   	 	}
   	 	
   	 	else if(ret.equals(STATUS_3)) {

	        // 強制アップデート
   	        return 3;
   	 	}
   	 	else if(ret.equals(STATUS_9)) {

   	        
   	        // メンテナンス中
   	        return 9;
   	 	}
		// エラー
		return 99;
   		 
	}
	
	void SetStatus() {
		if(ret.equals(STATUS_2)) {

			AlertDialog.Builder diag = new AlertDialog.Builder(mActivity);
   	        //diag.setTitle("App Titler")
   	        diag.setMessage("このアプリを更新しますか？");
   	        diag.setPositiveButton("いいえ",
   	                new DialogInterface.OnClickListener()
   	        {
   	            public void onClick(DialogInterface dialog, int which)
   	            {  

   	            }
   	        });
   	        diag.setNegativeButton("はい",
   	                new DialogInterface.OnClickListener()
   	        {
   	            public void onClick(DialogInterface dialog, int which)
   	            {
   	            	String myUrl = "https://play.google.com/store/apps/details?id="+mPkname;
	            	Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(myUrl));
	            	mActivity.startActivity(i);
	            	//mActivity.finish();
   	            }
   	        });
   	        diag.setCancelable(false);
   	        diag.create();
   	        diag.show();
   	        
   	        // 通常アップデート

   	 	}
   	 	
   	 	else if(ret.equals(STATUS_3)) {
   	 		
			View v = new View(mActivity);
			v.setBackgroundColor(0xff000000);
			mActivity.addContentView(v,new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        
   	        AlertDialog.Builder diag = new AlertDialog.Builder(mActivity);
   	        diag.setMessage("このアプリを更新してくだい！");
   	        diag.setPositiveButton("はい",
   	                new DialogInterface.OnClickListener()
   	        {
   	            public void onClick(DialogInterface dialog, int which)
   	            {  
   	            	String myUrl = "https://play.google.com/store/apps/details?id="+mPkname;
	            	Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(myUrl));
	            	mActivity.startActivity(i);
   	            	mActivity.finish();
   	            	android.os.Process.killProcess(android.os.Process.myPid());
   	            }
   	        });
   	        diag.setCancelable(false);
   	        diag.create();
   	        diag.show();
   	        
	        // 強制アップデート

   	 	}
   	 	else if(ret.equals(STATUS_9)) {
   	 		
			View v = new View(mActivity);
			v.setBackgroundColor(0xff000000);
			mActivity.addContentView(v,new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

   	 		
   	        AlertDialog.Builder diag = new AlertDialog.Builder(mActivity);
   	        diag.setMessage("現在メンテナンス中のため、しばらくの間ご利用いただけません。");
   	        diag.setPositiveButton("アプリ終了",
   	                new DialogInterface.OnClickListener()
   	        {
   	            public void onClick(DialogInterface dialog, int which)
   	            {  
   	            	mActivity.finish();
   	            	android.os.Process.killProcess(android.os.Process.myPid());
   	            }
   	        });
   	        diag.setCancelable(false);
   	        diag.create();
   	        diag.show();
   	        
   	        // メンテナンス中

   	 	}
		// エラー
		 
	}
	
	 /**
	 * バージョンコードを取得する
	 *
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context){
	    PackageManager pm = context.getPackageManager();
	    int versionCode = 0;
	    try{
	        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
	        versionCode = packageInfo.versionCode;
	    }catch(NameNotFoundException e){
	        e.printStackTrace();
	    }
	    return versionCode;
	}
	
	/**
	 * バージョン名を取得する
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
	    PackageManager pm = context.getPackageManager();
	    String versionName = "";
	    try{
	        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
	        versionName = packageInfo.versionName;
	    }catch(NameNotFoundException e){
	        e.printStackTrace();
	    }
	    return versionName;
	}
}