package pacman.controllers.Assignment3.Tree;

import java.util.ArrayList;
import java.util.List;


/**
 * Node in decision tree.
 */
public class Node {

    private final List<Node> children = new ArrayList<>();

    private Attribute label;
    private int edgeLabel = -1;
    private final boolean leaf;

    public Node(Attribute label, int edgeLabel, boolean leaf) {
        this.label = label;
        this.edgeLabel = edgeLabel;
        this.leaf = leaf;
    }

    public Attribute getLabel() {
        return label;
    }

    public void addChild(Node child) {
        if(!leaf){
            this.children.add(child);
        }
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }
}