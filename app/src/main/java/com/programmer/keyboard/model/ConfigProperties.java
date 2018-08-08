package com.programmer.keyboard.model;

import java.util.List;

/**
 * Created by prashanthramakrishnan on 08/08/18.
 */

public class ConfigProperties {
    List<ConfigSuggestions> suggestions;
    private String text;
    private String cursor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public List<ConfigSuggestions> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<ConfigSuggestions> suggestions) {
        this.suggestions = suggestions;
    }


}
