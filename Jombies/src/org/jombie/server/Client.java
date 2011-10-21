package org.jombie.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.jombie.common.Vector;
import org.jombie.unit.Unit;

public class Client {
	private static final int CLIENT_SPEED = 10;
	private String userName;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	private boolean connected;
	private boolean isMoving;
	private Game theGame;
	private Vector dir;
	private Unit unit;

	public Client(Game theGame, Socket sock) {
		try {
			setDir(new Vector());
			this.theGame = theGame;
			connected = false;
			setMoving(false);
			fromClient = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			toClient = new PrintWriter(sock.getOutputStream(), true);
		} catch (IOException ioe) {
		}
	}

	public boolean initHandShake() {
		toClient.println(Protocol.SERVER_HI);
		try {
			if (!fromClient.readLine().equals(Protocol.CLIENT_HI)) {
				return false;
			}
			setUserName(fromClient.readLine());
		} catch (IOException ioe) {
			return false;
		}
		connected = true;
		theGame.addClient(this);
		return true;
	}

	public boolean sendMessage(String message) {
		if (!connected)
			return false;
		toClient.println(message);
		return true;
	}

	public void recieveLoop() {
		try {
			String message = fromClient.readLine();
			if(message.equals(Protocol.CLIENT_BYE)) connected = false;
			theGame.parse(this, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			connected = false;
			return;
		}
	}

	public int getCurrPosX() {
		return (int) unit.getLocation().getxCoord();
	}

	public void setCurrPosX(int currPosX) {
		unit.getLocation().setxCoord(currPosX);
	}

	public int getCurrPosY() {
		return (int) unit.getLocation().getyCoord();
	}

	public void setCurrPosY(int currPosY) {
		unit.getLocation().setyCoord(currPosY);
	}

	public boolean isMoving() {
		return isMoving;
	}

	public Vector getPos() {
		return unit.getLocation();
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void progress() {
		if(isMoving){
			unit.getLocation().addScalarVector(dir, unit.getSpeed());
		}

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Vector getDir() {
		return dir;
	}

	public void setDir(Vector dir) {
		this.dir = dir;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setUnit(Unit theUnit) {
		this.unit = theUnit;	
	}

	public Unit getUnit() {
		return unit;
	}
}
