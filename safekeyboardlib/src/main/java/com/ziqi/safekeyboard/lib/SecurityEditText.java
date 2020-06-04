package com.ziqi.safekeyboard.lib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.lang.reflect.Method;

/**
 * SecurityEditText
 *
 * @author yidong (onlyloveyd@gmaill.com)
 * @date 2018/6/15 08:29
 */
public class SecurityEditText extends NullMenuEditText {
    private KeyboardDialog dialog;

    public SecurityEditText(Context context) {
        this(context, null);
    }

    public SecurityEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public SecurityEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SecurityEditText);
        a.recycle();
        initialize();
    }

    private void initialize() {
        setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setShowSoftInputOnFocus(false);
        } else {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            hideSystemKeyboard();
            showSoftInput();
        } else {
            hideSoftKeyboard();
        }
    }

    @Override
    public boolean performClick() {
        if (this.isFocused()) {
            hideSystemKeyboard();
            showSoftInput();
        }
        return false;
    }

    private void hideSystemKeyboard() {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    private void showSoftInput() {
        if (dialog == null) {
            dialog = KeyboardDialog.create(getContext());
        }
        dialog.setTargetEditText(this);
        dialog.show();
        if (listener != null) {
            listener.keyBoardShow();
        }
    }

    private void hideSoftKeyboard() {
        if (dialog != null) {
            dialog.dismiss();
            if (listener != null) {
                listener.keyBoardHide();
            }
        }
    }

    public boolean isShowSoftKeyboard() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.isFocused()) {
            hideSystemKeyboard();
            showSoftInput();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isFocused()) {
            hideSoftKeyboard();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus && hasFocus()) {
            //showSoftInput();
            hideSystemKeyboard();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                if (listener != null) {
                    listener.keyBoardHide();
                }
                return true;
            } else {
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private OnSoftKeyBoardChangeListener listener;

    /**
     * 监听键盘是否弹出来
     *
     * @param listener
     */
    public void setOnSoftKeyBoardChangeListener(OnSoftKeyBoardChangeListener listener) {
        this.listener = null;
        this.listener = listener;
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow();

        void keyBoardHide();
    }
}
