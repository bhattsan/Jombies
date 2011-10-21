package org.jombie.server;

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jombie.server.Messages.ClientMessage;
import org.jombie.server.Messages.ClientMessage.Location;
import org.jombie.unit.marines.Marine;

import com.google.protobuf.InvalidProtocolBufferException;

public class Game implements Runnable {
	private static final double THRESHOLD = 3;
	private String gameID;
	private int gameSecs;
	private int currSec;
	private HashMap<String, Integer> kills;
	private HashMap<String, Integer> deaths;
	private HashMap<String, Client> clientMap;
	private ArrayList<Client> teamA;
	private ArrayList<Client> teamB;
	private ArrayList<Client> clients;
	private ConcurrentLinkedQueue<String> msgQueue;
//	private BufferedImage theMap;
	public Game(String gameID, int mins, String mapName) {
		this.gameID = gameID;
		kills = new HashMap<>();
		deaths = new HashMap<>();
		teamA = new ArrayList<>();
		teamB = new ArrayList<>();
		clientMap = new HashMap<>();
		clients = new ArrayList<>();
		msgQueue = new ConcurrentLinkedQueue<>();
		gameSecs = mins * 60;
		currSec = 0;
//		try { :'(
//			theMap = ImageIO.read(new File(mapName));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public int addClient(Client toAdd){
		clients.add(toAdd);
		clientMap.put(toAdd.getUserName(), toAdd);
		toAdd.setUnit(new Marine());
		toAdd.sendMessage(gameID);
		if(teamA.size() > teamB.size()){
			teamB.add(toAdd);
			return 2;
		}else {
			teamA.add(toAdd);
			return 1;
		}
	}
	public void parse(Client user, String message) {
		msgQueue.add(user.getUserName()+ ":" + message);
	}

	public void runGame() {
		Thread t = new Thread(this);
		System.out.println("der der der");
		t.start();
	}

	final int delay = 20;

	@Override
	public void run() {
		int inThisSec = 0;
		System.out.println("Got here");
		for(Client currClient : clients){
			
			currClient.sendMessage(Protocol.GAME_START);
		}
		while (currSec < gameSecs) {
			parseMessages();
			inThisSec += delay;
			if (inThisSec / 1000 == 1) {
				currSec++;
				inThisSec = 0;
			}
			simulateGame();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		endGame();
	}

	private void parseMessages() {
		while(!msgQueue.isEmpty()){
			String parse = msgQueue.remove();
			System.out.println("GOT THIS!!"+ parse);
			String user = parse.substring(0, parse.indexOf(':'));
			String message = parse.substring(parse.indexOf(':')+1);
			Client theClient = clientMap.get(user);
			if(theClient == null) continue;
			String protoMess = message.substring(message.indexOf(':')+1);
			int length = Integer.parseInt(message.substring(0, message.indexOf(':')));
			ClientMessage theMessage = null;
			try {
				theMessage = ClientMessage.parseFrom(protoMess.substring(0,length).getBytes());
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(theMessage.hasChDir()){
				System.out.println("YEP");
				Location dir = theMessage.getChDir(); 
				theClient.setCurrPosX(dir.getX());
				theClient.setCurrPosY(dir.getY());
				theClient.getDir().setxCoord(dir.getDX());
				theClient.getDir().setyCoord(dir.getDy());
				
				System.out.println(user+","+theClient.getCurrPosX()+ " "+ theClient.getCurrPosY());
			} else if (theMessage.hasShootDie()){
				//shoot shit
				System.out.println("SHOT FIRED!");
			} else if (theMessage.hasDeath()){
				System.out.println("RELOADING!");
			}
		}
	}
	
	private void endGame() {
		// TODO Auto-generated method stub
		for(Client currClient : clients){
			currClient.sendMessage(Protocol.SERVER_BYE);
		}
	}
	
	private void simulateGame()
	{
//		for(Projectile proj : currProjectiles){
//			proj.updatePosition();
//			if()
//			for(Client currClient : clients){
//				if(proj.hasCollided(currClient.getUnit())){
//					//insert collision code here
//					System.out.println("collision");
//				}
//			}
//		}
		//blind faith in client
	}
}
