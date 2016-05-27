package com.iansoft.android;

public class Log {
	public static void print(String msg) {
		if (!Config.GetInstance().getDebuggable())
			return;

		StackTraceElement element = new Throwable().getStackTrace()[1];
		int line = element.getLineNumber();
		String file = element.getFileName();
		String method = element.getMethodName();

		msg = file + " - " + method + "() [line " + String.valueOf(line) + "]: " + msg; 
		android.util.Log.d(Constant.TAG, msg);
	}
}