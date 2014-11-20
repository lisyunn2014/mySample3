package com.unity3d.player.faceBook;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;

public class FaceBookActivity extends Activity {

	static String pending_request_bundle_key;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	Session session;
	boolean pendingRequest;
	private String shareMessage = null;
	private Request request;
	
	private String facebook_app_id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		facebook_app_id = getIntent().getExtras().getString("facebook_app_id");
		
		pending_request_bundle_key = getPackageName() + ":PendingRequest";

		this.session = createSession();
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		// シェアーの文言を取得する（"shareMessage"）
		shareMessage = getIntent().getExtras().getString("shareMessage");

		onClickRequest();
	}

	private void onClickRequest() {
		// ユーザーは既にログインした場合、直接に文言を発送する
		if (this.session.isOpened()) {
			performPublish();
		}
		// ユーザーはログインしていない場合、ログイン画面を表示する
		else {
			StatusCallback callback = new StatusCallback() {
				public void call(Session session, SessionState state,
						Exception exception) {
					if (exception != null) {
						if (exception.getMessage().equals(
								"User canceled log in.")) {

						} else {
							Toast.makeText(
									FaceBookActivity.this,
									"Facebookに接続が失敗しました、ネット状況を確認して、再度接続してみてください。",
									Toast.LENGTH_LONG).show();

							session = createSession();
						}

						FaceBookActivity.this.finish();
					}
				}
			};
			pendingRequest = true;

			this.session.openForRead(new Session.OpenRequest(this)
					.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO)
					.setCallback(callback));
		}
	}

	/**
	 * 文言をFacebookに発送するイベント
	 */
	private void sendRequests() {
		String requestIdsText = shareMessage;
		request = Request.newStatusUpdateRequest(session, requestIdsText,
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						try {
							if (response.getConnection() != null) {
								// 発送成功の場合
								if (response.getConnection().getResponseCode() == 200) {
									Toast.makeText(FaceBookActivity.this,
											"Facebookに投稿しました。",
											Toast.LENGTH_LONG).show();
								} else {
									if (response.getError() != null
											&& response.getError()
													.getErrorCode() == 506) {
										// 重複発送の場合
										Toast.makeText(
												FaceBookActivity.this,
												"Facebookに投稿が失敗しました、重複内容を投稿できません。",
												Toast.LENGTH_LONG).show();
									} else {
										// 発送失敗の場合
										Toast.makeText(
												FaceBookActivity.this,
												"Facebookに投稿が失敗しました、ネット状況を確認してください。",
												Toast.LENGTH_LONG).show();
									}
								}
							} else {
								// 発送失敗の場合
								Toast.makeText(FaceBookActivity.this,
										"Facebookに投稿が失敗しました、ネット状況を確認してください。",
										Toast.LENGTH_LONG).show();
								FaceBookActivity.this.finish();
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							FaceBookActivity.this.finish();
						}
					}
				});
		pendingRequest = false;
		request.executeAsync();
	}

	@Override
	protected void onDestroy() {
		if (request != null) {
			request = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		pendingRequest = savedInstanceState.getBoolean(
				pending_request_bundle_key, pendingRequest);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(pending_request_bundle_key, pendingRequest);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (this.session.onActivityResult(this, requestCode, resultCode, data)
				&& pendingRequest && this.session.getState().isOpened()) {
			performPublish();
		}
	}

	private Session createSession() {
		Session activeSession = Session.getActiveSession();
		if (activeSession == null || activeSession.getState().isClosed()) {
			activeSession = new Session.Builder(this).setApplicationId(
					facebook_app_id)
					.build();
			Session.setActiveSession(activeSession);
		}
		return activeSession;
	}

	private boolean hasPublishPermission() {
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	/**
	 * 発送できるかどうかの判断
	 */
	private void performPublish() {
		if (session != null) {
			if (hasPublishPermission()) {
				// 文言を発送する
				sendRequests();
			} else {
				// ログイン画面表示する
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						this, PERMISSIONS)
						.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO));
			}
		}
	}

}
