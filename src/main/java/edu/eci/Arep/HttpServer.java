package edu.eci.Arep;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpServer {
    private static final int PORT = 35000;
    private static final int THREAD_POOL_SIZE = 10; // Permite hasta 10 clientes simultáneamente

    public static void main(String[] args) {

        // Rutas para los metodos estaticos

        MicroFrameWork.get("/hello", (req, res) -> "¡Hola, mundo!");
        MicroFrameWork.get("/pi", (req, res) -> String.valueOf(Math.PI));
        MicroFrameWork.get("/weather", (req, res) ->{
            weatherService ws = new weatherService();
            @SuppressWarnings("static-access")
            String response = ws.handleWeatherRequest(req, res);
            return response;
        });

        // Rutas para la extracción de valores de consulta

        MicroFrameWork.get("hello/:name", (req, res) -> "¡Hola, " + req.getQueryString("name"));

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> {
                    try {
                        RequestHandler.handle(clientSocket);
                        clientSocket.close();
                    } catch (IOException e) {
                        System.err.println("Client handling error: " + e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
