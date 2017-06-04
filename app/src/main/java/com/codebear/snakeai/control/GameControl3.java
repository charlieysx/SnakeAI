package com.codebear.snakeai.control;

import com.codebear.snakeai.bean.Node;
import com.codebear.snakeai.bean.Snake;
import com.codebear.snakeai.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * description:
 * <p>
 * Created by CodeBear on 17/6/4.
 */

public class GameControl3 extends BaseControl {

    public GameControl3(GameView gameView) {
        super(gameView);
        sleepTime = 10;
    }

    @Override
    protected void getRandomFood() {
        List<Node> spaceList = getSpaceList(realSnake);
        if (spaceList.size() == 0) {
            return;
        }
        Random random = new Random();
        int pos = random.nextInt(spaceList.size());
        food.setXY(spaceList.get(pos).getX(), spaceList.get(pos).getY());
    }

    @Override
    protected boolean runSnake() {
        List<Node> nextStep = getNextStep();

        if (null != nextStep) {
            startMoveSnake(nextStep);
            return true;
        }
        return false;
    }

    /**
     * 记录跟着尾巴走的次数，防止进入死循环
     */
    private int loop = 0;

    /**
     * 获取下一步要走的方向
     *
     * @return
     */
    private List<Node> getNextStep() {
        if (realSnake.size() == lineNum * lineNum) {
            return null;
        }
        List<Node> nextStepList = null;
        Node nextStep = searchFood(realSnake, food);
        if (null != nextStep) {
            nextStepList = new ArrayList<>();
            List<Node> virtualSnake = new ArrayList<>();
            virtualSnake.addAll(realSnake);
            while (true) {
                Node virtualNextStep = searchFood(virtualSnake, food);
                nextStepList.add(virtualNextStep);
                virtualSnake.add(0, virtualNextStep);
                if (food.nodeEquals(virtualNextStep)) {
                    break;
                }
                virtualSnake.remove(virtualSnake.size() - 1);
            }
            Node nextStepToTail = searchFood(virtualSnake, virtualSnake.get(virtualSnake.size() - 1));
            if (null == nextStepToTail) {
                nextStepList = null;
            }
        }
        if (null == nextStepList) {
            nextStep = searchFood(realSnake, realSnake.get(realSnake.size() - 1));
            if (null == nextStep) {
                nextStep = getRandomStep(realSnake, false);
                if (null != nextStep) {
                    nextStepList = new ArrayList<>();
                    nextStepList.add(nextStep);
                    loop = 0;
                }
            } else if(loop >= realSnake.size() * 2) {
                nextStep = getRandomStep(realSnake, true);
                if (null != nextStep) {
                    nextStepList = new ArrayList<>();
                    nextStepList.add(nextStep);

                    loop = 0;
                }
            } else {
                nextStepList = new ArrayList<>();
                nextStepList.add(nextStep);
                loop++;
            }
        } else {
            loop = 0;
        }
        if (loop == realSnake.size() * 5) {
            nextStepList = null;
        }

        return nextStepList;
    }

    /**
     * 寻找食物
     *
     * @return
     */
    private Node searchFood(List<Node> snakes, Node foodNode) {
        int[][] map = getMap(snakes);
        List<Snake> routes = new ArrayList<>();
        routes.add(new Snake(snakes.get(0).getX(), snakes.get(0).getY()));
        while (!routes.isEmpty()) {
            Snake snake = routes.get(0);
            routes.remove(0);
            for (int i = 0; i < 4; ++i) {
                int tx = snake.x + f[i][0];
                int ty = snake.y + f[i][1];
                Node newNode = new Node(tx, ty);
                if (isInMap(newNode)) {
                    Snake s = new Snake(tx, ty);
                    s.nodes.addAll(snake.nodes);
                    s.nodes.add(newNode);
                    if (map[tx][ty] == SPACE) {
                        map[tx][ty] = SNAKE;
                        routes.add(s);
                    } else if (foodNode.nodeEquals(newNode)) {
                        return s.nodes.get(0);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 随机获取下一步
     *
     * @param snakes
     * @param shouldToTail
     * @return
     */
    private Node getRandomStep(List<Node> snakes, boolean shouldToTail) {
        Node first = snakes.get(0);
        List<Node> nodes = new ArrayList<>();
        int[][] map = getMap(snakes);
        for (int i = 0; i < 4; ++i) {
            int tx = first.getX() + f[i][0];
            int ty = first.getY() + f[i][1];
            Node node = new Node(tx, ty);
            if (isInMap(node) && map[tx][ty] == SPACE) {
                if(shouldToTail) {
                    List<Node> virtualSnake = new ArrayList<>();
                    virtualSnake.addAll(realSnake);
                    if(null != searchFood(virtualSnake, virtualSnake.get(virtualSnake.size() - 1))) {
                        nodes.add(new Node(tx, ty));
                    }
                } else {
                    nodes.add(new Node(tx, ty));
                }
            }
        }
        int count = nodes.size();
        if (count > 0) {
            Random random = new Random();
            return nodes.get(random.nextInt(count));
        }
        return null;
    }
}
