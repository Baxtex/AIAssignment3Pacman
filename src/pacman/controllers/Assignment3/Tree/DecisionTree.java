package pacman.controllers.Assignment3.Tree;

import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

public class DecisionTree {

    private final Node root;


    public DecisionTree(Node root) {
        this.root = root;
    }


    public Constants.MOVE findMove(Game game) {
        return null;
    }

    public int findMove(DataTuple dataTuple) {

        return 0;
    }

    public void printTree() {
        root.print(1);
    }
}
