package com.codebear.snakeai.control;

import com.codebear.snakeai.bean.Node;
import com.codebear.snakeai.view.GameView;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 17/6/4.
 */

public abstract class BaseControl {
    /**
     * 空位
     */
    protected static final int SPACE = 0;
    /**
     * 蛇
     */
    protected static final int SNAKE = 1;
    /**
     * 食物
     */
    protected static final int FOOD = 2;
    /**
     * 游戏视图
     */
    private GameView gameView;
    /**
     * 蛇
     */
    protected List<Node> realSnake;
    /**
     * 食物
     */
    protected Node food;
    /**
     * 每行蛇的长度
     */
    protected int lineNum;
    /**
     * 记录蛇是否在吃食物中
     */
    protected boolean running = false;
    /**
     * 蛇走的方向
     */
    protected int[][] f = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public BaseControl(GameView gameView) {
        this.gameView = gameView;
        lineNum = gameView.getLineNum();
        realSnake = new ArrayList<>();
        food = new Node();
    }

    protected void init() {
        realSnake.clear();
        food.setXY(-1, -1);
        running = true;

        for (int i = 0; i < 5; ++i) {
            realSnake.add(0, new Node(i, 0));
        }
        gameView.initGame();
        gameView.addSnake(realSnake);
        getRandomFood();
        gameView.addFood(food);
        gameView.reDraw();
    }

    /**
     * 随机生成食物位置
     */
    protected abstract void getRandomFood();

    /**
     * 真正开始游戏
     */
    protected abstract boolean runSnake();

    /**
     * 开始游戏(对外开放的接口)
     */
    public void start() {
        if (!running) {
            init();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!runSnake()) {
                            break;
                        }
                    }
                    running = false;
                }
            }).start();
        }
    }

    protected void startMoveSnake(Node nextStep) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        realSnake.add(0, nextStep);
        if (!food.nodeEquals(nextStep)) {
            gameView.move(nextStep);
            realSnake.remove(realSnake.size() - 1);
        } else {
            gameView.addSnakeBody(nextStep);
            food.setXY(-1, -1);
            getRandomFood();
            gameView.addFood(food);
        }
        gameView.reDraw();
    }

    /**
     * 根据传入的蛇获取对应的地图数组
     *
     * @param snakes
     * @return
     */
    protected int[][] getMap(List<Node> snakes) {
        int[][] map = new int[lineNum][lineNum];
        for (int i = 0; i < lineNum; ++i) {
            for (int j = 0; j < lineNum; ++j) {
                map[i][j] = SPACE;
            }
        }
        for (Node node : snakes) {
            map[node.getX()][node.getY()] = SNAKE;
        }
        if (isInMap(food)) {
            map[food.getX()][food.getY()] = FOOD;
        }

        return map;
    }

    /**
     * 获取空位
     *
     * @param snakes
     * @return
     */
    protected List<Node> getSpaceList(List<Node> snakes) {
        int[][] map = getMap(snakes);
        List<Node> nodeList = new ArrayList<>();
        for (int i = 0; i < lineNum; ++i) {
            for (int j = 0; j < lineNum; ++j) {
                if (map[i][j] == SPACE) {
                    nodeList.add(new Node(i, j));
                }
            }
        }
        return nodeList;
    }

    /**
     * 判断点是否在地图中
     *
     * @param node
     * @return
     */
    protected boolean isInMap(Node node) {
        return node.getX() >= 0 && node.getX() < lineNum && node.getY() >= 0 && node.getY() < lineNum;
    }
}
