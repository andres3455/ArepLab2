package edu.eci.Arep;



public class LambdaFunctions {

    public static void main(String[] args) {
    
        //FunctionNoParam f = () -> "Hello";
        //FunctionNoParam f2 = new FunctionNoParam() {
        // public String execute(){
         //   return "Hello";
         //}   
        //};

    FunctionNoParam <Double> f = () -> Math.PI;
    FunctionNoParam <String> f2 = () -> String.valueOf(Math.PI);

    System.out.println("Function Execute" + f.execute());
    System.out.println("Function Execute " + f2.execute());

    FunctionOneParameter<Integer, String> size = (String str) -> str.length();

    FunctionOneParameter<Double, Double> sin = (Angulo ) -> Math.sin(Angulo);
        
    System.out.println("Function Execute" + size.execute("Hola Mundo"));

    System.out.println("Function Execute" + sin.execute(1.5));

    
    }
    
}
