package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class ClienteMain {

	private AtomicInteger contador;
	private Cliente cliente;
	
	public ClienteMain(Object object,AtomicInteger contador) {
		super();
		// TODO Auto-generated constructor stub
		this.contador=contador;
		this.cliente=(Cliente)object;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		try (Socket socket = new Socket("localhost", 4444);){
			
			while(contador.get()<4) 
			{
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
				System.out.println("Cual es tu nombre?");
				String mensajeString = sc.nextLine();
				Cliente cliente= new Cliente(mensajeString);
				oos.writeObject(cliente);
			
				contador.getAndIncrement();
			}
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
