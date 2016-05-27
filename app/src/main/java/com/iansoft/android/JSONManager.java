package com.iansoft.android;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class JSONManager {
	private static JSONManager m_sInstance = null;

	private JSONArray array = null;
	private PreferencesManager preferences = null;

	public static JSONManager GetInstance() {
		if (m_sInstance == null) {
			m_sInstance = new JSONManager();
		}
		return m_sInstance;
	}

	public JSONManager() {
		preferences = new PreferencesManager();
		LoadData();
	}

	public void SaveData() {
		preferences.putString(Constant.DATA, array.toString());
	}

	public void LoadData() {
		String data = preferences.getString(Constant.DATA);
		try {
			if (data.matches("")) {
				array = new JSONArray();
			} else {
				array = new JSONArray(data);
			}
		} catch (JSONException e) {
			Log.print(e.toString());
		}
	}

	public JSONArray GetData() {
		return array;
	}

	public void AddObject(JSONObject object) {
		try {
			for(int i = 0; i < array.length(); i++) {
				JSONObject curObject = array.getJSONObject(i);
				String objectID = object.getString("id");
				String currentID = curObject.getString("id");
				if (objectID.equals(currentID)) {
					array.remove(i);
				}
			}
			array.put(object);
		} catch (JSONException e) {
			Log.print(e.toString());
		}
	}
}