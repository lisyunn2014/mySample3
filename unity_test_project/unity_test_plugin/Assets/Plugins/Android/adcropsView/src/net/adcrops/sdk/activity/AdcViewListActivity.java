/*
 * Copyright (C) 2011 8crops, Inc. All Rights Reserved.
 *
 * This program belongs to 8crops, Inc.
 * It is considered a trade secret and is not be divulged or used by parts who
 * have not received written authorization from the owner.
 *
 */

package net.adcrops.sdk.activity;

import java.text.MessageFormat;

import net.adcrops.sdk.AdcConstants;
import net.adcrops.sdk.AdcController;
import net.adcrops.sdk.AdcConversionSendWatchdog;
import net.adcrops.sdk.adapter.AdcAdDataAdapter;
import net.adcrops.sdk.view.R;
import net.adcrops.sdk.data.AdcAdData;
import net.adcrops.sdk.exception.AdcInitNotReachableNextworkExcepsion;
import net.adcrops.sdk.listener.AdcNetworkNotifyListener;
import net.adcrops.sdk.listener.AdcXMLRequestListener;
import net.adcrops.sdk.util.AdcLog;
import net.adcrops.sdk.util.AdcUtils;
import net.adcrops.sdk.util.AdcUtils.ADC_TITLEBAR_TYPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * リスト画面アクティビティクラス.<br>
 * 
 * <p>
 * リスト画面の表示を行います。<br>
 * このアクティビティのレイアウトは、res/layout/adcrops_view_list.xml、<br>
 * res/layout/adcrops_view_header.xml、<br>
 * res/layout/adcrops_view_fotter.xmlにて変更することが可能です。<br>
 * </p>
 * 
 * @author Tatsuya Uemura
 * @since 1.0.0
 */
public final class AdcViewListActivity extends ListActivity implements AdcXMLRequestListener,AdcNetworkNotifyListener{
	/** リスト表示のアダプタ */  
	private AdcAdDataAdapter adapter = null;  

    /** エラー時に表示するダイアログ */
    private AlertDialog alert;

    /** ハンドラ */
	private Handler handler = new Handler();

    /**
     * コンストラクタ.<br>
     * 
     * <p>
     * コンストラクタメソッドです。<br>
     * </p>
     * @since 1.0.0
     */
    public AdcViewListActivity() {
    	super();
    	AdcLog.debug("create instance AdcViewListActivity");
    }

	/**
	 * タイトルバーの種別を取得.<br>
	 * 
	 * <p>
	 * AndroidManifest.xmlに定義されているタイトルバー種別を取得します。<br>
	 * 0:なし<br>
	 * 1:文字列だけ<br>
	 * 2:アイコン＋文字列<br>
	 * </p>
	 * 
	 * @param context コンテキスト
	 * @return 設定されているタイトルバーの種別
	 * @since 1.0.0
	 */
    public static int getWindowTitleBarType(Context context) {
		ApplicationInfo info;
		try {
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			AdcLog.error("getWindowTitleBarType faild. ",e);
			return ADC_TITLEBAR_TYPE.NONE.value();
		}
		return new Integer(info.metaData.get(AdcConstants.WINDOW_TITLE_BAR_TYPE).toString()).intValue();
	}

	/**
	 * ウィンドウのタイトルの設定.<br>
	 * 
	 * <p>
	 * ウィンドウのタイトルを設定します。<br>
	 * アイコンは設定されませんので別途呼び出し側で設定する必要があります。<br>
	 * </p>
	 * 
	 * @param activity アクティビティオブジェクト
	 * @since 1.0.0
	 */
	public static void setWindowTitle(Activity activity) {
		int type = getWindowTitleBarType(activity);
		if( type == ADC_TITLEBAR_TYPE.NONE.value() ) {
			activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}else if (type == ADC_TITLEBAR_TYPE.STRING.value()) {
			activity.setTitle(R.string.adcrops_appname);
		}else if (type == ADC_TITLEBAR_TYPE.STRING_WITH_ICON.value()) {
			activity.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			activity.setTitle(R.string.adcrops_appname);
		}
	}

	
    /**
     * ダイアログを初期化.<br>
     * 
     * <p>
     * エラーダイアログの生成を行います。<br>
     * </p>
     * @since 1.0.0
     */
    private void initAlertDialog() {
		// エラー表示用のダイアログの準備
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		 
        dialog.setTitle(getResources().getText(R.string.adcrops_list_view_error_dialog_title));
 
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert = dialog.create();
    }
    
    @Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		
		// 念のためにここでもチェックする。
		if( !AdcController.isInstance() ) {
			finish();
			return;
		}
		// タイトルの設定
		setWindowTitle(this);
		
