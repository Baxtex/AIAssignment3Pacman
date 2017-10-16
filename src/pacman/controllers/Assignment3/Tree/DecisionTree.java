package pacman.controllers.Assignment3.Tree;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import static pacman.controllers.Assignment3.Tree.Attribute.Utility.getAttributeValue;

public class DecisionTree {

    private final Node root;

    public DecisionTree(Node root) {
        this.root = root;
    }

    //TODO implement.
    public Constants.MOVE findMove(Game game) {

        DataTuple dataTuple = new DataTuple(game, Constants.MOVE.NEUTRAL);
        int moveOrdinal = findMove(dataTuple);

        switch (moveOrdinal) {
            case 0:
                return Constants.MOVE.UP;
            case 1:
                return Constants.MOVE.RIGHT;
            case 2:
                return Constants.MOVE.DOWN;
            case 3:
                return Constants.MOVE.LEFT;
        }
        assert moveOrdinal > -1 && moveOrdinal < 5;
        return Constants.MOVE.NEUTRAL;
    }

    //Returns the resulting direction from putting the given tuple in the tree.
    public int findMove(DataTuple dataTuple) {
        Node node = root;
        while (!node.isLeaf()) {
            int attributeValue = getAttributeValue(dataTuple, root.getAttribute());
            node = node.getChildren().get(attributeValue);
        }
        return node.getDirection().ordinal();
    }

    //Prints a visual representation of the tree to the console.
    public void printTree() {
        root.print(1);
    }
}