package com.qwwuyu.lib.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qiwei on 2018/5/4 14:29
 * Description Spannable工具.
 */
public class SpannableUtils {
    public static void setPhoneSpannable(TextView tv, String content, SpanOnClickListener listener) {
        ClickSpan.Builder builder = new ClickSpan.Builder(null, listener)
                .normalTextColor(0xfff03059);
        setSpannable(tv, content, "(?<=\\D)\\d{11}(?=\\D)", builder);
    }

    public static void setSpannable(TextView tv, String content, String regex, ClickSpan.Builder builder) {
        Matcher matcher = Pattern.compile(regex).matcher(content);
        List<SpanBean> spanBeans = new ArrayList<>();
        while (matcher.find()) {
            SpanBean spanBean = new SpanBean(matcher.start(), matcher.end(), builder.txt(matcher.group()).build());
            spanBeans.add(spanBean);
        }
        setSpannable(tv, content, spanBeans);
    }

    public static void setSpannable(TextView tv, String content, List<SpanBean> spanBeans) {
        SpannableString spannable = new SpannableString(content);
        for (SpanBean spanBean : spanBeans) {
            spannable.setSpan(spanBean.clickSpan, spanBean.start, spanBean.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        LinkTouchMovementMethod linkTouchMovementMethod = new LinkTouchMovementMethod();
        tv.setMovementMethod(linkTouchMovementMethod);
        tv.setHighlightColor(0x00000000);
        tv.setText(spannable);
    }

    private static class LinkTouchMovementMethod extends LinkMovementMethod {
        private ClickSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                ClickSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        public void clear() {
            if (mPressedSpan != null) {
                mPressedSpan.setPressed(false);
                mPressedSpan = null;
            }
        }

        private ClickSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (y < 0 || y > textView.getHeight()) {
                return null;
            }
            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();
            x += textView.getScrollX();
            y += textView.getScrollY();
            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            ClickSpan[] link = spannable.getSpans(off, off, ClickSpan.class);
            if (link.length > 0) {
                return link[0];
            }
            return null;
        }
    }

    public static class ClickSpan extends ClickableSpan {
        private final String txt;
        private final SpanOnClickListener listener;

        private boolean isPressed;
        private int underlineColor = 0x00000000;
        private float underlineThickness = 0f;
        private int normalTextColor;
        private int pressedTextColor;
        private int normalBgColor = 0x00000000;
        private int pressedBgColor = 0x00000000;

        private ClickSpan(Builder builder) {
            txt = builder.txt;
            listener = builder.listener;
            underlineColor = builder.underlineColor;
            underlineThickness = builder.underlineThickness;
            normalTextColor = builder.normalTextColor;
            pressedTextColor = builder.pressedTextColor;
            normalBgColor = builder.normalBgColor;
            pressedBgColor = builder.pressedBgColor;
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) listener.onClick(txt);
        }

        public void setPressed(boolean pressed) {
            isPressed = pressed;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(isPressed ? pressedTextColor : normalTextColor);
            ds.bgColor = isPressed ? pressedBgColor : normalBgColor;
            try {
                final Method method = TextPaint.class.getMethod("setUnderlineText", Integer.TYPE, Float.TYPE);
                method.invoke(ds, underlineColor, underlineThickness);
            } catch (Exception ignored) {
            }
        }

        public static final class Builder {
            private String txt;
            private SpanOnClickListener listener;
            private int underlineColor;
            private float underlineThickness;
            private int normalTextColor;
            private int pressedTextColor;
            private int normalBgColor;
            private int pressedBgColor;

            public Builder(String txt, SpanOnClickListener listener) {
                this.txt = txt;
                this.listener = listener;
            }

            public Builder txt(String txt) {
                this.txt = txt;
                return this;
            }

            public Builder listener(SpanOnClickListener listener) {
                this.listener = listener;
                return this;
            }

            public Builder underlineColor(int underlineColor) {
                this.underlineColor = underlineColor;
                return this;
            }

            public Builder underlineThickness(float underlineThickness) {
                this.underlineThickness = underlineThickness;
                return this;
            }

            public Builder normalTextColor(int normalTextColor) {
                this.normalTextColor = normalTextColor;
                return this;
            }

            public Builder pressedTextColor(int pressedTextColor) {
                this.pressedTextColor = pressedTextColor;
                return this;
            }

            public Builder normalBgColor(int normalBgColor) {
                this.normalBgColor = normalBgColor;
                return this;
            }

            public Builder pressedBgColor(int pressedBgColor) {
                this.pressedBgColor = pressedBgColor;
                return this;
            }

            public ClickSpan build() {
                return new ClickSpan(this);
            }
        }
    }

    public interface SpanOnClickListener {
        void onClick(String txt);
    }

    public static class SpanBean {
        int start;
        int end;
        ClickSpan clickSpan;

        public SpanBean() {
        }

        public SpanBean(int start, int end, ClickSpan clickSpan) {
            this.start = start;
            this.end = end;
            this.clickSpan = clickSpan;
        }
    }
}
