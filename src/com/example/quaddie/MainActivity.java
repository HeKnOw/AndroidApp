package com.example.quaddie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bTutorial = (Button) findViewById(R.id.bTutorial);
        Button bMap = (Button) findViewById(R.id.bMap);
        ImageButton bSettings = (ImageButton)findViewById(R.id.bSettings);
        final Intent TutorialActivity = new Intent("com.example.quaddie.TUTORIAL"); 
        final Intent SettingsActivity = new Intent("com.example.quaddie.SETTINGS");
        final Intent MapActivity = new Intent("com.example.quaddie.MAPINTERFACE");
        
        
        bMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(MapActivity);
			}
        	
        });
        bTutorial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(TutorialActivity);
			}
        	
        });
        bSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(SettingsActivity);
			}
        	
        	
        });
      
       
        
    }
    
    
    
  


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	
}
