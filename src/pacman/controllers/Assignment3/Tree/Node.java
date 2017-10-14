package pacman.controllers.Assignment3.Tree;

import java.util.ArrayList;
import java.util.List;


/**
 * Node in decision tree.
 */
public class Node {

    private final List<Node> children = new ArrayList<>();

    private final Attribute attribute;
    private final int edgeLabel;

    public Node(Attribute attribute, int edgeLabel) {
        this.attribute = attribute;
        this.edgeLabel = edgeLabel;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() {
        return this.children;
    }

}