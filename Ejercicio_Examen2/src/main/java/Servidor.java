import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;


public class Servidor {
	
	public static void main(String[] args) {
		AtomicInteger clientes = new AtomicInteger(0);
		AtomicInteger numeroEnviar = new AtomicInteger(0);
		
		try (ServerSocket socket = new ServerSocket(4444);){
			
			Socket clientSocket = socket.accept();
			
			if (clientes.get() < 3) {
				
                try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                		ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream()))
                {
                	clientes.getAndIncrement();
                	
                	new Thread(() -> {
                		try {
                			numeroEnviar.getAndIncrement();
							oos.writeObject(new Mensaje("Servidor", String.valueOf(numeroEnviar.get())));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}).start();
                	
                	new Thread(() -> {
                		try {
                			Mensaje mensaje = (Mensaje)ois.readObject(); 
                			numeroEnviar.set(Integer.parseInt(mensaje.mensaje()));
                			System.out.println(numeroEnviar);
						} catch (IOException | ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}).start();
                		
                } catch (IOException e) {
                	System.err.println(e.getMessage());
                }
            }
			
			else {
				try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());)
				{
					oos.writeObject(new Mensaje("Servidor", "No puedes conectarte, ya hay 3 clientes conectados"));
					oos.writeBoolean(false);
				} catch (IOException e) {
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
