package com.iansoft.android.AdManager;

import com.facebook.ads.*;

import com.iansoft.android.Log;
import com.iansoft.android.Config;
import com.iansoft.android.Constant;
import com.iansoft.android.EpochManager;
import com.iansoft.android.PreferencesManager;

import android.app.Activity;

import android.view.View;
import android.widget.RelativeLayout;

public class FacebookBanner implements AdListener {
	private static FacebookBanner m_sInstance = null;

	private AdView adView = null;
	private Activity activity = null;
	private EpochManager epochManager = null;
	private PreferencesManager preferences = null;

	public FacebookBanner(Activity activity, String adID, View view) {
		if (m_sInstance == null) {
			m_sInstance = this;
		}
		this.activity = activity;

		epochManager = new EpochManager();
		preferences = new PreferencesManager();

		if (IsCanShowAds()) {
			AdSettings.addTestDevice(Config.GetInstance().getDeviceID());

			adView = new AdView(activity, adID, AdSize.BANNER_HEIGHT_50);
			adView.setAdListener(this);
			adView.loadAd();

			RelativeLayout layout = (RelativeLayout)view;
			RelativeLayout.LayoutParams bannerParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
			bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layout.addView(adView, bannerParameters);
		}
	}

	@Override
	public void onError(Ad ad, AdError error) {
	}

	@Override
	public void onAdLoaded(Ad ad) {
	}

	@Override
	public void onAdClicked(Ad ad) {
		hideAds();
	}

	public void destroy() {
		if (adView != null) {
			adView.destroy();
		}
	}

	private void hideAds() {
		long currentEpoch = epochManager.getCurrentEpoch();
		preferences.putLong(Constant.HIDE_BANNER_EXPIRED, currentEpoch + Constant.EXPRIED_TIME);
		destroy();
	}

	private boolean IsCanShowAds() {
		long currentEpoch = epochManager.getCurrentEpoch();
		long expriedHideAds = preferences.getLong(Constant.HIDE_BANNER_EXPIRED);

		if (epochManager.getCurrentDate() > preferences.getInt(Constant.HIDE_ALL_EXPIRED)) {
			if (currentEpoch - expriedHideAds > Constant.EXPRIED_TIME) {
				return true;
			}
		}
		return false;
	}
}