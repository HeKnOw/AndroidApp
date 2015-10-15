package com.example.quaddie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Settings extends Activity {

	private final IntentFilter intentFilter = new IntentFilter();
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	PeerListListener peerListener;
	RegistrationListener mRegistrationListener;
	String mServiceName;
	ServerSocket mServerSocket;
	DiscoveryListener mDiscoveryListener;
	ResolveListener mResolveListener;
	int mLocalPort;
	NsdManager mNsdManager;
	NsdServiceInfo mService;
	String SERVICE_TYPE = "_ipp._udp";
	private ServerSocket mConnection;
	private NsdManager mNsdHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		Button bCLI = (Button) findViewById(R.id.bCLI);
		Button bPing = (Button) findViewById(R.id.bPing);
		Switch sWifi = (Switch) findViewById(R.id.sWifi);
		final Intent CommandLineActivity = new Intent(
				"com.example.quaddie.COMMANDINTERFACE");
		/*
		 * // Indicates a change in the Wi-fi P2P status
		 * intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		 * 
		 * // Indicates a change in the list for the available peers
		 * intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		 * 
		 * // Indicates the state of WI-Fi P2P connectivity has changed
		 * intentFilter
		 * .addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		 * 
		 * // indicates this device's details have changed intentFilter
		 * .addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		 */

		sWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					/*
					 * DialogFragment dialogPeers = new PeerListDialog();
					 * PeerListDialog test = (PeerListDialog) dialogPeers;
					 * peerListener = test.peerListListener;
					 * dialogPeers.show(getFragmentManager(), "PeersList");
					 */
					initializeServerSocket();
					initializeRegistrationListener();
					initializeResolveListener();
					initializeDiscoveryListener();
					registerService(mLocalPort);
					if (mDiscoveryListener != null) {
						Log.d("Wifi",
								"The listener for discovery is not null, we are going to discover services now");
						mNsdManager.discoverServices(SERVICE_TYPE,
								NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
					} else {
						Log.d("Wifi", "Discovery listener Returned null");
					}
				} else {
					Log.d("Off", "The switch is off");
				}
			}
		});

		bCLI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(CommandLineActivity);
			}

		});

		bPing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UdpClient udpClient = new UdpClient(5000,"Ping");
				udpClient.execute();
			}

		});

		/*
		 * mManager = (WifiP2pManager)
		 * getSystemService(Context.WIFI_P2P_SERVICE); mChannel =
		 * mManager.initialize(this, getMainLooper(), null); mReceiver = new
		 * WifiBroadcastReceiver(mManager, mChannel, this, peerListener);
		 */
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mNsdHelper != null) {
			mNsdHelper.registerService(mService, mConnection.getLocalPort(),
					mRegistrationListener);
			mNsdHelper.discoverServices(SERVICE_TYPE, mLocalPort,
					mDiscoveryListener);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mNsdHelper != null) {
			mNsdManager.unregisterService(mRegistrationListener);
			mNsdManager.stopServiceDiscovery(mDiscoveryListener);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mNsdManager != null) {
			mNsdManager.unregisterService(mRegistrationListener);
			mNsdManager.stopServiceDiscovery(mDiscoveryListener);
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++++USER
	// FUNCTIONS++++++++++++++++++++++++++++++++++++++++++++++
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public void registerService(int port) {
		// Create the NsdServiceInfo object, and populate it.
		NsdServiceInfo serviceInfo = new NsdServiceInfo();

		// The name is subject to change based on conflicts
		// with other services advertised on the same network.
		serviceInfo.setServiceName("Quadie's Chat App");
		serviceInfo.setServiceType("_http._udp.");
		serviceInfo.setPort(port);

		mNsdManager = (NsdManager) this.getSystemService(Context.NSD_SERVICE);

		mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD,
				mRegistrationListener);

	}

	public void initializeServerSocket() {
		// Initialize a server socket on the next available port.
		try {
			mServerSocket = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Store the chosen port.
		mLocalPort = mServerSocket.getLocalPort();
	}

	public void initializeRegistrationListener() {
		mRegistrationListener = new NsdManager.RegistrationListener() {

			@Override
			public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
				// Save the service name. Android may have changed it in order
				// to
				// resolve a conflict, so update the name you initially
				// requested
				// with the name Android actually used.
				Log.d("Wifi", "Registered Service");
				mServiceName = NsdServiceInfo.getServiceName();
			}

			@Override
			public void onRegistrationFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				// Registration failed! Put debugging code here to determine
				// why.
			}

			@Override
			public void onServiceUnregistered(NsdServiceInfo arg0) {
				// Service has been unregistered. This only happens when you
				// call
				// NsdManager.unregisterService() and pass in this listener.
			}

			@Override
			public void onUnregistrationFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				// Unregistration failed. Put debugging code here to determine
				// why.
			}
		};
	}

	public void initializeDiscoveryListener() {

		// Instantiate a new DiscoveryListener
		mDiscoveryListener = new NsdManager.DiscoveryListener() {

			// Called as soon as service discovery begins.
			@Override
			public void onDiscoveryStarted(String regType) {
				Log.d("Wifi", "Service discovery started");
			}

			@Override
			public void onServiceFound(NsdServiceInfo service) {
				// A service was found! Do something with it.
				Log.d("Wifi", "Service discovery success" + service);
				if (!service.getServiceType().equals(SERVICE_TYPE)) {
					// Service type is the string containing the protocol and
					// transport layer for this service.
					Log.d("Wifi",
							"Unknown Service Type: " + service.getServiceType());
				} else if (service.getServiceName().equals(mServiceName)) {
					// The name of the service tells the user what they'd be
					// connecting to. It could be "Bob's Chat App".
					Log.d("Wifi", "Same machine: " + mServiceName);
				} else if (service.getServiceName().contains("NsdChat")) {
					mNsdManager.resolveService(service, mResolveListener);
				}
			}

			@Override
			public void onServiceLost(NsdServiceInfo service) {
				// When the network service is no longer available.
				// Internal bookkeeping code goes here.
				Log.e("Wifi", "service lost" + service);
			}

			@Override
			public void onDiscoveryStopped(String serviceType) {
				Log.i("Wifi", "Discovery stopped: " + serviceType);
			}

			@Override
			public void onStartDiscoveryFailed(String serviceType, int errorCode) {
				Log.e("Wifi", "Discovery failed: Error code:" + errorCode);
				mNsdManager.stopServiceDiscovery(this);
			}

			@Override
			public void onStopDiscoveryFailed(String serviceType, int errorCode) {
				Log.e("Wifi", "Discovery failed: Error code:" + errorCode);
				mNsdManager.stopServiceDiscovery(this);
			}
		};
	}

	public void initializeResolveListener() {
		mResolveListener = new NsdManager.ResolveListener() {

			@Override
			public void onResolveFailed(NsdServiceInfo serviceInfo,
					int errorCode) {
				// Called when the resolve fails. Use the error code to debug.
				Log.e("Wifi", "Resolve failed" + errorCode);
			}

			@Override
			public void onServiceResolved(NsdServiceInfo serviceInfo) {
				Log.e("Wifi", "Resolve Succeeded. " + serviceInfo);

				if (serviceInfo.getServiceName().equals(mServiceName)) {
					Log.d("Wifi", "Same IP.");
					return;
				}
				mService = serviceInfo;
				int port = mService.getPort();
				InetAddress host = mService.getHost();
			}
		};
	}

}