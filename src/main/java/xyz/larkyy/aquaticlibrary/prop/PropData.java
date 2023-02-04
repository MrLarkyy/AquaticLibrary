package xyz.larkyy.aquaticlibrary.prop;

import java.util.HashMap;
import java.util.Map;

public class PropData {

    private final Map<String,String> data;

    public PropData(Map<String,String> data) {
        this.data = data;
    }

    public PropData() {
        this.data = new HashMap<>();
    }

    public Map<String, String> getData() {
        return data;
    }

    public void addData(String key, String value) {
        data.put(key,value);
    }

    public void removeData(String key) {
        data.remove(key);
    }

    public boolean containsData(String key) {
        return data.containsKey(key);
    }

    public String getData(String str) {
        return data.get(str);
    }

}
