package com.programmer.keyboard.model;

/**
 * Created by prashanthramakrishnan on 08/08/18.
 */

public class Configuration {
    private String id;
    private ConfigProperties properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConfigProperties getProperties() {
        return properties;
    }

    public void setProperties(ConfigProperties properties) {
        this.properties = properties;
    }
}
