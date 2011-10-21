package org.jombie.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jombie.server.Messages.ClientMessage;
import org.jombie.server.Messages.ClientMessage.Location;
import org.jombie.server.Messages.ServerMessage;
import org.jombie.server.Messages.ServerMessage.DeathNews;
import org.jombie.server.Messages.ServerMessage.Info;
import org.jombie.server.Messages.ServerMessage.Projectile;
import org.jombie.server.Messages.ServerMessage.Projectile.Type;
import org.jombie.server.Messages.ServerMessage.UnitType;
import org.jombie.server.Messages.ServerMessage.newComer;
import org.jombie.server.Messages.ServerMessage.newComer.Team;
import org.jombie.unit.marines.Marine;

import com.google.protobuf.InvalidProtocolBufferException;

public class Game implements Runnable {
	private String gameID;
	private int gameSecs;
	private int currSec;
	private HashMap<String, Client> clientMap;
	private ArrayList<String> teamA;
	private ArrayList<String> teamB;
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
		broadCastQueue = new ConcurrentLinkedQueue<>();
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
			teamB.add(toAdd.getUserName());
			return 2;
		}else {
			teamA.add(toAdd.getUserName());
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
		sendNewComers();
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

	
	private void sendNewComers() {
		for(Client currClient : clients){
			ServerMessage.Builder build = ServerMessage.newBuilder();
			newComer.Builder newC = newComer.newBuilder();
			newC.setTeam(teamA.contains(currClient.getUserName()) ? Team.A : Team.B);
			newC.setName(currClient.getUserName());
			newC.setType(UnitType.Marine);
			if(teamA.contains(currClient.getUserName())){
					newC.setX( (Math.random()*350+20));
					newC.setY( (Math.random()*300+20));
			} else {
				newC.setX( (Math.random()*600+1300));
				newC.setY( (Math.random()*500+950));
			}
			newC.setDirX(0);
			newC.setDirY(0);
			build.setNew(newC);
			String mess = new BigInteger(build.build().toByteArray()).toString(16);
			for(Client curr : clients){
					curr.sendMessage(mess);
			}
				
		}
	}
	private void sendToBroadCast() {
		for(String message : broadCastQueue){
			BigInteger bi = new BigInteger(message,16);
			try {
				ServerMessage.parseFrom(bi.toByteArray());
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Client currClient : clients){
				if(!currClient.sendMessage(message)) System.out.println("uh oh");
			}
		}
		broadCastQueue.clear();
	}
	private void parseMessages() {
		broadCastQueue.clear();
		while(!msgQueue.isEmpty()){
			String parse = msgQueue.remove();
			String user = parse.substring(0, parse.indexOf(':'));
			String message = parse.substring(parse.indexOf(':')+1);
			Client theClient = clientMap.get(user);
			if(theClient == null) continue;
			ClientMessage theMessage = null;
			BigInteger bi = new BigInteger(message, 16);
			byte [] data = bi.toByteArray();
			try {
				theMessage = ClientMessage.parseFrom(data);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			if(theMessage.hasChDir()){
				Location dir = theMessage.getChDir(); 
				theClient.setCurrPosX(dir.getX());
				theClient.setCurrPosY(dir.getY());
				theClient.getDir().setxCoord(dir.getDX());
				theClient.getDir().setyCoord(dir.getDy());
				System.out.println(theClient.getPos());
				//set the broadcast message
				ServerMessage.Builder serverB = ServerMessage.newBuilder();
				Info.Builder infoB = Info.newBuilder();
				infoB.setX(theClient.getPos().getxCoord());
				infoB.setY( theClient.getPos().getyCoord());
				infoB.setHandX( theClient.getDir().getxCoord());
				infoB.setHandY( theClient.getDir().getyCoord());
				infoB.setUser(theClient.getUserName());
				serverB.setInfo(infoB);
				broadCastQueue.add(new BigInteger(serverB.build().toByteArray()).toString(16));
				
			} else if (theMessage.hasDeath()){
				String killer = theMessage.getDeath().getKiller();
				String victim = theClient.getUserName();
				
				ServerMessage.Builder death = ServerMessage.newBuilder();
				DeathNews.Builder newB = DeathNews.newBuilder();
				newB.setKiller(killer);
				newB.setVictim(victim);
				death.setDeath(newB);
				
				broadCastQueue.add(new BigInteger(death.build().toByteArray()).toString(16));
				
				System.out.println("DEATH!");
			} else if (theMessage.hasShootDie()){
				ServerMessage.Builder newShoot = ServerMessage.newBuilder();
				Projectile.Builder shoot = Projectile.newBuilder();
				shoot.setX(theMessage.getShootDie().getX());
				shoot.setY(theMessage.getShootDie().getY());
				shoot.setDirX(theMessage.getShootDie().getDirX());
				shoot.setDirY(theMessage.getShootDie().getDirY());
				shoot.setType(Type.PistolBullet);
				shoot.setOwner(theClient.getUserName());
				newShoot.setProj(shoot);
				System.out.println("got shome");
				
				broadCastQueue.add(new BigInteger(newShoot.build().toByteArray()).toString(16));
				
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
