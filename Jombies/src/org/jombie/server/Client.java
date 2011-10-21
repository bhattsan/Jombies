package org.jombie.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.jombie.common.Vector;

public class Client {
	private static final int CLIENT_SPEED = 10;
	private String userName;
	private BufferedReader fromClient;
	private PrintWriter toClient;
	private boolean connected;
	private boolean isMoving;
	private Game theGame;
	private int currPosX;
	private int currPosY;
	private Vector currPos;
	private Vector dir;

	public Client(Game theGame, Socket sock) {
		try {
			currPos = new Vector();
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
			theGame.parse(this, message);
			System.out.println(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			connected = false;
			return;
		}
	}

	public int getCurrPosX() {
		return currPosX;
	}

	public void setCurrPosX(int currPosX) {
		this.currPosX = currPosX;
		currPos.setxCoord(currPosX);
	}

	public int getCurrPosY() {
		return currPosY;
	}

	public void setCurrPosY(int currPosY) {
		this.currPosY = currPosY;
		currPos.setxCoord(currPosX);
	}

	public boolean isMoving() {
		return isMoving;
	}

	public Vector getPos() {
		return currPos;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void progress() {
		if(isMoving){
			currPos.addScalarVector(dir, CLIENT_SPEED);
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
}
