package io.wany.cherry.amethyst.json;

import java.util.HashMap;

public class JSONObject {

  private final HashMap<String, Object> object = new HashMap<>();

  public JSONObject() {}

  public void set(String key, Object value) {
    if (value == null) {
      this.object.remove(key);
    }
    else {
      this.object.put(key, value);
    }
  }

  public JSONObject getJSONObject(String key) {
    Object value = this.object.get(key);
    if (value instanceof JSONObject obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public JSONArray getJSONArray(String key) {
    Object value = this.object.get(key);
    if (value instanceof JSONArray obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public String getString(String key) {
    Object value = this.object.get(key);
    if (value instanceof String obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public Boolean getBoolean(String key) {
    Object value = this.object.get(key);
    if (value instanceof Boolean obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public Integer getInteger(String key) {
    Object value = this.object.get(key);
    if (value instanceof Integer obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public Double getDouble(String key) {
    Object value = this.object.get(key);
    if (value instanceof Double obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public Long getLong(String key) {
    Object value = this.object.get(key);
    if (value instanceof Long obj) {
      return obj;
    }
    else {
      return null;
    }
  }

  public Float getFloat(String key) {
    Object value = this.object.get(key);
    if (value instanceof Float obj) {
      return obj;
    }
    else {
      return null;
    }
  }

}
