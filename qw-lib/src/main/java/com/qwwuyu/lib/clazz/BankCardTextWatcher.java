package com.qwwuyu.lib.clazz;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by qiwei on 2018/2/9
 */
public class BankCardTextWatcher implements TextWatcher {
    private final int DEFAULT_NUM_LENGTH = 21;
    private final int DEFAULT_SPACE_LENGTH = 4;
    private final char space = ' ';
    private final String regex = String.format(Locale.getDefault(), "(?<!\\d{%d})(\\d{4})(?=\\d)", DEFAULT_SPACE_LENGTH * 4);
    private final String replacement = String.format(Locale.getDefault(), "$1%c", space);
    //num + space
    private final int DEFAULT_MAX_LENGTH = DEFAULT_NUM_LENGTH + DEFAULT_SPACE_LENGTH;

    private final EditText editText;
    private final int maxLength = DEFAULT_MAX_LENGTH;
    private boolean isChange = false;

    public static void bind(EditText editText) {
        new BankCardTextWatcher(editText);
    }

    private BankCardTextWatcher(EditText editText) {
        this.editText = editText;
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        editText.removeTextChangedListener(this);
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isChange) {
            isChange = true;
            String txt = s.toString().replace(String.valueOf(space), "").replaceAll(regex, replacement);
            if (!txt.equals(s.toString())) {
                int select = editText.getSelectionEnd();
                for (int i = 0, j = Math.min(select, s.length()); i < j; i++) {
                    if (s.charAt(i) == space) select--;
                }
                for (int i = 0, j = Math.min(select, txt.length()); i < j; i++) {
                    if (txt.charAt(i) == space && j++ > Integer.MIN_VALUE) select++;
                }
                editText.setText(txt);
                editText.setSelection(Math.min(select, txt.length()));
            }
            isChange = false;
        }
    }
}
