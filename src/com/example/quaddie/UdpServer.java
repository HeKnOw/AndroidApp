package com.example.quaddie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.AsyncTask;
import android.util.Log;

public class UdpServer extends AsyncTask<Void, Void, String> {

	DatagramSocket socket;
	DatagramPacket in;
	private int port;
	private boolean udpServerOpen;
	String sentence;
	public String chatIn;
	InetAddress IPAddress;

	UdpServer(int port) {
		this.port = port;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			udpServerOpen = true;
			while (udpServerOpen) {
				RunUdpServer(port);
			}
			Log.d("Wifi", "Server Started");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Wifi", "Server trhrew an exception: " + e.toString());
			chatIn = "Server exception " + e.toString();
		}
		return chatIn;
	}

	public String ReceiveUDPMessage() throws IOException {
		socket.setSoTimeout(5000);
		socket.receive(in);
		Log.d("Wifi", "Server Datagram received");
		sentence = new String(in.getData(), 0, in.getLength());
		IPAddress = in.getAddress();
		port = in.getPort();
		System.out.println("MESSAGE RECEIVED  " + sentence + "  " + IPAddress
				+ "         " + port);
		socket.close();
		Log.d("Wifi", "Server Datagram socket closed");
		return sentence;
	}

	private void RunUdpServer(int port) throws java.io.IOException {
		socket = new DatagramSocket(port,InetAddress.getByName("192.168.100.101"));
		Log.d("Wifi", "Server Datagram created");
		byte[] lMsg = new byte[4096];
		in = new DatagramPacket(lMsg, lMsg.length);
		Log.d("Wifi", " server Datagram packet created");
		try {
			chatIn = ReceiveUDPMessage();
			Log.d("Wifi", socket.getInetAddress().toString() + " says: " + chatIn);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("Wifi", "Receive message threw an exception: " + e.toString());
			chatIn = "Error exception: " + e.toString();
		}

	}

	public void stopUDPServer() {
		udpServerOpen = false;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		result = this.chatIn;
		
	}

	

}
