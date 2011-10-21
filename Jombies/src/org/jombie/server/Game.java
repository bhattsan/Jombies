package org.jombie.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jombie.server.Messages.ClientMessage;
import org.jombie.server.Messages.ClientMessage.Location;
import org.jombie.server.Messages.ServerMessage;
import org.jombie.server.Messages.ServerMessage.DeathNews;
import org.jombie.server.Messages.ServerMessage.Info;
import org.jombie.server.Messages.ServerMessage.Projectile;
import org.jombie.unit.marines.Marine;

import com.google.protobuf.InvalidProtocolBufferException;

public class Game implements Runnable {
	private String gameID;
	private int gameSecs;
	private int currSec;
	private HashMap<String, Client> clientMap;
	private ArrayList<Client> teamA;
	private ArrayList<Client> teamB;
	private ArrayList<Client> clients;
	private ConcurrentLinkedQueue<String> msgQueue;
	private ConcurrentLinkedQueue<String> broadCastQueue;
//	private BufferedImage theMap;
	public Game(String gameID, int mins, String mapName) {
		this.gameID = gameID;
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
		t.start();
	}

	final int delay = 20;

	@Override
	public void run() {
		int inThisSec = 0;
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
			sendToBroadCast();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		endGame();
	}

	
	private void sendToBroadCast() {
		for(String message : broadCastQueue){
			for(Client currClient : clients){
				currClient.sendMessage(message);
			}
		}
	}
	private void parseMessages() {
		broadCastQueue.clear();
		while(!msgQueue.isEmpty()){
			String parse = msgQueue.remove();
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
				e.printStackTrace();
			}
			if(theMessage.hasChDir()){
				System.out.println("YEP");
				Location dir = theMessage.getChDir(); 
				theClient.setCurrPosX(dir.getX());
				theClient.setCurrPosY(dir.getY());
				theClient.getDir().setxCoord(dir.getDX());
				theClient.getDir().setyCoord(dir.getDy());
				//set the broadcast message
				ServerMessage.Builder serverB = ServerMessage.newBuilder();
				Info.Builder infoB = Info.newBuilder();
				infoB.setX((int) theClient.getPos().getxCoord());
				infoB.setY((int) theClient.getPos().getyCoord());
				infoB.setHandX((int) theClient.getDir().getxCoord());
				infoB.setHandY((int) theClient.getDir().getyCoord());
				serverB.setInfo(infoB);
				String mess = serverB.build().toByteString().toStringUtf8();
				broadCastQueue.add(mess.length()+":"+mess);
				
				System.out.println(user+","+theClient.getCurrPosX()+ " "+ theClient.getCurrPosY());
			} else if (theMessage.hasDeath()){
				//shoot shit
				String killer = theMessage.getDeath().getKiller();
				String victim = theClient.getUserName();
				
				ServerMessage.Builder death = ServerMessage.newBuilder();
				DeathNews.Builder newB = DeathNews.newBuilder();
				newB.setKiller(killer);
				newB.setVictim(victim);
				death.setDeath(newB);
				
				String mess = death.build().toByteString().toStringUtf8();
				broadCastQueue.add(mess.length()+":"+mess);
				
				System.out.println("DEATH!");
			} else if (theMessage.hasShootDie()){
				ServerMessage.Builder newShoot = ServerMessage.newBuilder();
				Projectile.Builder shoot = Projectile.newBuilder();
				shoot.setX(theMessage.getShootDie().getX());
				shoot.setY(theMessage.getShootDie().getY());
				shoot.setDirX(theMessage.getShootDie().getDirX());
				shoot.setDirY(theMessage.getShootDie().getDirY());
				newShoot.setProj(shoot);
				
				String mess = newShoot.build().toByteString().toStringUtf8();
				broadCastQueue.add(mess.length()+":"+mess);
				
			} 
		}
	}
	
	private void endGame() {
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
