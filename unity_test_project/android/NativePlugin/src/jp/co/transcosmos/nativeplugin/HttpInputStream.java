package jp.co.transcosmos.nativeplugin;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;


/**
 * HTTP通信でGETリクエストを投げる処理を非同期で行うタスク。
 *
 */
//AsyncTaskのサブクラスとして、バックグラウンド処理用のタスクを記述
public class HttpInputStream extends AsyncTask<Void, Void, InputStream>{
	
	// 初期化事項
	private String url = null;
	
	private InputStream input = null;

	public HttpInputStream(String url) {
	    // 初期化		
	    this.url = url;

	}

	@Override
	protected InputStream doInBackground(Void... params) {
		  try {
			  URL url = new URL(this.url);
			  HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			  connection.setDoInput(true);
			  connection.connect();

			  input = connection.getInputStream();
	  
			  } catch (OutOfMemoryError e) {
				  //画像が大きすぎたりする場合
				  e.printStackTrace();
				  return null;
			  } catch (Exception e){
				  e.printStackTrace();
			  	  return null;
			  }

	          return input;

		}	
}