package com.example.quaddie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.AsyncTask;
import android.util.Log;

public class UdpClient extends AsyncTask<Void, Void, Void> {


	private int port;
	private String message;

    public UdpClient(int port,String message) {
        this.port = port;
        this.message = message;
    } 

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		try {
			sendUDPMessage(port,message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Wifi", "Exception thrown: " + e.toString());
		}
		return null;
	}
	

	public void sendUDPMessage(int port, String message) throws java.io.IOException {
        DatagramSocket socket = new DatagramSocket();
        Log.d("Wifi", "Datagram created");
        InetAddress serverIP = InetAddress.getByName("192.168.100.2");
        byte[] outData = (message).getBytes();
        Log.d("Wifi", "Server and data setup");
        DatagramPacket out = new DatagramPacket(outData,outData.length, serverIP,port);
        Log.d("Wifi", "Datagram packet created");
        socket.send(out);
        Log.d("Wifi", "Datagram sent thru socket");
        socket.close();
        Log.d("Wifi", "Datagram socket closed");
    }
	
	/*protected void onProgressUpdate(void... progress) {
        setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        showDialog("Downloaded " + result + " bytes");
    }*/
}
