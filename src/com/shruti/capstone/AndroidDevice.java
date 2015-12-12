package com.shruti.capstone;

import android.app.Activity;
import android.os.Bundle;

public class AndroidDevice extends Activity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main);
		new taskTracker().execute();
	}
}