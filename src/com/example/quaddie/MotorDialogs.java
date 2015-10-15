package com.example.quaddie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class MotorDialogs extends DialogFragment{

	 public static MotorDialogs newInstance(int message) {
	        MotorDialogs f = new MotorDialogs();
	        Bundle args = new Bundle();
	        args.putInt("message", message);
	        f.setArguments(args);
	        return f;
	    }
	 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		int message = getArguments().getInt("message");
		
		return new AlertDialog.Builder(getActivity())
		.setMessage(message)
		.setPositiveButton("Okay", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		})
		
		.create();
	}
	
}
