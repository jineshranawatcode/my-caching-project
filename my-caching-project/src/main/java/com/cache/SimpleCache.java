package com.cache;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K,V> {
    private final Map<K,V> cache;

    public SimpleCache(){
        this.cache = new HashMap<>();
    }
    public void put(K key, V value){
        cache.put(key, value);
    }
    public V get(K key){
        return cache.get(key);
    }
    public void remove(K key){
        cache.remove(key);
    }
    public void clear(){
        cache.clear();
    }

    public int size(){
        return cache.size();
    }
    public static void main(String[] args) {
        SimpleCache<String, String> cache = new SimpleCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        System.out.println(cache.get("key1"));
        cache.remove("key2");
        System.out.println(cache.size());
        cache.clear();
        System.out.println(cache.size());
        
    }    
}
