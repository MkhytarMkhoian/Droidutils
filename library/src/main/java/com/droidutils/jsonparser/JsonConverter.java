package com.droidutils.jsonparser;

import com.droidutils.jsonparser.annotation.JsonKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Mkhitar on 17.09.2014.
 */
public class JsonConverter {

    private Object mGlobal;

    public <T> T readJson(String response, Class<T> responseType) throws Exception {

        return readFromJson(new JSONObject(response), responseType);
    }

    private <T> T readFromJson(JSONObject json, Class<T> responseType) throws Exception {

        List<String> jsonKeys = new ArrayList<String>();
        setKeys(json, jsonKeys);

        T responseObject = responseType.newInstance();
        Field[] fields = responseObject.getClass().getDeclaredFields();
        Map<String, Field> responseObjKeys = new HashMap<String, Field>(fields.length);

        for (Field field : fields) {
            JsonKey key = field.getAnnotation(JsonKey.class);
            if (key != null) {
                field.setAccessible(true);
                responseObjKeys.put(key.value(), field);
            }
        }

        for (String key : jsonKeys) {
            read(json.get(key), responseObjKeys, key, responseObject);
        }

        return responseObject;
    }

    private <T> void read(Object object, Map<String, Field> fields, String key, T responseObject) throws Exception {

        if (object instanceof JSONObject) {

            JSONObject jsonObject = (JSONObject) object;
            Field field = fields.get(key);
            if (field == null) {

                Iterator jsonKeysIterator = jsonObject.keys();
                while (jsonKeysIterator.hasNext()) {
                    String innerKey = (String) jsonKeysIterator.next();
                    read(jsonObject.get(innerKey), fields, innerKey, responseObject);
                }
                return;
            }

            Class<?> type = field.getType();
            field.set(responseObject, readFromJson(jsonObject, type));

        } else if (object instanceof JSONArray) {

            Field field = fields.get(key);
            if (field == null) {
                return;
            }
            JSONArray jsonArray = (JSONArray) object;

            if (Collection.class.isAssignableFrom(field.getType())) {
                Type type = field.getGenericType();
                if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
                    ParameterizedType type2 = (ParameterizedType) type;
                    Type typeArgument = type2.getActualTypeArguments()[0];
                    Class<?> typeArgClass = (Class<?>) typeArgument;

                    Collection collection = getCollectionInstance(field.getType());

                    if (Number.class.isAssignableFrom(typeArgClass)
                            || String.class.isAssignableFrom(typeArgClass)
                            || Character.class.isAssignableFrom(typeArgClass)) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            collection.add(jsonArray.opt(i));
                        }
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            collection.add(readFromJson((JSONObject) jsonArray.opt(i), typeArgClass));
                        }
                    }

