package pl.tajchert.glassware.numpad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class AddTransactionActivity extends Activity implements
		SensorEventListener {

	private TextView mLeft;
	private TextView mMiddle;
	private TextView mRight;
	private SensorManager mSensorManager;
	private Sensor mOrientation;
	
	private boolean firstRun = true;
	private int firstAngleVal;
	private int angleVal = 0;

	Float azimuth_angle;
	Float pitch_angle;
	Float roll_angle;

	private SharedPreferences prefs;

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.i(Tools.AWESOME_TAG, "KEYCODE_DPAD_CENTER");
			mSensorManager.unregisterListener(this);
			this.finish();
			
			return true;
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.select_number);

		prefs = this.getSharedPreferences("com.tajchert.glassware.eyewallet",Context.MODE_PRIVATE);
		mLeft = (TextView) findViewById(R.id.left);
		mMiddle = (TextView) findViewById(R.id.middle);
		mRight= (TextView) findViewById(R.id.right);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation, 300000);
		//mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);//TODO
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
		if(firstRun){
			firstAngleVal = (int) azimuth_angle;
			firstRun = false;
		}
		angleVal = (int)(azimuth_angle - firstAngleVal);
		//Log.d(Tools.AWESOME_TAG, "tmpAngle: " + angleVal);
		if(Math.abs(angleVal)>50){
			firstAngleVal = (int)(azimuth_angle - Tools.sliceSize);
			angleVal = (int)(azimuth_angle - firstAngleVal);
		}
		new GetNumbers().execute(angleVal + "");
	}

	private class GetNumbers extends AsyncTask<String, Void, String> {
		private int ang;
		private String [] numbers;

		@Override
		protected String doInBackground(String... params) {
			ang = Integer.parseInt(params[0]);
			numbers = Tools.getNumbers(ang);
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			mLeft.setText(numbers[0] + "");
			mMiddle.setText(numbers[1] + "");
			mRight.setText(numbers[2] + "");
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
