package com.iansoft.android.AdManager;

import com.facebook.ads.*;

import com.iansoft.android.Log;
import com.iansoft.android.Config;
import com.iansoft.android.Constant;
import com.iansoft.android.EpochManager;
import com.iansoft.android.ConnectServer;
import com.iansoft.android.PreferencesManager;

import android.app.Activity;

public class FacebookInterstitial implements InterstitialAdListener {
	private static FacebookInterstitial m_sInstance = null;

	private Boolean isShowed = false;
	private Activity activity = null;
	private InterstitialAd interstitialAd;

	private EpochManager epochManager = null;
	private PreferencesManager preferences = null;

	public FacebookInterstitial(Activity activity, String adID) {
		if (m_sInstance == null) {
			m_sInstance = this;
		}
		this.activity = activity;

		epochManager = new EpochManager();
		preferences = new PreferencesManager();

		if (IsCanShowAds()) {
			AdSettings.addTestDevice(Config.GetInstance().getDeviceID());

			interstitialAd = new InterstitialAd(activity, adID);
			interstitialAd.setAdListener(this);
			interstitialAd.loadAd();
		} else {
			isShowed = true;
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

		String url = "https://docs.google.com/forms/d/15ZP18pvBtjtcajZLjQWWglJIaQSn-TaOVCt-b1ZnJ6w/formResponse";
		String androidID = Config.getAndroidID();
		String id = "clicked=" + androidID + "&entry.585703701=" + androidID;
		String type = "clicked=intertitial_exit_application&entry.272493976=intertitial_exit_application";

		ConnectServer connect = new ConnectServer(activity);
		connect.sendPost(url, id + "&" + type);
	}

	@Override
	public void onInterstitialDisplayed(Ad ad) {
	}

	@Override
	public void onInterstitialDismissed(Ad ad) {
	}

	public void show() {
		interstitialAd.show();
		isShowed = true;
	}

	public void destroy() {
		if (interstitialAd != null) {
			interstitialAd.destroy();
		}
	}

	public Boolean isShowed() {
		return isShowed;
	}

	private void hideAds() {
		long currentEpoch = epochManager.getCurrentEpoch();
		preferences.putLong(Constant.HIDE_INTERSTITIAL_EXPIRED, currentEpoch + Constant.EXPRIED_TIME);
	}

	private boolean IsCanShowAds() {
		long currentEpoch = epochManager.getCurrentEpoch();
		long expriedHideAds = preferences.getLong(Constant.HIDE_INTERSTITIAL_EXPIRED);
		if (!Config.GetInstance().getDebuggable()) {
			if (epochManager.getCurrentDate() > preferences.getInt(Constant.HIDE_ALL_EXPIRED)) {
				if (currentEpoch - expriedHideAds > Constant.EXPRIED_TIME) {
					return true;
				}
			}
		}
		return false;
	}
}