package pacman.controllers.Assignment3.Tree.Attribute;

import dataRecording.DataTuple;

public class Utility {

    /**
     * Returns the discrete attribute value for a given tuple and specified attribute.
     */
    public static int getAttributeValue(DataTuple tuple, Attribute attribute) {
        switch (attribute) {
            case isBlinkyEdible:
                return tuple.normalizeBoolean(tuple.isBlinkyEdible);
            case isInkyEdible:
                return tuple.normalizeBoolean(tuple.isInkyEdible);
            case isPinkyEdible:
                return tuple.normalizeBoolean(tuple.isPinkyEdible);
            case isSueEdible:
                return tuple.normalizeBoolean(tuple.isSueEdible);
            case blinkyDir:
                return tuple.blinkyDir.ordinal();
            case inkyDir:
                return tuple.inkyDir.ordinal();
            case pinkyDir:
                return tuple.pinkyDir.ordinal();
            case sueDir:
                return tuple.sueDir.ordinal();
            case blinkyDist:
                return tuple.discretizeDistance(tuple.blinkyDist).ordinal();
            case inkyDist:
                return tuple.discretizeDistance(tuple.inkyDist).ordinal();
            case pinkyDist:
                return tuple.discretizeDistance(tuple.pinkyDist).ordinal();
            case sueDist:
                return tuple.discretizeDistance(tuple.sueDist).ordinal();
            case numberOfTotalPillsInLevel:
                return tuple.discretizeNumberOfPowerPills(tuple.numberOfTotalPillsInLevel).ordinal();
            case numOfPillsLeft:
                return tuple.discretizeNumberOfPowerPills(tuple.numOfPillsLeft).ordinal();
            case numberOfTotalPowerPillsInLevel:
                return tuple.discretizeNumberOfPowerPills(tuple.numberOfTotalPowerPillsInLevel).ordinal();
            case numPowerPillsLeft:
                return tuple.discretizeNumberOfPowerPills(tuple.numOfPowerPillsLeft).ordinal();
            case pacmanPosition:
                return tuple.discretizePosition(tuple.pacmanPosition).ordinal();
            case currentScore:
                return tuple.discretizeCurrentScore(tuple.currentScore).ordinal();
            case currentLevelTime:
                return tuple.discretizeCurrentLevelTime(tuple.currentLevelTime).ordinal();
            case totalGameTime:
                return tuple.discretizeTotalGameTime(tuple.totalGameTime).ordinal();
            case pacmanLivesLeft:
                return tuple.pacmanLivesLeft;
        }
        throw new IllegalStateException("Attribute value was not found!");
    }
}
