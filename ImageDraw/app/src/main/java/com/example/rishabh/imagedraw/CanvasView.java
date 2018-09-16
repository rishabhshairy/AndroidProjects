package com.example.rishabh.imagedraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Rishabh on 10/14/2017.
 */

public class CanvasView extends View {
    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private static final float TOLERANCE=5;
    private float mX,mY;
    Context context;



     public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
         this.context=context;
         mPath=new Path();
         mPaint=new Paint();
         mPaint.setColor(Color.BLACK);
         mPaint.setAntiAlias(true);
         mPaint.setStyle(Paint.Style.STROKE);
         mPaint.setStrokeJoin(Paint.Join.ROUND);
         mPaint.setStrokeWidth(4f);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    private void startTouch(float x, float y){
        mPath.moveTo(x,y);
        mX=x;
        mY=y;
    }

    private void moveTouch(float x,float y){
        float dx=Math.abs(x-mX);
        float dy=Math.abs(y-mY);
        if(dx>=TOLERANCE||dy>=TOLERANCE){
            mPath.quadTo(mX,mY,(x+mX)/2,(y+mY)/2);
            mX=x;
            mY=y;
        }

    }
    public void clearCanvas(){
        mPath.reset();
        invalidate();
    }

    private void touchUp(){
        mPath.lineTo(mX,mY);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
            default:
                Toast.makeText(context, "End", Toast.LENGTH_SHORT).show();
                return false;


        }

        return true;
    }
}
