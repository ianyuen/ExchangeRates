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

public class HandleSJC {
	private static HandleSJC m_sInstance = null;

	private String urlString = "http://www.sjc.com.vn/xml/tygiavang.xml";
	private ArrayList<String> added = new ArrayList<String>();

	public static HandleSJC GetInstance() {
		if (m_sInstance == null) {
			m_sInstance = new HandleSJC();
		}
		return m_sInstance;
	}

	public HandleSJC() {
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
				case XmlPullParser.START_TAG:
					if (name.equals("item")) {
						String buy, sell, code;
						buy = parser.getAttributeValue(0);
						sell = parser.getAttributeValue(1);
						code = parser.getAttributeValue(2);
						AddItem(buy, sell, code);
					}
					break;
				case XmlPullParser.END_TAG:
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

	private void AddItem(String buy, String sell, String code) {
		String convertedCode = ConvertCode(code);
		if (convertedCode == "")
			return;

		if (!added.contains(convertedCode)) {
			added.add(convertedCode);

			JSONObject object = new JSONObject();
			try {
				object.put("id", convertedCode);
				object.put("buy", buy);
				object.put("sell", sell);
				object.put("source", "SJC");
			} catch (JSONException e) {
				Log.print(e.toString());
			}
			JSONManager.GetInstance().AddObject(object);
		}
	}

	private String ConvertCode(String code) {
		String convertedCode = "";
		if (code.contains("SJC 1L"))
			convertedCode = "SJC";
		else if (code.contains("SJC 99"))
			convertedCode = "SJC nhẫn";
		else if (code.contains("99,99%"))
			convertedCode = "9999";
		else if (code.contains("99%"))
			convertedCode = "24K (99%)";
		else if (code.contains("75%"))
			convertedCode = "18K (75%)";
		else if (code.contains("58,3%"))
			convertedCode = "14K (58,3%)";
		else if (code.contains("41,7%"))
			convertedCode = "14K (41,7%)";

		return convertedCode;
	}

	public ArrayList<String> GetData() {
		ArrayList<String> datas = new ArrayList<String>();
		datas.add("");
		datas.add(Config.getAppContext().getResources().getString(R.string.buy));
		datas.add(Config.getAppContext().getResources().getString(R.string.sell));
		try {
			JSONArray array = JSONManager.GetInstance().GetData();
			for(int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (object.getString("source").equals("SJC")) {
					datas.add(object.getString("id"));
					datas.add(object.getString("buy"));
					datas.add(object.getString("sell"));
				}
			}
		} catch (JSONException e) {
			Log.print(e.toString());
		}
		return datas;
	}

	public int GetNumberColume() {
		return 3;
	}
}