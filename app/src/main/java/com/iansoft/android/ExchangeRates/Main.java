package com.iansoft.android.ExchangeRates;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.jirbo.adcolony.*;

import com.iansoft.android.Log;
import com.iansoft.android.Config;
import com.iansoft.android.Constant;
import com.iansoft.android.AdManager.FacebookBanner;
import com.iansoft.android.JSONManager;
import com.iansoft.android.EpochManager;
import com.iansoft.android.PreferencesManager;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdColonyAdAvailabilityListener, AdColonyV4VCListener, AdColonyAdListener {
	private static Activity m_sInstance = null;

	private Button reward = null;
	private GridView gridView = null;	
	private EditText fullName = null;
	private EditText phoneNumber = null;
	private TextView description = null;
	private AdColonyV4VCAd v4VCAd = null;
	private NavigationView navigationView = null;

	private EpochManager epoch = null;
	private FacebookBanner banner = null;
	private PreferencesManager preferences = null;

	final private String APP_ID  = "app95ccde4e50564561a8";
	final private String ZONE_ID = "vz5e29f5c49b674f3f96";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		m_sInstance = this;

		reward = (Button)findViewById(R.id.reward);
		gridView = (GridView)findViewById(R.id.gridView);
		fullName = (EditText)findViewById(R.id.fullName);
		phoneNumber = (EditText)findViewById(R.id.phoneNumber);
		description = (TextView)findViewById(R.id.description);

		banner = new FacebookBanner(this, getResources().getString(R.string.banner_home_bottom), findViewById(R.id.mainLayout));
		AdColony.configure(this, "version:1.0,store:google", APP_ID, ZONE_ID);
		AdColony.addAdAvailabilityListener(this);
		AdColony.addV4VCListener(this);
		v4VCAd = new AdColonyV4VCAd().withConfirmationDialog().withResultsDialog().withListener(Main.this);

		epoch = new EpochManager();
		preferences = new PreferencesManager();
		InitNavigationView();
		ShowSJC();
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.nav_sjc) {
			ShowSJC();
		} else if (id == R.id.nav_vcb) {
			ShowVCB();
		} else if (id == R.id.nav_hide_ads) {
			ShowVideo();
		} else if (id == R.id.nav_reward) {
			ShowReward();
		}

		DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		AdColony.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AdColony.resume(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		JSONManager.GetInstance().SaveData();
		if (banner != null) {
			banner.destroy();
		}
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			navigationView.getMenu().findItem(R.id.nav_reward).setVisible(IsCanShowReward());
			navigationView.getMenu().findItem(R.id.nav_hide_ads).setVisible(IsCanShowVideo());
		} else {
			JSONManager.GetInstance().SaveData();
		}
	}

	@Override
	public void onAdColonyAdStarted(AdColonyAd ad) {
	}

	@Override
	public void onAdColonyV4VCReward(AdColonyV4VCReward reward) {
		if (reward.success()) {
			int amount  = reward.amount();
			String name = reward.name();
			preferences.putInt(Constant.HIDE_ALL_EXPIRED, epoch.getCurrentDate());
		}
	}

	@Override
	public void onAdColonyAdAttemptFinished(AdColonyAd ad) {
	}

	@Override
	public void onAdColonyAdAvailabilityChange(final boolean available, String zone_id) {
	}

	private void InitNavigationView() {
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		toggle.syncState();

		navigationView = (NavigationView)findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	private void ShowSJC() {
		gridView.setVisibility(View.VISIBLE);

		reward.setVisibility(View.GONE);
		fullName.setVisibility(View.GONE);
		phoneNumber.setVisibility(View.GONE);
		description.setVisibility(View.GONE);

		gridView.setNumColumns(HandleSJC.GetInstance().GetNumberColume());
		gridView.setAdapter(new GridViewAdapter(HandleSJC.GetInstance().GetData()));
	}

	private void ShowVCB() {
		gridView.setVisibility(View.VISIBLE);

		reward.setVisibility(View.GONE);
		fullName.setVisibility(View.GONE);
		phoneNumber.setVisibility(View.GONE);
		description.setVisibility(View.GONE);

		gridView.setNumColumns(HandleVCB.GetInstance().GetNumberColume());
		gridView.setAdapter(new GridViewAdapter(HandleVCB.GetInstance().GetData()));
	}

	private void ShowVideo() {
		v4VCAd.show();
	}

	private void ShowReward() {
		gridView.setVisibility(View.GONE);

		reward.setVisibility(View.VISIBLE);
		fullName.setVisibility(View.VISIBLE);
		phoneNumber.setVisibility(View.VISIBLE);
		description.setVisibility(View.VISIBLE);
	}

	private boolean IsCanShowVideo() {
		int hideAdsExpried = preferences.getInt(Constant.HIDE_ALL_EXPIRED);
		if (!Config.getDebuggable()) {
			if (epoch.getCurrentDate() > hideAdsExpried) {
				return true;
			}
		}
		return false;
	}

	private boolean IsCanShowReward() {
		return !preferences.getBoolean("registeredReward");
	}

	public void btnGetRewardClick(View view) {
		String txtFullName = fullName.getText().toString();
		String txtPhoneNumber = phoneNumber.getText().toString();
		if (txtFullName.matches("")) {
			Toast.makeText(m_sInstance, getResources().getString(R.string.missing_name), Toast.LENGTH_LONG).show();
			return;
		}
		if (txtPhoneNumber.matches("")) {
			Toast.makeText(m_sInstance, getResources().getString(R.string.missing_phone), Toast.LENGTH_LONG).show();
			return;
		}

		Toast.makeText(m_sInstance, getResources().getString(R.string.reward_notification), Toast.LENGTH_LONG).show();
		navigationView.getMenu().findItem(R.id.nav_reward).setVisible(false);
		preferences.putBoolean("registeredReward", true);
		ShowSJC();
	}
}