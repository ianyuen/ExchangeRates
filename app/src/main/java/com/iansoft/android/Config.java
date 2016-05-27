package com.iansoft.android;

import android.content.Context;

public class Config {
	private static Config m_sInstance = null;

	private static String saveDir = "";
	private static String deviceID = "";//use for test Ads
	private static String androidID = "";
	private static String appPackage = "";
	private static Context appContext = null;
	private static Boolean isDebuggable = false;

	public static Config GetInstance() {
		if (m_sInstance == null) {
			m_sInstance = new Config();
		}
		return m_sInstance;
	}

	public static void setSaveDir(String value) {
		saveDir = value;
	}

	public static String getSaveDir() {
		return saveDir;
	}

	public static void setDeviceID(String value) {
		deviceID = value;
	}

	public static String getDeviceID() {
		return deviceID;
	}

	public static void setAndroidID(String value) {
		androidID = value;
	}

	public static String getAndroidID() {
		return androidID;
	}

	public static void setAppPackage(String value) {
		appPackage = value;
	}

	public static String getAppPackage() {
		return appPackage;
	}

	public static void setAppContext(Context context) {
		appContext = context;
	}

	public static Context getAppContext() {
		return appContext;
	}

	public static void setDebuggable(Boolean value) {
		isDebuggable = value;
	}

	public static Boolean getDebuggable() {
		return isDebuggable;
	}
}