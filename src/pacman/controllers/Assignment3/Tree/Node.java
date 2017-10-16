package pacman.controllers.Assignment3.Tree;

import pacman.game.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Node in decision tree.
 */
public class Node {

    private final List<Node> children = new ArrayList<>();

    private final Attribute attribute;

    private Constants.MOVE direction;

    int attributeValue = -1;

    public Node(Attribute attribute) {
        this.attribute = attribute;
    }

    public Node(Attribute attribute, int direction) {
        this.attribute = attribute;
        if (direction == 0) {
            this.direction = Constants.MOVE.UP;
        } else if (direction == 1) {
            this.direction = Constants.MOVE.RIGHT;
        } else if (direction == 2) {
            this.direction = Constants.MOVE.DOWN;
        } else if (direction == 3) {
            this.direction = Constants.MOVE.LEFT;
        }

    }

    public void setAttributeValue(int i) {
        attributeValue = i;
    }

    public void addChild(Node child) {
        child.setAttributeValue(children.size());
        children.add(child);
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public Constants.MOVE getDirection() {
        return direction;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public String toString() {

        if (attributeValue != -1) {
            if (attribute != null) {
                return attributeValue + ":" + this.attribute.name();
            } else {
                return attributeValue + ":" + this.direction.name();
            }
        } else {
            if (attribute != null) {
                return this.attribute.name();
            } else {
                return this.direction.name();
            }
        }


    }

    public void print(int level) {
        for (int i = 1; i < level; i++) {
            System.out.print("|\t");
        }
        System.out.println("|" + this.toString());
        for (Node child : children) {
            child.print(level + 1);
        }
    }

    public Attribute getAttribute() {
        return attribute;
    }

}