		// エラーダイアログの準備
		initAlertDialog();
    	try {
    		AdcController.setActivity(this);
		} catch (AdcInitNotReachableNextworkExcepsion e) {
			AdcLog.warn("AdcViewList AdcInitNotReachableNextworkExcepsion:" + e.getLocalizedMessage());
	    	alert.setMessage(getResources().getText(R.string.adcrops_list_view_error_dialog_msg_init_network));
	    	alert.setButton("OK", new DialogInterface.OnClickListener() {
	    		@Override
	    		public void onClick(DialogInterface dialog, int which) {
	    	    	finish();
            	}
	    	});
	    	alert.show();
			return;
		}

		// ListViewにヘッダーを追加(必ずsetAdapterの前に呼び出すこと)
		getListView().addHeaderView(
                getLayoutInflater().inflate(R.layout.adcrops_view_header, null),
                null,
                true
        );
        // ListViewにフッターを追加(必ずsetAdapterの前に呼び出すこと)
		getListView().addFooterView(
                getLayoutInflater().inflate(R.layout.adcrops_view_footer, null),
                null,
                true
        );

        // 次の○○件を設定する。
        String text = ((TextView)findViewById(R.id.adcrops_list_view_FooterTextView)).getText().toString();
        MessageFormat message = new MessageFormat(text);
		text = message.format(new String[]{new Integer(AdcUtils.getAppListCount()).toString()});
        ((TextView)findViewById(R.id.adcrops_list_view_FooterTextView)).setText(text);
        
		adapter = new AdcAdDataAdapter(this, R.layout.adcrops_view_list);  
		setListAdapter(adapter);  

