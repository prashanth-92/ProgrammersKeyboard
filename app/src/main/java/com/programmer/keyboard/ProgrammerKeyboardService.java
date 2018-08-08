package com.programmer.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.programmer.keyboard.model.ConfigProperties;
import com.programmer.keyboard.model.ConfigSuggestions;
import com.programmer.keyboard.model.Configuration;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by prashanthramakrishnan on 27/07/18.
 */

public class ProgrammerKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private final String KEY_PREFIX = "c";
    private final int LEFT_KEYCODE = 901;
    private final int UP_KEYCODE = 902;
    private final int DOWN_KEYCODE = 903;
    private final int RIGHT_KEYCODE = 904;
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private Map<String, Configuration> configMap = null;

    @Override
    public View onCreateInputView() {
        try {
            configMap = getConfig();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keys_layout);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    private Map<String, Configuration> getConfig() throws IOException, JSONException {
        //Read keys.json file
        InputStream in = getResources().openRawResource(getResources().getIdentifier("keys", "raw", getPackageName()));
        //Convert it into config object
        Map<String, Configuration> config = KeyJSONHelper.readJsonStream(in);
        return config;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (null == inputConnection)
            return;

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
                break;
            case Keyboard.KEYCODE_DONE:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case LEFT_KEYCODE:
                moveLeft(inputConnection);
                break;
            case UP_KEYCODE:
                moveUp(inputConnection);
                break;
            case DOWN_KEYCODE:
                moveDown(inputConnection);
                break;
            case RIGHT_KEYCODE:
                moveRight(inputConnection);
                break;
            default:
                ConfigProperties configProperties = configMap.get(KEY_PREFIX + primaryCode).getProperties();
                inputConnection.commitText(configProperties.getText(), 1);
                setSuggestions(configProperties.getSuggestions());
                moveLeftNTimes(Integer.parseInt(configProperties.getCursor()), inputConnection);
        }
    }

    private void moveLeftNTimes(int N, InputConnection inputConnection) {
        for (int i = 0; i < N; i++) {
            moveLeft(inputConnection);
        }
    }

    private void moveLeft(InputConnection inputConnection) {
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT));
    }

    private void moveRight(InputConnection inputConnection) {
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT));
    }

    private void moveUp(InputConnection inputConnection) {
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP));
    }

    private void moveDown(InputConnection inputConnection) {
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN));
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }

    private void setSuggestions(List<ConfigSuggestions> configSuggestions) {
        List<Keyboard.Key> keys = keyboard.getKeys();
        for (int i = 0; i < 3; i++) {
            Keyboard.Key key = keys.get(i);
            clearSuggestion(key);
            if (configSuggestions.size() == 0)
                continue;
            ConfigSuggestions suggestion = configSuggestions.get(i);
            key.label = suggestion.getDisplay();
            key.codes = new int[]{Integer.parseInt(suggestion.getKeyCode())};
        }
        keyboardView.setKeyboard(keyboard);
    }

    private void clearSuggestion(Keyboard.Key key) {
        key.label = "";
        key.codes = new int[]{};
    }
}