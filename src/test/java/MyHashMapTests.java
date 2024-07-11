import com.zharnikova.example.MyHashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

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

}
