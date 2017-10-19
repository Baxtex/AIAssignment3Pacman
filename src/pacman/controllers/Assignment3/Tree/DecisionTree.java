package pacman.controllers.Assignment3.Tree;

import dataRecording.DataTuple;
import pacman.game.Game;

import static pacman.controllers.Assignment3.Tree.Attribute.Utility.getAttributeValue;
import static pacman.game.Constants.MOVE;

public class DecisionTree {

    private final Node root;

    public DecisionTree(Node root) {
        this.root = root;
    }

    /**
     * Finds a move given the state of the game.
     */
    public MOVE findMove(Game game) {
        return findMove(new DataTuple(game, MOVE.NEUTRAL));
    }

    /**
     * Returns a ordinal of the move to make given that dataTuple was sent to the tree.
     */
    public MOVE findMove(DataTuple dataTuple) {
        StringBuilder treeTravelerStr = new StringBuilder();
        Node node = root;
        while (!node.isLeaf()) {
            int attributeValue = getAttributeValue(dataTuple, node.getAttribute());
            treeTravelerStr.append(node.getAttribute().name()).append("->").append(attributeValue).append(" ");
            node = node.getChildren().get(attributeValue);
        }
        treeTravelerStr.append(":").append(node.getDirection().name());
        System.out.println(treeTravelerStr); //Prints the traversal to the console.
        return node.getDirection();
    }

    /**
     * Prints a visual representation of the tree to the console.
     */
    public void printTree() {
        root.print(1);
    }
}