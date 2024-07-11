package com.zharnikova.example;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * <h1>�������� � ���������� ������ HashMap</h1>
 * ��������� ������ put(), get(), remove()
 * �������� ������ MyHashMap<K, V></K,V>, ������� ��������� ��������� Serializable ��� ��������, ��� ���������� ����� ������ ����� ���� ��������� � ����� ������ � ����� ������������� ������� � ������ ��� ������ ������.
 * ������������ ��������� ��������� ��������� ������� � ���������� ��� �� ���� ��� ��������� � ���� ��� ������������ �������������.
 * @param <K></K> ���� HashMap
 * @param <V></V> �������� HashMap
 * @author Zharnikova Kseniya
 * @version 1.0
 */
public class MyHashMap<K,V> implements Serializable {
    Locale locale = new Locale("ru", "Russia");
    /**
     * ���������� ������������� ������ ��� ������.
     * �������� 1L ��������� �� ��, ��� ��� ������ ������ ������
     */
    private static final long serialVersionUID = 1L;

    /**
     * ������� ���-������� �� ��������� (2^4==16)
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * ����������� ��������� ������� ���-������� = 1073741824
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * ����������� �������� ��� �������, ������������ �� ���������
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * ������ ���� Entry[], ������� �������� ���������� ������ �� ������ (�������) ��������
     *  @param <K></K> ���� HshMap
     *  @param <V></V> �������� HashMap
     */
    private Entry<K, V>[] entryTable;

    /**
     * ���������� ��������� HashMap-�
     */
    private int size;

    /**
     * ����������� ��������, ������������ �� ���������
     */
    private final float loadFactor;

    /**
     * ���������� ���������� ���������, ��� ���������� ��������, ������ ���-������� ������������� ����� (capacity * loadFactor).
     */
    private int threshold;



