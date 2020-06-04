package com.ziqi.safekeyboard.lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.lang.ref.WeakReference;

public class KeyboardDialog extends Dialog {

    private WeakReference<SecurityEditText> mTargetEditText;

    public KeyboardDialog(Context context) {
        super(context, R.style.NoFrameDialog);
    }

    public static KeyboardDialog create(Context context) {
        return new KeyboardDialog(context);
    }

    public void setTargetEditText(SecurityEditText securityEditText) {
        if (this.mTargetEditText == null || this.mTargetEditText.get() == null) {
            this.mTargetEditText = new WeakReference<>(securityEditText);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_dialog_keyboard);
        initAttribute();
        initKeyboards();
        initKeyboardChooser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            setCanceledOnTouchOutside(false);
            window.setAttributes(layoutParams);
            window.setWindowAnimations(R.style.KeyboardDialogAnimation);
        }
    }

    private void initAttribute() {
    }

    private void initKeyboards() {
    }

    private void initKeyboardChooser() {

    }

    private void hideKeyboard() {
        dismiss();
        hideSystemKeyboard();
    }

    private void hideSystemKeyboard() {
        InputMethodManager manager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            if (mTargetEditText.get() != null) {
                manager.hideSoftInputFromWindow(mTargetEditText.get().getWindowToken(), 0);
            }
        }
    }

}
