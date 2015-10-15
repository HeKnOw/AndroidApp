package com.example.quaddie;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CommandInterface extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.command_interface);
		final TextView output = (TextView) findViewById(R.id.tvOutput);
		final EditText input = (EditText) findViewById(R.id.tCommandLine);
		Button bExecute = (Button) findViewById(R.id.bExecute);
		final Transducer transducer = new Transducer();

		
		output.setMovementMethod(new ScrollingMovementMethod());
		
		input.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						output.append("\n");
						output.append(transducer.transduce(input.getText()
								.toString()));
						input.setText("");
						return true;
					
					default:
						break;
					}
				}
				return false;
			}
		

		});

		bExecute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				output.append("\n");
				output.append(transducer.transduce(input.getText().toString()));
				input.setText("");

			}

		});
		
		 
		
	}
	
	

}