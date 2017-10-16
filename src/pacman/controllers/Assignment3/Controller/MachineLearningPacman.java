package pacman.controllers.Assignment3.Controller;

import pacman.controllers.Assignment3.Tree.DecisionTree;
import pacman.controllers.Assignment3.Tree.TreeBuilder;
import pacman.controllers.Controller;

import pacman.game.Game;

import static pacman.game.Constants.MOVE;

/**
 * Controller that decides what move pacman should do based on the decision in a decision tree built
 * with machine learning.
 */
public class MachineLearningPacman extends Controller<MOVE> {


    private DecisionTree tree;

    public MachineLearningPacman() {
        TreeBuilder treeBuilder = new TreeBuilder();
        tree = treeBuilder.buildDecisionTree();
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        return tree.findMove(game);
    }
}
