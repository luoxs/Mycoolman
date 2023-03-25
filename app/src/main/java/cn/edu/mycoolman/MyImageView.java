package cn.edu.mycoolman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float x=0;
    private float y=0;
    Rect localRect;
    Boolean bo ;
    private float radius=100;
    private int r=255;   //red
    private int g=255;   //green
    private int b=255;    //blue

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }
    Paint paint = new Paint();

    public MyImageView(Context context){
        super(context);
    }
    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setARGB(255,r,g,b);
        //paint.setColor(Color.BLUE);
        localRect = new Rect();
        bo = this.getLocalVisibleRect(localRect);
        if(x<1) {
            x = localRect.right / 2;
            y = localRect.bottom / 2;
        }
        radius = localRect.right/8;
        canvas.drawCircle(x,y,radius, paint);
    }

}
