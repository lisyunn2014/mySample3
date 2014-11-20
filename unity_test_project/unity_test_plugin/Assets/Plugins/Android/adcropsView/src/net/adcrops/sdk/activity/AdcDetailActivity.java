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

import net.adcrops.sdk.AdcController;
import net.adcrops.sdk.view.R;
import net.adcrops.sdk.data.AdcAdData;
import net.adcrops.sdk.util.AdcLog;
import net.adcrops.sdk.util.AdcUtils;
import net.adcrops.sdk.util.AdcUtils.ADC_TITLEBAR_TYPE;
import net.adcrops.sdk.util.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 詳細画面アクティビティクラス.<br>
 * 
 * <p>
 * 詳細画面の表示を行います。<br>
 * このアクティビティのレイアウトは、res/layout/adcrops_view_detail.xmlにて変更することが可能です。<br>
 * </p>
 * @author Tatsuya Uemura
 * @since 1.0.0
 */
public final class AdcDetailActivity extends Activity implements OnClickListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// タイトルの設定
        AdcViewListActivity.setWindowTitle(this);

        setContentView(R.layout.adcrops_view_detail);

        if(AdcViewListActivity.getWindowTitleBarType(this) == ADC_TITLEBAR_TYPE.STRING_WITH_ICON.value()) {
        	// リソースIDで表示するアイコンを指定する (requestWindowFeature より後に記述する)
        	setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.adcrops_icon);
        }

        AdcAdData data = null;
        
        // AdcAdDataを受け取る。
        if(( savedInstanceState == null ) || ( savedInstanceState.isEmpty()) ) {
        	data = (AdcAdData)getIntent().getExtras().get("INTENT_PARAM＿ADC_DETAIL");
        }
        // 念のためにNullチェック
        if( data == null) {
        	super.finish();
        	AdcLog.error("adcDetailActivity param error.");
        	return;
        }
        
        // アイコン設定
        ImageView iconView = (ImageView)findViewById(R.id.adcrops_detail_view_appIcon);
		AdcController.setAppIcon(iconView, data.getImageIcon());

		// アプリ名設定
        TextView appName = (TextView)findViewById(R.id.adcrops_detail_view_appName);
        appName.setText(data.getTitle());
        
        // マーケット名設定
        TextView marketName = (TextView)findViewById(R.id.adcrops_detail_view_marketName);
        marketName.setText(data.getMarketName());

        // カテゴリ名設定
        TextView categoryName = (TextView)findViewById(R.id.adcrops_detail_view_category);
        if(StringUtils.isEmpty(data.getCategory())) {
        	categoryName.setVisibility(View.GONE);
        }else{
        	categoryName.setText(data.getCategory());
        	categoryName.setVisibility(View.VISIBLE);
        }
        
        // 価格
        TextView price = (TextView)findViewById(R.id.adcrops_detail_view_price);
        if(data.getPrice() == 0 ) {
			// 価格が0だったら無料文字列
			String p = getApplicationContext().getResources().getString(R.string.adcrops_list_view_price_text_view_free);
			price.setText(p);
        }else{
			String p = getApplicationContext().getResources().getString(R.string.adcrops_list_view_price_text_view);
	        MessageFormat message = new MessageFormat(p);
			price.setText(message.format(new String[]{new Integer(data.getPrice()).toString()}));
        }
		
		ImageView arrowUpIcon = (ImageView)findViewById(R.id.adcrops_detail_view_arrow_up);
		ImageView arrowDownIcon = (ImageView)findViewById(R.id.adcrops_detail_view_arrow_down);
		// 矢印
		if( data.getOldPrice() == -1 || data.getOldPrice() == data.getPrice()) {
			// 旧価格無しまたは、同じ
			arrowUpIcon.setVisibility(View.GONE);
			arrowDownIcon.setVisibility(View.GONE);
		}else if( data.getOldPrice() > data.getPrice() ) {
			// 旧価格 > 価格
			arrowUpIcon.setVisibility(View.GONE);
			arrowDownIcon.setVisibility(View.VISIBLE);
		}else if( data.getOldPrice() < data.getPrice() ) {
			// 旧価格 < 価格
			arrowUpIcon.setVisibility(View.VISIBLE);
			arrowDownIcon.setVisibility(View.GONE);
		}

		// ポイント
        TextView point = (TextView)findViewById(R.id.adcrops_detail_view_point);
        if( AdcUtils.usePointDisplayItem()) {
	        if( data.getPoint() != 0) {
	        	String p = getApplicationContext().getResources().getString(R.string.adcrops_list_view_point_text_view);
	        	MessageFormat message = new MessageFormat(p);
	        	point.setText(message.format(new String[]{new Float(data.getPoint()).toString()}));
	        }else{
	        	// 非表示
	        	point.setVisibility(View.GONE);
	        	// アイコンも非表示
	            ImageView pointIcon = (ImageView)findViewById(R.id.adcrops_detail_view_point_imageView);
	            pointIcon.setVisibility(View.GONE);
	        }
        }else{
        	point.setVisibility(View.GONE);
        	// アイコンも非表示
            ImageView pointIcon = (ImageView)findViewById(R.id.adcrops_detail_view_point_imageView);
            pointIcon.setVisibility(View.GONE);
        }
        
        // 成果条件
        EditText conversion = (EditText)findViewById(R.id.adcrops_detail_view_conversionEditText);
        conversion.setText(data.getCvCondition());

        // 説明文
        EditText description = (EditText)findViewById(R.id.adcrops_detail_view_descriptionEditText);
        description.setText(data.getDescription());
        
        // 説明文
        EditText detail = (EditText)findViewById(R.id.adcrops_detail_view_detailEditText);
        detail.setText(data.getDetail());

        // インストールボタン
        Button installButton = (Button)findViewById(R.id.adcrops_detail_view_install_button);
        installButton.setOnClickListener(new AdcOnClickListener(data));
        
        // 閉じるボタンにリスナーを登録
        Button button = (Button)findViewById(R.id.adcrops_detail_view_close_button);
        button.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View v) {
        finish();
	}

	@Override
	public void finish() {
		super.finish();
	}

	/**
	 * インストールボタンクリックリスナクラス.<br>
	 * 
	 * <p>
	 * クリックされた時に動作するリスナです。<br>
	 * </p>
	 * @author Tatsuya Uemura
	 * @since 1.0.0
	 */
	class AdcOnClickListener implements OnClickListener {
		/** 広告データ */
		private AdcAdData data;
		
		/**
		 * コンストラクタ.<br>
		 * 
		 * <p>
		 * 広告データを設定します。<br>
		 * </p>
		 * @param data 広告データ
		 */
		public AdcOnClickListener(AdcAdData data) {
			this.data = data;
		}
		
		@Override
		public void onClick(View v) {
        	AdcController.onClick(data.getLinkUrl());
		}
	}

}