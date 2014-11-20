package com.unity3d.player.twitter;


import com.unity3d.player.twitter.TwitterApp.TwDialogListener;
import twitter4j.TwitterException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class TwitterActivity extends Activity {

	private TwitterApp mTwitter;

	private static final int TWITTER_ON_COMPLETE = 1;
	private static final int TWITTER_RECEIVED_SAME_TWEET = 2;
	private static final int TWITTER_RECEIVED_FAILED = 3;
	private Thread mThread = null;

	private String shareMessage = null;
	private String twitter_consumer_key = null;
	private String twitter_secret_key = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitter_consumer_key = getIntent().getExtras().getString("twitter_consumer_key");
		twitter_secret_key = getIntent().getExtras().getString("twitter_secret_key");
		
		mTwitter = new TwitterApp(this, twitter_consumer_key,twitter_secret_key);
		mTwitter.setListener(mTwLoginDialogListener);

		// シェアーの文言を取得する（"shareMessage"）
		shareMessage = getIntent().getExtras().getString("shareMessage");

		// ユーザーは既にログインした場合、直接に文言を発送する
		if (mTwitter.hasAccessToken()) {
			sendToTwitter();
		}
		// ユーザーはログインしていない場合、ログイン画面を表示する
		else {
			mTwitter.authorize();
		}
	}

	private void sendToTwitter() {
		String review = shareMessage;
		postToTwitter(review);
	}

	/**
	 * 文言をtwitterに発送するイベント
	 * 
	 * @param review
	 *            発送される文言
	 */
	private void postToTwitter(final String review) {
		mThread = new Thread() {
			@Override
			public void run() {
				try {
					// エラーが出ない場合は、発送成功した。whatをTWITTER_ON_COMPLETEに設置する。
					mTwitter.updateStatus(review);
					Message msg = new Message();
					msg.what = TWITTER_ON_COMPLETE;
					mHandler.sendMessage(msg);

				} catch (TwitterException e) {

					if (e.getStatusCode() == 403) {
						// エラーコードは403の場合は、重複発送の証拠だから、whatをTWITTER_RECEIVED_SAME_TWEETに設置する。
						Message msg = new Message();
						msg.what = TWITTER_RECEIVED_SAME_TWEET;
						mHandler.sendMessage(msg);
					} else {
						// 他の場合で発送失敗なら、whatをTWITTER_RECEIVED_FAILEDに設置する。
						Message msg = new Message();
						msg.what = TWITTER_RECEIVED_FAILED;
						mHandler.sendMessage(msg);
					}

				} finally {
					TwitterActivity.this.finish();
				}
			}
		};
		mThread.start();
	}

	@Override
	protected void onDestroy() {
		if (mThread != null && mThread.isAlive()) {
			mThread.interrupt();
		}
		if (mTwitter != null) {
			mTwitter.shutDownTwitter();
		}
		super.onDestroy();
	}

	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) {
			sendToTwitter();
		}

		@Override
		public void onError(String value) {
			if (value.equals("Error getting access token")) {

			} else {
				Toast.makeText(TwitterActivity.this,
						"Twitterに接続が失敗しました、ネット状況を確認して、再度接続してみてください。",
						Toast.LENGTH_LONG).show();
			}
			TwitterActivity.this.finish();
		}
	};

	/**
	 * what値によって、適当にユーザーにアラート提示を表示する。
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TWITTER_ON_COMPLETE:
				Toast.makeText(TwitterActivity.this, "Twitterに投稿しました。",
						Toast.LENGTH_LONG).show();
				break;
			case TWITTER_RECEIVED_SAME_TWEET:
				Toast.makeText(TwitterActivity.this,
						"Twitterに投稿が失敗しました、重複内容を投稿できません", Toast.LENGTH_LONG)
						.show();
				break;
			case TWITTER_RECEIVED_FAILED:
				Toast.makeText(TwitterActivity.this,
						"Twitterに投稿が失敗しました、ネット状況を確認してください。", Toast.LENGTH_LONG)
						.show();
				break;
			}
		}

	};

}
