package com.iansoft.android.ExchangeRates;

import com.iansoft.android.Log;
import com.iansoft.android.Config;
import com.iansoft.android.JSONManager;
import com.iansoft.android.AsyncCaller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import android.app.Activity;

public class HandleVCB {
	private static HandleVCB m_sInstance = null;

	private String urlString = "https://www.vietcombank.com.vn/exchangerates/ExrateXML.aspx";
	private ArrayList<String> added = new ArrayList<String>();

	public static HandleVCB GetInstance() {
		if (m_sInstance == null) {
			m_sInstance = new HandleVCB();
		}
		return m_sInstance;
	}

	public HandleVCB() {
		FetchXML();
	}

	public void FetchXML() {
		AsyncCaller caller = new AsyncCaller();
		caller.execute(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection connection = null;
					connection = (HttpURLConnection)url.openConnection();
					connection.setReadTimeout(10000);
					connection.setConnectTimeout(15000);
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					connection.connect();

					InputStream inputStream = connection.getInputStream();
					XmlPullParserFactory xmlFactoryObject;
					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser parser = xmlFactoryObject.newPullParser();
					parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					parser.setInput(inputStream, null);
					ParseXML(parser);
					inputStream.close();
				} catch (Exception e) {
					Log.print(e.toString());
				}
			}
		});
	}

	private void ParseXML(XmlPullParser parser) {
		try {
			int event;
			event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.END_TAG:
					if (name.equals("Exrate")) {
						String buy, sell, code, transfer;
						buy = parser.getAttributeValue(2);
						sell = parser.getAttributeValue(4);
						code = parser.getAttributeValue(0);
						transfer = parser.getAttributeValue(3);
						AddItem(buy, sell, code, transfer);
					}
					break;
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.TEXT:
					break;
				default:
					break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			Log.print(e.toString());
		}
	}

	private void AddItem(String buy, String sell, String code, String transfer) {
		if (!added.contains(code)) {
			added.add(code);

			JSONObject object = new JSONObject();
			try {
				object.put("id", code);
				object.put("buy", buy);
				object.put("sell", sell);
				object.put("source", "VCB");
				object.put("transfer", transfer);
			} catch (JSONException e) {
				Log.print(e.toString());
			}
			JSONManager.GetInstance().AddObject(object);
		}
	}

	public ArrayList<String> GetData() {
		ArrayList<String> datas = new ArrayList<String>();
		datas.add("");
		datas.add(Config.getAppContext().getResources().getString(R.string.buy));
		datas.add(Config.getAppContext().getResources().getString(R.string.sell));
		datas.add(Config.getAppContext().getResources().getString(R.string.transfer));
		try {
			JSONArray array = JSONManager.GetInstance().GetData();
			for(int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (object.getString("source").equals("VCB")) {
					datas.add(object.getString("id"));
					datas.add(object.getString("buy"));
					datas.add(object.getString("sell"));
					datas.add(object.getString("transfer"));
				}
			}
		} catch (JSONException e) {
			Log.print(e.toString());
		}
		return datas;
	}

	public int GetNumberColume() {
		return 4;
	}
}