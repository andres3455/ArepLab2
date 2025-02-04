package edu.eci.Arep;

@FunctionalInterface
public interface InterfaceRoute {
    String handle(Request req, Response res);
}
