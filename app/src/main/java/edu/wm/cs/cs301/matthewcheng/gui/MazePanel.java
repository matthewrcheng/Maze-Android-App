package edu.wm.cs.cs301.matthewcheng.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

public class MazePanel extends View implements P5PanelF21 {

    private static final String TAG = "mazePanel" ;
    private int dim = 1200;
    //paint color
    int mColor;
    //paint for MazePanel view
    Paint MazePaint;
    //canvas for MazePanel view
    Canvas MazeCanvas;
    //bitmap for MazePanel view
    Bitmap MazeBitmap;

    public MazePanel(Context context) {
        super(context, null);
        init(null, 0);
    }

    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public void init(AttributeSet attrs, int defStyle) {
        MazePaint = new Paint();
        MazeBitmap = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);
        MazeCanvas = new Canvas(MazeBitmap);
        MazePaint.setAntiAlias(true);
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDraw(Canvas a) {
        //addBackground(0);
        a.drawBitmap(MazeBitmap, 0 ,0, MazePaint);
        MazePaint.setColor(Color.RED);

        addLine(0,0, getWidth(), 0);
        addLine(0,0,0, getHeight());
        addLine(getWidth(), 0, getWidth(), getHeight());
        addLine(0, getHeight(), getWidth(), getHeight());
        update();
        //testDraw();
    }

    private void testDraw() {
        addBackground(50);
        MazePaint.setColor(Color.GREEN);
        addFilledOval(200,200,100,100);
        MazePaint.setColor(Color.YELLOW);
        addFilledRectangle(0,0,100,100);
        MazePaint.setColor(Color.RED);
        addLine(100,100,200,200);
        addFilledOval(100,100,100,200);
        MazePaint.setColor(Color.BLACK);
        addMarker(100, 100,  "S");
        MazePaint.setColor(Color.BLUE);
        addFilledPolygon(new int[]{300,300,350,400,400},new int[]{300,350,400,350,300},5);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(dim, dim);
    }

    public void update() {
        invalidate();
    }

    @Override
    public void commit() {
        invalidate();
    }

    @Override
    public boolean isOperational() {
        return true;
    }

    @Override
    public void setColor(int rgb) {
        MazePaint.setColor(rgb);
        //update();
    }

    @Override
    public void setColor(int rgb, float alpha) {
        Color col = Color.valueOf(rgb);
        float red = col.red();
        float green = col.green();
        float blue = col.blue();
        MazePaint.setColor(Color.argb(alpha, red, green, blue));
        //update();
    }

    @Override
    public int getColor() {
        return MazePaint.getColor();
    }

    /**
     * Converts the rgb values of a Color into their Integer representation.
     * @param r red value of the color
     * @param g green value of the color
     * @param b blue value of the color
     * @return parsed Integer representation
     */
    public static Integer getInt(int r, int g, int b) {
        String rs = pad(Integer.toHexString((int) r));
        String gs = pad(Integer.toHexString((int) g));
        String bs = pad(Integer.toHexString((int) b));
        String hex = rs + gs + bs;
        return Integer.parseInt(hex, 16);
    }

    /**
     * Helper method for getInt that ensures the input string
     * is a viable hex value by adding a 0 to the front if the
     * value is only 1 character long. This is useful if we are
     * planning on concatenating the hex strings.
     * @param s string hex value to be padded
     * @return padded hex value ready for concatenation and parsing
     */
    private static String pad(String s) {
        return (s.length() == 1) ? "0" + s : s;
    }

    @Override
    public void addBackground(float percentToExit) {
        MazePaint.setColor(Color.BLACK);
        MazePaint.setStyle(Paint.Style.FILL);
        MazeCanvas.drawRect(0, 0, getWidth(), getHeight()/2, MazePaint);
        MazePaint.setColor(Color.GRAY);
        MazePaint.setStyle(Paint.Style.FILL);
        MazeCanvas.drawRect(0, getHeight()/2, getWidth(), getHeight(), MazePaint);
        update();
    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        Rect rect = new Rect(x,y,x+width,y+height);
        MazePaint.setStyle(Paint.Style.FILL);
        MazeCanvas.drawRect(rect,MazePaint);
        //update();
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Path path = new Path();

        Polygon(path, xPoints, yPoints, nPoints);

        MazePaint.setStyle(Paint.Style.FILL);
        MazeCanvas.drawPath(path, MazePaint);
        //update();
    }

    private void Polygon(Path path, int[] xPoints, int[] yPoints, int nPoints) {

        path.moveTo(xPoints[0],yPoints[0]);

        int i;
        for (i = 1; i < nPoints; i++) {
            path.lineTo(xPoints[i],yPoints[i]);
        }

        path.close();
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Path path = new Path();

        Polygon(path, xPoints, yPoints, nPoints);

        MazePaint.setStyle(Paint.Style.STROKE);
        MazeCanvas.drawPath(path, MazePaint);
        //update();
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        Log.v(TAG, "Drawing MazePanel line");
        MazePaint.setStrokeWidth(5);
        MazePaint.setStyle(Paint.Style.STROKE);
        MazeCanvas.drawLine(startX, startY, endX, endY, MazePaint);
        MazePaint.setStrokeWidth(1);
        //update();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        MazePaint.setStyle(Paint.Style.FILL);
        MazeCanvas.drawOval(x,y,x+width,y+height,MazePaint);
        //update();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        MazePaint.setStyle(Paint.Style.STROKE);
        MazeCanvas.drawArc(x,y,x+width,y+height, startAngle, arcAngle,false, MazePaint);
        //update();
    }

    @Override
    public void addMarker(float x, float y, String str) {
        MazePaint.setTextSize(30);
        MazeCanvas.drawText(str, x, y, MazePaint);
        //update();
    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {

    }
}
