package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class ClienteMain {
	
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Socket socket = new Socket("localhost", 4444);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println((String) ois.readObject());
            String nombre = sc.nextLine();
            oos.writeObject(nombre);

            Thread hiloCliente = new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje= (String) ois.readObject()) != null) {
                        System.out.println("Servidor: " + mensaje);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Conexión con el servidor cerrada.");
                }
            });
            hiloCliente.start();

            String message;
            while (!(message = sc.nextLine()).equalsIgnoreCase("exit")) {
                oos.writeObject(message);
            }

            hiloCliente.interrupt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
