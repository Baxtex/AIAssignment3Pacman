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
    blinkyDist(6),
    inkyDist(6),
    pinkyDist(6),
    sueDist(6),
    blinkyDir(4),
    inkyDir(4),
    pinkyDir(4),
    sueDir(4),
    pacmanPosition(5),
    pacmanLivesLeft(4),
    currentScore(5),
    totalGameTime(5),
    currentLevelTime(5),
    numOfPillsLeft(5),
    numPowerPillsLeft(5),
    numberOfNodesInLevel(5),
    numberOfTotalPillsInLevel(5),
    numberOfTotalPowerPillsInLevel(5);

    private int nbrOfAttributeValues;

    Attribute(int value) {
        this.nbrOfAttributeValues = value;
    }

    public int getNbrOfAttributeValues() {
        return nbrOfAttributeValues;
    }
}
