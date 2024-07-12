package com.zharnikova.example;

import java.util.*;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        myHashMap.put("24", 4309);
        myHashMap.put("34", 450);
        System.out.println(myHashMap.entrySet());
        MyHashMap <String,Integer> myHashMap1 = new MyHashMap<>();
        myHashMap1.put("29", 4378);
        myHashMap1.put("39", 48);
        System.out.println(myHashMap1.entrySet());
        myHashMap.putAll(myHashMap1);
        System.out.println(myHashMap.entrySet());
    }
}