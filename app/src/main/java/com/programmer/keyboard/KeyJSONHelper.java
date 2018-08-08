package com.programmer.keyboard;

import android.util.JsonReader;

import com.programmer.keyboard.model.ConfigProperties;
import com.programmer.keyboard.model.ConfigSuggestions;
import com.programmer.keyboard.model.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prashanthramakrishnan on 08/08/18.
 */

public class KeyJSONHelper {

    private static final String ID = "id";
    private static final String PROPERTIES = "properties";
    private static final String TEXT = "text";
    private static final String CURSOR = "cursor";
    private static final String SUGGESTIONS = "suggestions";
    private static final String DISPLAY = "display";
    private static final String KEYCODE = "keyCode";

    public static Map<String, Configuration> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readKeys(reader);
        } finally {
            reader.close();
        }
    }

    private static Map<String, Configuration> readKeys(JsonReader reader) throws IOException {
        Map<String, Configuration> map = new HashMap<>();
        reader.beginArray();
        while (reader.hasNext()) {
            Configuration config = readMessage(reader);
            map.put(config.getId(), config);
        }
        reader.endArray();
        return map;
    }

    private static Configuration readMessage(JsonReader reader) throws IOException {
        Configuration config = new Configuration();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(ID)) {
                config.setId(reader.nextString());
            } else if (name.equals(PROPERTIES)) {
                config.setProperties(readProperties(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }

    private static ConfigProperties readProperties(JsonReader reader) throws IOException {
        ConfigProperties configProperties = new ConfigProperties();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(TEXT)) {
                configProperties.setText(reader.nextString());
            } else if (name.equals(CURSOR)) {
                configProperties.setCursor(reader.nextString());
            } else if (name.equals(SUGGESTIONS)) {
                configProperties.setSuggestions(readConfigSuggestionList(reader));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return configProperties;
    }

    private static List<ConfigSuggestions> readConfigSuggestionList(JsonReader reader) throws IOException {
        List<ConfigSuggestions> configSuggestionsList = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            configSuggestionsList.add(readConfigSuggestions(reader));
        }
        reader.endArray();
        return configSuggestionsList;
    }

    private static ConfigSuggestions readConfigSuggestions(JsonReader reader) throws IOException {
        ConfigSuggestions configSuggestions = new ConfigSuggestions();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(DISPLAY)) {
                configSuggestions.setDisplay(reader.nextString());
            } else if (name.equals(KEYCODE)) {
                configSuggestions.setKeyCode(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return configSuggestions;
    }
}
