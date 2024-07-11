package com.zharnikova.example;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * <h1>Создание и реализация своего HashMap</h1>
 * Реализует методы put(), get(), remove()
 * создание класса MyHashMap<K, V></K,V>, который реализует интерфейс Serializable это означает, что экземпляры этого класса могут быть сохранены в поток байтов и затем восстановлены обратно в объект без потери данных.
 * Сериализация позволяет сохранять состояние объекта и передавать его по сети или сохранять в файл для последующего использования.
 * @param <K></K> ключ HashMap
 * @param <V></V> значение HashMap
 * @author Zharnikova Kseniya
 * @version 1.0
 */
public class MyHashMap<K,V> implements Serializable {
    Locale locale = new Locale("ru", "Russia");
    /**
     * уникальный идентификатор версии для класса.
     * Значение 1L указывает на то, что это первая версия класса
     */
    private static final long serialVersionUID = 1L;

    /**
     * емкость хеш-таблицы по умолчанию (2^4==16)
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * максимально возможная емкость хеш-таблицы = 1073741824
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * коэффициент загрузки хэш таблицы, используемый по умолчанию
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * массив типа Entry[], который является хранилищем ссылок на списки (цепочки) значений
     *  @param <K></K> ключ HshMap
     *  @param <V></V> значение HashMap
     */
    private Entry<K, V>[] entryTable;

    /**
     * количество элементов HashMap-а
     */
    private int size;

    /**
     * коэффициент загрузки, используемый по умолчанию
     */
    private final float loadFactor;

    /**
     * предельное количество элементов, при достижении которого, размер хэш-таблицы увеличивается вдвое (capacity * loadFactor).
     */
    private int threshold;



