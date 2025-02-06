package edu.eci.Arep;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AppTest {

    private static final int PORT = 35000;
    private static ServerSocket serverSocket;

    @BeforeAll
    static void setup() throws IOException {

        serverSocket = new ServerSocket(PORT);

        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept(); // Acepta nuevas conexiones
                    RequestHandler.handle(clientSocket); // Procesa la solicitud
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Simula el registro de rutas sin iniciar el servidor
        MicroFrameWork.StaticFiles("src/main/resources/static");
        MicroFrameWork.get("/App/hello", (req, res) -> "¡Hola, mundo!");
        MicroFrameWork.get("/App/pi", (req, res) -> String.valueOf(Math.PI));
    }

    @AfterAll
    static void tearDown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close(); // Cierra el servidor después de las pruebas
        }
    }

    @Test
    public void testHandleGetRequest_StaticFile() throws IOException {
        // Simula una solicitud GET
        Socket socket = new Socket("localhost", PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Enviar una solicitud GET
        writer.println("GET /index.html HTTP/1.1");
        writer.println("Host: localhost");
        writer.println(); // Línea en blanco para finalizar los encabezados

        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            System.out.println("Response: " + responseLine); // Imprime la respuesta del servidor
            if (responseLine.contains("HTTP/1.1 200 OK")) {
                // Verifica que el código de estado sea 200 OK
                assertTrue(responseLine.contains("200 OK"));
                break;
            }
        }

        socket.close();
    }

    @Test
    void testStaticFilesPath() {
        assertEquals("src/main/resources/static", MicroFrameWork.getStaticFilesPath());
    }

    @Test
    void testRegisteredRoutes() {
        assertNotNull(MicroFrameWork.getRoute("/App/hello"));
        assertNotNull(MicroFrameWork.getRoute("/App/pi"));
    }

    @Test
    public void testAddAndGetRoute() {
        // Configuración
        MicroFrameWork.get("/test", (req, res) -> "Test Route");

        // Verificación
        InterfaceRoute route = MicroFrameWork.getRoute("/test");
        assertNotNull(route, "La ruta no debería ser nula");

        Request req = new Request("/test", null);
        Response res = new Response();
        String response = route.handle(req, res);
        assertEquals("Test Route", response, "La respuesta de la ruta no coincide");
    }

    @Test
    public void testHandleWeatherRequest() {
        // Configuración
        weatherService ws = new weatherService();
        Request req = new Request("/App/weather?city=Bogota", null);
        Response res = new Response();

        // Ejecución
        String response = ws.handleWeatherRequest(req, res);

        // Verificación
        assertTrue(response.contains("\"city\": \"Bogota\""), "La respuesta debe incluir la ciudad Bogota");
        assertTrue(response.contains("\"temperature\":"), "La respuesta debe incluir la temperatura");
        assertTrue(response.contains("\"humidity\":"), "La respuesta debe incluir la humedad");
        assertTrue(response.contains("\"condition\":"), "La respuesta debe incluir la condición climática");
        assertEquals(200, res.getcodestatus(), "El código de estado debería ser 200");
    }


    @Test
    public void testHandleHelloRequest() throws IOException {
        // Simula una solicitud GET para la ruta "/hello"
        Socket socket = new Socket("localhost", PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Enviar una solicitud GET
        writer.println("GET /hello?name=Andres HTTP/1.1");
        writer.println("Host: localhost");
        writer.println(); // Línea en blanco para finalizar los encabezados

        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            System.out.println("Response: " + responseLine); // Imprime la respuesta del servidor
            if (responseLine.contains("¡Hola, Andres!")) {
                // Verifica que el saludo sea correcto
                assertTrue(responseLine.contains("¡Hola, Andres!"));
                break;
            }
        }

        socket.close();
    }

}
