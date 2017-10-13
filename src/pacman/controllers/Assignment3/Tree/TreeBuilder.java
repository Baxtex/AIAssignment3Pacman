package pacman.controllers.Assignment3.Tree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.*;
import java.util.stream.Collectors;

import static pacman.game.Constants.MOVE;

/**
 * Should build a decision tree by using the ID3 approach.
 */
public class TreeBuilder {

    //TODO Maybe save discrete values directly in the tuple, would  probably make some statements more clean.
    public DecisionTree buildDecisionTree() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        DataTuple[] trainingSet = getTrainingSet(dataSet);
        DataTuple[] testSet = getTestSet(dataSet, trainingSet.length);
        ArrayList<Attribute> attributes = initializeAttributesList();

        Node tree = generateTree(trainingSet, attributes);

        return null;
    }

    private DataTuple[] getTrainingSet(DataTuple[] dataSet) {
        int sixtyPercent = (int) (dataSet.length * 0.6);
        DataTuple[] trainingSet = new DataTuple[sixtyPercent];
        for (int i = 0; i < sixtyPercent; i++) {
            trainingSet[i] = dataSet[i];
        }
        return trainingSet;
    }

    private DataTuple[] getTestSet(DataTuple[] dataSet, int startIndex) {
        int fortyPercent = (int) (dataSet.length - startIndex);
        DataTuple[] testSet = new DataTuple[fortyPercent];
        for (int i = 0; startIndex < dataSet.length; i++) {
            testSet[i] = dataSet[startIndex];
            startIndex++;
        }
        return testSet;
    }

    //TODO Just some random attributes for now, maybe select other attributes.
    private ArrayList<Attribute> initializeAttributesList() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(Attribute.isBlinkyEdible);
        attributes.add(Attribute.isInkyEdible);
        attributes.add(Attribute.isPinkyEdible);
        attributes.add(Attribute.isSueEdible);
        attributes.add(Attribute.blinkyDir);
        attributes.add(Attribute.inkyDir);
        attributes.add(Attribute.pinkyDir);
        attributes.add(Attribute.sueDir);
        //   attributes.add(Attribute.numberOfTotalPillsInLevel);
        attributes.add(Attribute.numOfPillsLeft);
        //  attributes.add(Attribute.numberOfTotalPowerPillsInLevel);
        attributes.add(Attribute.numPowerPillsLeft);

        return attributes;
    }

    private Node generateTree(DataTuple[] trainingSet, ArrayList<Attribute> attributes) {
        Node node;

        if (allTuplesSameClass(trainingSet)) {
            node = new Node(trainingSet[0].DirectionChosen);
        } else if (attributes.isEmpty()) {
            node = new Node(getMajorityClass(trainingSet));
        } else {
            node = computeAttributeNode(trainingSet, attributes);
        }
        return node;
    }

    private boolean allTuplesSameClass(DataTuple[] trainingSet) {
        MOVE move = trainingSet[0].DirectionChosen;
        for (int i = 1; i < trainingSet.length; i++) {
            if (trainingSet[i].DirectionChosen != move) {
                return false;
            }
        }
        return true;
    }

    private MOVE getMajorityClass(DataTuple[] trainingSet) {
        MOVE majorityLabel = null;
        long majorityValue = 0;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            long nbrOfThisDirection = Arrays.stream(trainingSet).filter(a -> a.DirectionChosen.ordinal() == finalI).count();
            if (nbrOfThisDirection > majorityValue) {
                if (i == 0) {
                    majorityLabel = MOVE.UP;
                } else if (i == 1) {
                    majorityLabel = MOVE.RIGHT;
                } else if (i == 2) {
                    majorityLabel = MOVE.DOWN;
                } else if (i == 3) {
                    majorityLabel = MOVE.LEFT;
                } else if (i == 4) {
                    majorityLabel = MOVE.NEUTRAL;
                }
                majorityValue = nbrOfThisDirection;
            }
        }
        return majorityLabel; //TODO NEEDS TESTING!!!
    }

    private Node computeAttributeNode(DataTuple[] trainingSet, ArrayList<Attribute> attributes) {
        Node node;
        //1. Call the attribute selection method on D and the attribute list, in order to choose the current attribute A: S(D, attribute list) -> A:
        Attribute attribute = attributeSelection(trainingSet, attributes);
        // 2. Label N as A and remove A from the attribute list:
        node = new Node(attribute);

        // 3. For each value aj in attribute A:
        //    a) Separate all tuples in D so that attribute A takes the value aj , creating the subset Dj:
        int numberOfSubSets = getNumberOfSubsets(attribute);
        List<List<DataTuple>> subSetsOfAttributesValue = new ArrayList<>(numberOfSubSets);

        for (DataTuple tuple : trainingSet) {
            int tupleAttributeValue = getAttributeValue(tuple, attribute);
            subSetsOfAttributesValue.get(tupleAttributeValue).add(tuple);
        }

        for (List<DataTuple> subSet : subSetsOfAttributesValue) {
            if (subSet.isEmpty()) {
                //    b) If Dj is empty, add a child node to N labeled with the majority class in D:
                node.addChild(new Node(getMajorityClass(trainingSet)));
            } else {
                //    c) Otherwise, add the resulting node from calling Generate_Tree(Dj , attribute) as a child node to N:
                node.addChild(generateTree(subSet.toArray(new DataTuple[subSet.size()]), attributes));
            }
        }
        //4. Return N:
        return node;
    }

    //TODO Add all the relevant Attributes.
    private int getAttributeValue(DataTuple tuple, Attribute attribute) {
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
            case numberOfTotalPillsInLevel:
                int nbr = tuple.discretizeNumberOfPowerPills(tuple.numberOfTotalPillsInLevel).ordinal();
                return nbr;
            case numOfPillsLeft:
                return tuple.discretizeNumberOfPowerPills(tuple.numOfPillsLeft).ordinal();
            case numberOfTotalPowerPillsInLevel:
                return tuple.discretizeNumberOfPowerPills(tuple.numberOfTotalPowerPillsInLevel).ordinal();
            case numPowerPillsLeft:
                return tuple.discretizeNumberOfPowerPills(tuple.numOfPowerPillsLeft).ordinal();
        }
        return -100000; //Attribute not found
    }

    //Should have the same kind of switch as in getAttributeValue.
    //TODO All all relevant attributes and a switch statement.
    private int getNumberOfSubsets(Attribute attribute) {
        switch (attribute) {
            case isBlinkyEdible:
                return 2;
            case isInkyEdible:
                return 2;
            case isPinkyEdible:
                return 2;
            case isSueEdible:
                return 2;
            case blinkyDir:
                return 4;
            case inkyDir:
                return 4;
            case pinkyDir:
                return 4;
            case sueDir:
                return 4;
            case numberOfTotalPillsInLevel:
                return 5;
            case numOfPillsLeft:
                return 5;
            case numberOfTotalPowerPillsInLevel:
                return 5;
            case numPowerPillsLeft:
                return 5;
        }
        return -100000; //Attribute not found
    }

    /**
     * Attribute selection method which is built with the ID3 algorithm.
     * ID3 is short for Iterative Dichotomiser.
     * It uses information gain as the criterion to choose attribute A: a function calculates how much
     * information gain provides every candidate attribute in the list. The one with the highest gain is chosen to
     * become A, the optimal attribut.
     */
    private Attribute attributeSelection(DataTuple[] trainingSet, List<Attribute> attributes) {
        double averageInformationGain = calculateAverageInformationGain(trainingSet, attributes);

        List<Pair<Attribute, Double>> attributesInformationGain = new ArrayList<>(attributes.size());

        for (Attribute attribute : attributes) {
            double attributeInformationGain = averageInformationGain - calculateAttributeGain(trainingSet, attribute);
            attributesInformationGain.add(new Pair(attribute, attributeInformationGain));
        }

        Pair<Attribute, Double> res = Collections.max(attributesInformationGain, Comparator.comparingDouble(p -> (double) p.second));
        return res.first;

    }

    //Generic pair/wrapper class that contains two values.
    private class Pair<T, U> {
        public final T first;
        public final U second;

        private Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    private double calculateAttributeGain(DataTuple[] trainingSet, Attribute attribute) {
        int totalNumberOfTuples = trainingSet.length;
        double informationGain = 0.0;
        int nbrOfSubsets = getNumberOfSubsets(attribute);
        for (int i = 0; i < nbrOfSubsets; i++) {
            int finalI = i;
            List<DataTuple> subSet = Arrays.stream(trainingSet).filter(a -> getAttributeValue(a, attribute) == finalI).collect(Collectors.toList());
            double logValue = 0.0;
            for (int j = 0; j < 5; j++) {
                int finalJ = j;
                long nbrOfThisDirection = subSet.stream().filter(a -> a.DirectionChosen.ordinal() == finalJ).count();

                double result = ((double) nbrOfThisDirection / (double) subSet.size());
                if (subSet.size() != 0 && result != 0) {
                    logValue += (-result * log2(result));
                }
            }
            double result = ((double) subSet.size() / (double) totalNumberOfTuples) * logValue;
            if (result != 0) {
                informationGain += result;
            }
        }
        return informationGain;
    }

    //Should probably only be needed to be called once to be more effective
    private double calculateAverageInformationGain(DataTuple[] trainingSet, List<Attribute> attributes) {
        int totalNumberOfTuples = trainingSet.length;
        double informationGain = 0.0;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            long nbrOfThisDirection = Arrays.stream(trainingSet).filter(a -> a.DirectionChosen.ordinal() == finalI).count();
            double result = ((double) nbrOfThisDirection / (double) totalNumberOfTuples);
            if (result != 0) {
                informationGain += (-result * log2(result));
            }
        }
        return informationGain;
    }

    private double log2(double n) { //Fix: n is always 0
        double d = (Math.log(n) / Math.log(2));
        return d;
    }
}
