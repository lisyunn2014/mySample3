/*
 * Copyright (C) 2011 8crops, Inc. All Rights Reserved.
 *
 * This program belongs to 8crops, Inc.
 * It is considered a trade secret and is not be divulged or used by parts who
 * have not received written authorization from the owner.
 *
 */

package net.adcrops.sdk.adapter;

import java.text.MessageFormat;
import java.util.ArrayList;

import net.adcrops.sdk.AdcController;
import net.adcrops.sdk.view.R;
import net.adcrops.sdk.activity.AdcViewListActivity;
import net.adcrops.sdk.data.AdcAdData;
import net.adcrops.sdk.data.AdcViewHolder;
import net.adcrops.sdk.util.AdcLog;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// カスタムビュー？
// http://weide-dev.blogspot.com/2010/04/xml-customview.html
// カスタムコンポーネント
// http://wikiwiki.jp/android/?%A5%AB%A5%B9%A5%BF%A5%E0%A5%B3%A5%F3%A5%DD%A1%BC%A5%CD%A5%F3%A5%C8%A4%CE%BA%EE%C0%AE
// android xml レイアウト 使わない
// http://stachibana.biz/?p=1239

/**
 * 広告リストデータ表示用のアダプタクラス.<br>
 * 
 * <p>
 * リストビューに表示する広告リストデータオブジェクトをアダプタに設定します。<br>
 * </p>
 * 
 * @author Tatsuya Uemura
 * @since 1.0.0
 */
public final class AdcAdDataAdapter extends ArrayAdapter<AdcAdData>{
	/** リストデータ */
	private ArrayList<AdcAdData> items;
	/** レイアウト */
	private LayoutInflater inflater;  
	/** リストアクティビティ */
	private AdcViewListActivity activity;
	
	/**
	 * コンストラクタ.<br>
	 * 
	 * <p>
	 * コンストラクタメソッドです。<br>
	 * </p>
	 * 
	 * @param context コンテキストオブジェクト
	 * @param textViewResourceId リソースID
	 * @since 1.0.0
	 */
	public AdcAdDataAdapter(Context context, int textViewResourceId) {  
		super(context, textViewResourceId, AdcController.getAdDataList());  

		this.items = AdcController.getAdDataList();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		activity = (AdcViewListActivity)context;
	}  

