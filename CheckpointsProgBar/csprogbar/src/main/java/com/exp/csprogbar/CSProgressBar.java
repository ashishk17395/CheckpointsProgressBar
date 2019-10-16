package com.exp.csprogbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class CSProgressBar extends View {


    int csProgress;
    int csSize;
    float csBarHeight;
    int csFilledColor;
    int csUnfilledColor;
    int csCheckpoints[];
    int csCheckpointColors[];

    String csMarkers[];
    int csMarkerTextColor;
    Drawable csMarkerDrawable;
    float csMarkerTextSize;
    float csMarkerHeight;
    float csMarkerWidth;
    float csMarkerBarPadding;
    float csMarkerTextDrawablePadding;
    Typeface csMarkerTypeface;

    String csSubMarkers[];
    int csSubMarkerTextColor;
    Drawable csSubMarkerDrawable;
    float csSubMarkerTextSize;
    float csSubMarkerHeight;
    float csSubMarkerWidth;
    float csSubMarkerBarPadding;
    Typeface csSubMarkerTypeface;
    float csSubMarkerTextPaddingBottom;

    String csFilledText;
    boolean csFormatFilledText;
    boolean csIsCollapsed;

    private Paint paint;
    private Path path;
    private RectF clippingRect;
    private Paint textPaint;

    private ProgressBarChangedListener mListener;
    private int clearedCheckPoint = -1;
    private long mUiThreadId;

    //region Constructors
    public CSProgressBar(Context context) {
        super(context);
        mUiThreadId = Thread.currentThread().getId();
        init(null);

    }

    public CSProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mUiThreadId = Thread.currentThread().getId();
        init(attrs);

    }

    public CSProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mUiThreadId = Thread.currentThread().getId();
        init(attrs);

    }

    public CSProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mUiThreadId = Thread.currentThread().getId();
        init(attrs);

    }
    //endregion

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CSProgressBar, 0, 0);
        try {
            // extract attributes to member variables from typedArray
            setCsProgress(typedArray.getInt(R.styleable.CSProgressBar_csProgress, 0));
            setCsSize(typedArray.getInt(R.styleable.CSProgressBar_csSize, 100));
            setCsBarHeight(typedArray.getDimension(R.styleable.CSProgressBar_csBarHeight, 0));
            setCsFilledColor(typedArray.getColor(R.styleable.CSProgressBar_csFilledColor, Color.BLUE));
            setCsUnfilledColor(typedArray.getColor(R.styleable.CSProgressBar_csUnfilledColor, Color.GRAY));

            TypedArray tCh = getResources().obtainTypedArray(typedArray.getResourceId(R.styleable.CSProgressBar_csCheckpoints, 0));
            int[] _chkpntsArr = new int[tCh.length()];
            for (int i = 0; i < _chkpntsArr.length; i++)
                _chkpntsArr[i] = tCh.getInt(i, 0);
            setCsCheckpoints(_chkpntsArr);
            tCh.recycle();

            TypedArray tChColors = getResources().obtainTypedArray(typedArray.getResourceId(R.styleable.CSProgressBar_csCheckpointColors, 0));
            int[] _colorArr = new int[tChColors.length()];
            for (int i = 0; i < _colorArr.length; i++) {
                if (tChColors.peekValue(i).type == TypedValue.TYPE_ATTRIBUTE) {
                    TypedValue typedValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(tChColors.peekValue(i).data, typedValue, true);
                    _colorArr[i] = typedValue.data;
                } else {
                    _colorArr[i] = tChColors.getColor(i, 0);
                }
            }
            setCsCheckpointColors(_colorArr);
            tChColors.recycle();

            TypedArray tMark = getResources().obtainTypedArray(typedArray.getResourceId(R.styleable.CSProgressBar_csMarkers, 0));
            String[] _tMarkArr = new String[tMark.length()];
            for (int i = 0; i < _tMarkArr.length; i++)
                _tMarkArr[i] = tMark.getString(i);
            setCsMarkers(_tMarkArr);
            tMark.recycle();

            setCsMarkerTextColor(typedArray.getColor(R.styleable.CSProgressBar_csMarkerTextColor, Color.BLACK));
            setCsMarkerDrawable(typedArray.getDrawable(R.styleable.CSProgressBar_csMarkerDrawable));
            setCsMarkerTextSize(typedArray.getDimension(R.styleable.CSProgressBar_csMarkerTextSize, 14f));
            setCsMarkerHeight(typedArray.getDimension(R.styleable.CSProgressBar_csMarkerHeight, 0f));
            setCsMarkerWidth(typedArray.getDimension(R.styleable.CSProgressBar_csMarkerWidth, 0f));
            setCsMarkerBarPadding(typedArray.getDimension(R.styleable.CSProgressBar_csMarkerBarPadding, 0f));
            setCsMarkerTextDrawablePadding(typedArray.getDimension(R.styleable.CSProgressBar_csMarkerTextDrawablePadding, 0f));

            TypedArray tSubMark = getResources().obtainTypedArray(typedArray.getResourceId(R.styleable.CSProgressBar_csSubMarkers, 0));
            String[] _tSubMarkArr = new String[tSubMark.length()];
            for (int i = 0; i < _tSubMarkArr.length; i++)
                _tSubMarkArr[i] = tSubMark.getString(i);
            setCsSubMarkers(_tSubMarkArr);
            tSubMark.recycle();

            setCsSubMarkerTextColor(typedArray.getColor(R.styleable.CSProgressBar_csSubMarkerTextColor, Color.BLACK));
            setCsSubMarkerDrawable(typedArray.getDrawable(R.styleable.CSProgressBar_csSubMarkerDrawable));
            setCsSubMarkerTextSize(typedArray.getDimension(R.styleable.CSProgressBar_csSubMarkerTextSize, 10f));
            setCsSubMarkerHeight(typedArray.getDimension(R.styleable.CSProgressBar_csSubMarkerHeight, 0f));
            setCsSubMarkerWidth(typedArray.getDimension(R.styleable.CSProgressBar_csSubMarkerWidth, 0f));
            setCsSubMarkerBarPadding(typedArray.getDimension(R.styleable.CSProgressBar_csSubMarkerBarPadding, 0f));
            setCsSubMarkerTextPaddingBottom(typedArray.getDimension(R.styleable.CSProgressBar_csSubMarkerTextPaddingBottom, -1f));


            setCsFilledText(typedArray.getString(R.styleable.CSProgressBar_csFilledText));
            setCsFormatFilledText(typedArray.getBoolean(R.styleable.CSProgressBar_csFormattedFilledText, true));
            setCsIsCollapsed(typedArray.getBoolean(R.styleable.CSProgressBar_csIsCollapsed, false));

        } finally {
            typedArray.recycle();
        }

        paint = new Paint();
        textPaint = new Paint();
        path = new Path();
        clippingRect = new RectF();
        if (csMarkerTypeface == null)
            csMarkerTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        if (csSubMarkerTypeface == null)
            csSubMarkerTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (MeasureSpec.getSize(heightMeasureSpec) + getCsSubMarkerBarPadding() + getCsSubMarkerHeight() + getPaddingBottom() + getCsMarkerBarPadding() + getCsMarkerHeight() + getPaddingTop());
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float barTop = getPaddingTop() + getCsMarkerHeight() + getCsMarkerBarPadding();
        float barLeft = getPaddingStart();
        float barRight = getWidth() - getPaddingEnd();
        float mainBarWidth = barRight - barLeft;
        float barBottom = barTop + csBarHeight;
        float cornerRad = csBarHeight / 2.0f;
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        paint.setColor(csUnfilledColor);
        canvas.drawRoundRect(barLeft, barTop, barRight, barBottom, cornerRad, cornerRad, paint);

        clippingRect.set(barLeft, barTop, barRight, barBottom);
        path.addRoundRect(clippingRect, cornerRad, cornerRad, Path.Direction.CW);

        //draw checkpoint markers
        if (!csIsCollapsed) {
            for (int i = csCheckpoints.length - 1; i >= 0; i--) {
                float barWidth = mainBarWidth * csCheckpoints[i] * 1.0f / csSize;
                if (barWidth > mainBarWidth - cornerRad)
                    barWidth = mainBarWidth - cornerRad;

                textPaint.setColor(csMarkerTextColor);
                textPaint.setTextSize(csMarkerTextSize);
                textPaint.setTypeface(csMarkerTypeface);
                csMarkerDrawable.setBounds((int) (barWidth + barLeft - (csMarkerWidth + csMarkerTextDrawablePadding) / 2), 0, (int) (barWidth + barLeft - csMarkerTextDrawablePadding / 2), (int) csMarkerHeight);
                canvas.drawText(csMarkers[i], ((int) (barWidth + barLeft + csMarkerTextDrawablePadding / 2)) * 1.0f, csMarkerHeight - (csMarkerHeight - csMarkerTextSize) / 2, textPaint);
                csMarkerDrawable.draw(canvas);


                textPaint.setColor(csSubMarkerTextColor);
                textPaint.setTextSize(csSubMarkerTextSize);
                textPaint.setTypeface(csSubMarkerTypeface);
                textPaint.setTextAlign(Paint.Align.CENTER);
                csSubMarkerDrawable.setBounds((int) (barWidth + barLeft - csSubMarkerWidth / 2),
                        (int) (barBottom + csSubMarkerBarPadding),
                        (int) (barWidth + barLeft + csSubMarkerWidth / 2),
                        (int) (barBottom + csSubMarkerBarPadding + csSubMarkerHeight));
                csSubMarkerDrawable.draw(canvas);
                if(csSubMarkerTextPaddingBottom!=-1f)
                    canvas.drawText(csSubMarkers[i], barWidth + barLeft, barBottom + csSubMarkerBarPadding + csSubMarkerHeight - csSubMarkerTextPaddingBottom, textPaint);
                else
                    canvas.drawText(csSubMarkers[i], barWidth + barLeft, barBottom + csMarkerBarPadding + (csSubMarkerHeight + csSubMarkerTextSize)/2  , textPaint);
            }
        }


        //draw checkpoints
        for (int i = csCheckpoints.length - 1; i >= 0; i--) {
            paint.setColor(csCheckpointColors[i]);
            float barWidth = mainBarWidth * csCheckpoints[i] * 1.0f / csSize;
            canvas.clipPath(path);
            canvas.drawRect(barLeft, barTop, barWidth + barLeft, barBottom, paint);
        }

        //Incase of collapsed
        if (csIsCollapsed) {
            for (int i = csCheckpoints.length - 2; i >= -1; i--) {
                float barWidth = i != -1 ? mainBarWidth * csCheckpoints[i] * 1.0f / csSize + cornerRad / 2 : cornerRad / 2;
                textPaint.setColor(csMarkerTextColor);
                textPaint.setTextSize(csMarkerTextSize);
                csMarkerDrawable.setBounds((int) (barWidth - getCsMarkerTextSize()), (int) (barTop + (csBarHeight - csMarkerTextSize) / 2), (int) (barWidth), (int) getCsMarkerTextSize());
                canvas.drawText(csMarkers[i + 1], barWidth, (int) (barBottom - (csBarHeight - csMarkerTextSize) / 2), textPaint);
                csMarkerDrawable.draw(canvas);

            }
        }


        paint.setColor(csFilledColor);
        float progWidth = (csProgress * 1.0f / csSize) * mainBarWidth;
        canvas.save();
        canvas.clipRect(barLeft, barTop, progWidth + barLeft, barBottom);
        canvas.drawRoundRect(barLeft, barTop, barRight, barBottom, cornerRad, cornerRad, paint);
        updateProgressChanged();
        canvas.restore();


    }

    //region Getters and Setters
    public int getCsProgress() {
        return csProgress;
    }

    public void setCsProgress(int csProgress) {
        if (csProgress <= csSize)
            this.csProgress = csProgress;
        else
            this.csProgress = csSize;
        invalidate();
        updateProgressChanged();

    }

    public boolean isCsIsCollapsed() {
        return csIsCollapsed;
    }

    public void setCsIsCollapsed(boolean csIsCollapsed) {
        this.csIsCollapsed = csIsCollapsed;
        invalidate();
    }

    public int getCsSize() {
        return csSize;
    }

    public void setCsSize(int csSize) {
        this.csSize = csSize;
        postInvalidate();
    }

    public int[] getCsCheckpoints() {
        return csCheckpoints;
    }

    public void setCsCheckpoints(int[] csCheckpoints) {
        this.csCheckpoints = csCheckpoints;
        postInvalidate();
    }

    public int[] getCsCheckpointColors() {
        return csCheckpointColors;
    }

    public void setCsCheckpointColors(int[] csCheckpointColors) {
        this.csCheckpointColors = csCheckpointColors;
        postInvalidate();
    }

    public String[] getCsSubMarkers() {
        return csSubMarkers;
    }

    public void setCsSubMarkers(String[] csSubMarkers) {
        this.csSubMarkers = csSubMarkers;
        postInvalidate();
    }

    public String[] getCsMarkers() {
        return csMarkers;
    }

    public void setCsMarkers(String[] csMarkers) {
        this.csMarkers = csMarkers;
        postInvalidate();
    }

    public int getCsFilledColor() {
        return csFilledColor;
    }

    public void setCsFilledColor(int csFilledColor) {
        this.csFilledColor = csFilledColor;
        postInvalidate();
    }

    public int getCsSubMarkerTextColor() {
        return csSubMarkerTextColor;
    }

    public void setCsSubMarkerTextColor(int csSubMarkerTextColor) {
        this.csSubMarkerTextColor = csSubMarkerTextColor;
        postInvalidate();
    }

    public int getCsUnfilledColor() {
        return csUnfilledColor;
    }

    public void setCsUnfilledColor(int csUnfilledColor) {
        this.csUnfilledColor = csUnfilledColor;
        postInvalidate();
    }

    public int getCsMarkerTextColor() {
        return csMarkerTextColor;
    }

    public void setCsMarkerTextColor(int csMarkerTextColor) {
        this.csMarkerTextColor = csMarkerTextColor;
        postInvalidate();
    }

    public String getCsFilledText() {
        return csFilledText;
    }

    public void setCsFilledText(String csFilledText) {
        this.csFilledText = csFilledText;
        postInvalidate();
    }

    public Drawable getCsSubMarkerDrawable() {
        return csSubMarkerDrawable;
    }

    public void setCsSubMarkerDrawable(Drawable csSubMarkerDrawable) {
        this.csSubMarkerDrawable = csSubMarkerDrawable;
        postInvalidate();
    }

    public Drawable getCsMarkerDrawable() {
        return csMarkerDrawable;
    }

    public void setCsMarkerDrawable(Drawable csMarkerDrawable) {
        this.csMarkerDrawable = csMarkerDrawable;
        postInvalidate();
    }

    public boolean isCsFormatFilledText() {
        return csFormatFilledText;
    }

    public void setCsFormatFilledText(boolean csFormatFilledText) {
        this.csFormatFilledText = csFormatFilledText;
        postInvalidate();
    }

    public float getCsBarHeight() {
        return csBarHeight;
    }

    public void setCsBarHeight(float csBarHeight) {
        this.csBarHeight = csBarHeight;
        postInvalidate();
    }

    public float getCsMarkerTextSize() {
        return csMarkerTextSize;
    }

    public void setCsMarkerTextSize(float csMarkerTextSize) {
        this.csMarkerTextSize = csMarkerTextSize;
        postInvalidate();
    }

    public float getCsMarkerHeight() {
        return csMarkerHeight;
    }

    public void setCsMarkerHeight(float csMarkerHeight) {
        this.csMarkerHeight = csMarkerHeight;
        postInvalidate();
    }

    public float getCsMarkerBarPadding() {
        return csMarkerBarPadding;
    }

    public void setCsMarkerBarPadding(float csMarkerBarPadding) {
        this.csMarkerBarPadding = csMarkerBarPadding;
        postInvalidate();
    }

    public float getCsSubMarkerTextSize() {
        return csSubMarkerTextSize;
    }

    public void setCsSubMarkerTextSize(float csSubMarkerTextSize) {
        this.csSubMarkerTextSize = csSubMarkerTextSize;
        postInvalidate();
    }

    public float getCsSubMarkerHeight() {
        return csSubMarkerHeight;
    }

    public void setCsSubMarkerHeight(float csSubMarkerHeight) {
        this.csSubMarkerHeight = csSubMarkerHeight;
        postInvalidate();
    }

    public float getCsSubMarkerBarPadding() {
        return csSubMarkerBarPadding;
    }

    public void setCsSubMarkerBarPadding(float csSubMarkerBarPadding) {
        this.csSubMarkerBarPadding = csSubMarkerBarPadding;
        postInvalidate();
    }

    public float getCsMarkerWidth() {
        return csMarkerWidth;
    }

    public void setCsMarkerWidth(float csMarkerWidth) {
        this.csMarkerWidth = csMarkerWidth;
        postInvalidate();
    }

    public float getCsSubMarkerWidth() {
        return csSubMarkerWidth;
    }

    public void setCsSubMarkerWidth(float csSubMarkerWidth) {
        this.csSubMarkerWidth = csSubMarkerWidth;
        postInvalidate();
    }

    public float getCsMarkerTextDrawablePadding() {
        return csMarkerTextDrawablePadding;
    }

    public void setCsMarkerTextDrawablePadding(float csMarkerTextDrawablePadding) {
        this.csMarkerTextDrawablePadding = csMarkerTextDrawablePadding;
        postInvalidate();
    }


    public float getCsSubMarkerTextPaddingBottom() {
        return csSubMarkerTextPaddingBottom;
    }

    public void setCsSubMarkerTextPaddingBottom(float csSubMarkerTextPaddingBottom) {
        this.csSubMarkerTextPaddingBottom = csSubMarkerTextPaddingBottom;
        postInvalidate();
    }
    //endregion

    //region Listeners
    private void updateProgressChanged() {
        if (mListener != null) {
            mListener.onProgressChanged(getCsProgress());
            updateCheckPointCleared();
        }
    }

    private void updateCheckPointCleared() {
        int currentClearedIndex = -1;
        for (int i = 0; i < csCheckpoints.length; i++) {
            if (csProgress > csCheckpoints[i])
                currentClearedIndex = i;
        }

        if (currentClearedIndex != clearedCheckPoint) {
            clearedCheckPoint = currentClearedIndex;
            mListener.onCheckPointCleared(clearedCheckPoint);
        }

    }

    public void setCsProgressChangeListener(ProgressBarChangedListener listener) {
        mListener = listener;
    }

    public interface ProgressBarChangedListener {

        public void onProgressChanged(int progress);

        public void onCheckPointCleared(int indexOfLatestCheckpoint);
    }

    //endregion
}