import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Cliente {
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AtomicInteger numeroEnviar = new AtomicInteger(0);
        AtomicInteger numeroCliente = new AtomicInteger(0);

        try (Socket socket = new Socket("localhost", 4444);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

        	new Thread(() -> {
        		try {
        			Mensaje mensaje = (Mensaje)ois.readObject(); 
        			numeroEnviar.set(Integer.parseInt(mensaje.mensaje()));
        			if(numeroEnviar.get()==11) {
        				socket.close();
        			}else {
        				System.out.println(numeroEnviar);
            			numeroEnviar.getAndIncrement();
        			}
        			
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}).start();
        	
        	new Thread(() -> {
        		try {
					oos.writeObject(new Mensaje("Cliente " + numeroCliente, String.valueOf(numeroEnviar.get())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}).start();
        	
        	String mensaje="";
        	if(!ois.readBoolean()) {
        		while(!mensaje.equalsIgnoreCase("no")) {
        			System.out.println("Quieres volver a intentarlo?");
        			mensaje=sc.nextLine();
        		}
        		socket.close();
        	}
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
