package edu.eci.Arep;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AppTest {

    @BeforeAll
    static void setup() {
        // Simula el registro de rutas sin iniciar el servidor
        MicroFrameWork.StaticFiles("src/main/resources/static");
        MicroFrameWork.get("/App/hello", (req, res) -> "¡Hola, mundo!");
        MicroFrameWork.get("/App/pi", (req, res) -> String.valueOf(Math.PI));
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
}
