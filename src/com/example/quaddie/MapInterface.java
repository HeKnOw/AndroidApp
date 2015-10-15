package com.example.quaddie;

import java.text.DateFormat;
import java.util.Date;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MapInterface extends FragmentActivity implements
		OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {

	GoogleApiClient mGoogleApiClient;
	Location mLastConnection;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	String mLastUpdatedTime;
	GoogleMap map;
	static LatLng MYLOCATION;
	boolean mRequestingLocationUpdates;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_interface);
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		createLocationRequests();
		Button bLand = (Button) findViewById(R.id.bLand);
		Button bTakeOff = (Button) findViewById(R.id.bTakeOff);
		Button bStop = (Button) findViewById(R.id.bStop);
		Button bSendPlusOne = (Button) findViewById(R.id.bUp1);
		Button bSendMinusOne = (Button) findViewById(R.id.bDown1);

		bLand.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UdpClient udpClient = new UdpClient(5000,"down");
				udpClient.execute();
			}
			
		});
		
		bTakeOff.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UdpClient udpClient = new UdpClient(5000,"up");
				udpClient.execute();
				
			}
			
		});
		
		bStop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				UdpClient udpClient = new UdpClient(5000,"stop");
				udpClient.execute();
			}
			
			
		});
		
		bSendPlusOne.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UdpClient udpClient = new UdpClient(5000,"1");
				udpClient.execute();
			}
			
		});
		
		bSendMinusOne.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UdpClient udpClient = new UdpClient(5000,"-1");
				udpClient.execute();
			}
			
		});
	}

	@Override
	public void onMapReady(GoogleMap Googlemap) {
		// TODO Auto-generated method stub
		map = Googlemap;

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		startLocationUpdates();
		if (mGoogleApiClient.isConnected()) {
			Log.d("Google", "Api Connected");
		}
		mLastConnection = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		mCurrentLocation = mLastConnection;
		// ------------------------------------------------------------------------------------
		if (mLastConnection != null) {
			MYLOCATION = new LatLng(mLastConnection.getLatitude(),
					mLastConnection.getLongitude());
		} else {
			Log.d("Google", "Location returned null");
		}
		// ------------------------------------------------------------------------------------

		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(MYLOCATION).zoom(17).bearing(90).tilt(30).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		map.addMarker(new MarkerOptions().position(MYLOCATION).title(
				"Quadie's app"));
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.d("Google", "Connection Suspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.d("Google", "Api Connection Failed");
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mCurrentLocation = location;
		mLastUpdatedTime = DateFormat.getTimeInstance().format(new Date());
		if(mCurrentLocation != null){
			Log.d("Google", "Current Latitude" + String.valueOf(mCurrentLocation.getLatitude()));
			updateUI();
		}
		else{
			Log.d("Google", "Updated Location Failed");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mGoogleApiClient.isConnected() && !mRequestingLocationUpdates){
			startLocationUpdates();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopLocationUpdates();
	}

	// *******************************************************************************
	// *******************************USER
	// METHODS***********************************
	// *******************************************************************************

	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
		mRequestingLocationUpdates = true;
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
		mRequestingLocationUpdates = false;
	}

	protected void createLocationRequests() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(20000);
		mLocationRequest.setFastestInterval(50000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	}

	private void updateUI() {
		// TODO Auto-generated method stub
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(MYLOCATION).zoom(17).bearing(90).tilt(30).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		map.addMarker(new MarkerOptions().position(MYLOCATION).title(
				"Quadie's app"));
		Log.d("Google", "Location updated time:" + mLastUpdatedTime);
	}


}
