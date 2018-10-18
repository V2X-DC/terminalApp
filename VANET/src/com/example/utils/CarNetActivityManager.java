package com.example.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;



public class CarNetActivityManager {

	private static CarNetActivityManager mInstance;
	private List<Activity> activities = new LinkedList<Activity>();

	private CarNetActivityManager() {

	}

	public synchronized static CarNetActivityManager getInstance() {
		if (mInstance == null) {
			mInstance = new CarNetActivityManager();
		}
		return mInstance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}

