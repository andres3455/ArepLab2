package edu.eci.Arep;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

class HttpServerTest {
    private static ExecutorService serverExecutor;

    @BeforeAll
    static void startServer() {
        serverExecutor = Executors.newSingleThreadExecutor();
        serverExecutor.submit(() -> httpServer.main(new String[] {})); // Inicia el servidor
        try {
            Thread.sleep(2000); // Esperar que el servidor arranque
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterAll
    static void stopServer() {
        serverExecutor.shutdownNow(); // Detener el servidor después de las pruebas
    }

    @Test
    void testHelloEndpoint() throws IOException {
        try (Socket socket = new Socket("localhost", 35000);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("GET /App/hello HTTP/1.1");
            writer.println("Host: localhost");
            writer.println();

            String responseLine = reader.readLine();
            assertNotNull(responseLine);
            assertTrue(responseLine.contains("200 OK"));

            String header;
            while ((header = reader.readLine()) != null && !header.isEmpty()) {
                System.out.println("Header: " + header); // Opcional: Para depuración
            }

            // Leer el cuerpo de la respuesta
            String body = reader.readLine();
            assertEquals("¡Hola, mundo!", body);
        }
    }

}