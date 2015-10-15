package com.example.quaddie;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class Tutorial extends Activity implements View.OnTouchListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		ImageView iv = (ImageView) findViewById(R.id.q1);
		if (iv != null) {
			iv.setOnTouchListener(this);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean handledHere = false;

		final int action = ev.getAction();

		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
		
			handledHere = true;
			break;

		case MotionEvent.ACTION_UP:
			// On the UP, we do the click action.
			// The hidden image (image_areas) has three different hotspots on
			// it.
			// The colors are red, blue,green and yellow.
			// Use image_areas to determine which region the user touched.
			int touchColor = getHotspotColor(R.id.iAreas, evX, evY);

			// Compare the touchColor to the expected values.
			// Note that we use a Color Tool object to test whether the observed
			// color is close enough to the real color to
			// count as a match. We do this because colors on the screen do not
			// match the map exactly because of scaling and
			// varying pixel density.
			ColorTool ct = new ColorTool();
			int tolerance = 50;
			if (ct.closeMatch(Color.RED, touchColor, tolerance)) 
			{
				DialogFragment newFragment = MotorDialogs.newInstance(R.string.motor1);
				newFragment.show(getFragmentManager(), "message");
			} 
			else if (ct.closeMatch (Color.BLUE, touchColor, tolerance + 100))
			{
				DialogFragment newFragment = MotorDialogs.newInstance(R.string.motor2);
				newFragment.show(getFragmentManager(), "message");
			}
			else if (ct.closeMatch (Color.YELLOW, touchColor, tolerance))
			{
				DialogFragment newFragment = MotorDialogs.newInstance(R.string.motor3);
				newFragment.show(getFragmentManager(), "message");
			}
			else if (ct.closeMatch (Color.GREEN, touchColor, tolerance))
			{
				DialogFragment newFragment = MotorDialogs.newInstance(R.string.motor4);
				newFragment.show(getFragmentManager(), "message");
			}
			else if (ct.closeMatch (Color.rgb(255, 10, 255), touchColor, tolerance + 200))
			{
				DialogFragment newFragment = MotorDialogs.newInstance(R.string.beaglebone);
				newFragment.show(getFragmentManager(), "message");
			}
			else
			{
				//no color
			}
			
			handledHere = true;
			break;

		default:
			handledHere = false;
		} 

		return handledHere;
	}

	private int getHotspotColor(int hotspotId, int x, int y) {
		// TODO Auto-generated method stub
		ImageView img = (ImageView) findViewById(hotspotId);
		if (img == null) {
			Log.d("ImageAreasActivity", "Hot spot image not found");
			return 0;
		} else {
			img.setDrawingCacheEnabled(true);
			Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
			if (hotspots == null) {
				Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
				return 0;
			} else {
				img.setDrawingCacheEnabled(false);
				return hotspots.getPixel(x, y);
			}
		}
	}

}
