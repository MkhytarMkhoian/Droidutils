package com.droidutils.jsonparser;

import android.util.JsonReader;
import android.util.JsonToken;

import com.droidutils.jsonparser.annotation.JsonKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

        T responseObject = responseType.newInstance();
        Iterator jsonKeysIterator = json.keys();
        List<String> jsonKeys = new ArrayList<String>();

        while (jsonKeysIterator.hasNext()) {
            String key = (String) jsonKeysIterator.next();
            jsonKeys.add(key);
        }

        Field[] fields = responseObject.getClass().getDeclaredFields();
        Map<String, Field> responseObjKeys = new HashMap<String, Field>(fields.length);

        for (Field field : fields) {
            JsonKey key = field.getAnnotation(JsonKey.class);
            if (key != null) {
                field.setAccessible(true);
                responseObjKeys.put(key.value(), field);

                if (jsonKeys.contains(key.value())) {
                    readRecursively(json.get(key.value()), responseObjKeys, key.value(), responseObject);
                }
            }
        }
        return responseObject;
    }

    private <T> void readRecursively(Object object, Map<String, Field> fields, String key, T responseObject) throws Exception {

       if (object instanceof JSONObject) {

            Field field = fields.get(key);

        } else if (object instanceof JSONArray) {

            Field field = fields.get(key);
            JSONArray jsonArray = (JSONArray) object;

            if (Collection.class.isAssignableFrom(field.getType())) {
                Type type = field.getGenericType();
                if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
                    ParameterizedType type2 = (ParameterizedType) type;
                    Type typeArgument = type2.getActualTypeArguments()[0];
                    Class<?> typeArgClass = (Class<?>) typeArgument;

                    Collection collection = new ArrayList();
                    if (Number.class.isAssignableFrom(typeArgClass)
                            || String.class.isAssignableFrom(typeArgClass)
                            || Character.class.isAssignableFrom(typeArgClass)){

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

                if (componentClass.isPrimitive() || String.class.isAssignableFrom(componentClass)){
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
            fields.get(key).set(responseObject, object);

        } else if (object instanceof Boolean) {
            fields.get(key).setBoolean(responseObject, (Boolean) object);

        } else if (object instanceof Integer) {
            fields.get(key).setInt(responseObject, (Integer) object);

        } else if (object instanceof Double) {
            fields.get(key).setDouble(responseObject, (Double) object);

        } else if (object instanceof Long) {
            fields.get(key).setLong(responseObject, (Long) object);

        } else {
            throw new RuntimeException("Unexpected readObject value: " + object);
        }
    }

    private Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

    public <T> T get(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }

    public <T> void put(Class<T> clazz, T favorite) {
        map.put(clazz, favorite);
    }

    //////////

    public <T> T readJson(InputStream in, Class<T> responseType) throws Exception {

        T response = responseType.newInstance();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        startReadJson(reader, response);

        return response;
    }

    private <T> void startReadJson(JsonReader reader, T response) throws Exception {

        Field[] fields = response.getClass().getDeclaredFields();
        Map<String, Field> keysMap = new HashMap<String, Field>(fields.length);

        for (Field field : fields) {
            JsonKey key = field.getAnnotation(JsonKey.class);
            if (key != null) {
                field.setAccessible(true);
                keysMap.put(key.value(), field);

                if (!field.getType().isPrimitive()
                        && !Collection.class.isAssignableFrom(field.getType())
                        && !Map.class.isAssignableFrom(field.getType())) {

                    field.set(response, field.getType().newInstance());
                }

            }
        }
        readRecursivelyJsonObject(reader, keysMap, response, null);
    }

    private <T> void readRecursivelyJsonObject(JsonReader reader, Map<String, Field> fields, T response, Field field) throws Exception {
        JsonToken jsonToken = reader.peek();

        switch (jsonToken) {

            case BEGIN_ARRAY:
                if (field == null) {
                    reader.skipValue();
                } else {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        readRecursivelyJsonObject(reader, fields, response, field);
                    }
                    reader.endArray();
                }
                break;
            case BEGIN_OBJECT:

                reader.beginObject();
                while (reader.hasNext()) {
                    readRecursivelyJsonObject(reader, fields, response, field);
                }
                reader.endObject();

                break;
            case BOOLEAN:
                if (field == null) {
                    reader.skipValue();
                } else {
                    field.setBoolean(response, reader.nextBoolean());
                }

                break;
            case END_ARRAY:

                break;
            case END_DOCUMENT:

                break;
            case END_OBJECT:

                break;
            case NAME:
                field = fields.get(reader.nextName());

                if (Collection.class.isAssignableFrom(field.getType())) {
                    Type type = field.getGenericType();
                    if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
                        ParameterizedType type2 = (ParameterizedType) type;
                        Type typeArgument = type2.getActualTypeArguments()[0];
                        Class typeArgClass = (Class) typeArgument;

                        startReadJson(reader, typeArgClass.newInstance());
                    }
                } else {
                    readRecursivelyJsonObject(reader, fields, response, field);
                }
                break;
            case NULL:
                reader.nextNull();
                break;
            case NUMBER:

                if (field == null) {
                    reader.skipValue();
                } else {
                    String value = reader.nextString();
                    Class<?> type = field.getType();

                    if (type == double.class) {
                        field.setDouble(response, Double.valueOf(value));
                    } else if (type == int.class) {
                        field.setInt(response, Integer.valueOf(value));
                    } else if (type == long.class) {
                        field.setLong(response, Long.valueOf(value));
                    }
                }
                break;
            case STRING:
                if (field == null) {
                    reader.skipValue();
                } else {
                    String s = reader.nextString();
                    field.set(response, s);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    ////////

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
            } else {
//                jsonStringer.endArray();
            }
        } else {
            jsonStringer.endObject(); // TO - DO

//            if (Collection.class.isAssignableFrom(mGlobal.getClass())) {
//                if (mGlobal.getClass().getDeclaredFields().length > 0){
//                    jsonStringer.endObject();
//                } else {
//                   // jsonStringer.endArray();
//                }
//            }
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
