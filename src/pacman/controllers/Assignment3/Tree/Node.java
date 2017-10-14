package pacman.controllers.Assignment3.Tree;

import java.util.ArrayList;
import java.util.List;


/**
 * Node in decision tree.
 */
public class Node {

    private final List<Node> children = new ArrayList<>();

    private final Attribute label;
    private final int edgeLabel ;

    public Node(Attribute label, int edgeLabel) {
        this.label = label;
        this.edgeLabel = edgeLabel;
    }

    public Attribute getLabel() {
        return label;
    }

    public void addChild(Node child) {
    }

    public List<Node> getChildren() {
        return this.children;
    }

}