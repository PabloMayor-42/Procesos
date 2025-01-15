package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import cliente.ClienteMain;

public class Servidor {
	 private static final AtomicInteger clientes= new AtomicInteger(0);
	 private static final Map<String, ObjectOutputStream> mapaClientes = Collections.synchronizedMap(new HashMap<>());
	 
	public static void main(String[] args) {
		
		try (ServerSocket serverSocket = new ServerSocket(4444)) {

            while (clientes.get() < 4) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                	try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                         ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream()))
                	{
                		oos.writeObject("Cual es tu nombre?");
                        String nombre = (String) ois.readObject();
                        System.out.println(nombre + " se ha conectado.");

                        synchronized (mapaClientes) {
                        	mapaClientes.put(nombre, oos);
                        }
                        clientes.incrementAndGet();

                        String message;
                        while ((message = (String) ois.readObject()) != null) {
                        	System.out.println("Mensaje de " + nombre + ": " + message);
                        }
                        } catch (IOException | ClassNotFoundException e) {
                            System.err.println(e.getMessage());
                        }
                }).start();
            }

            System.out.println("Todos los clientes se han conectado. Iniciando comunicación...");

            // Hilo para enviar "ping" periódicamente
            new Thread(() -> {
                while (true) {
                    synchronized (mapaClientes) {
                        for (Map.Entry<String, ObjectOutputStream> cliente : mapaClientes.entrySet()) {
                            try {
                                cliente.getValue().writeObject("ping");
                            } catch (IOException e) {
                                System.err.println("Error al enviar ping a " + cliente.getKey());
                            }
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }).start();

        }  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
