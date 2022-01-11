package com.example.product_inventory_application;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import static com.example.product_inventory_application.paint.paintbrush;
import static com.example.product_inventory_application.paint.path;


public class Draw extends View {

    static Canvas staticcanvas;
    public static ArrayList<Path> pathList = new ArrayList<Path>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public ViewGroup.LayoutParams param;
    public static int current_brush = Color.BLACK;
    public Draw (Context context) {
        super(context);
        init (context);
    }
    public Draw (Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init (context);
    }
    public Draw (Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init (context);
    }
    private void init(Context context){
        paintbrush.setAntiAlias (true);
        paintbrush.setColor (Color.BLACK);
        paintbrush.setStyle (Paint.Style.STROKE);
        paintbrush.setStrokeCap (Paint.Cap.ROUND);
        paintbrush.setStrokeJoin (Paint.Join.ROUND);
        paintbrush.setStrokeWidth (100f);

        param = new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        float x = event.getX ();
        float y = event.getY ();
        switch(event.getAction ()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo (x,y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo (x,y);
                pathList.add(path);
                colorList.add(current_brush);
                invalidate ();
                return true;
            default:
                return false;
        }
    }
    @Override
    protected void onDraw(Canvas canvas){
        for (int i=0; i < pathList.size(); i++) {
            paintbrush.setColor (colorList.get (i));
            canvas.drawPath (pathList.get (i),paintbrush);
            staticcanvas = canvas;
            invalidate ();
        }
    }

}
