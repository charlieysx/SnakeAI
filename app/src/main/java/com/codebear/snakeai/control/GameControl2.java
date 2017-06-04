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

public class GameControl2 extends BaseControl {

    public GameControl2(GameView gameView) {
        super(gameView);
        sleepTime = 50;
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
        List<Node> nextStepList = searchFood(realSnake, food);
        if (null != nextStepList) {
            List<Node> virtualSnake = new ArrayList<>();
            virtualSnake.addAll(realSnake);
            for(Node node : nextStepList) {
                virtualSnake.add(0, node);
                virtualSnake.remove(virtualSnake.size() - 1);
            }
            List<Node> nextStepToTail = searchFood(virtualSnake, virtualSnake.get(virtualSnake.size() - 1));
            if (null == nextStepToTail) {
                nextStepList = null;
            }
        }
        if (null == nextStepList) {
            nextStepList = searchFood(realSnake, realSnake.get(realSnake.size() - 1));
            if (null == nextStepList) {
                Node nextStep = getRandomStep(realSnake);
                if(null != nextStep) {
                    nextStepList = new ArrayList<>();
                    nextStepList.add(nextStep);
                    loop = 0;
                }
            } else {
                loop++;
            }
        } else {
            loop = 0;
        }
        if (loop == realSnake.size() * 2) {
            nextStepList = null;
        }

        return nextStepList;
    }

    /**
     * 寻找食物
     *
     * @return
     */
    private List<Node> searchFood(List<Node> snakes, Node foodNode) {
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
                        return s.nodes;
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
     * @return
     */
    private Node getRandomStep(List<Node> snakes) {
        Node first = snakes.get(0);
        List<Node> nodes = new ArrayList<>();
        int[][] map = getMap(snakes);
        for (int i = 0; i < 4; ++i) {
            int tx = first.getX() + f[i][0];
            int ty = first.getY() + f[i][1];
            Node node = new Node(tx, ty);
            if (isInMap(node) && map[tx][ty] == SPACE) {
//                for (int i2 = 0; i2 < 4; ++i2) {
//                    int tx2 = tx + f[i2][0];
//                    int ty2 = ty + f[i2][1];
//                    if (isInMap(new Node(tx2, ty2)) && map[tx2][ty2] == SPACE) {
                        nodes.add(new Node(tx, ty));
//                    }
//                }
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
