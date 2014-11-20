package jp.co.transcosmos.nativeplugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * HTTP通信でGETリクエストを投げる処理を非同期で行うタスク。
 *
 */
//AsyncTaskのサブクラスとして、バックグラウンド処理用のタスクを記述
public class HttpImageView extends AsyncTask<Void, Void, ImageView>{
	
	// 初期化事項
	private String url = null;
	
	private Bitmap myBitmap = null;
	
	private ImageView image = null;
	
	private Activity mActivity = null;

	public HttpImageView(Activity me, String url) {
	    // 初期化
		
	    this.url = url;
	    
	    mActivity = me;
	}

	@Override
	protected ImageView doInBackground(Void... params) {
		  try {
			  URL url = new URL(this.url);
			  HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			  connection.setDoInput(true);
			  connection.connect();

			  InputStream input = connection.getInputStream();
			  myBitmap = BitmapFactory.decodeStream(input);

			  
			  
			  } catch (OutOfMemoryError e) {
				  //画像が大きすぎたりする場合
				  e.printStackTrace();
				  return null;
			  } catch (Exception e){
				  e.printStackTrace();
			  	  return null;
			  }
	          image = new ImageView(mActivity);
	          image.setImageBitmap(myBitmap);
	          return image;

		}	
}