package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import cliente.ClienteMain;

public class Servidor {
	public static void main(String[] args) {
		
		AtomicInteger contador = new AtomicInteger(0);
		
		try(ServerSocket server = new ServerSocket(4444);){
			
			for (int i = 0; i < 4; i++) {
				Socket socket = server.accept();
				System.out.println("Cual es tu nombre?");
				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
				
				try {
					ClienteMain cliente = new ClienteMain(entrada.readObject(),contador);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
