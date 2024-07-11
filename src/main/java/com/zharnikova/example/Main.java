package com.zharnikova.example;

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

        MyHashMap<String, String> map = new MyHashMap<>(3);

        map.put("a", "Hall");
        map.put("b", "Hslls");

        System.out.println(map.size());

        String Hall = map.get("a");
        String Hslls = map.get("b");

        System.out.println(map.containsKey("a"));
        System.out.println(map.containsKey("b"));

        map.remove("a");
        System.out.println(map.containsKey("a"));

        MyHashMap<String, String> map2 = new MyHashMap<>();
        map2.put("1", "h");
        map2.put("2", "bs");
        map2.put("3", "fa");
        map2.put("4", "fc");
        map2.put("5", "hc");
        map2.put("6", "df");
        map2.put("7", "g");
        map2.put("8", "bcx");
        map2.put("9", "bth");
        map2.put("10", "ttd");
        map2.put("11", "ggb");
        map2.put("12", "vcb");
        map2.put("13", "xvcb");
        map2.put("14", "cbr");
        map2.put("15", "rg");
        map2.put("16", "gd");
        map2.keySet().forEach(k -> System.out.print(k + " "));
        System.out.println();
        map2.values().forEach(v -> System.out.print(v + " "));

        for (MyHashMap.Entry<String, String> entry : map2.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println(map.size());
        System.out.println(map2.size());
        System.out.println(map2.get("6"));
        System.out.println(map2.get("16"));

        map2.remove("1");
        map2.remove("2");
        System.out.println(map2.size());

        MyHashMap<Integer, Integer> map1 = new MyHashMap<>();
        for (int i = 0; i < 1003 ; i++) {
            map1.put(i,  i);
        }

        System.out.println(map1.size());
    }
}