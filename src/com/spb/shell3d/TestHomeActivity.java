package com.spb.shell3d;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestHomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		Log.i("hxj", "TestHomeActivity");
	}
}
