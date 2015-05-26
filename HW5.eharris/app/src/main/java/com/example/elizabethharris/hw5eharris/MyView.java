package com.example.elizabethharris.hw5eharris;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class MyView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private Tomato tom = new Tomato();
    public float accelx = 0;
    public float accely = 0;
    public float drainx = 0;
    public float drainy = 0;
    public float drainr = 40;
    public float width = 0;
    public float height = 0;


    private Paint paint = new Paint();
    public Runnable animator = new Runnable() {
        @Override
        public void run() {
            //one round of physics update
            physics();
            //invalidate screen view
            invalidate();
            //adds to the queue;
            //post another event
            postDelayed(this, 20);

        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension(width, height);
    }

    public void physics() {
        //basic
        tom.x += tom.velx;
        tom.y += tom.vely;
        tom.velx += accelx*0.1;
        tom.vely += accely*0.1;
        Log.i("x= " + tom.x, "y= " + tom.y );
        //add in friction
        float difx = tom.x - drainx;
        float dify = tom.y - drainy;
        double totaldif = Math.sqrt(difx * difx + dify * dify);


        //Bounce of Walls
//        //Top
        if (tom.y - tom.r < 0) {
            tom.y = 0 + tom.r;
            tom.vely *= -1;
        }
////        //Bottom
        if (tom.y + tom.r > height) {
            tom.y = height - (tom.r-10);
            tom.vely *= -1;
        }
////        //Right
        if (tom.x + tom.r > width) {
            tom.x = width - tom.r;
            tom.velx *= -1;
        }
//        //Left
        if (tom.x - tom.r < 0) {
            tom.x = 0 + tom.r;
            tom.velx *= -1;
        }
        //Win into the drain
        if (totaldif < tom.r + drainr) {
            win();
        }


        //bottom Interior?
        if(tom.y + tom.r > (height* 2 / 3) && tom.y -tom.r > (height* 2 / 3)){
            if(tom.x > (width))/*all the way to to the middle edge*/{
                //do nothing
            }
            else if(tom.y <(height* 2 / 3)){
                tom.vely = 0;
                tom.y = (height* 2 / 3) + tom.r;
            }else if(tom.y>(height* 2 / 3)){
                tom.vely =0;
                tom.y = (height* 2 / 3) - tom.r;
            }
        }
//        //Top Interior?
        if(tom.y + tom.r > (height* 1 / 3) && tom.y -tom.r > (height* 1 / 3)){
            if(tom.x > (width * 1 / 3))/*all the way to to the middle edge*/{
                //do nothing
            }
            else if(tom.y <(height* 1 / 3)){
                tom.vely = 0;
                tom.y = (height* 1 / 3) + tom.r;
            }else if(tom.y>(height* 1 / 3)){
                tom.vely =0;
                tom.y = (height* 1 / 3) - tom.r;
            }
        }

    }

    public void win() {
//        int duration = Toast.LENGTH_SHORT;
//        Toast toast = Toast.makeText(context, s, duration);
//        toast.show();

    }

    protected void onDraw(Canvas c) {
        width = c.getWidth();
        height = c.getHeight();
        //draw all the things you want to the screen
        //we "paint" on the "canvas"
        //draw background
        paint.setColor(Color.WHITE);
        //(lefttop,righttop,width, height)
        //or(left, top, right, bottom)
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), paint);
        //top left line
        paint.setColor(Color.BLACK);
        c.drawLine(0, height * 1 / 3, width * 2 / 3, height * 1 / 3, paint);
        //top right line
//        paint.setColor(Color.BLACK);
        c.drawLine(width * 1 / 3, height * 2 / 3, width, height * 2 / 3, paint);
        //draw tomato
        paint.setColor(Color.RED);
        c.drawCircle(tom.x+10, tom.y+10, tom.r, paint);
        //draw drain
        paint.setColor(Color.GRAY);
        c.drawCircle(width * 1 / 2, height* 15 / 16, drainr, paint);


    }

    public MyView(Context context) {
        super(context);
        //init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.MyView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.MyView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.MyView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.MyView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.MyView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
//        Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }


    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
