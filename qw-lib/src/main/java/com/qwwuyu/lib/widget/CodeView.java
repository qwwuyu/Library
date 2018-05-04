package com.qwwuyu.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 填写验证码控件
 */
public class CodeView extends LinearLayout implements View.OnClickListener {
    private static final String DEFAULT_TRANSFORMATION = "●";
    /** 边框宽度dp */
    private int lineWith = 1;
    /** 控件文字长度 */
    private int length = 4;
    /** 当前输入的文字数组 */
    private String[] pasStrArr = new String[length];
    /** 当前文字显示TextView数组 */
    private TextView[] pasTxtArr = new TextView[length];
    /** 监听删除的EditText */
    private DelListenerEditText inputView;
    /** 当内容改变时候的监听 */
    private OnPasChangeListener onPasChangeListener;
    private Paint paint = new Paint();
    private int linePadding, lineHeight, marginBtm;

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CodeView(Context context) {
        super(context);
        init(context);
    }

    public int dip2px(Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void init(Context context) {
        paint.setAntiAlias(true);
        paint.setColor(0xffff6764);
        paint.setStyle(Paint.Style.FILL);
        setBackgroundColor(Color.TRANSPARENT);
        linePadding = dip2px(context, 6);
        lineHeight = dip2px(context, 2);
        marginBtm = dip2px(context, 0);
        // 设置基础属性
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        lineWith = (int) (getResources().getDisplayMetrics().density * lineWith + 0.5);
        // 填充View
        inputView = new DelListenerEditText(context);
        inputView.setMaxEms(2);
        inputView.addTextChangedListener(textWatcher);
        inputView.setCursorVisible(false);
        setTextStyle(inputView);
        LayoutParams viewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        viewParams.weight = 1;
        addView(inputView, viewParams);
        pasTxtArr[0] = inputView;
        for (int i = 1; i < length; i++) {
            pasTxtArr[i] = new TextView(context);
            setTextStyle(pasTxtArr[i]);
            addView(pasTxtArr[i], viewParams);
        }
        setOnClickListener(this);
        showKeyBoard();
    }

    private void setTextStyle(TextView textView) {
        textView.setTextSize(19);
        textView.setPadding(0, 0, 0, 0);
        textView.setInputType(InputType.TYPE_CLASS_NUMBER);
        textView.setTransformationMethod(null);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xff222222);
        textView.setBackgroundColor(0);
    }

    @Override
    public void onClick(View v) {
        showKeyBoard();
    }

    private void showKeyBoard() {
        inputView.setFocusable(true);
        inputView.setFocusableInTouchMode(true);
        inputView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(inputView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /** 当删除的时候回调 */
    private void onDeleteClick() {
        for (int i = pasStrArr.length - 1; i >= 0; i--) {
            if (pasStrArr[i] != null) {
                pasStrArr[i] = null;
                pasTxtArr[i].setText(null);
                notifyTextChanged();
                break;
            }
        }
    }

    /** 监听删除的EditText */
    private class DelListenerEditText extends android.support.v7.widget.AppCompatEditText {
        public DelListenerEditText(Context context) {
            super(context);
        }

        @Override
        public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
            return new InputConnectionWrapper(super.onCreateInputConnection(outAttrs), true) {
                @Override
                public boolean sendKeyEvent(KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                        onDeleteClick();
                        return true;
                    }
                    return super.sendKeyEvent(event);
                }

                @Override
                public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                    if (beforeLength == 1 && afterLength == 0) {
                        return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                    }
                    return super.deleteSurroundingText(beforeLength, afterLength);
                }
            };
        }
    }

    /** 控制文本显示 */
    private PasswordTransformationMethod transformationMethod = new PasswordTransformationMethod() {
        @Override
        public CharSequence getTransformation(final CharSequence source, View view) {
            return new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return source.subSequence(start, end);
                }

                @Override
                public int length() {
                    return source.length();
                }

                @Override
                public char charAt(int index) {
                    return DEFAULT_TRANSFORMATION.charAt(0);
                }
            };
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null) return;
            String newStr = s.toString();
            if (newStr.length() == 1) {
                pasStrArr[0] = newStr;
                notifyTextChanged();
            } else if (newStr.length() == 2) {
                String newNum = newStr.substring(1);
                for (int i = 0; i < pasStrArr.length; i++) {
                    if (pasStrArr[i] == null) {
                        pasStrArr[i] = newNum;
                        pasTxtArr[i].setText(newNum);
                        notifyTextChanged();
                        break;
                    }
                }
                inputView.removeTextChangedListener(this);
                inputView.setText(pasStrArr[0]);
                inputView.setSelection(inputView.length());
                inputView.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void notifyTextChanged() {
        if (onPasChangeListener != null) {
            onPasChangeListener.onPasChange(getPas());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth(), h = getHeight();
        for (int i = 0; i < length; i++) {
            canvas.drawRect(i * w / length + linePadding, h - lineHeight - marginBtm, (i + 1) * w / length - linePadding, h - marginBtm, paint);
        }
    }

    /** 获取当前输入的密码 */
    public String getPas() {
        StringBuilder sb = new StringBuilder("");
        for (String aPasStrArr : pasStrArr) {
            if (aPasStrArr != null) sb.append(aPasStrArr);
        }
        return sb.toString();
    }

    /** 设置密码显示 */
    public void setPasswordVisibility(boolean visible) {
        for (TextView textView : pasTxtArr) {
            textView.setTransformationMethod(visible ? null : transformationMethod);
            if (textView instanceof EditText) {
                EditText et = (EditText) textView;
                et.setSelection(et.getText().length());
            }
        }
    }

    public void clear() {
        for (int i = pasStrArr.length - 1; i >= 0; i--) {
            pasStrArr[i] = null;
            pasTxtArr[i].setText(null);
        }
        notifyTextChanged();
    }

    /** 设置密码改变时候的监听 */
    public void setOnPasChangeListener(OnPasChangeListener onPasChangeListener) {
        this.onPasChangeListener = onPasChangeListener;
    }

    public interface OnPasChangeListener {
        void onPasChange(String pas);
    }
}