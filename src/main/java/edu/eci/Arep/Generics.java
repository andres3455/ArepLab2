package edu.eci.Arep;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class Generics {

    @SuppressWarnings("unchecked")
    public static void main(String[]args){

        @SuppressWarnings("rawtypes")
        List<Integer> intList = new LinkedList(); 
        intList.add(0);
        //intList.add ("Hola");   
    Integer x = (Integer) intList.iterator().next();
    
}
}
