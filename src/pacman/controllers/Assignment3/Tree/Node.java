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

    public String toString() {
        if (edgeLabel == -1) {
            return attribute.name();
        } else if (attribute != null) {
            return attribute.name() + ":" + String.valueOf(edgeLabel);

        } else {

            if (edgeLabel == 0) {
                return "UP";
            } else if (edgeLabel == 1) {
                return "RIGHT";
            } else if (edgeLabel == 2) {
                return "DOWN";
            } else if (edgeLabel == 3) {
                return "LEFT";
            } else {
                return "NEUTRAL";
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
}