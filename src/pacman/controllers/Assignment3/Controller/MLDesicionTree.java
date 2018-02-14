package pacman.controllers.Assignment3.Controller;

import pacman.controllers.Assignment3.Tree.DecisionTree;
import pacman.controllers.Assignment3.Tree.TreeBuilder;
import pacman.controllers.Controller;
import pacman.game.Game;

import static pacman.game.Constants.MOVE;

/**
 * Controller that decides what move Pacman should do based on the decision in a decision tree built
 * with machine learning.
 */
public class MLDesicionTree extends Controller<MOVE> {

    private final DecisionTree tree;

    /**
     * Constructor that instantiates a new TreeBuilder that builds a decision tree.
     */
    public MLDesicionTree() {
        TreeBuilder treeBuilder = new TreeBuilder();
        tree = treeBuilder.buildDecisionTree();
    }

    /**
     * Returns the MOVE direction for Pacman to do.
     */
    public MOVE getMove(Game game, long timeDue) {
        return tree.findMove(game);
    }
}
