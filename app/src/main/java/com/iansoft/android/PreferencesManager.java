package com.iansoft.android;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
	private static PreferencesManager m_sInstance = null;

	private SharedPreferences preferences = null;
	private SharedPreferences.Editor editor = null;

	public PreferencesManager() {
		if (m_sInstance == null) {
			m_sInstance = this;
		}
		m_sInstance.editor = Config.GetInstance().getAppContext().getSharedPreferences(Config.GetInstance().getAppPackage(), Context.MODE_PRIVATE).edit();
		m_sInstance.preferences = Config.GetInstance().getAppContext().getSharedPreferences(Config.GetInstance().getAppPackage(), Context.MODE_PRIVATE);
	}

	public int getInt(String name) {
		return m_sInstance.preferences.getInt(name, 0);
	}

	public void putInt(String name, int value) {
		m_sInstance.editor.putInt(name, value);
		m_sInstance.editor.commit();
	}

	public long getLong(String name) {
		return m_sInstance.preferences.getLong(name, 0);
	}

	public void putLong(String name, long value) {
		m_sInstance.editor.putLong(name, value);
		m_sInstance.editor.commit();
	}

	public String getString(String name) {
		return m_sInstance.preferences.getString(name, "");
	}

	public void putString(String name, String value) {
		m_sInstance.editor.putString(name, value);
		m_sInstance.editor.commit();
	}

	public Boolean getBoolean(String name) {
		return m_sInstance.preferences.getBoolean(name, false);
	}

	public void putBoolean(String name, Boolean value) {
		m_sInstance.editor.putBoolean(name, value);
		m_sInstance.editor.commit();
	}
}