	@Override  
	public View getView(int position, View convertView, ViewGroup parent) {  
		// ビューを受け取る  
		View view = convertView;
		AdcViewHolder holder = null;
		
		if (view == null) {  
			// 受け取ったビューがnullなら新しくビューを生成  
			view = inflater.inflate(R.layout.adcrops_view_list, null);  
			// 背景画像をセットする  
			view.setBackgroundResource(R.drawable.adcrops_background);
			// ホルダを作成
			holder = new AdcViewHolder();  
			
	        // アプリアイコン設定
			ImageView appIcon = (ImageView)view.findViewById(R.id.adcrops_list_view_appIcon);
	        holder.appIcon = appIcon;  

	        // アプリ名設定
			TextView appName = (TextView)view.findViewById(R.id.adcrops_list_view_appName);  
			appName.setTypeface(Typeface.DEFAULT_BOLD);  
	        holder.appName = appName;
	        
	        // 説明文設定
			TextView description = (TextView)view.findViewById(R.id.adcrops_list_view_description);  
	        holder.description = description;
	        
	        // 価格設定
	        holder.price = (TextView)view.findViewById(R.id.adcrops_list_view_price);
	        
	        // 価格UP画像設定
			ImageView arrowUpIcon = (ImageView)view.findViewById(R.id.adcrops_list_view_arrow_up);
			holder.arrowUpIcon = arrowUpIcon;

	        // 価格DOWN画像設定
			ImageView arrowDownIcon = (ImageView)view.findViewById(R.id.adcrops_list_view_arrow_down);
			holder.arrowDownIcon = arrowDownIcon;
	        
			// インストールORインストール後ボタン設定
	        holder.button = (Button)view.findViewById(R.id.adcrops_list_view_install_button);

	        // タグ設定
	        view.setTag(holder);  
		}else {  
	        holder = (AdcViewHolder)view.getTag();  
	    }  
		// 表示すべきデータの取得  
		AdcAdData item = (AdcAdData)items.get(position);  
		if (item != null) {
			AdcLog.debug(items.size() + ":AdcAdDataAdapter add("+position+"):" + item.getTitle());
			  
			// アプリ名をビューにセット  
			if (holder.appName != null) {  
				holder.appName.setText(item.getTitle());  
			}
			  
			// 説明文をビューにセット  
			if (holder.description != null) {  
				holder.description.setText(item.getDescription());  
			}

			// 価格
			if( holder.price != null) {
		        if(item.getPrice() == 0 ) {
					// 価格が0だったら無料文字列
					String p = getContext().getResources().getString(R.string.adcrops_list_view_price_text_view_free);
					holder.price.setText(p);
		        }else{
					String p = getContext().getResources().getString(R.string.adcrops_list_view_price_text_view);
			        MessageFormat message = new MessageFormat(p);
					holder.price.setText(message.format(new String[]{new Integer(item.getPrice()).toString()}));
		        }
				
				// 矢印
				if( item.getOldPrice() == -1 || item.getOldPrice() == item.getPrice()) {
					// 旧価格無しまたは、同じ
					holder.arrowUpIcon.setVisibility(View.GONE);
					holder.arrowDownIcon.setVisibility(View.INVISIBLE);
				}else if( item.getOldPrice() > item.getPrice() ) {
					// 旧価格 > 価格
					holder.arrowUpIcon.setVisibility(View.GONE);
					holder.arrowDownIcon.setVisibility(View.VISIBLE);
				}else if( item.getOldPrice() < item.getPrice() ) {
					// 旧価格 < 価格
					holder.arrowUpIcon.setVisibility(View.VISIBLE);
					holder.arrowDownIcon.setVisibility(View.GONE);
				}
			}else{
				// 価格がなければ矢印は表示しない
				holder.arrowUpIcon.setVisibility(View.GONE);
				holder.arrowDownIcon.setVisibility(View.GONE);
			}
			
			// アプリアイコンをビューにセット
			AdcController.setAppIcon(holder.appIcon, item.getImageIcon());
			
		    // ボタンにリスナを追加する。
		    holder.button.setOnClickListener(new AdcOnClickListener(item));
		    
		    // ボタンの表示を切り替える。
		    if(item.isInstalled()) {
			    // インストール済み時のボタン
		    	String s = getContext().getResources().getString(R.string.adcrops_list_view_installed_button);
		    	Drawable d = getContext().getResources().getDrawable(R.drawable.adcrops_installed_button);
		    	holder.button.setBackgroundDrawable(d);
		    	holder.button.setText(s);
		    }else{
		    	// インストールボタン
		    	String s = getContext().getResources().getString(R.string.adcrops_list_view_install_button);
		    	Drawable d = getContext().getResources().getDrawable(R.drawable.adcrops_install_button);
		    	holder.button.setBackgroundDrawable(d);
		    	holder.button.setText(s);
		    }
			// インプレッション送信
			if(!item.isImpSend()) {
				AdcController.sendImpression(item);
			}
		}
		return view;  
	} 
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		activity.setFooterEndText();
		AdcLog.debug("AdcAdDataAdapter notifyDataSetChanged");
	}

	/**
	 * インストール／インストール済みクリックリスナ.<br>
	 * 
	 * <p>
	 * ボタンがクリックされた時にインストール／インストール済みを判定してページに遷移します。<br>
	 * </p>
	 * 
	 * @since 1.0.0
	 */
	class AdcOnClickListener implements OnClickListener {
		/** 広告データ */
		private AdcAdData data;
		
		/**
		 * コンストラクタ.<br>
		 * 
		 * <p>
		 * コンストラクタメソッドです。<br>
		 * </p>
		 * 
		 * @param data 広告データ
		 * @since 1.0.0
		 */
		public AdcOnClickListener(AdcAdData data) {
			this.data = data;
		}
		
		@Override
		public void onClick(View v) {
        	if(data.isInstalled()) {
        		// カスタマイズされた任意のURLへ
    			activity.jumpInstalledLink(data);
        	}else{
        		// アプリダウンロードURLへ
        		AdcController.onClick(data.getLinkUrl());
        	}
		}
	}
}