                    field.set(responseObject, collection);
                }
            } else if (field.getType().isArray()) {
                Class<?> type = field.getType();

                int sourceLength = jsonArray.length();
                Class<?> componentClass = type.getComponentType();
                Object result = Array.newInstance(componentClass, sourceLength);

                if (componentClass.isPrimitive() || String.class.isAssignableFrom(componentClass)) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Array.set(result, i, jsonArray.opt(i));
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Array.set(result, i, readFromJson((JSONObject) jsonArray.opt(i), componentClass));
                    }
                }

                field.set(responseObject, result);
            }

        } else if (object instanceof String) {
            if (fields.get(key) != null) {
                fields.get(key).set(responseObject, object);
            }

        } else if (object instanceof Boolean) {
            if (fields.get(key) != null) {
                fields.get(key).setBoolean(responseObject, (Boolean) object);
            }

        } else if (object instanceof Integer) {
            if (fields.get(key) != null) {
                fields.get(key).setInt(responseObject, (Integer) object);
            }

        } else if (object instanceof Double) {
            if (fields.get(key) != null) {
                fields.get(key).setDouble(responseObject, (Double) object);
            }

        } else if (object instanceof Long) {
            if (fields.get(key) != null) {
                fields.get(key).setLong(responseObject, (Long) object);
            }
        } else {
            throw new RuntimeException("Unexpected readObject value: " + object);
        }
    }

    private void setKeys(JSONObject jsonObject, List<String> keys) {

        Iterator jsonKeysIterator = jsonObject.keys();
        while (jsonKeysIterator.hasNext()) {
            String key = (String) jsonKeysIterator.next();
            keys.add(key);
        }
    }

    private Collection getCollectionInstance(Class<?> type) throws Exception {

        if (type.isInterface()) {
            if (List.class.isAssignableFrom(type)) {
                return new ArrayList();
            } else if (Set.class.isAssignableFrom(type)) {
                return new HashSet();
            } else if (Queue.class.isAssignableFrom(type)) {
                return new PriorityQueue();
            }
        } else {
            return (Collection) type.newInstance();
        }

        return null;
    }

    public String convertToJsonString(Object object) throws Exception {
        JSONStringer jsonStringer = new JSONStringer();
        mGlobal = object;
        return parseObject(object, jsonStringer);
    }

    private String parseObject(Object object, JSONStringer jsonStringer) throws Exception {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if (Collection.class.isAssignableFrom(clazz)) {

            if (fields.length > 0) {
                jsonStringer.object();
                readRecursivelyObject(object, jsonStringer, clazz.getAnnotation(JsonKey.class).value());
            } else {
                readRecursivelyObject(object, jsonStringer, "");
            }

        } else {
            jsonStringer.object();
        }

        for (Field field : fields) {
            field.setAccessible(true);

            JsonKey jsonKey = field.getAnnotation(JsonKey.class);
            if (jsonKey != null) {
                readRecursivelyObject(field.get(object), jsonStringer, jsonKey.value());
            }
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            if (fields.length > 0) {
                jsonStringer.endObject();
            }
        } else {
            jsonStringer.endObject();
        }
        return jsonStringer.toString();
    }

    private Object[] convertToObjectArray(Object array) throws JSONException {
        Object[] array2 = new Object[Array.getLength(array)];

        for (int i = 0; i < array2.length; i++) {
            array2[i] = Array.get(array, i);
        }
        return array2;
    }

    private void readRecursivelyObject(Object object, JSONStringer jsonStringer, String key) throws Exception {
        if (object instanceof Map) {

            Set<String> keys = ((Map<String, Object>) object).keySet();
            for (String key2 : keys) {
                Object o = ((Map<String, Object>) object).get(key);
                readRecursivelyObject(o, jsonStringer, key2);
            }
        } else if (object instanceof Object[]
                || object instanceof int[]
                || object instanceof double[]
                || object instanceof boolean[]
                || object instanceof short[]
                || object instanceof float[]
                || object instanceof byte[]
                || object instanceof long[]
                || object instanceof char[]) {

            if (!key.isEmpty()) {
                jsonStringer.key(key);
            }
            jsonStringer.array();

            Object[] objects = convertToObjectArray(object);
            for (Object o : objects) readRecursivelyObject(o, jsonStringer, "");
            jsonStringer.endArray();

        } else if (object instanceof Collection) {
            readRecursivelyObject(((Collection) object).toArray(), jsonStringer, key);

        } else if (object instanceof String
                || object instanceof Boolean
                || object instanceof Number
                || object instanceof Character) {

            if (!key.isEmpty()) {
                jsonStringer.key(key);
            }
            jsonStringer.value(object);

        } else if (object != null && !object.getClass().getName().equals(Object.class.getName())) {
            if (!key.isEmpty()) {
                jsonStringer.key(key);
            }
            parseObject(object, jsonStringer);

        } else if (object == null) {
            jsonStringer.key(key);
            jsonStringer.value(JSONObject.NULL);
        } else {
            throw new RuntimeException("Unexpected readObject value: " + object);
        }
    }
}
