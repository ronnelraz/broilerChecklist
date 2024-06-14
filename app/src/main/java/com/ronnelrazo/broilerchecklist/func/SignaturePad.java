package com.ronnelrazo.broilerchecklist.func;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ronnelrazo.broilerchecklist.R;

public class SignaturePad extends View {
    private static final float DEFAULT_STROKE_WIDTH = 5f;

    private float strokeWidth = DEFAULT_STROKE_WIDTH;
    private int strokeColor = Color.BLACK;

    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private float lastX, lastY;

    public SignaturePad(Context context) {
        this(context, null);
    }

    public SignaturePad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignaturePad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SignaturePad, defStyleAttr, 0);
        strokeWidth = typedArray.getDimension(R.styleable.SignaturePad_strokeWidth, DEFAULT_STROKE_WIDTH);
        strokeColor = typedArray.getColor(R.styleable.SignaturePad_strokeColor, Color.BLACK);
        typedArray.recycle();
        initialize();
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        paint.setStrokeWidth(strokeWidth);
        invalidate(); // Request a redraw to reflect the change
    }

    public void setStrokeColor(int color) {
        strokeColor = color;
        paint.setColor(strokeColor);
        invalidate(); // Request a redraw to reflect the change
    }

    private void initialize() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(strokeColor);
        paint.setStrokeCap(Paint.Cap.ROUND); // Round stroke ends
        paint.setStrokeJoin(Paint.Join.ROUND); // Round corners
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); // Enable blending for smoothness
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                lastX = x;
                lastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                // Calculate control points for quadratic Bezier curve
                float controlX = (x + lastX) / 2;
                float controlY = (y + lastY) / 2;
                // Draw quadratic Bezier curve from last point to current point
                path.quadTo(lastX, lastY, controlX, controlY);
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                // Draw the path on the canvas
                canvas.drawPath(path, paint);
                // Reset the path for the next stroke
                path.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void clear() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void clearSignature() {
        path.reset();
        invalidate();
    }

    public boolean isSignatureEmpty() {
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                    return false; // Signature is not empty
                }
            }
        }
        return true; // Signature is empty
    }



    public Bitmap getBitmap() {
        return bitmap;
    }
}
