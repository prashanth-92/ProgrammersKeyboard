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

public class ProgrammerKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private final String KEY_PREFIX = "c";
    private final int LEFT_KEYCODE = 901;
    private final int UP_KEYCODE = 902;
    private final int DOWN_KEYCODE = 903;
    private final int RIGHT_KEYCODE = 904;
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
                int resId = getResourceId(primaryCode, KEY_PREFIX);
                /* Get code for key and position to move the cursor after code is pasted */
                String[] res = getResources().getStringArray(resId);
                String code = res[1];
                Log.d("Code", code);
                inputConnection.commitText(code, 1);
                int pos = Integer.parseInt(res[0]);
                Log.d("Cursor moved", String.valueOf(pos));
                /*Position the cursor for user's input */
                moveLeftNTimes(pos, inputConnection);
        }
    }

    private void moveLeftNTimes(int N, InputConnection inputConnection) {
        for (int i = 0; i < N; i++) {
            moveLeft(inputConnection);
        }
    }

    private int getResourceId(int primaryCode, String prefix) {
        Log.d("Key code", String.valueOf(primaryCode));
        return getId(prefix + primaryCode, R.array.class);
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
