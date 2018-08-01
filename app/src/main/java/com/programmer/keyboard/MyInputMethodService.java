package com.programmer.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.lang.reflect.Field;

/**
 * Created by prashanthramakrishnan on 27/07/18.
 */

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private final String KEY_PREFIX = "c";
    private final String KEY_CUR_PREFIX = "cur";
    private KeyboardView keyboardView;
    private Keyboard keyboard;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keys_layout);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
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
        if (inputConnection != null) {
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
                case 901:
                    moveLeft(inputConnection);
                    break;
                case 902:
                    moveUp(inputConnection);
                    break;
                case 903:
                    moveDown(inputConnection);
                    break;
                case 904:
                    moveRight(inputConnection);
                    break;
                default:
                    String textToPrint = getText(getResourceId(primaryCode, KEY_PREFIX)).toString();
                    Log.d("code", textToPrint);
                    inputConnection.commitText(textToPrint, 1);
                    int pos = Integer.parseInt(getText(getResourceId(primaryCode, KEY_CUR_PREFIX)).toString());
                    Log.d("Cursor moved", String.valueOf(pos));
                    moveLeftNTimes(pos, inputConnection);
            }
        }
    }

    private void moveLeftNTimes(int N, InputConnection inputConnection) {
        for (int i = 0; i < N; i++) {
            moveLeft(inputConnection);
        }
    }

    private int getResourceId(int primaryCode, String prefix) {
        Log.d("key code", String.valueOf(primaryCode));
        return getId(prefix + primaryCode, R.string.class);
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

    private int getId(String resourceName, Class<?> c) {
        Field idField = null;
        int id = 0;
        try {
            idField = c.getDeclaredField(resourceName);
            id = idField.getInt(idField);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        }
        return id;
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
}
