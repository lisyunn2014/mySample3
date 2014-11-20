package jp.co.transcosmos.nativeplugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

/**
 * HTTP通信でGETリクエストを投げる処理を非同期で行うタスク。
 *
 */
//AsyncTaskのサブクラスとして、バックグラウンド処理用のタスクを記述
public class HttpGetTask extends AsyncTask<Void, Void, String>{
	
	// 初期化事項
	private String url = null;

	public HttpGetTask(String url) {
	    // 初期化
	    this.url = url;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			// AndroidHttpClientを使ってみた (Android 2.2 Froyoから使えます)
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android UserAgent");
			HttpResponse res = client.execute(new HttpGet(url));
				
					// HttpResponseのEntityデータをStringへ変換
	                BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
	                StringBuilder builder = new StringBuilder();
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	        	        builder.append(line);
	                }
		        
	                return builder.toString();
		} catch (Exception e) {
			e.getStackTrace();
			return "";
		}
	}
    	
}