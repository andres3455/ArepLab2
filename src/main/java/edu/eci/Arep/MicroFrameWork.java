package edu.eci.Arep;


import java.util.HashMap;
import java.util.Map;

public class MicroFrameWork {

    private static String staticFilesPath = "src/main";

    private static final Map<String, InterfaceRoute> routes = new HashMap<>();

    public static void get(String path, InterfaceRoute route) {
        routes.put(path, route);
    }

    public static InterfaceRoute getRoute(String path) {
        return routes.get(path);
    }

    public static void StaticFiles(String path) {
        staticFilesPath = path;
    }

    public static String getStaticFilesPath() {
        return staticFilesPath;
    }

    

}
