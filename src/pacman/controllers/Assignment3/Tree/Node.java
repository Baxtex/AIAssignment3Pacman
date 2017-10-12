package pacman.controllers.Assignment3.Tree;

import java.util.ArrayList;
import java.util.List;

import static pacman.game.Constants.MOVE;

/**
 * Node in decision tree.
 */
public class Node {

    private final List<Node> children = new ArrayList<>();

    private Node parent;
    private final MOVE move;
    private Attribute attribute;

    public Node(MOVE move) {
        this.move = move;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public Node getParentNode() {
        return this.parent;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }
}