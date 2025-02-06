package edu.eci.Arep;

import java.util.Map;
import java.util.Random;

public class weatherService {

    private Random random;
    
        public weatherService(){
            this(new Random());
        }
        public weatherService(Random random) {
                this.random = random;
        }
        
        public String handleWeatherRequest(Request path, Response res) {
            // Aquí parseamos los parámetros de la consulta
            Map<String, String> queryParams = parseQueryParams(path);
            String city = queryParams.getOrDefault("city", "Bogota");
    
            int temperature = random.nextInt(15) + 10; 
            int humidity = random.nextInt(50) + 50; 
    
            String[] conditions = {"Soleado", "Nublado", "Lluvioso", "Tormenta"};
            String condition = conditions[random.nextInt(conditions.length)];
    
            // Construir la respuesta en formato JSON
            String jsonResponse = String.format("{\"city\": \"%s\", \"temperature\": %d, \"humidity\": %d, \"condition\": \"%s\"}",
                                                city, temperature, humidity, condition);
    
            return jsonResponse;
        }
    
        private static Map<String, String> parseQueryParams(Request path) {
            return path.getQueryParams();
        }

}
