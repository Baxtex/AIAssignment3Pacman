package pacman.controllers.Assignment3.Tree.Attribute;

/**
 * Enum that has all the attributes that a DataTuple has. The value associated with each enum corresponds
 * to the number of possible values that the attribute can have. I.e isBlinkyEdible(2) can be either 0 or 1, false or true, and therefore returns 2 as the
 * number of values it can be.
 */
public enum Attribute {
    isBlinkyEdible(2),
    isInkyEdible(2),
    isPinkyEdible(2),
    isSueEdible(2),

    blinkyDist(4),
    inkyDist(4),
    pinkyDist(4),
    sueDist(4),

    blinkyDir(4),
    inkyDir(4),
    pinkyDir(4),
    sueDir(4),
    pacmanPosition(5),
    pacmanLivesLeft(4),
    currentScore(3),
    totalGameTime(3),
    currentLevelTime(3),
    numOfPillsLeft(3),
    numPowerPillsLeft(3),
    numberOfNodesInLevel(3),
    numberOfTotalPillsInLevel(3),
    numberOfTotalPowerPillsInLevel(3);

    private final int nbrOfAttributeValues;

    Attribute(int value) {
        this.nbrOfAttributeValues = value;
    }

    public int getNbrOfAttributeValues() {
        return nbrOfAttributeValues;
    }
}
