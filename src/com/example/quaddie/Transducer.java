package com.example.quaddie;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Transducer {

	Functionality functions = new Functionality();

	public String transduce(String command) {
		String result = "";
		if (!command.isEmpty()) {
			StringTokenizer tokens = new StringTokenizer(command);

			List<String> commandList = new LinkedList<String>();

			while (tokens.hasMoreTokens()) {
				commandList.add(tokens.nextToken());
			}

			if (commandList.get(0).equals("BATTERY")
					|| commandList.get(0).equals("BAT")) {
				result += "The battery has some amount left";
			} else if (commandList.get(0).equals("ALTITUDE")
					|| commandList.get(0).equals("ALT")) {
				result += "The altitude is so many meters above ground";
			} else if (commandList.get(0).equals("P2P")
					|| commandList.get(0).equals("WIFI")
					|| commandList.get(0).equals("DIRECT")) {
				result += functions.P2p();
			} else if (commandList.get(0).equals("LS")
					|| commandList.get(0).equals("LIST")
					|| commandList.get(0).equals("help")
					|| commandList.get(0).equals("HELP")) {
				result += "Battery(BATTERY/BAT)\nAltitude(ALTITUDE/ALT)\nList(LIST/LS)";
			} else if (commandList.get(0).equals("CHAT")
					|| commandList.get(0).equals("chat")
					|| commandList.get(0).equals("C")) {
				UdpServer udpServer = new UdpServer(5001);
				result += "UDP Server Started";
				result += udpServer.execute();
			}
			else {
				result += "ERROR: Undentified command";
			}
		}

		return result;
	}

}
