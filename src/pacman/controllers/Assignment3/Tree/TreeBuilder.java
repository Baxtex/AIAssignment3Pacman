package pacman.controllers.Assignment3.Tree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Builds a decision tree by using the ID3 approach.
 */
public class TreeBuilder {

    public DecisionTree buildDecisionTree() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        DataTuple[] trainingSet = getTrainingSet(dataSet);
        DataTuple[] testSet = getTestSet(dataSet, trainingSet.length);
        ArrayList<Attribute> attributes = initializeAttributesList();

        DecisionTree tree = new DecisionTree(generateTree(trainingSet, attributes));
        tree.printTree();
        testAccuracy(tree, testSet);
        return null;
    }

    //TODO Fix, tree is built the wrong way...
    private Node generateTree(DataTuple[] set, ArrayList<Attribute> attributes) {

        Node node;

        if (allTuplesSameClass(set)) {

            node = new Node(set[0].DirectionChosen.ordinal());

        } else if (attributes.isEmpty()) {

            node = new Node(getMajorityClass(set));

        } else {

            Attribute attribute = attributeSelection(set, attributes);

            attributes.remove(attribute);

            node = new Node(attribute);

            for (int i = 0; i < getNumberOfSubsets(attribute); i++) {

                List<DataTuple> subSet = getSubset(set, attribute, i);

                if (subSet.isEmpty()) {

                    node.addChild(new Node(getMajorityClass(set)));

                } else {

                    node.addChild(generateTree(subSet.toArray(new DataTuple[subSet.size()]), attributes)); //Recursive call!

                }
            }
        }
        return node;
    }

    private List<DataTuple> getSubset(DataTuple[] trainingSet, Attribute attribute, int finalI) {
        return Arrays.stream(trainingSet).filter(a -> getAttributeValue(a, attribute) == finalI).collect(Collectors.toList());
    }

    private DataTuple[] getTrainingSet(DataTuple[] dataSet) {
        int seventyPercent = (int) (dataSet.length * 0.7);
        DataTuple[] trainingSet = new DataTuple[seventyPercent];
        System.arraycopy(dataSet, 0, trainingSet, 0, seventyPercent);
        return trainingSet;
    }

    private DataTuple[] getTestSet(DataTuple[] dataSet, int startIndex) {
        int fortyPercent = (dataSet.length - startIndex);
        DataTuple[] testSet = new DataTuple[fortyPercent];
        for (int i = 0; startIndex < dataSet.length; i++) {
            testSet[i] = dataSet[startIndex];
            startIndex++;
        }
        return testSet;
    }

    //Test the classifier with the testset to calculate the accuracy level of it.
    //alculates and prints a confusion matrix.
    private void testAccuracy(DecisionTree tree, DataTuple[] testSet) {

        //Confusion matrix:
        int[][] confusionMatrix = new int[6][6];
        for (DataTuple aTestSet : testSet) {
            int testTupleClassValue = aTestSet.DirectionChosen.ordinal();
            int classifierClassValue = findMove(aTestSet, tree.getRoot());
            if (testTupleClassValue == classifierClassValue) {
                confusionMatrix[testTupleClassValue][testTupleClassValue] += 1;
            } else if (testTupleClassValue != classifierClassValue) {
                confusionMatrix[testTupleClassValue][classifierClassValue] += 1;
            }
        }

        //Compute the totals:
        int lastColValue = 0;
        int totalValue = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            int totalRowValue = 0;
            for (int j = 0; j < confusionMatrix[i].length; j++) {
                totalRowValue += confusionMatrix[i][j];
                lastColValue += confusionMatrix[j][i];
            }
            totalValue += totalRowValue;
            confusionMatrix[i][5] = totalRowValue;
            confusionMatrix[5][i] = lastColValue;
            lastColValue = 0;
        }
        confusionMatrix[5][5] = totalValue;

        //print the matrix:
        System.out.println("");
        System.out.println(" Up  Right Down Left Neu  | Total");
        System.out.println("__________________________|__________");

        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i == 5) {
                System.out.println("__________________________|__________");
            }

            for (int j = 0; j < confusionMatrix[i].length; j++) {

                if (j == 5) {
                    System.out.print(" | ");
                }

                if (confusionMatrix[i][j] == 0) {
                    System.out.print(" 000 ");
                } else {
                    System.out.print(" " + confusionMatrix[i][j] + " ");
                }
            }
            System.out.println();
        }


        //Find the accuracy
        int accuracySum = 0;
        int errorSum = 0;
        for (int i = 0; i < confusionMatrix.length - 1; i++) {

            accuracySum += confusionMatrix[i][i];
            errorSum += confusionMatrix[i][5 - i];
        }
        double accuracyRes = (double) accuracySum / (double) confusionMatrix[5][5];
        double errorRes = (double) errorSum / (double) confusionMatrix[5][5];

        System.out.println("Accuracy Rate: " + accuracyRes);
        System.out.println("Error Rate: " + errorRes);
    }

    //To be moved to the decisionstree class later, just used for testing for now.
    private int findMove(DataTuple dataTuple, Node root) {

        while (!root.isLeaf()) {
            int attributeValue = getAttributeValue(dataTuple, root.getAttribute());
            root = root.getChildren().get(attributeValue);
        }
        return root.getDirection().ordinal();
    }

    //TODO Just some random attributes for now, maybe select other attributes.
    private ArrayList<Attribute> initializeAttributesList() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(Attribute.isBlinkyEdible);
        //attributes.add(Attribute.isInkyEdible);
        //attributes.add(Attribute.isPinkyEdible);
        attributes.add(Attribute.isSueEdible);
        attributes.add(Attribute.blinkyDir);
        //attributes.add(Attribute.inkyDir);
        //attributes.add(Attribute.pinkyDir);
        //attributes.add(Attribute.sueDir);
        attributes.add(Attribute.numOfPillsLeft);
        //attributes.add(Attribute.numPowerPillsLeft);
        // attributes.add(Attribute.pacmanPosition);
        attributes.add(Attribute.currentScore);
        attributes.add(Attribute.currentLevelTime);
        attributes.add(Attribute.pacmanLivesLeft);
        return attributes;
    }

    //Returns true if the direction choosen class is the same value for all tuples.
    private boolean allTuplesSameClass(DataTuple[] trainingSet) {
        return Arrays.stream(trainingSet).allMatch(a -> a.DirectionChosen == trainingSet[0].DirectionChosen);
    }

    private int getMajorityClass(DataTuple[] trainingSet) {
        int majorityLabel = -1;
        long majorityValue = 0;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            long nbrOfThisDirection = Arrays.stream(trainingSet).filter(a -> a.DirectionChosen.ordinal() == finalI).count();
            if (nbrOfThisDirection > majorityValue) {
                majorityLabel = i;
                majorityValue = nbrOfThisDirection;
            }
        }
        return majorityLabel; //TODO NEEDS TESTING!!!
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
            case pacmanLivesLeft:
                return tuple.pacmanLivesLeft;
        }

        return -100000; //Attribute not found
    }

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
            case pacmanPosition:
                return 5;
            case currentScore:
                return 5;
            case currentLevelTime:
                return 5;
            case pacmanLivesLeft:
                return 4;

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
        double averageInformationGain = calculateAverageInformationGain(trainingSet);

        List<Pair<Attribute, Double>> attributesInformationGain = new ArrayList<>(attributes.size());

        for (Attribute attribute : attributes) {
            double attributeInformationGain = averageInformationGain - calculateAttributeGain(trainingSet, attribute);
            attributesInformationGain.add(new Pair(attribute, attributeInformationGain));
        }

        Pair<Attribute, Double> res = Collections.max(attributesInformationGain, Comparator.comparingDouble(p -> (double) p.second));
        return res.first;

    }

    //Calculates the attribute gain for a single attribute.
    private double calculateAttributeGain(DataTuple[] trainingSet, Attribute attribute) {
        int totalNumberOfTuples = trainingSet.length;
        double informationGain = 0.0;
        int nbrOfSubsets = getNumberOfSubsets(attribute);
        for (int i = 0; i < nbrOfSubsets; i++) {
            List<DataTuple> subSet = getSubset(trainingSet, attribute, i);
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

    //Calculates the average information gain on all the tuples in the data set.
    private double calculateAverageInformationGain(DataTuple[] trainingSet) {
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

    //Returns the log2 of a double.
    private double log2(double n) {
        return (Math.log(n) / Math.log(2));
    }

    //Generic pair/wrapper class that contains two values.
    private class Pair<T, U> {
        final T first;
        final U second;

        private Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }
}
