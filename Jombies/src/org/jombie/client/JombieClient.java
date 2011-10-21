package org.jombie.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.jombie.server.Protocol;

public class JombieClient {
	private BufferedReader fromServer;
	private PrintWriter toServer;
	public JombieClient(String server) throws UnknownHostException, IOException {
		Socket theServer = new Socket ( server, Protocol.PORT_NUMBER);
		fromServer = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
		toServer = new PrintWriter(theServer.getOutputStream(), true);
		//start handshake
		if(!fromServer.readLine().equals(Protocol.SERVER_HI)){
			System.out.println("NOT A JOMBIE SERVER!");
			return;
		}
		toServer.println(Protocol.CLIENT_HI);
		String userName =JOptionPane.showInputDialog("HERP DERP");
	
		toServer.println(userName);
		System.out.println(fromServer.readLine());
		System.out.println(fromServer.readLine());
		System.out.println(fromServer.readLine());
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		JombieClient client = new JombieClient("localhost");
	}
}
