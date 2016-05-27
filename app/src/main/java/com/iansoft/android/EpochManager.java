package com.iansoft.android;

public class EpochManager {

	public int getCurrentDate() {
		return (int)(getCurrentEpoch() / 86400);
	}

	public long getCurrentEpoch() {
		return System.currentTimeMillis() / 1000;
	}
}