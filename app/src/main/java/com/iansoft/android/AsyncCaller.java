package com.iansoft.android;

import android.os.Handler;
import android.os.AsyncTask;

public class AsyncCaller extends AsyncTask<Runnable, Void, Void> {
	private Handler handler = null;

	public AsyncCaller() {
	}

	protected Void doInBackground(Runnable... params) {
		if (handler == null) {
			handler = new Handler(Config.GetInstance().getAppContext().getMainLooper());
		}
		if (handler != null) {
			handler.post(params[0]);
		}
		return null;
	}
}