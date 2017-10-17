package pacman.controllers.Assignment3.Tree;

import pacman.controllers.Assignment3.Tree.Attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

import static pacman.game.Constants.MOVE;

/**
 * Node in decision tree.
 */
class Node {

    private final List<Node> children = new ArrayList<>();

    private Attribute attribute;

    private MOVE direction;

    private int attributeValue = -1;

    /**
     * Constructor that sets the attribute that this Node represents.
     *
     * @param attribute
     */
    public Node(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Takes the direction as an ordinal and sets this Node's
     * Move direction to the correct Move.
     * @param direction
     */
    public Node(int direction) {
        if (direction == 0) {
            this.direction = MOVE.UP;
        } else if (direction == 1) {
            this.direction = MOVE.RIGHT;
        } else if (direction == 2) {
            this.direction = MOVE.DOWN;
        } else if (direction == 3) {
            this.direction = MOVE.LEFT;
        }
    }

    private void setAttributeValue(int attributeValue) {
        this.attributeValue = attributeValue;
    }

    public void addChild(Node child) {
        child.setAttributeValue(children.size());
        children.add(child);
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public MOVE getDirection() {
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