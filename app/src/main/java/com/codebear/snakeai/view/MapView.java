package com.codebear.snakeai.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.codebear.snakeai.bean.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 17/6/1.
 */

public class MapView extends View implements GameView {
    /**
     * 地图宽度(由于地图是正方形,所以地图高度等于地图宽度)
     */
    private int mPanelWidth;
    /**
     * 蛇身的行高
     */
    private float mLineHeight;
    /**
     * 地图的最大行数
     */
    private int MAX_LINE_NUM = 12;
    /**
     * 食物的画笔
     */
    private Paint foodPaint;
    /**
     * 蛇的画笔
     */
    private Paint snakePaint;
    /**
     * 地图的画笔
     */
    private Paint boardPaint;
    /**
     * 蛇
     */
    private List<Node> snakeBody;
    /**
     * 食物
     */
    private Node food;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        boardPaint = new Paint();
        foodPaint = new Paint();
        snakePaint = new Paint();

        //初始化地图画笔
        boardPaint.setColor(0x55000000);
        //设置抗锯齿
        boardPaint.setAntiAlias(true);
        //设置防抖动
        boardPaint.setDither(true);
        //设置为空心
        boardPaint.setStyle(Paint.Style.FILL);

        //初始化食物画笔
        foodPaint.setColor(Color.YELLOW);
        //设置抗锯齿
        foodPaint.setAntiAlias(true);
        //设置防抖动
        foodPaint.setDither(true);
        //设置为实心
        foodPaint.setStyle(Paint.Style.FILL);

        //初始化蛇身画笔
        snakePaint.setColor(Color.BLACK);
        //设置抗锯齿
        snakePaint.setAntiAlias(true);
        //设置防抖动
        snakePaint.setDither(true);
        //设置为实心
        snakePaint.setStyle(Paint.Style.FILL);

        snakeBody = new ArrayList<>();
        food = new Node();
    }

    /**
     * 测量宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        //此处的逻辑判断是处理当我们自定义的View被嵌套在ScrollView中时,获得的测量模式
        // 会是UNSPECIFIED
        // 使得到的widthSize或者heightSize为0
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        Log.d("pyh", "onMeasure: width" + width + "height" + heightSize);
        //调用此方法使我们的测量结果生效
        setMeasuredDimension(width, width);
    }

    /**
     * 宽高变化时调用此方法
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //此处的参数w就是在onMeasure()方法中设置的自定义View的大小
        //计算出棋盘宽度和行高
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE_NUM;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawFood(canvas);
        drawSnake(canvas);
    }

    /**
     * 绘制地图
     *
     * @param canvas
     */
    private synchronized void drawBoard(Canvas canvas) {
        Rect rect = new Rect();
        rect.set(0, 0, mPanelWidth, mPanelWidth);
        canvas.drawRect(rect, boardPaint);
    }

    /**
     * 绘制食物
     *
     * @param canvas
     */
    private synchronized void drawFood(Canvas canvas) {
        if (food.getX() >= 0 && food.getY() >= 0 && food.getX() < MAX_LINE_NUM && food.getY() < MAX_LINE_NUM) {
            float x = (mLineHeight / 2) + mLineHeight * food.getX();
            float y = (mLineHeight / 2) + mLineHeight * food.getY();
            canvas.drawCircle(x, y, mLineHeight / 2, foodPaint);
        }
    }

    /**
     * 绘制蛇
     *
     * @param canvas
     */
    private synchronized void drawSnake(Canvas canvas) {
        for (int i = 0; i < snakeBody.size(); ++i) {
            Node node = snakeBody.get(i);
            float x = (mLineHeight / 2) + mLineHeight * node.getX();
            float y = (mLineHeight / 2) + mLineHeight * node.getY();
            canvas.drawCircle(x, y, mLineHeight / 2, snakePaint);
            if (i == 0) {
                canvas.drawCircle(x, y, mLineHeight / 5, foodPaint);
            }
            if(i == snakeBody.size() - 1) {
                canvas.drawCircle(x, y, mLineHeight / 10, foodPaint);
            }
        }
    }

    @Override
    public void initGame() {
        snakeBody.clear();
        food.setXY(-1, -1);
    }

    /**
     * 批量添加蛇身体
     *
     * @param nodes
     */
    private void addAllBody(List<Node> nodes) {
        if (null != nodes && nodes.size() > 0) {
            snakeBody.addAll(nodes);
        }
    }

    /**
     * 添加蛇身体
     *
     * @param node
     */
    private void addBody(Node node) {
        if (null != node) {
            snakeBody.add(0, node);
        }
    }

    /**
     * 移除蛇尾巴
     */
    private void removeTail() {
        snakeBody.remove(snakeBody.size() - 1);
    }

    /**
     * 设置食物
     *
     * @param node
     */
    private void setFood(Node node) {
        if (null != node) {
            food = node;
        }
    }

    /**
     * 重画，在添加蛇、食物等信息后需要调用该方法绘制
     */
    @Override
    public synchronized void reDraw() {
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    @Override
    public synchronized void move(Node node) {
        addBody(node);
        removeTail();
    }

    @Override
    public void addFood(Node node) {
        setFood(node);
    }

    @Override
    public void addSnakeBody(Node node) {
        addBody(node);
    }

    @Override
    public void addSnake(List<Node> nodes) {
        addAllBody(nodes);
    }

    @Override
    public int getLineNum() {
        return MAX_LINE_NUM;
    }
}
