package com.hms;

/**
 * Created by rajive on 4/24/15.
 */
public class ValueHolder {

    private String key;
    private String value;

    public ValueHolder(String key, String value){
        this.key= key;
                this.value=value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
