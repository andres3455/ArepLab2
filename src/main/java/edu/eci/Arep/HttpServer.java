package edu.eci.Arep;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public class HttpServer {
    private static final int PORT = 35000;
    private static Supplier<String> servicio;

    public static void start(String[] args) {


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);

            while (true) { 
                Socket clientSocket = serverSocket.accept();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }

    }
    
    public static void get(String route, Supplier<String> s){
        servicio = s;
    }

    public static void staticfiles(String path){

    }
}
