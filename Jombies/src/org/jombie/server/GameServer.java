package org.jombie.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	static volatile int goodClients = 0; 
	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(Protocol.PORT_NUMBER);
		Socket newClient = null;
		Game theGame = new Game("GAME!", 1, "TestMap.png");
		System.out.println("Starting server!!");
		int clients = 0;
		while(true){
			newClient = socket.accept();
			System.out.println("accepted a client");
			Client client = new Client(theGame, newClient);
			clients++;
			ClientThread clt = new ClientThread();
			clt.client = client;
			Thread t = new Thread(clt);
			t.start();
			if(clients==2)
				break;
		}
		while(goodClients<2);
		System.out.println("Running game");
		theGame.runGame();
	}
	static class ClientThread implements Runnable{
		Client client;
		public void run() {
			System.out.println("sanity");
			if(client.initHandShake())
				goodClients++;
			
		}
		
	}
}