    /**
     * Этот метод используется для создания массива типа Entry с  элементами различного типа данных
     * @param n это размер массива Entry<K,V></K,V>[n]
     * @return Entry<?, ?></?,>[n] врозвращает массив Entry<K,V></K,V>[n]
     */
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] newTable(int n) {
        return (Entry<K, V>[]) new Entry<?, ?>[n];
    }


    /**
     * создается конструктор класса MyHashMap, который вызывает другой конструктор с параметрами DEFAULT_INITIAL_CAPACITY и DEFAULT_LOAD_FACTOR
     * конструктор Hashmap по умолчанию (capacity=16, load factor=0.75)
     */
    public MyHashMap() {

        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * конструктор класса MyHashmap с указанием емкости
     * Этот конструктор вызывает другой конструктор с двумя параметрами, передавая в него значение initialCapacity и значение константы DEFAULT_LOAD_FACTOR.
     * Это позволяет создать экземпляр MyHashMap с заданной начальной ёмкостью, используя при этом стандартное значение коэффициента загрузки
     *
     * @param initialCapacity емкость хеш-таблицы
     */
    public MyHashMap(int initialCapacity) {

        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * конструктор класса MyHashMap, который принимает два параметра: initialCapacity и loadFactor
     * Конструктор проверяет корректность входных параметров:
     * Если initialCapacity меньше нуля, генерируется исключение IllegalArgumentException с сообщением о том, что начальная ёмкость должна быть степенью двойки.
     * Если initialCapacity больше максимально допустимой ёмкости, она устанавливается равной максимально допустимой ёмкости.
     * Если loadFactor меньше или равен нулю или является NaN, генерируется исключение IllegalArgumentException с сообщением о недопустимом коэффициенте нагрузки.
     * На каждой итерации цикла значение capacity удваивается путём сдвига влево на один бит
     * создание таблицы entryTable с заданной емкостью,
     * присваивание loadFactor, вычисление порогового значения threshold
     *
     * @param initialCapacity емкость хеш-таблицы
     * @param loadFactor      коэффициент загрузки хэш таблицы
     * @throws IllegalArgumentException При ошибки ввода  аргумента, не соответствующего логике использования
     */
    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Начальная емкость должна быть степенью двойки: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Недопустимый коэффициент нагрузки: " + loadFactor);
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
     * Метод equal проверяет равенство двух объектов x и y.
     * Если объекты равны по ссылке (x == y), то возвращает true.
     * Если объекты не равны по ссылке, то вызывается метод equals для объекта x, который сравнивает его с объектом y.
     * Если equals возвращает true, то метод equal также возвращает true. В противном случае возвращается false.
     *
     * @param x объект типа Object
     * @param y объект типа Object
     * @return boolean возвращает true или falls после сравнения объектов
     */
    private static boolean equal(Object x, Object y) {

        return x == y || x.equals(y);
    }

    /**
     * Метод hash(Object key) используется для вычисления хэш-кода ключа в хеш-таблице.
     * Он принимает объект key и возвращает его хэш-код, который затем используется для определения местоположения элемента в таблице.
     * Хэш-код вычисляется с помощью метода hashCode() объекта key.
     * Затем полученный хэш-код преобразуется в положительное число с помощью функции Math.abs() для избежания отрицательных значений.
     * После этого полученное значение хэшируется с помощью операции взятия остатка от деления на длину таблицы (entryTable.length), что позволяет распределить элементы по таблице более равномерно.
     *
     * @param key ключ типа Object
     * @return int возвращает hashcode после его вычисления
     */
    final int hash(Object key) {

        return Math.abs(key.hashCode()) % entryTable.length;
    }

    /**
     * Метод getTable() возвращает массив объектов типа Entry<K, V>.
     * Это позволяет получить доступ к внутренней структуре данных хеш-таблицы.
     * @return Entry<K,V></K,V>[] возвращает массив объектов типа Entry<K, V></K,>
     */
    private Entry<K, V>[] getTable() {

        return entryTable;
    }

    /**
     * Метод size() предназначен для получения количества элементов в хеш-таблице.
     * Если размер равен нулю, то возвращается 0. В противном случае возвращается актуальное значение размера.
     *
     * @return int количество элементов в хэш-таблице
     */
    public int size() {
        if (size == 0) {
            return 0;
        }
        return size;
    }

    /**
     * Метод isEmpty() предназначен для проверки, является ли хэш таблица пустой
     * используется метод size(), который возращает количество элементов в хэш-таблице
     *
     * @return boolean возвращает true, если хэш таблица пуста, иначе false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Метод loadFactor() выполняет приведение типа текущего значения поля this.loadFactor к типу Float
     *
     * @return float возвращает текущее значение поля loadFactor
     */
    public Float loadFactor(){
        return (float) this.loadFactor;
    }

    /**
     * метод get(Object key) реализует поиск элемента в хеш-таблице по ключу
     * Если ключ key равен null, метод возвращает null.
     * Иначе вычисляется хеш-код ключа (int h = hash(key)) и производится поиск элемента в хеш-таблице (Entry<K, V></K,>[] tab = getTable(); Entry<K, V></K,> entry = tab[h]).
     * Далее происходит последовательный обход элементов таблицы (while (entry != null)), начиная с элемента с индексом, равным хеш-коду ключа.
     * Для каждого элемента проверяется, совпадает ли его хеш-код с хеш-кодом ключа (entry.hash == h) и равен ли ключ элементу (equal(key, entry.k)).
     * Если оба условия выполняются, то возвращается значение элемента (entry.v).
     * Если элемент с искомым ключом не найден, метод возвращает null.
     *
     * @param key ключ HashMap
     * @return V возвращает значение элемента
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
     * Метод getEntry принимает объект типа Object в качестве ключа и возвращает соответствующий элемент из таблицы (Entry<K, V></K,>)
     * Вычисляется хэш-код ключа с помощью функции hash
     * Полученный хэш используется для поиска элемента в таблице tab, которая является массивом элементов типа Entry<K, V></K,>
     * В таблице ищется элемент с индексом, равным хэш-коду ключа. Если такой элемент найден (entry != null), то начинается проверка соответствия ключа
     * Проверяется, соответствует ли ключ искомому элементу. Если да, то возвращается найденный элемент
     * В противном случае, если текущий элемент не равен нулю (entry != null) и его ключ не совпадает с ключом поиска (!(entry.hash == hash && equal(key, entry.k))), то происходит переход к следующему элементу (entry = entry.next)
     * Процесс повторяется до тех пор, пока не будет найден подходящий элемент или пока не будет достигнут конец списка (entry == null)
     * @param key ключ HashMap
     * @return entry возвращает найденный элемент
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
     * Метод getEntry используется для получения значения по ключу, и если это значение не равно null, то ключ считается присутствующим в хэш таблице
     *
     * @param key ключ HashMap
     * @return boolean который возвращает true, если ключ имеется в хэш таблице, и false в противном случае
     */
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    /**
     * Метод put добавляет пару ключ-значение в HashMap
     * Если добавляемый ключ равен null, то выбрасывается исключение RuntimeException
     * Проверяет существует ли ключ в HashMap, если нет, то добавляетсяя вместо со значением, в противном случае,заменяет старое значение новым
     *
     * @param key   ключ HashMap
     * @param value значение HashMap
     * @return the v
     * @throws RuntimeException указывает на ошибку в коде, которую необходимо исправить
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new RuntimeException("Key равен нулю: " + key);
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
     * метод resize изменяет размер хэш таблицы
     * @param i количество элементов
     */
    private void resize(int i) {
    }

    /**
     * метод putAll принимает в качестве аргумента m объект типа MyHashMap, который является расширением K и V
     * Внутри метода происходит итерация по массиву table, который содержит записи типа Entry<K, V></K,>
     * Для каждой записи вызывается метод put, который добавляет пару ключ-значение в текущий экземпляр MyHashMap
     * Это позволяет скопировать все элементы из m в текущий экземпляр MyHashMap
     *
     * @param m объект типа MyHashMap
     */
    public void putAll(MyHashMap<? extends K, ? extends V> m) {
        Entry<K, V>[] table = getTable();
        for (Entry<K, V> entry : table) {
            put(entry.getKey(), entry.getValue());
        }
    }


    /**
     * метод remove() ля удаления элемента с заданным ключом key из структуры данных, основанной на хэш-таблице.
     * Метод сначала вычисляет хеш-значение ключа (hash(key)), затем ищет элемент в таблице (getTable()), используя это хеш-значение для определения начального индекса
     * Далее происходит обход элементов таблицы, начиная с найденного элемента (prev), пока не будет найден элемент с совпадающим ключом (equal(key, entry.k))
     * Если такой элемент найден, то он удаляется из таблицы и возвращается его значение (entry.v)
     * В противном случае, если элемент не найден, метод возвращает null
     *
     * @param key ключ HashMap
     * @return V значение HashMap
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
     * метод keySet() возвращает набор ключей (тип Set<K></K>), содержащихся в таблице Entry<K, V></K,>[] table, которая получается через вызов метода getTable()
     * Метод перебирает все элементы массива table и добавляет ключи (метод getKey()) всех не-null элементов в новый HashSet keyS
     * В конце метод возвращает созданный HashSet с ключами
     *
     * @return Set<K>  HashSet с ключами
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
     * метод entrySet возвращает набор всех элементов отображения в виде объектов Map.Entry<K, V></K,>
     * Метод использует массив Entry<K, V></K,>[] table, полученный из метода getTable, и добавляет все элементы массива в новый HashSetentrySet, если они не равны null
     * Затем метод возвращает созданный HashSet
     *
     * @return Set<Entry < K, V>> возвращает созданный HashSet
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
     * Метод values возвращает коллекцию всех значений отображения
     * Метод использует массив Entry<K, V></K,> table, полученный из метода getTable, и добавляет значения элементов массива в новый HashSet values, если соответствующие элементы массива не равны null
     * Затем метод возвращает созданный HashSet
     *
     * @return Collection<V>  возвращает созданный HashSet со значениями хэш таблицы
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
     * Класс Entry является статическим вложенным классом класса HashMap
     * Он представляет собой узел в структуре данных хэш-таблицы, используемой для хранения элементов в HashMap
     * Каждый экземпляр класса Entry содержит следующие поля:
     * k - ключ элемента, который хранится в этом узле
     * v - значение элемента, которое соответствует ключу k
     * hash - хеш-код ключа k, который используется для определения места узла в хэш-таблице
     * next - ссылка на следующий узел в связанном списке, который формируется в случае коллизии хешей
     * Конструктор класса принимает четыре аргумента: хеш-код ключа, сам ключ, значение и ссылку на следующий узел
     * Эти аргументы используются для инициализации соответствующих полей экземпляра класса
     *
     * @param <K></K> ключ элемента
     * @param <V></V> значения элемента
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
         * переопределение метода equals(), который проверяет равенство двух объектов
         * Он сначала проверяет, являются ли текущий объект и переданный объект одним и тем же объектом, возвращая true в этом случае
         * Затем он проверяет, является ли переданный объект null или имеет ли он другой класс, чем текущий объект, возвращая false в этих случаях
         * Если оба эти условия не выполняются, то предполагается, что переданный объект является экземпляром класса Entry, и происходит сравнение ключей и значений обоих объектов
         * Если ключи равны или ключ текущего объекта равен ключу переданного объекта, и значения равны или значение текущего объекта равно значению переданного объекта, то возвращается true
         * В противном случае возвращается false.
         * @param object объект типа Object
         * @return boolean возвращает true или false
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
         * переопределение метода hashcode(), который возвращает уникальное целое число для каждого объекта класса
         * Инициализируется переменная result значением 1
         * Затем вычисляется хэш-код поля k и добавляется к result с использованием множителя 31. Если поле k равно null, то к result добавляется 0
         * Аналогично вычисляется хэш-код поля v и добавляется к result с использованием множителя 31. Если поле v равно null, то к result добавляется 0
         * возвращается итоговое значение result
         * обеспечивает уникальность хэш-кода для каждого объекта класса, учитывая значения его полей k и v
         * @return int возвращает hashCode объекта класса
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
         * переопределение метода toString(), который возвращает конкатенацию ключа и значения элемента, разделённых строкой " => "
         * Это позволяет удобно выводить содержимое элемента при выводе всей карты
         * @return String возвращает строку с ключом и значением элемента
         */
        @Override
        public String toString() {
            return getKey() + " => " + getValue();
        }
    }

}
