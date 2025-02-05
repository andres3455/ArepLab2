package edu.eci.Arep;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class RequestHandler {

    public static void handle(Socket clientSocket) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return;
        }

        System.out.println("Request: " + requestLine);

        StringTokenizer tokens = new StringTokenizer(requestLine);
        String method = tokens.nextToken(); // GET, POST, etc.
        String fullpath = tokens.nextToken(); // /, /hello?name = Andrés

        if (!method.equals("GET")) {
            sendResponse(writer, 405, "Method Not Allowed", "Solo se permite GET.");
            return;
        }

        // Separar la ruta del query string (si existe)

        String path;
        String queryString = null;
        if (fullpath.contains("?")) {
            String[] parts = fullpath.split("\\?");
            path = parts[0];
            queryString = parts.length > 1 ? parts[1] : null;
        } else {
            path = fullpath;
        }



        if (path.equals("/")) {
            path = "/index.html"; // por defecto
            
        }

        //servir un archivo estático desde el MicroFrameWork
        String staticPath = MicroFrameWork.getStaticFilesPath(); // Obtener la ruta de los archivos estáticos
        File file = new File(staticPath + path);
        

        if (file.exists() && !file.isDirectory()) {
            sendFileResponse(writer, file);
            writer.close();
            reader.close();
            return;
        }

        if (path.equals("/hello")) {
            Request req = new Request(path, queryString);
            String name = req.getQueryParams().get("name");

            String responseBody = "¡Hola, " + (name != null ? name : "mundo") + "!";
            sendResponse(writer, 200, "OK", responseBody);
        }

        // Si no es un archivo, buscar en el framework de rutas
        else {
            InterfaceRoute route = MicroFrameWork.getRoute(path);
            if (route != null) {
                Request req = new Request(path, null);
                Response res = new Response();
                String responseBody = route.handle(req, res);
                sendResponse(writer, res.getcodestatus(), "OK", responseBody);
            } else {
                sendResponse(writer, 404, "Not Found", "Ruta no encontrada.");
            }
        }

        writer.close();
        reader.close();
    }

    private static void sendFileResponse(PrintWriter writer, File file) throws IOException {
        String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));

        writer.printf("HTTP/1.1 200 OK\r\n");
        writer.println("Content-Type: " + (contentType != null ? contentType : "text/plain") + "; charset=UTF-8");
        writer.println("Content-Length: " + file.length());
        writer.println();

        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = fileReader.readLine()) != null) {
            writer.println(line);
        }
        fileReader.close();
    }

    private static void sendResponse(PrintWriter writer, int statusCode, String statusMessage, String body) {
        writer.printf("HTTP/1.1 %d %s\r\n", statusCode, statusMessage);
        writer.println("Content-Type: text/plain; charset=UTF-8");
        writer.println("Content-Length: " + body.length());
        writer.println();
        writer.println(body);
    }

    public static void sendResponseToClient(PrintWriter writer, int statusCode, String statusMessage, String body) {
        sendResponse(writer, statusCode, statusMessage, body);
    }
}
