package org.jombie.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.jombie.common.Vector;
import org.jombie.server.Messages.ClientMessage;
import org.jombie.server.Messages.ClientMessage.Death;
import org.jombie.server.Messages.ClientMessage.Location;
import org.jombie.server.Messages.ClientMessage.Shoot;
import org.jombie.server.Messages.ServerMessage;
import org.jombie.server.Messages.ServerMessage.newComer.Team;
import org.jombie.server.Protocol;
import org.jombie.unit.Unit;
import org.jombie.unit.marines.Marine;

import com.google.protobuf.InvalidProtocolBufferException;

public class JombieClient implements Runnable {
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private PanelTest theView;

	public JombieClient(PanelTest theView, String server, String string)
			throws UnknownHostException, IOException {
		Socket theServer = new Socket(server, Protocol.PORT_NUMBER);
		this.theView = theView;
		fromServer = new BufferedReader(new InputStreamReader(
				theServer.getInputStream()));
		toServer = new PrintWriter(theServer.getOutputStream(), true);
		// start handshake
		if (!fromServer.readLine().equals(Protocol.SERVER_HI)) {
			System.out.println("NOT A JOMBIE SERVER!");
			return;
		}
		toServer.println(Protocol.CLIENT_HI);

		toServer.println(string);
		System.out.println(fromServer.readLine());
		System.out.println(fromServer.readLine());
		Thread t = new Thread(this);
		t.start();
	}

	public void sendCoords(Vector pos, Vector direction, boolean isMoving) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		Location.Builder locBuilder = Location.newBuilder();
		locBuilder.setX( pos.getxCoord());
		locBuilder.setY( pos.getyCoord());
		locBuilder.setDX( direction.getxCoord());
		locBuilder.setDy( direction.getyCoord());
		builder.setChDir(locBuilder);
		sendMessage(builder.build());
	}

	public void sendDeath(String killer) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		Death.Builder death = Death.newBuilder();
		death.setKiller(killer);
		builder.setDeath(death);
		sendMessage(builder.build());
	}

	public void sendShoot(Vector direciton, Vector location) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		Shoot.Builder shootBuilder = Shoot.newBuilder();
		shootBuilder.setX( location.getxCoord());
		shootBuilder.setY( location.getyCoord());
		shootBuilder.setDirX( direciton.getxCoord());
		shootBuilder.setDirY( location.getyCoord());
		builder.setShootDie(shootBuilder);
		sendMessage(builder.build());
	}

	private void sendMessage(ClientMessage message) {
		BigInteger bi = new BigInteger(message.toByteArray());
		toServer.println(bi.toString(16));
	}

	// public static void main(String[] args) throws UnknownHostException,
	// IOException {
	// JombieClient client = new JombieClient("localhost");
	// Vector derp = new Vector();
	// derp.setxCoord(1);
	// derp.setyCoord(2);
	// client.sendCoords(derp, derp, true);
	// }
	@Override
	public void run() {
		String reply = "";
		while (true) {
			try {
				reply = fromServer.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(reply==Protocol.SERVER_BYE) return;
			ServerMessage serverMess = null;
			byte[] data = new BigInteger(reply, 16).toByteArray();
			try {
				serverMess = ServerMessage.parseFrom(data);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			if (serverMess.hasInfo()) {
				Vector location = new Vector();
				Vector dir = new Vector();
				dir.setxCoord(serverMess.getInfo().getHandX());
				dir.setyCoord(serverMess.getInfo().getHandY());
				location.setxCoord(serverMess.getInfo().getX());
				location.setyCoord(serverMess.getInfo().getY());
				theView.getInfo(serverMess.getInfo().getUser(), location, dir);

			} else if (serverMess.hasDeath()) {
				theView.deathNewsArrived(serverMess.getDeath().getKiller(),
						serverMess.getDeath().getVictim());

			} else if (serverMess.hasProj()) {
				System.out.println("snae");
				Vector dir = new Vector();
				Vector location = new Vector();
				dir.setxCoord(serverMess.getProj().getDirX());
				dir.setyCoord(serverMess.getProj().getDirY());
				location.setxCoord(serverMess.getProj().getX());
				location.setyCoord(serverMess.getProj().getY());
				theView.projectilesSpawned(location, dir, serverMess.getProj()
						.getOwner());
			} else if (serverMess.hasNew()) {
				Vector location = new Vector();
				location.setxCoord(serverMess.getNew().getX());
				location.setyCoord(serverMess.getNew().getY());
				Vector direction = new Vector();
				direction.setxCoord(serverMess.getNew().getDirX());
				direction.setyCoord(serverMess.getNew().getDirY());

				theView.newComerArrived(
						serverMess.getNew().getName(),
						serverMess.getNew().getTeam() == Team.A ? Unit.Team.TEAM_A
								: Unit.Team.TEAM_B, new Marine(), location,
						direction);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
