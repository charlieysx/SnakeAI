package com.codebear.snakeai.bean;

/**
 * description:
 * <p>
 * Created by CodeBear on 17/6/1.
 */

public class Node {
    /**
     * 节点的坐标
     */
    private int x;
    private int y;

    public Node() {
        x = y = -1;
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean nodeEquals(Node n) {
        return null != n && x == n.x && y == n.y;
    }
}
