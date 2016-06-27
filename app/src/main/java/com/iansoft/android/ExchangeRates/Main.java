package com.iansoft.android.ExchangeRates;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.jirbo.adcolony.*;

import com.iansoft.android.Log;
import com.iansoft.android.Config;
import com.iansoft.android.AdManager.FacebookBanner;
import com.iansoft.android.JSONManager;
import com.iansoft.android.EpochManager;
import com.iansoft.android.ConnectServer;
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
		} else if (id == R.id.nav_upload) {
			UploadData();
		}

		DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerLayout.closeDrawer(GravityCompat.START);
		return true;
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
			navigationView.getMenu().findItem(R.id.nav_upload).setVisible(IsCanShowUpload());
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
		AdColonyV4VCAd ad = new AdColonyV4VCAd().withConfirmationDialog().withResultsDialog().withListener(Main.this);
		ad.show();
	}

	private void ShowReward() {
		gridView.setVisibility(View.GONE);

		reward.setVisibility(View.VISIBLE);
		fullName.setVisibility(View.VISIBLE);
		phoneNumber.setVisibility(View.VISIBLE);
		description.setVisibility(View.VISIBLE);
	}

	private void UploadData() {
		preferences.putInt("currentDate", epoch.getCurrentDate());
		navigationView.getMenu().findItem(R.id.nav_upload).setVisible(false);

		ArrayList<String> needUpload = new ArrayList<String>();
		needUpload.add("SJC");
		needUpload.add("9999");
		needUpload.add("24K");
		needUpload.add("USD");
		needUpload.add("AUD");
		needUpload.add("EUR");
		needUpload.add("GBP");
		needUpload.add("JPY");

		try {
			JSONArray array = JSONManager.GetInstance().GetData();
			for(int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String id = object.getString("id");
				String buy = object.getString("buy");
				String sell = object.getString("sell");
				if (needUpload.contains(id)) {
					UploadExchange(id, buy, sell);
				}
			}
		} catch (JSONException e) {
			Log.print(e.toString());
		}
	}

	private void UploadExchange(String exchangeID, String exchangeBuy, String exchangeSell) {
		String url = "https://docs.google.com/forms/d/1yYqTmyh4oIbg1wQiECNRZhPcE7RLDO7dy2n20ruIEgU/formResponse";
		
		String id = "analytics=" + exchangeID + "&entry.1471248749=" + exchangeID;
		String buy = "analytics=" + exchangeBuy + "&entry.311396850=" + exchangeBuy;
		String sell = "analytics=" + exchangeSell + "&entry.1429083367=" + exchangeSell;
		String data = id + "&" + buy + "&" + sell;

		ConnectServer connect = new ConnectServer(this);
		connect.sendPost(url, data);
	}

	private boolean IsCanShowUpload() {
		if (Config.getDebuggable()) {
			if (epoch.getCurrentDate() > preferences.getInt("currentDate")) {
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

		txtFullName.replace(" ", "%20");
		String androidID = Config.getAndroidID();

		String url = "https://docs.google.com/forms/d/1DWxOlmt8vXP6y2XLjiHxdJysOf1IdUdHR21R8568PA8/formResponse";

		String id = "reward=" + androidID + "&entry.1341337758=" + androidID;
		String name = "reward=" + txtFullName + "&entry.779451674=" + txtFullName;
		String phone = "reward=" + txtPhoneNumber + "&entry.1497050153=" + txtPhoneNumber;
		String data = id + "&" + name + "&" + phone;

		ConnectServer connect = new ConnectServer(this);
		connect.sendPost(url, data);

		Toast.makeText(m_sInstance, getResources().getString(R.string.reward_notification), Toast.LENGTH_LONG).show();
		navigationView.getMenu().findItem(R.id.nav_reward).setVisible(false);
		preferences.putBoolean("registeredReward", true);
		ShowSJC();
	}
}