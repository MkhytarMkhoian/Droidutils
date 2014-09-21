package com.droidutils.jsonparser;

import com.droidutils.jsonparser.annotation.JsonKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mkhitar on 17.09.2014.
 */
public class JsonConverter<T> implements AbstractJsonConverter<T> {

    private Object mGlobal;

    @Override
    public T readJson(JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    public String convertToJsonString(Object object) throws Exception {
        JSONStringer jsonStringer = new JSONStringer();
        mGlobal = object;
        return parseObject(object, jsonStringer);
    }

    private String parseObject(Object object, JSONStringer jsonStringer) throws Exception {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if (Collection.class.isAssignableFrom(clazz)) {

            if (fields.length > 0){
                jsonStringer.object();
                readObject(object, jsonStringer, clazz.getAnnotation(JsonKey.class).value());
            } else {
                readObject(object, jsonStringer, "");
            }

        } else {
            jsonStringer.object();
        }

        for (Field field : fields) {
            field.setAccessible(true);

            JsonKey jsonKey = field.getAnnotation(JsonKey.class);
            if (jsonKey != null) {
                readObject(field.get(object), jsonStringer, jsonKey.value());
            }
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            if (fields.length > 0){
                jsonStringer.endObject();
            } else {
                jsonStringer.endArray();
            }
        } else {
            jsonStringer.endObject();

            if (Collection.class.isAssignableFrom(mGlobal.getClass())) {
                if (mGlobal.getClass().getDeclaredFields().length > 0){
                    jsonStringer.endObject();
                } else {
                    jsonStringer.endArray();
                }
            }
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

    private void readObject(Object object, JSONStringer jsonStringer, String key) throws Exception {
        if (object instanceof Map) {

            Set<String> keys = ((Map<String, Object>) object).keySet();
            for (String key2 : keys) {
                Object o = ((Map<String, Object>) object).get(key);
                readObject(o, jsonStringer, key2);
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
            for (Object o : objects) readObject(o, jsonStringer, "");
            jsonStringer.endArray();

        } else if (object instanceof Collection) {
            readObject(((Collection) object).toArray(), jsonStringer, key);

        } else if (object instanceof String
                || object instanceof Boolean
                || object instanceof Number
                || object instanceof Character) {

            if (!key.isEmpty()) {
                jsonStringer.key(key);
            }
            jsonStringer.value(object);

        } else if (object != null && !object.getClass().getName().equals(Object.class.getName())) {
            if (!key.isEmpty()){
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