    /**
     * ���� ����� ������������ ��� �������� ������� ���� Entry �  ���������� ���������� ���� ������
     * @param n ��� ������ ������� Entry<K,V></K,V>[n]
     * @return Entry<?, ?></?,>[n] ����������� ������ Entry<K,V></K,V>[n]
     */
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] newTable(int n) {
        return (Entry<K, V>[]) new Entry<?, ?>[n];
    }


    /**
     * ��������� ����������� ������ MyHashMap, ������� �������� ������ ����������� � ����������� DEFAULT_INITIAL_CAPACITY � DEFAULT_LOAD_FACTOR
     * ����������� Hashmap �� ��������� (capacity=16, load factor=0.75)
     */
    public MyHashMap() {

        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * ����������� ������ MyHashmap � ��������� �������
     * ���� ����������� �������� ������ ����������� � ����� �����������, ��������� � ���� �������� initialCapacity � �������� ��������� DEFAULT_LOAD_FACTOR.
     * ��� ��������� ������� ��������� MyHashMap � �������� ��������� ��������, ��������� ��� ���� ����������� �������� ������������ ��������
     *
     * @param initialCapacity ������� ���-�������
     */
    public MyHashMap(int initialCapacity) {

        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * ����������� ������ MyHashMap, ������� ��������� ��� ���������: initialCapacity � loadFactor
     * ����������� ��������� ������������ ������� ����������:
     * ���� initialCapacity ������ ����, ������������ ���������� IllegalArgumentException � ���������� � ���, ��� ��������� ������� ������ ���� �������� ������.
     * ���� initialCapacity ������ ����������� ���������� �������, ��� ��������������� ������ ����������� ���������� �������.
     * ���� loadFactor ������ ��� ����� ���� ��� �������� NaN, ������������ ���������� IllegalArgumentException � ���������� � ������������ ������������ ��������.
     * �� ������ �������� ����� �������� capacity ����������� ���� ������ ����� �� ���� ���
     * �������� ������� entryTable � �������� ��������,
     * ������������ loadFactor, ���������� ���������� �������� threshold
     *
     * @param initialCapacity ������� ���-�������
     * @param loadFactor      ����������� �������� ��� �������
     * @throws IllegalArgumentException ��� ������ �����  ���������, �� ���������������� ������ �������������
     */
    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("��������� ������� ������ ���� �������� ������: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("������������ ����������� ��������: " + loadFactor);
        }

        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        entryTable = newTable(capacity);
        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
    }

    /**
     * ����� equal ��������� ��������� ���� �������� x � y.
     * ���� ������� ����� �� ������ (x == y), �� ���������� true.
     * ���� ������� �� ����� �� ������, �� ���������� ����� equals ��� ������� x, ������� ���������� ��� � �������� y.
     * ���� equals ���������� true, �� ����� equal ����� ���������� true. � ��������� ������ ������������ false.
     *
     * @param x ������ ���� Object
     * @param y ������ ���� Object
     * @return boolean ���������� true ��� falls ����� ��������� ��������
     */
    private static boolean equal(Object x, Object y) {

        return x == y || x.equals(y);
    }

    /**
     * ����� hash(Object key) ������������ ��� ���������� ���-���� ����� � ���-�������.
     * �� ��������� ������ key � ���������� ��� ���-���, ������� ����� ������������ ��� ����������� �������������� �������� � �������.
     * ���-��� ����������� � ������� ������ hashCode() ������� key.
     * ����� ���������� ���-��� ������������� � ������������� ����� � ������� ������� Math.abs() ��� ��������� ������������� ��������.
     * ����� ����� ���������� �������� ���������� � ������� �������� ������ ������� �� ������� �� ����� ������� (entryTable.length), ��� ��������� ������������ �������� �� ������� ����� ����������.
     *
     * @param key ���� ���� Object
     * @return int ���������� hashcode ����� ��� ����������
     */
    final int hash(Object key) {

        return Math.abs(key.hashCode()) % entryTable.length;
    }

    /**
     * ����� getTable() ���������� ������ �������� ���� Entry<K, V>.
     * ��� ��������� �������� ������ � ���������� ��������� ������ ���-�������.
     * @return Entry<K,V></K,V>[] ���������� ������ �������� ���� Entry<K, V></K,>
     */
    private Entry<K, V>[] getTable() {

        return entryTable;
    }

    /**
     * ����� size() ������������ ��� ��������� ���������� ��������� � ���-�������.
     * ���� ������ ����� ����, �� ������������ 0. � ��������� ������ ������������ ���������� �������� �������.
     *
     * @return int ���������� ��������� � ���-�������
     */
    public int size() {
        if (size == 0) {
            return 0;
        }
        return size;
    }

    /**
     * ����� isEmpty() ������������ ��� ��������, �������� �� ��� ������� ������
     * ������������ ����� size(), ������� ��������� ���������� ��������� � ���-�������
     *
     * @return boolean ���������� true, ���� ��� ������� �����, ����� false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * ����� loadFactor() ��������� ���������� ���� �������� �������� ���� this.loadFactor � ���� Float
     *
     * @return float ���������� ������� �������� ���� loadFactor
     */
    public Float loadFactor(){
        return (float) this.loadFactor;
    }

    /**
     * ����� get(Object key) ��������� ����� �������� � ���-������� �� �����
     * ���� ���� key ����� null, ����� ���������� null.
     * ����� ����������� ���-��� ����� (int h = hash(key)) � ������������ ����� �������� � ���-������� (Entry<K, V></K,>[] tab = getTable(); Entry<K, V></K,> entry = tab[h]).
     * ����� ���������� ���������������� ����� ��������� ������� (while (entry != null)), ������� � �������� � ��������, ������ ���-���� �����.
     * ��� ������� �������� �����������, ��������� �� ��� ���-��� � ���-����� ����� (entry.hash == h) � ����� �� ���� �������� (equal(key, entry.k)).
     * ���� ��� ������� �����������, �� ������������ �������� �������� (entry.v).
     * ���� ������� � ������� ������ �� ������, ����� ���������� null.
     *
     * @param key ���� HashMap
     * @return V ���������� �������� ��������
     */
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        int h = hash(key);
        Entry<K, V>[] tab = getTable();
        Entry<K, V> entry = tab[h];
        while (entry != null) {
            if (entry.hash == h && equal(key, entry.k)) {
                return entry.v;
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * ����� getEntry ��������� ������ ���� Object � �������� ����� � ���������� ��������������� ������� �� ������� (Entry<K, V></K,>)
     * ����������� ���-��� ����� � ������� ������� hash
     * ���������� ��� ������������ ��� ������ �������� � ������� tab, ������� �������� �������� ��������� ���� Entry<K, V></K,>
     * � ������� ������ ������� � ��������, ������ ���-���� �����. ���� ����� ������� ������ (entry != null), �� ���������� �������� ������������ �����
     * �����������, ������������� �� ���� �������� ��������. ���� ��, �� ������������ ��������� �������
     * � ��������� ������, ���� ������� ������� �� ����� ���� (entry != null) � ��� ���� �� ��������� � ������ ������ (!(entry.hash == hash && equal(key, entry.k))), �� ���������� ������� � ���������� �������� (entry = entry.next)
     * ������� ����������� �� ��� ���, ���� �� ����� ������ ���������� ������� ��� ���� �� ����� ��������� ����� ������ (entry == null)
     * @param key ���� HashMap
     * @return entry ���������� ��������� �������
     */

    private Entry<K, V> getEntry(Object key) {
        int hash = hash(key);
        Entry<K, V>[] tab = getTable();
        Entry<K, V> entry = tab[hash];
        while (entry != null && !(entry.hash == hash && equal(key, entry.k))) {
            entry = entry.next;
        }
        return entry;
    }

    /**
     * ����� getEntry ������������ ��� ��������� �������� �� �����, � ���� ��� �������� �� ����� null, �� ���� ��������� �������������� � ��� �������
     *
     * @param key ���� HashMap
     * @return boolean ������� ���������� true, ���� ���� ������� � ��� �������, � false � ��������� ������
     */
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    /**
     * ����� put ��������� ���� ����-�������� � HashMap
     * ���� ����������� ���� ����� null, �� ������������� ���������� RuntimeException
     * ��������� ���������� �� ���� � HashMap, ���� ���, �� ������������ ������ �� ���������, � ��������� ������,�������� ������ �������� �����
     *
     * @param key   ���� HashMap
     * @param value �������� HashMap
     * @return the v
     * @throws RuntimeException ��������� �� ������ � ����, ������� ���������� ���������
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new RuntimeException("Key ����� ����: " + key);
        }
        int hash = hash(key);
        Entry<K, V>[] tab = getTable();

        for (Entry<K, V> entry = tab[hash]; entry != null; entry = entry.next) {
            if (hash == entry.hash && equal(key, entry.k)) {
                V oldValue = entry.v;
                if (value != oldValue) {
                    entry.v = value;
                }
                return oldValue;
            }
        }

        Entry<K, V> entry = tab[hash];
        tab[hash] = new Entry<>(hash, key, value, entry);

        if (++size >= threshold) {
            resize(tab.length * 2);
        }

        return null;
    }

    /**
     * ����� resize �������� ������ ��� �������
     * @param i ���������� ���������
     */
    private void resize(int i) {
    }

    /**
     * ����� putAll ��������� � �������� ��������� m ������ ���� MyHashMap, ������� �������� ����������� K � V
     * ������ ������ ���������� �������� �� ������� table, ������� �������� ������ ���� Entry<K, V></K,>
     * ��� ������ ������ ���������� ����� put, ������� ��������� ���� ����-�������� � ������� ��������� MyHashMap
     * ��� ��������� ����������� ��� �������� �� m � ������� ��������� MyHashMap
     *
     * @param m ������ ���� MyHashMap
     */
    public void putAll(MyHashMap<? extends K, ? extends V> m) {
        Entry<K, V>[] table = getTable();
        for (Entry<K, V> entry : table) {
            put(entry.getKey(), entry.getValue());
        }
    }


    /**
     * ����� remove() �� �������� �������� � �������� ������ key �� ��������� ������, ���������� �� ���-�������.
     * ����� ������� ��������� ���-�������� ����� (hash(key)), ����� ���� ������� � ������� (getTable()), ��������� ��� ���-�������� ��� ����������� ���������� �������
     * ����� ���������� ����� ��������� �������, ������� � ���������� �������� (prev), ���� �� ����� ������ ������� � ����������� ������ (equal(key, entry.k))
     * ���� ����� ������� ������, �� �� ��������� �� ������� � ������������ ��� �������� (entry.v)
     * � ��������� ������, ���� ������� �� ������, ����� ���������� null
     *
     * @param key ���� HashMap
     * @return V �������� HashMap
     */
    public V remove(Object key) {
        int hash = hash(key);
        Entry<K, V>[] tab = getTable();
        Entry<K, V> prev = tab[hash];
        Entry<K, V> entry = prev;

        while (entry != null) {
            Entry<K, V> next = entry.next;
            if (hash == entry.hash && equal(key, entry.k)) {
                size--;
                if (prev == entry) {
                    tab[hash] = next;
                } else {
                    prev.next = next;
                }
                return entry.v;
            }
            prev = entry;
            entry = next;
        }

        return null;
    }


    /**
     * ����� keySet() ���������� ����� ������ (��� Set<K></K>), ������������ � ������� Entry<K, V></K,>[] table, ������� ���������� ����� ����� ������ getTable()
     * ����� ���������� ��� �������� ������� table � ��������� ����� (����� getKey()) ���� ��-null ��������� � ����� HashSet keyS
     * � ����� ����� ���������� ��������� HashSet � �������
     *
     * @return Set<K>  HashSet � �������
     */
    public Set<K> keySet() {
        Set<K> keyS = new HashSet<>();
        Entry<K, V>[] table = getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                keyS.add(table[i].getKey());
            }
        }

        return keyS;
    }

    /**
     * ����� entrySet ���������� ����� ���� ��������� ����������� � ���� �������� Map.Entry<K, V></K,>
     * ����� ���������� ������ Entry<K, V></K,>[] table, ���������� �� ������ getTable, � ��������� ��� �������� ������� � ����� HashSetentrySet, ���� ��� �� ����� null
     * ����� ����� ���������� ��������� HashSet
     *
     * @return Set<Entry < K, V>> ���������� ��������� HashSet
     */
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();

        Entry<K, V>[] table = getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                entrySet.add(table[i]);
            }
        }

        return entrySet;
    }

    /**
     * ����� values ���������� ��������� ���� �������� �����������
     * ����� ���������� ������ Entry<K, V></K,> table, ���������� �� ������ getTable, � ��������� �������� ��������� ������� � ����� HashSet values, ���� ��������������� �������� ������� �� ����� null
     * ����� ����� ���������� ��������� HashSet
     *
     * @return Collection<V>  ���������� ��������� HashSet �� ���������� ��� �������
     */
    public Collection<V> values() {
        Set<V> values = new HashSet<>();

        Entry<K, V>[] table = getTable();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                values.add(table[i].getValue());
            }
        }

        return values;
    }

    /**
     * ����� Entry �������� ����������� ��������� ������� ������ HashMap
     * �� ������������ ����� ���� � ��������� ������ ���-�������, ������������ ��� �������� ��������� � HashMap
     * ������ ��������� ������ Entry �������� ��������� ����:
     * k - ���� ��������, ������� �������� � ���� ����
     * v - �������� ��������, ������� ������������� ����� k
     * hash - ���-��� ����� k, ������� ������������ ��� ����������� ����� ���� � ���-�������
     * next - ������ �� ��������� ���� � ��������� ������, ������� ����������� � ������ �������� �����
     * ����������� ������ ��������� ������ ���������: ���-��� �����, ��� ����, �������� � ������ �� ��������� ����
     * ��� ��������� ������������ ��� ������������� ��������������� ����� ���������� ������
     *
     * @param <K></K> ���� ��������
     * @param <V></V> �������� ��������
     */
    static class Entry<K, V> {
        private K k;
        private V v;
        /**
         * The Hash.
         */
        final int hash;
        private Entry<K, V> next;

        /**
         * Instantiates a new Entry.
         *
         * @param hash the hash
         * @param k    the k
         * @param v    the v
         * @param next the next
         */
        public Entry(int hash, K k, V v, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
            this.hash = hash;
        }

        /**
         * Gets key.
         *
         * @return the key
         */
        public K getKey() {
            return k;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public V getValue() {
            return v;
        }

        /**
         * ��������������� ������ equals(), ������� ��������� ��������� ���� ��������
         * �� ������� ���������, �������� �� ������� ������ � ���������� ������ ����� � ��� �� ��������, ��������� true � ���� ������
         * ����� �� ���������, �������� �� ���������� ������ null ��� ����� �� �� ������ �����, ��� ������� ������, ��������� false � ���� �������
         * ���� ��� ��� ������� �� �����������, �� ��������������, ��� ���������� ������ �������� ����������� ������ Entry, � ���������� ��������� ������ � �������� ����� ��������
         * ���� ����� ����� ��� ���� �������� ������� ����� ����� ����������� �������, � �������� ����� ��� �������� �������� ������� ����� �������� ����������� �������, �� ������������ true
         * � ��������� ������ ������������ false.
         * @param object ������ ���� Object
         * @return boolean ���������� true ��� false
         */
        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass().getName() != object.getClass().getName()) {
                return false;
            }
            Entry<K, V> entry = (Entry<K, V>) object;
            K k1 = getKey();
            Object k2 = entry.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                V v1 = getValue();
                Object v2 = entry.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2))) {
                    return true;
                }
            }
            return false;
        }

        /**
         * ��������������� ������ hashcode(), ������� ���������� ���������� ����� ����� ��� ������� ������� ������
         * ���������������� ���������� result ��������� 1
         * ����� ����������� ���-��� ���� k � ����������� � result � �������������� ��������� 31. ���� ���� k ����� null, �� � result ����������� 0
         * ���������� ����������� ���-��� ���� v � ����������� � result � �������������� ��������� 31. ���� ���� v ����� null, �� � result ����������� 0
         * ������������ �������� �������� result
         * ������������ ������������ ���-���� ��� ������� ������� ������, �������� �������� ��� ����� k � v
         * @return int ���������� hashCode ������� ������
         */
        @Override
        public int hashCode() {
            K k = getKey();
            V v = getValue();

            int result = 1;
            final int prime = 31;
            result = prime * result + ((k == null) ? 0 : k.hashCode());
            result = prime * result + ((v == null) ? 0 : v.hashCode());

            return result;
        }

        /**
         * ��������������� ������ toString(), ������� ���������� ������������ ����� � �������� ��������, ���������� ������� " => "
         * ��� ��������� ������ �������� ���������� �������� ��� ������ ���� �����
         * @return String ���������� ������ � ������ � ��������� ��������
         */
        @Override
        public String toString() {
            return getKey() + " => " + getValue();
        }
    }

}
