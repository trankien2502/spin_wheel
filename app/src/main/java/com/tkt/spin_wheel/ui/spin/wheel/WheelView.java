package com.tkt.spin_wheel.ui.spin.wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tkt.spin_wheel.R;

import java.util.Arrays;
import java.util.List;

public class WheelView extends View {
    private Paint paint;
    private TextPaint textPaint;
    private List<Integer> colors;
    private List<String> itemTexts;
    private int numberOfItems, textSizeItem = 20, repeatOption = 1, copyNumberOfItemByRepeat;
    private float sweepAngle;


    public WheelView(Context context) {
        super(context);
        init(null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        //paint draw wheel
        paint = new Paint();
        paint.setAntiAlias(true);
        //paint draw text
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/digitalt.ttf");
        textPaint = new TextPaint();
        textPaint.setTypeface(typeface);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(textSizeItem);
        //draw slice of wheel
        copyNumberOfItemByRepeat = repeatOption * numberOfItems;
        sweepAngle = 360f / copyNumberOfItemByRepeat;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        copyNumberOfItemByRepeat = repeatOption * numberOfItems;
        this.sweepAngle = 360f / copyNumberOfItemByRepeat;
        invalidate();
    }

    public void setColors(List<Integer> customColors) {
        if (customColors != null && customColors.size() > 0) {
            this.colors = customColors;
        } else {
            this.colors = Arrays.asList(getContext().getResources().getColor(R.color.none_color));
        }
        invalidate();
    }

    public void setTextItems(List<String> customTexts) {
        if (customTexts != null && customTexts.size() > 0) {
            this.itemTexts = customTexts;
        } else {
            this.itemTexts = Arrays.asList("", "", "", "", "",
                    "", "", "", "", "", "", "");
        }
        invalidate();
    }

    public void setTextSizeItem(int textSizeItem) {
        int sizeConvert = 20 + (textSizeItem - 1) * 10;
        this.textSizeItem = sizeConvert;
        textPaint.setTextSize(sizeConvert);
        invalidate();
    }

    public void setRepeatOption(int repeatTime) {
        int maxNumberOfItems = 24;
        int repeatOptionValid = maxNumberOfItems / numberOfItems;
        if (repeatTime > repeatOptionValid) repeatTime = repeatOptionValid;
        int numPlus = numberOfItems;
        int numCoppy = numberOfItems;
        if (repeatTime > 1) {
            for (int i = 1; i < repeatTime; i++) {
                numCoppy += numPlus;
            }
        }
        this.repeatOption = repeatTime;
        copyNumberOfItemByRepeat = repeatOption * numberOfItems;
        this.sweepAngle = 360f / copyNumberOfItemByRepeat;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        int cx = width / 2;
        int cy = height / 2;
        float startAngle = 0;

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int textWidth = (int) (radius * 2 / 3);
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        RectF rectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        copyNumberOfItemByRepeat = repeatOption * numberOfItems;
        for (int i = 0; i < copyNumberOfItemByRepeat; i++) {
            //draw slice
            paint.setColor(colors.get(i % colors.size()));
            canvas.drawArc(rectF, startAngle + (i * sweepAngle), sweepAngle, true, paint);
            //count to draw text on mid ò slice
            float tan = (textHeight / 2) / ((float) radius / 3);
            float plusAngle = (float) Math.toDegrees(Math.atan(tan));
            float angle = startAngle + (i * sweepAngle) + (sweepAngle / 2);
            float x = (float) (cx + (radius / 3) * Math.cos(Math.toRadians(angle + plusAngle / 2)));
            float y = (float) (cy + (radius / 3) * Math.sin(Math.toRadians(angle + plusAngle / 2)));
            //draw text
            canvas.save();
            canvas.translate(x, y);
            canvas.rotate(angle);
            String textItem = itemTexts.get(i % itemTexts.size());
            CharSequence charSequence = TextUtils.ellipsize(textItem, textPaint, textWidth, TextUtils.TruncateAt.END);
            canvas.drawText(charSequence, 0, charSequence.length(), 0, 0, textPaint);
            canvas.restore();
        }
    }
}

