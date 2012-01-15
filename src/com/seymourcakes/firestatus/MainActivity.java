package com.seymourcakes.firestatus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView statusText;
	private TextView extraText;
	private BroadcastReceiver batteryReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.statusText = (TextView) findViewById(R.id.status_text);
		this.extraText = (TextView) findViewById(R.id.extra_text);
		this.statusText.setText("Calculating battery % ...");
		setupBatteryEventBroadcastAndReceiver();
	}

	@Override
	public void onStop() {
		super.onStart();
        unregisterReceiver(batteryReceiver);
	}
	
	public void info(View v) {
		
	}
	
	private void updateBackgroundWithStatusLevel(int statusLevel) {
		View layout = MainActivity.this.findViewById(R.id.layout);
		String bgCode = "#66cc33";
		if (statusLevel < 25) {
			bgCode = "#ff000";
		} else if (statusLevel < 50) {
			bgCode = "#ffcc00";
		} else if (statusLevel < 75) {
			bgCode = "#cccc33";
		}
		layout.setBackgroundColor(Color.parseColor(bgCode));
	}
	
	private void setupBatteryEventBroadcastAndReceiver() {
		batteryReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int batteryLevel = intent.getIntExtra("level", 0);
				
				updateBackgroundWithStatusLevel(batteryLevel);
				
				statusText.setText(String.valueOf(batteryLevel) + "%");

				int status = intent.getIntExtra("status",
						BatteryManager.BATTERY_STATUS_UNKNOWN);

				String strStatus = "NA";
				if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
					strStatus = "Charging";
				} else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
					strStatus = "Dis-charging";
				} else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
					strStatus = "Not charging";
				} else if (status == BatteryManager.BATTERY_STATUS_FULL) {
					strStatus = "Full";
				}

				extraText.setText(strStatus);

				// Peek into all the info
				// Bundle bundle = intent.getExtras();
				// Log.d("BATTERY", "Bundle keys: " + bundle.keySet());

				int health = intent.getIntExtra("health",
						BatteryManager.BATTERY_HEALTH_UNKNOWN);
				String strHealth = "NA";
				if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
					strHealth = "Good";
				} else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
					strHealth = "Over Heat";
				} else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
					strHealth = "Dead";
				} else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
					strHealth = "Over Voltage";
				} else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
					strHealth = "Unspecified Failure";
				}

				if (health != BatteryManager.BATTERY_HEALTH_GOOD) {
					// warn only if batter looks bad
					extraText.setText(strHealth);
				}
				
			}

		};

		this.registerReceiver(this.batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

}