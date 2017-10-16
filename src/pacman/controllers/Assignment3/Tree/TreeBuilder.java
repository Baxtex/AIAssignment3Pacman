package pacman.controllers.Assignment3.Tree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Assignment3.Tree.Attribute.Attribute;

import java.util.*;
import java.util.stream.Collectors;

import static pacman.controllers.Assignment3.Tree.Attribute.Utility.getAttributeValue;
import static pacman.controllers.Assignment3.Tree.Attribute.Utility.getNumberOfSubsets;

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

        testAccuracy(tree, testSet);
        tree.printTree();
        return tree;
    }

    //Generates the tree recursivly in a depth first approach.
    private Node generateTree(DataTuple[] set, ArrayList<Attribute> attributes) {

        Node node;

        if (allTuplesSameClass(set)) {

            node = new Node(set[0].DirectionChosen.ordinal());

        } else if (attributes.isEmpty()) {

            node = new Node(getMajorityClass(set));

        } else {

            Attribute attribute = attributeSelection(set, attributes);

            node = new Node(attribute);

            for (int i = 0; i < getNumberOfSubsets(attribute); i++) {

                List<DataTuple> subSet = getSubset(set, attribute, i);

                if (subSet.isEmpty()) {

                    node.addChild(new Node(getMajorityClass(set)));

                } else {

                    node.addChild(generateTree(subSet.toArray(new DataTuple[subSet.size()]), attributes));

                }
            }
        }
        return node;
    }

    //Takes the first 70% tuples as trainingset.
    private DataTuple[] getTrainingSet(DataTuple[] dataSet) {
        int seventyPercent = (int) (dataSet.length * 0.7);
        DataTuple[] trainingSet = new DataTuple[seventyPercent];
        System.arraycopy(dataSet, 0, trainingSet, 0, seventyPercent);
        return trainingSet;
    }

    //Takes the last 30% as testset
    private DataTuple[] getTestSet(DataTuple[] dataSet, int startIndex) {
        int thirtyPercent = (dataSet.length - startIndex);
        DataTuple[] testSet = new DataTuple[thirtyPercent];
        for (int i = 0; startIndex < dataSet.length; i++) {
            testSet[i] = dataSet[startIndex];
            startIndex++;
        }
        return testSet;
    }

    //Selects and returns a subset from the given set for certain attribute value.
    private List<DataTuple> getSubset(DataTuple[] trainingSet, Attribute attribute, int finalI) {
        return Arrays.stream(trainingSet).filter(a -> getAttributeValue(a, attribute) == finalI).collect(Collectors.toList());
    }

    //Test the classifier with the testset to calculate the accuracy level of it with a confusion matrix.
    private void testAccuracy(DecisionTree tree, DataTuple[] testSet) {

        //Build the confusionmatrix.
        int[][] confusionMatrix = new int[5][5];
        for (DataTuple dataTuple : testSet) {
            int testTupleClassValue = dataTuple.DirectionChosen.ordinal();
            int classifierClassValue = tree.findMove(dataTuple);
            if (testTupleClassValue == classifierClassValue) {
                confusionMatrix[testTupleClassValue][testTupleClassValue] += 1;
            } else {
                confusionMatrix[testTupleClassValue][classifierClassValue] += 1;
            }
        }

        //Compute the totals:
        int accuracySum = 0;
        int errorSum = 0;
        int lastColValue = 0;
        int totalValue = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            int totalRowValue = 0;
            for (int j = 0; j < confusionMatrix[i].length; j++) {
                totalRowValue += confusionMatrix[i][j];
                lastColValue += confusionMatrix[j][i];
            }
            totalValue += totalRowValue;
            confusionMatrix[i][4] = totalRowValue;
            confusionMatrix[4][i] = lastColValue;
            lastColValue = 0;

            if (confusionMatrix.length != i + 1) {
                accuracySum += confusionMatrix[i][i];
                errorSum += confusionMatrix[i][4 - i];
            }
        }
        confusionMatrix[4][4] = totalValue;

        //print the matrix to the console.
        System.out.println("");
        System.out.println(" Up  Right Down Left   | Total");
        System.out.println("_______________________|__________");
        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i == 4) {
                System.out.println("________________________|__________");
            }

            for (int j = 0; j < confusionMatrix[i].length; j++) {

                if (j == 4) {
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

        double accuracyRes = (double) accuracySum / (double) confusionMatrix[4][4];
        double errorRes = (double) errorSum / (double) confusionMatrix[4][4];
        System.out.println("Accuracy Rate: " + accuracyRes);
        System.out.println("Error Rate: " + errorRes);
    }

    //Creates and initializes a list with the attributes to include in the tree.
    private ArrayList<Attribute> initializeAttributesList() {
        ArrayList<Attribute> attributes = new ArrayList<>();
       // attributes.add(Attribute.isInkyEdible);
        //attributes.add(Attribute.isPinkyEdible);
        attributes.add(Attribute.isSueEdible);
        //attributes.add(Attribute.blinkyDir);
        //attributes.add(Attribute.inkyDir);
        //attributes.add(Attribute.pinkyDir);
        //attributes.add(Attribute.sueDir);
        attributes.add(Attribute.numOfPillsLeft);
        //attributes.add(Attribute.numPowerPillsLeft);
       // attributes.add(Attribute.pacmanPosition);
        attributes.add(Attribute.currentScore);
        //attributes.add(Attribute.currentLevelTime);
        attributes.add(Attribute.pacmanLivesLeft);
        attributes.add(Attribute.totalGameTime);
        //attributes.add(Attribute.blinkyDist);
       // attributes.add(Attribute.inkyDist);
        attributes.add(Attribute.pinkyDist);
        //attributes.add(Attribute.sueDist);


        return attributes;
    }


    //Returns true if the direction chosen class is the same value for all tuples.
    private boolean allTuplesSameClass(DataTuple[] set) {
        return Arrays.stream(set).allMatch(a -> a.DirectionChosen == set[0].DirectionChosen);
    }

    //Returns the direction that most of the tuples in the set contains.
    private int getMajorityClass(DataTuple[] set) {
        int majorityLabel = -1;
        long majorityValue = 0;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            long nbrOfThisDirection = Arrays.stream(set).filter(a -> a.DirectionChosen.ordinal() == finalI).count();
            if (nbrOfThisDirection > majorityValue) {
                majorityLabel = i;
                majorityValue = nbrOfThisDirection;
            }
        }
        return majorityLabel;
    }

    /**
     * Attribute selection method which is built with the ID3 algorithm.
     * ID3 is short for Iterative Dichotomiser.
     * It uses information gain as the criterion to choose attribute A: a function calculates how much
     * information gain provides every candidate attribute in the list. The one with the highest gain is chosen to
     * become A, the optimal attribut.
     */
    private Attribute attributeSelection(DataTuple[] set, List<Attribute> attributes) {
        double averageInformationGain = calculateAverageInformationGain(set);

        List<Pair<Attribute, Double>> attributesInformationGain = new ArrayList<>(attributes.size());

        for (Attribute attribute : attributes) {
            double attributeInformationGain = averageInformationGain - calculateAttributeGain(set, attribute);
            attributesInformationGain.add(new Pair(attribute, attributeInformationGain));
        }

        Pair<Attribute, Double> res = Collections.max(attributesInformationGain, Comparator.comparingDouble(p -> (double) p.second));

        attributes.remove(res.first);
        return res.first;

    }

    //Calculates the attribute gain for a single attribute.
    private double calculateAttributeGain(DataTuple[] set, Attribute attribute) {
        int totalNumberOfTuples = set.length;
        double informationGain = 0.0;
        int nbrOfSubsets = getNumberOfSubsets(attribute);
        for (int i = 0; i < nbrOfSubsets; i++) {
            List<DataTuple> subSet = getSubset(set, attribute, i);
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
    private double calculateAverageInformationGain(DataTuple[] set) {
        int totalNumberOfTuples = set.length;
        double informationGain = 0.0;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            long nbrOfThisDirection = Arrays.stream(set).filter(a -> a.DirectionChosen.ordinal() == finalI).count();
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
