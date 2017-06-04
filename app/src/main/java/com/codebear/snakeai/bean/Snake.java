package com.codebear.snakeai.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 17/6/1.
 */

public class Snake {
    /**
     * 路径
     */
    public List<Node> nodes;
    public int x;
    public int y;

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        nodes = new ArrayList<>();
    }
}
