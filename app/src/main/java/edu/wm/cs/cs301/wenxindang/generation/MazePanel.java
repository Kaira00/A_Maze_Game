package edu.wm.cs.cs301.wenxindang.generation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import edu.wm.cs.cs301.wenxindang.R;

@SuppressWarnings("ALL")
public class MazePanel extends View implements P5Panel{

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private Drawable background;

    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        initPaint();
        background = getResources().getDrawable(R.drawable.b);
    }
    public MazePanel(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        initPaint();
        background = getResources().getDrawable(R.drawable.b);
    }

    private void initPaint(){
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.teal_700));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(36);
        paint.setStrokeWidth(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
    }

    /**
     * Commits all accumulated drawings to the UI.
     * Substitute for MazePanel.update method.
     */
    @Override
    public void commit() {
        invalidate();
    }

    /**
     * Tells if instance is able to draw. This ability depends on the
     * context, for instance, in a testing environment, drawing
     * may be not possible and not desired.
     * Substitute for code that checks if graphics object for drawing is not null.
     *
     * @return true if drawing is possible, false if not.
     */
    @Override
    public boolean isOperational() {
        return false;
    }

    /**
     * Sets the color for future drawing requests. The color setting
     * will remain in effect till this method is called again and
     * with a different color.
     * Substitute for Graphics.setColor method.
     *
     * @param rgb gives the red green and blue encoded value of the color
     */
    @Override
    public void setColor(int rgb) {
        paint.setColor(rgb);
    }

    public void setARGB(int a,int r,int g,int b) {
        paint.setARGB(a,r,g,b);
    }

    public void setAlpha(int a) {
        paint.setAlpha(a);
    }

    public void setTypeface(Typeface typeface) {
        paint.setTypeface(typeface);
    }

    public void setTextSize(int textSize){
        paint.setTextSize(textSize);
    }


    /**
     * Returns the RGB value for the current color setting.
     *
     * @return integer RGB value
     */
    @Override
    public int getColor() {
        return paint.getColor();
    }

    /**
     * Draws two solid rectangles to provide a background.
     * Note that this also erases any previous drawings.
     * The color setting adjusts to the distance to the exit to
     * provide an additional clue for the user.
     * Colors transition from black to gold and from grey to green.
     * Substitute for FirstPersonView.drawBackground method.
     *
     * @param percentToExit gives the distance to exit
     */
    @Override
    public void addBackground(float percentToExit) {
        /*paint.setARGB(200,246,198,42);
        addFilledRectangle(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT/2);
//        paint.setColor(getBackgroundColor(percentToExit, false));
        paint.setARGB(200,202,90,41);
        addFilledRectangle(0, Constants.VIEW_WIDTH/2, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT/2);*/
        background.setBounds(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
        background.draw(canvas);
//        paint.setARGB(150,186,182,209);
//        paint.setARGB(100,202,90,41);
        paint.setARGB(90,246,198,42);
        addFilledRectangle(0, Constants.VIEW_WIDTH/2, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT/2);
    }


    /**
     * Adds a filled rectangle.
     * The rectangle is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis.
     * Substitute for Graphics.fillRect() method
     *
     * @param x      is the x-coordinate of the top left corner
     * @param y      is the y-coordinate of the top left corner
     * @param width  is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(x, y, x + width, y + height);
        canvas.drawRect(rect, paint);
    }

    /**
     * Adds a filled polygon.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.fillPolygon() method
     *
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }
        path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(path, paint);
    }

    /**
     * Adds a polygon.
     * The polygon is not filled.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.drawPolygon method
     *
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }
        path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(path, paint);
    }

    /**
     * Adds a line.
     * A line is described by {@code (x,y)} coordinates for its
     * starting point and its end point.
     * Substitute for Graphics.drawLine method
     *
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX   is the x-coordinate of the end point
     * @param endY   is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX,startY,endX,endY,paint);
    }

    /**
     * Adds a filled oval.
     * The oval is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis. An oval is
     * described like a rectangle.
     * Substitute for Graphics.fillOval method
     *
     * @param x      is the x-coordinate of the top left corner
     * @param y      is the y-coordinate of the top left corner
     * @param width  is the width of the oval
     * @param height is the height of the oval
     */
    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(x, y, x + width, y + height);
        canvas.drawOval(oval, paint);
    }

    /**
     * Adds the outline of a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at startAngle and extends for arcAngle degrees,
     * using the current color. Angles are interpreted such that 0 degrees
     * is at the 3 o'clock position. A positive value indicates a counter-clockwise
     * rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is
     * (x, y) and whose size is specified by the width and height arguments.
     * The resulting arc covers an area width + 1 pixels wide
     * by height + 1 pixels tall.
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the start
     * and end of the arc segment will be skewed farther along the longer
     * axis of the bounds.
     * Substitute for Graphics.drawArc method
     *
     * @param x          the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y          the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width      the width of the arc to be drawn.
     * @param height     the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle   the angular extent of the arc, relative to the start angle.
     */
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF(x, y, x + width, y + height);
        canvas.drawArc(oval, startAngle, arcAngle, true, paint);
    }

    /**
     * Adds a string at the given position.
     * Substitute for CompassRose.drawMarker method
     *
     * @param x   the x coordinate
     * @param y   the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str) {
        paint.setStyle(Paint.Style.FILL);
        /*paint.setColor(Color.BLACK);*/
        paint.setTextSize(80);
        canvas.drawText(str, x, y, paint);
    }
}
