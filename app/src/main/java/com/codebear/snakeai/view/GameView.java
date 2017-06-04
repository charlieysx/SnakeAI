package com.codebear.snakeai.view;

import com.codebear.snakeai.bean.Node;

import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 17/6/1.
 */

public interface GameView {
    /**
     * 移动
     *
     * @param node
     */
    void move(Node node);

    /**
     * 添加食物
     *
     * @param node
     */
    void addFood(Node node);

    /**
     * 添加蛇身体
     *
     * @param node
     */
    void addSnakeBody(Node node);

    /**
     * 添加蛇
     *
     * @param nodes
     */
    void addSnake(List<Node> nodes);

    /**
     * 重绘
     */
    void reDraw();

    /**
     * 获取每行食物个数
     *
     * @return
     */
    int getLineNum();

    /**
     * 初始化
     */
    void initGame();
}