		// タイトルアイコンの設定
		if(getWindowTitleBarType(this) == ADC_TITLEBAR_TYPE.STRING_WITH_ICON.value()) {
        	// リソースIDで表示するアイコンを指定する (requestWindowFeature より後に記述する)
        	setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.adcrops_icon);
        }
		
		// 初回だけonCreate時に初期リストを読み込む。
		if(AdcController.getAdDataList().size() == 0) {
			ProgressBar progress = (ProgressBar)findViewById(R.id.adcrops_list_view_header_progressbar);
			AdcController.requestDataList(AdcViewListActivity.this,adapter,progress);
		}else{
        	// 次のページが存在しない場合は無効にする。
			setFooterEndText();
		}
	    
        // クリックリスナーを設定
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
            	// バックグランドの画像を元に戻す。
            	ListView listView = (ListView) parent;
            	for (int i = 0; i < listView.getChildCount(); i++) {
            		if (listView.getChildAt(i).getId() != R.id.adcrops_list_view_header &&
            				listView.getChildAt(i).getId() != R.id.adcrops_list_view_footer) {
                		listView.getChildAt(i).setBackgroundResource(R.drawable.adcrops_background);
            		}
				}
        		
                // クリックされたViewがヘッダーか判定
            	if(view.getId() == R.id.adcrops_list_view_header) {
            		AdcLog.debug("AdcViewList click header.");
                    // 次の○○件を設定する。
                    String text = getApplicationContext().getResources().getText(R.string.adcrops_list_view_footer_text_view).toString();
                    MessageFormat message = new MessageFormat(text);
            		text = message.format(new String[]{new Integer(AdcUtils.getAppListCount()).toString()});
                    ((TextView)findViewById(R.id.adcrops_list_view_FooterTextView)).setText(text);
            		
            		// データをリセットする。
            		AdcController.resetDataList();
            		adapter.clear();
            		adapter = new AdcAdDataAdapter(AdcViewListActivity.this, R.layout.adcrops_view_list);
            		setListAdapter(adapter);
            		// ヘッダープログレスバー
            		ProgressBar headerProgress = (ProgressBar)findViewById(R.id.adcrops_list_view_header_progressbar);
            		AdcController.requestDataList(AdcViewListActivity.this,adapter,headerProgress);
            	}else if (view.getId() == R.id.adcrops_list_view_footer) {
                    // クリックされたViewがフッターか判定
            		AdcLog.debug("AdcViewList click footer.");
            		if(AdcController.isNextAdData()) {
                		// フッタープログレスバー
                		ProgressBar footerProgress = (ProgressBar)findViewById(R.id.adcrops_list_view_footer_progressbar);
                		AdcController.requestDataList(AdcViewListActivity.this,adapter,footerProgress);
            		}else{
            			setFooterEndText();
            		}
            	}else{
                	// Viewのアイテムがクリックされた
                    // クリックされたアイテムを取得
                	AdcAdData item = (AdcAdData) listView.getItemAtPosition(position);
                	AdcLog.debug("AdcViewList click Item:" + item);
                	
                	// 選択された背景画像をだけ変更する。
            		if (view.getId() != R.id.adcrops_list_view_header &&
            				view.getId() != R.id.adcrops_list_view_footer) {
            			view.setBackgroundResource(R.drawable.adcrops_selected_background);
            		}
                	
                	// 未インストールのアプリでは詳細画面を表示
                	if(!item.isInstalled()) {
                		Intent intent = new Intent( AdcViewListActivity.this, AdcDetailActivity.class );
                		intent.putExtra( "INTENT_PARAM＿ADC_DETAIL", item );
                		startActivity( intent );
                	}else{
                		// カスタマイズされた任意のURLへ
                		jumpInstalledLink(item);
                	}
                }
            }
        });
		
		// アイテム選択リスナを設定
		// これは矢印キーで反応するらしい。
		getListView().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                ListView listView = (ListView) parent;
            	// バックグランドの画像を元に戻す。
            	for (int i = 0; i < listView.getChildCount(); i++) {
            		if (listView.getChildAt(i).getId() != R.id.adcrops_list_view_header &&
            				listView.getChildAt(i).getId() != R.id.adcrops_list_view_footer) {
                		listView.getChildAt(i).setBackgroundResource(R.drawable.adcrops_background);
            		}
				}
        		if (view.getId() != R.id.adcrops_list_view_header &&
        				view.getId() != R.id.adcrops_list_view_footer) {
        			// 選択された背景画像をだけ変更する。
        			view.setBackgroundResource(R.drawable.adcrops_selected_background);
        		}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * インストール済みのページへ遷移.<br>
     * 
     * <p>
     * インストール済みページURLにパラメータを付与し遷移します。<br>
     * インストール済みページURLの設定はAndroidMainfest.xmlのADC_INSTALLED_LIST_ITEM_URLに指定します。<br>
     * </p>
     * @param data 広告データ
     * @since 1.0.0
     */
    public void jumpInstalledLink(AdcAdData data) {
		// カスタマイズされた任意のURLへ
    	String url = AdcUtils.getInstalledListItemUrl();
        // 広告ID
		url = AdcUtils.addUrlParam(url, "articleId", data.getArticleId());
        // アプリID
		url = AdcUtils.addUrlParam(url, "appId", data.getAppId());
		// マーケットID
		url = AdcUtils.addUrlParam(url, "marketId", data.getMarketId());
        // md5したUDID
		url = AdcUtils.addUrlParam(url, "euid", AdcUtils.getEuid());
		AdcLog.debug("redirect installed url:" + url);
    	AdcController.onClick(url);
    }
    
    /**
     * 次の○○件のフッターに終了文字列を設定.<br>
     * 
     * <p>
     * 次のページが無いときにフッターの文字列を変更します。<br>
     * </p>
     * @since 1.0.0
     */
    public void setFooterEndText() {
    	if(!AdcController.isNextAdData()) {
			AdcLog.debug("AdcViewList not next page.");
			TextView footer = ((TextView)findViewById(R.id.adcrops_list_view_FooterTextView));
			// 文字を変える
			footer.setText(getApplicationContext().getResources().getText(R.string.adcrops_list_view_footer_text_view_no_data));
    	}
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        AdcLog.debug("AdcViewList onPause" );
        // バックエンドでのコンバージョン送信を開始する。
        AdcConversionSendWatchdog.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdcLog.debug("AdcViewList onResume" );
        // バックエンドでのコンバージョン送信を停止する。
        AdcConversionSendWatchdog.stop();
    }

	@Override
	public void onAdcXMLRequestSucceful() {
		AdcLog.debug("AdcViewList onAdcXMLRequestSucceful");
	}

	@Override
	public void onAdcXMLRequestTimeout(Exception e) {
		AdcLog.error("AdcViewList onAdcXMLRequestTimeout",e);
		// ダイアログを表示する。
		handler.post(new Runnable() { 
		    public void run() {
		    	alert.setMessage(getResources().getText(R.string.adcrops_list_view_error_dialog_msg_network));
		    	alert.show();
		    }  
		});  
	}

	@Override
	public void onAdcXMLRequestError(Exception e) {
		AdcLog.error("AdcViewList onAdcXMLRequestError",e);
		// ダイアログを表示する。
		handler.post(new Runnable() { 
		    public void run() {
		    	alert.setMessage(getResources().getText(R.string.adcrops_list_view_error_dialog_msg_network));
		    	alert.show();
		    }  
		});  
	}

	/* (非 Javadoc)
	 * @see net.adcrops.listener.AcNetworkNotifyListener#onAcRequestNotReachableStatusError()
	 */
	@Override
	public void onAdcRequestNotReachableStatusError() {
		AdcLog.debug("##### onAdcRequestNotReachableStatusError");
		// ダイアログを表示する。
		handler.post(new Runnable() { 
		    public void run() {
		    	alert.setMessage(getResources().getText(R.string.adcrops_list_view_error_dialog_msg_network));
		    	alert.show();
		    }  
		});  
	}

}
