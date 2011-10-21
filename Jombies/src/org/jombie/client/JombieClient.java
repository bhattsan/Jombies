package org.jombie.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.jombie.common.Vector;
import org.jombie.server.Messages.ClientMessage;
import org.jombie.server.Messages.ClientMessage.Death;
import org.jombie.server.Messages.ClientMessage.Location;
import org.jombie.server.Messages.ClientMessage.Shoot;
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
	}
	public void sendCoords(Vector pos, Vector direction, boolean isMoving){
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		Location.Builder locBuilder = Location.newBuilder();
		locBuilder.setX((int) pos.getxCoord());
		locBuilder.setY((int) pos.getyCoord());
		locBuilder.setDX((int) direction.getxCoord());
		locBuilder.setDy((int) direction.getyCoord());
		builder.setChDir(locBuilder);
		sendMessage(builder.build().toByteString().toStringUtf8());
	}
	public void sendDeath(String killer){
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		Death.Builder death = Death.newBuilder();
		death.setKiller(killer);
		builder.setDeath(death);
		sendMessage(builder.build().toByteString().toStringUtf8());
	}
	public void sendShoot(Vector direciton, Vector location){
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		Shoot.Builder shootBuilder = Shoot.newBuilder();
		shootBuilder.setX((int) location.getxCoord());
		shootBuilder.setY((int) location.getyCoord());
		shootBuilder.setDirX((int) direciton.getxCoord());
		shootBuilder.setDirY((int) location.getyCoord());
		builder.setShootDie(shootBuilder);
		sendMessage(builder.build().toByteString().toStringUtf8());
	}
	private void sendMessage(String message) {
		toServer.println(message.length()+":"+message);
		
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		JombieClient client = new JombieClient("localhost");
		Vector derp = new Vector();
		derp.setxCoord(1);
		derp.setyCoord(2);
		client.sendCoords(derp, derp, true);
	}
}
