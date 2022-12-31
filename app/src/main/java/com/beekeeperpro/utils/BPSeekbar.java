package com.beekeeperpro.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class BPSeekbar extends androidx.appcompat.widget.AppCompatSeekBar {
    private String[] values;

    private Paint mPaint;

    public BPSeekbar(Context context) {
        super(context);
        init();
    }

    public void setValues(String[] values){
        this.values = values;
    }

    public BPSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BPSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(20);
        values = new String[0];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dessin des traits
        int width = getWidth()-getPaddingLeft()-getPaddingRight();
        int height = getHeight();
        int interval = width / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            canvas.drawLine(i * interval+getPaddingLeft(), height/4f, i * interval+getPaddingLeft(), 3*height / 4f, mPaint);
        }

        // Dessin des labels
        for (int i = 0; i < values.length; i++) {
            String label = values[i];
            float labelWidth = mPaint.measureText(label);
            float x = i * interval + getPaddingLeft() - labelWidth / 2f;
            float y = height / 2f + mPaint.getTextSize();
            canvas.drawText(label, x, y, mPaint);
        }
    }
}