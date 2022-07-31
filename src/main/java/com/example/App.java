package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class App {

    static Map<String, String> param;

    static {
        param = new HashMap<>();
        param.put("path.id1", "1");
        param.put("path.id2", "2");
        param.put("path.id3", "3");
        param.put("path.id4", "4");
        // param.put("path.id5", "5");
    }

    public static void main(String[] args) {
        String response = getResponse();

        JSONObject obj = new JSONObject(response);
        // System.out.println(obj);
        //

        Map xx = fromJSONtoMap(obj);

        updateResponseRecursively(xx);

        JSONObject ss = new JSONObject(xx);

        write(ss);
    }

    private static void updateResponseRecursively(Map<String, Object> map) {

        Set<String> keys = map.keySet();

        keys.iterator().forEachRemaining(key -> {
            Object value = map.get(key);
            if (value instanceof String) {
                if (param.containsKey(value)) {
                    map.put(key, param.get(value));
                }
            } else if (value instanceof Map) {
                updateResponseRecursively((Map) map.get(key));
            }
        });

    }

    private static String getResponse() {
        StringBuffer str = new StringBuffer();
        File file = new File("src/resources/target.json");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String s;
            while ((s = br.readLine()) != null) {
                str.append(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }

    public static Map<String, ?> fromJSONtoMap(JSONObject jsonobj) throws JSONException {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Iterator<String> keys = jsonobj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = fromJSONtoList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = fromJSONtoMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<?> fromJSONtoList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = fromJSONtoList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = fromJSONtoMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    private static void write(JSONObject obj) {
        try (FileWriter file = new FileWriter("src/resources/result.json")) {
            file.write(obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
