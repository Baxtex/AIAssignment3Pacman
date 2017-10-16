package pacman.controllers.Assignment3.Tree;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import static pacman.controllers.Assignment3.Tree.Attribute.Utility.getAttributeValue;
import static pacman.game.Constants.MOVE;

public class DecisionTree {

    private final Node root;

    public DecisionTree(Node root) {
        this.root = root;
    }

    //Finds a move given the state of the game.
    public Constants.MOVE findMove(Game game) {

        DataTuple dataTuple = new DataTuple(game, Constants.MOVE.NEUTRAL);
        int moveOrdinal = findMove(dataTuple);

        switch (moveOrdinal) {
            case 0:
                return MOVE.UP;
            case 1:
                return MOVE.RIGHT;
            case 2:
                return MOVE.DOWN;
            case 3:
                return MOVE.LEFT;
        }
        assert moveOrdinal > -1 && moveOrdinal < 5;
        return Constants.MOVE.NEUTRAL;
    }

    //Returns a ordinal of the move to make given that datatuple was sent to the tree.
    public int findMove(DataTuple dataTuple) {
        String treeTraverser = "";
        Node node = root;
        while (!node.isLeaf()) {
            int attributeValue = getAttributeValue(dataTuple, node.getAttribute());
            treeTraverser += node.getAttribute().name() + "->" +attributeValue + " ";
            node = node.getChildren().get(attributeValue);
        }
        treeTraverser += ":" + node.getDirection().name();
        System.out.println(treeTraverser);
        return node.getDirection().ordinal();
    }

    //Prints a visual representation of the tree to the console.
    public void printTree() {
        root.print(1);
    }
}