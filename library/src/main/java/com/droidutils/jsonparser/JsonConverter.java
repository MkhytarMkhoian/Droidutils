package com.droidutils.jsonparser;

import com.droidutils.jsonparser.annotation.JsonKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Mkhitar on 17.09.2014.
 */
public class JsonConverter<T> implements AbstractJsonConverter<T> {

    @Override
    public T readJson(JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    public String convertToJsonString(T object) throws Exception {
        JSONStringer jsonStringer = new JSONStringer();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if (Collection.class.isAssignableFrom(clazz)) {
            writeArray(((Collection) object).toArray(), jsonStringer, null);
        } else {
            jsonStringer.object();
        }

        for (Field field : fields) {
            field.setAccessible(true);

            JsonKey jsonKey = field.getAnnotation(JsonKey.class);
            if (jsonKey != null) {
                Class type = field.getType();

                if (type.isPrimitive() || String.class.isAssignableFrom(type)) {
                    jsonStringer.key(jsonKey.value()).value(field.get(object));
                } else if (type.isArray()) {
                    writeArray(field.get(object), jsonStringer, jsonKey.value());
                } else {
                    jsonStringer.key(jsonKey.value()).value(field.get(object));
                }

            }
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            jsonStringer.endArray();
        } else {
            jsonStringer.endObject();
        }
        return jsonStringer.toString();
    }

    private Object[] writeArray(Object array, JSONStringer jsonStringer, String key) throws JSONException {
        Object[] array2 = new Object[Array.getLength(array)];
        if (array2.length > 0) {
            if (key != null) {
                jsonStringer.key(key);
            }
            jsonStringer.array();
        }
        for (int i = 0; i < array2.length; i++) {
            array2[i] = Array.get(array, i);
            jsonStringer.value(array2[i]);
        }
        if (array2.length > 0) {
            jsonStringer.endArray();
        }
        return array2;
    }

    private void writeObject(JSONStringer jsonStringer, Field field, String arrayKey) throws JSONException {
        jsonStringer.object();

        jsonStringer.endObject();
    }

    private static void jsonify(Object object, JSONStringer jsonStringer) throws JSONException {
        if (object instanceof Map) {
            jsonStringer.object();
            for (String key : ((Map<String, Object>) object).keySet()) {
                jsonStringer.key(key);
                jsonify((((Map<String, Object>) object)).get(key), jsonStringer);
            }
            jsonStringer.endObject();
        } else if (object instanceof Object[]) {
            jsonStringer.array();
            for (Object o : (Object[]) object) jsonify(o, jsonStringer);
            jsonStringer.endArray();
        } else if (object instanceof Collection) {
            jsonify(((Collection) object).toArray(), jsonStringer);
        } else if (object instanceof String) {
            jsonStringer.value(object);
        } else if (object instanceof Boolean) {
            jsonStringer.value(object);
        } else if (object instanceof Integer) {
            jsonStringer.value(object);
        } else if (object instanceof Double) {
            jsonStringer.value(object);
        } else if (object instanceof Long) {
            jsonStringer.value(object);
        } else if (object == null) {
            jsonStringer.value(JSONObject.NULL);
        } else {
            throw new RuntimeException("Unexpected jsonify value: " + object);
        }
    }
}
