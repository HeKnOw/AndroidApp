package com.example.quaddie;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class PeerListDialog extends DialogFragment {

	private List peers = new ArrayList();
	// gotta call peerlistdialog.adapter.notifydatachanged() to change the list
	ArrayAdapter<String> adapter;

	public PeerListListener peerListListener = new PeerListListener() {

		@Override
		public void onPeersAvailable(WifiP2pDeviceList peerList) {
			// TODO Auto-generated method stub
			// Out with the old, in with the new.
			peers.clear();
			peers.addAll(peerList.getDeviceList());
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.peersdialog, peers);
			// If an AdapterView is backed by this data, notify it
			// of the change. For instance, if you have a ListView of available
			// peers, trigger an update.
			adapter.notifyDataSetChanged();
			if (peers.size() == 0) {
				Log.d("P2P", "No devices found");
				return;
			}
			else {
				Log.d("P2P", "Devices found" + peers.get(0).toString());
			}
		}
	};


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Available Connections").setAdapter(adapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item

					}
				});
		return builder.create();
	}

}
