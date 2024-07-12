import com.zharnikova.example.MyHashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * The type My hash map tests.
 */
@RunWith(JUnit4.class)
public class MyHashMapTests {

    /**
     * The Default hash map.
     */
    MyHashMap<String, String> defaultHashMap = new MyHashMap<>();
    /**
     * The Sized hash map.
     */
    MyHashMap<String, String> sizedHashMap = new MyHashMap<>(5);

    MyHashMap<Integer, Integer> map = new MyHashMap<>();


    /**
     * Init.
     */
    @Before
    public void put() {
        defaultHashMap.put("1", "India");
        defaultHashMap.put("2", "China");
        defaultHashMap.put("3", "Vietnam");

        sizedHashMap.put("2", "tomato");
        sizedHashMap.put("3", "cucumber");
        sizedHashMap.put("4", "eggplant");

        for (int i = 0; i < 1000 ; i++) {
            map.put(i,  i);
        }
    }


    /**
     * Test map return proper value for key.
     */
    @Test
    public void testMapReturnProperValueForKey() {
        String expectedResult = "India";
        String actualResult = defaultHashMap.get("1");
        assertEquals(expectedResult, actualResult);

        Integer expectedRes = 675;
        Integer actualRes = map.get(675);
        assertEquals(expectedRes,actualRes);

    }

    /**
     * Test map return proper size.
     */
    @Test
    public void testMapReturnProperSize() {
        int expectedResult = 3;
        int actualResult = defaultHashMap.size();
        assertEquals(expectedResult, actualResult);

        int expectedRes = 1000;
        int actualres = map.size();
        assertEquals(expectedRes,actualres);
    }


    /**
     * Test map return proper value after adding elements with null key.
     */
    @Test
    public void testMapReturnProperValueAfterAddingElementsWithNullKey() {
        String expectedResult = "France";
        defaultHashMap.put("4", "German");
        defaultHashMap.put("5", "Spain");
        defaultHashMap.put("6", "France");
        String actualResult = defaultHashMap.get("6");
        assertEquals(expectedResult, actualResult);

        Integer expectedRes = 1003;
        map.put(1001, 1001);
        map.put(1002, 1003);
        map.put(1003, 1002);
        Integer actualRes = map.get(1002);
        assertEquals(expectedRes, actualRes);

    }

    /**
     * Test map return proper size after increasing map capacity.
     */
    @Test
    public void testMapReturnProperSizeAfterIncreasingMapCapacity() {
        int expectedResult = 6;
        sizedHashMap.put("5", "orange");
        sizedHashMap.put("6", "grape");
        sizedHashMap.put("7", "apple");
        int actualResult = sizedHashMap.size();
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test map return size after removing map capacity.
     */
    @Test
    public void testMapReturnSizeAfterRemovingMapCapacity(){
        int expectedResult = 2;
        defaultHashMap.remove("3");
        int actualResult = defaultHashMap.size();
        assertEquals(expectedResult, actualResult);


    }

    /**
     * Put and get no collisions.
     */
    @Test
    public void putAndGetNoCollisions() {
        MyHashMap<Integer, Integer> map = new MyHashMap<Integer, Integer>();

        for (int i = 0; i < 1000 ; i++) {
            map.put(i,  i);
        }
        for (int i = 0; i < 1000 ; i++) {
            assertEquals((Integer) i, map.get(i));
        }

        assertEquals(1000, map.size());


    }

    @Test
    public void isEmpty(){
        Boolean expectedResult = false;
        Boolean actualResult = map.isEmpty();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public  void loadFactor(){
        Float expectedResult = 0.75f;
        Float actualResult = map.loadFactor();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testHashcode(){
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertNotEquals(obj1.hashCode(), obj2.hashCode());

        assertEquals(obj1.hashCode(), obj1.hashCode());
    }

    @Test
    public void size(){
        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        int expectedResult = 0;
        int actualResult = myHashMap.size();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRemoveNotExcistKey() {

        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        myHashMap.put("24", 4309);
        myHashMap.put("34", 450);
        Integer expectedResult = null;
        Integer actualResult = myHashMap.remove("26");
        assertEquals(expectedResult, actualResult);

        }

    @Test
    public void testKeySet(){
        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        myHashMap.put("24", 4309);
        myHashMap.put("34", 450);
        Set<String> expectedResult = new HashSet<>();
        expectedResult.add("24");
        expectedResult.add("34");
        Set<String> actualResult = myHashMap.keySet();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testEntrySet(){
        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        myHashMap.put("24", 4309);
        myHashMap.put("34", 450);
        Set<MyHashMap.Entry<String,Integer>> actualResult =  myHashMap.entrySet();
        Set<MyHashMap.Entry<String, Integer>> expectedResult = new HashSet<>();
        expectedResult.addAll(myHashMap.entrySet());
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testValues(){
        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        myHashMap.put("24", 4309);
        myHashMap.put("34", 450);

        Collection<Integer> expectedResult = new HashSet<>();
        expectedResult.add(4309);
        expectedResult.add(450);
        Collection<Integer> actualResult = myHashMap.values();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    public void testPutAll(){
        MyHashMap <String,Integer> myHashMap = new MyHashMap<>();
        myHashMap.put("24", 4309);
        myHashMap.put("34", 450);
        MyHashMap <String,Integer> actualResult = new MyHashMap<>();
        actualResult.put("29", 4378);
        actualResult.put("39", 48);
        actualResult.putAll(myHashMap);
        MyHashMap <String,Integer> expectedResult = new MyHashMap<>();
        expectedResult.put("24", 4309);
        expectedResult.put("34", 450);
        expectedResult.put("29", 4378);
        expectedResult.put("39", 48);
        assertEquals(expectedResult.entrySet(), actualResult.entrySet());

    }

    @Test
    public void testPutExistKey(){
        MyHashMap <String,Integer> actualResult = new MyHashMap<>();
        actualResult.put("24", 4309);
        actualResult.put("34", 450);
        actualResult.put("24", 75894);
        MyHashMap <String,Integer> expectedResult = new MyHashMap<>();
        expectedResult.put("24", 75894);
        expectedResult.put("34", 450);
        assertEquals(actualResult.entrySet(),expectedResult.entrySet());

    }
}
