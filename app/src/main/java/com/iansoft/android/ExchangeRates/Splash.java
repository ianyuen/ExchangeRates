package com.iansoft.android.ExchangeRates;

import com.iansoft.android.Log;
import com.iansoft.android.Config;
import com.iansoft.android.Constant;
import com.iansoft.android.EpochManager;
import com.iansoft.android.PreferencesManager;
import com.iansoft.android.AdManager.FacebookInterstitial;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.Intent;
import android.provider.Settings.Secure;

public class Splash extends Activity {
	private static Activity m_sInstance = null;

	private HandleSJC handleSJC;
	private HandleVCB handleVCB;

	private PreferencesManager preferences = null;
	private FacebookInterstitial interstitial = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		m_sInstance = this;

		initConstantValue();
		handleSJC = new HandleSJC();
		handleVCB = new HandleVCB();
		interstitial = new FacebookInterstitial(this, getResources().getString(R.string.intertitial_exit_application));

		CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
			public void onTick(long millisUntilFinished) {}
			public void onFinish() {
				Intent intent = new Intent(m_sInstance, Main.class);
				startActivity(intent);
				finish();
			}
		}.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (interstitial != null) {
			if (!interstitial.isShowed()) {
				interstitial.show();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	}

	private boolean isDebuggable() {
		return (0 != (m_sInstance.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
	}

	private String generateAndroidID() {
		EpochManager epochManager = new EpochManager();
		String androidID = Secure.getString(Config.getAppContext().getContentResolver(), Secure.ANDROID_ID);
		return androidID + ":" + epochManager.getCurrentEpoch();
	}

	private void initConstantValue() {
		Config.GetInstance().setAppContext(getApplicationContext());

		preferences = new PreferencesManager();
		Config.GetInstance().setSaveDir(Config.getAppContext().getFilesDir().toString());
		Config.GetInstance().setAppPackage(Config.getAppContext().getPackageName());
		Config.GetInstance().setDebuggable(isDebuggable());

		Config.GetInstance().setAndroidID(preferences.getString(Constant.ANDROID_ID));
		if (Config.GetInstance().getAndroidID().matches("")) {
			Config.GetInstance().setAndroidID(generateAndroidID());
			preferences.putString(Constant.ANDROID_ID, Config.GetInstance().getAndroidID());
		}
	}
}