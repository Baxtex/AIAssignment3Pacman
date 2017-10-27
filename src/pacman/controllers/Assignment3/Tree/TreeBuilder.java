package pacman.controllers.Assignment3.Tree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Assignment3.Tree.Attribute.Attribute;

import java.util.*;
import java.util.stream.Collectors;

import static pacman.controllers.Assignment3.Tree.Attribute.Utility.getAttributeValue;


/**
 * Automatic construction of a decision tree using the ID3 method to select attributes.
 */
public class TreeBuilder {

    public DecisionTree buildDecisionTree() {
        Pair<DataTuple[], DataTuple[]> splitSets = getDataSetsBootstraping();
        DataTuple[] trainingSet = splitSets.first;
        DataTuple[] testSet = splitSets.second;

        ArrayList<Attribute> attributes = initializeAttributesList();

        DecisionTree tree = new DecisionTree(generateTree(trainingSet, attributes));

        calculateConfusionMatrix(tree, testSet);
        tree.printTree();
        return tree;
    }

    /**
     * Gets the data set and splits it into training and test set by bootstraping and returning it.
     */
    private Pair<DataTuple[], DataTuple[]> getDataSetsBootstraping() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        List<DataTuple> trainingSet = new ArrayList<>();

        //Generate Training set.
        Random random = new Random();
        int seventyPercent = (int) (dataSet.length * 0.7);
        for (int i = 0; i < seventyPercent; i++) {
            int randomIndex = random.nextInt(dataSet.length);
            trainingSet.add(dataSet[randomIndex]);
        }
        DataTuple[] trainingSetArray = trainingSet.toArray(new DataTuple[0]);

        //Generate Test set.
        List<DataTuple> testSet = new ArrayList<>();
        for (DataTuple aDataSet : dataSet) {
            if (!contains(trainingSetArray, aDataSet)) {
                testSet.add(aDataSet);
            }
        }

        DataTuple[] testSetArray = testSet.toArray(new DataTuple[0]);

        return new Pair<>(trainingSetArray, testSetArray);
    }


    /**
     * Splits the data set by 70/30.
     */
    private Pair<DataTuple[], DataTuple[]> getDataSetsSimpleSplit() {
        DataTuple[] dataSet = DataSaverLoader.LoadPacManData();
        int seventyPercent = (int) (dataSet.length * 0.7);
        DataTuple[] trainingSet = new DataTuple[seventyPercent];
        System.arraycopy(dataSet, 0, trainingSet, 0, seventyPercent);

        int startIndex = trainingSet.length;
        int thirtyPercent = (dataSet.length - startIndex);
        DataTuple[] testSet = new DataTuple[thirtyPercent];
        for (int i = 0; startIndex < dataSet.length; i++) {
            testSet[i] = dataSet[startIndex];
            startIndex++;
        }

        return new Pair<>(trainingSet, testSet);
    }

    /**
     * Creates and initializes a list with the attributes to include in the tree.
     * Uncomment and comment to choose which to use.
     */
    private ArrayList<Attribute> initializeAttributesList() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(Attribute.isSueEdible);
        attributes.add(Attribute.blinkyDir);
        attributes.add(Attribute.blinkyDist);
        attributes.add(Attribute.inkyDist);

        //attributes.add(Attribute.numOfPillsLeft);
        //attributes.add(Attribute.pinkyDist);
        //attributes.add(Attribute.pacmanLivesLeft);
        //attributes.add(Attribute.totalGameTime);
        //attributes.add(Attribute.inkyDir);
        //attributes.add(Attribute.pinkyDir);
        //attributes.add(Attribute.sueDir);
        //attributes.add(Attribute.isInkyEdible);
        //attributes.add(Attribute.isPinkyEdible);
        //attributes.add(Attribute.numPowerPillsLeft);
        //attributes.add(Attribute.pacmanPosition);
        //attributes.add(Attribute.currentScore);
        //attributes.add(Attribute.currentLevelTime);
        //attributes.add(Attribute.sueDist);
        return attributes;
    }

    /**
     * Generates the tree recursivly in a depth first approach.
     */
    private Node generateTree(DataTuple[] set, ArrayList<Attribute> attributes) {
        Node node;
        if (allTuplesSameClass(set)) {
            node = new Node(set[0].DirectionChosen.ordinal());
        } else if (attributes.isEmpty()) {
            node = new Node(getMajorityClass(set));
        } else {
            Attribute attribute = attributeSelectionC45(set, attributes);
            node = new Node(attribute);

            for (int attributeValue = 0; attributeValue < attribute.getNbrOfAttributeValues(); attributeValue++) {
                List<DataTuple> subSet = getSubset(set, attribute, attributeValue);
                if (subSet.isEmpty()) {
                    node.addChild(new Node(getMajorityClass(set)));
                } else {
                    node.addChild(generateTree(subSet.toArray(new DataTuple[subSet.size()]), attributes));
                }
            }
        }
        return node;
    }

    /**
     * Returns true if the direction chosen class is the same value for all tuples.
     */
    private boolean allTuplesSameClass(DataTuple[] set) {
        return Arrays.stream(set).allMatch(a -> a.DirectionChosen == set[0].DirectionChosen);
    }

    /**
     * Returns the direction that most of the tuples class value in the given set.
     */
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
    private Attribute attributeSelectionID3(DataTuple[] set, List<Attribute> attributes) {
        double averageInformationGain = calculateAverageInformationGain(set);

        List<Pair<Attribute, Double>> attributesInformationGain = new ArrayList<>(attributes.size());

        for (Attribute attribute : attributes) {
            double attributeInformationGain = (averageInformationGain - calculateAttributeGain(set, attribute));
            attributesInformationGain.add(new Pair(attribute, attributeInformationGain));
        }

        Pair<Attribute, Double> res = Collections.max(attributesInformationGain, Comparator.comparingDouble(p -> p.second));

        attributes.remove(res.first);
        return res.first;

    }

    /**
     * C4.5 is an extension of the ID3 algorithm. It looks at the GainRatio by dividing the attribute information gain by the splitinfo gain.
     */
    private Attribute attributeSelectionC45(DataTuple[] set, List<Attribute> attributes) {
        double averageInformationGain = calculateAverageInformationGain(set);

        List<Pair<Attribute, Double>> attributesInformationGain = new ArrayList<>(attributes.size());
        for (Attribute attribute : attributes) {
            double averageGainAttribute = calculateAttributeGain(set, attribute);
            double gainRatio = ((averageInformationGain - averageGainAttribute) / averageGainAttribute);
            attributesInformationGain.add(new Pair(attribute, gainRatio));
        }

        Pair<Attribute, Double> res = Collections.max(attributesInformationGain, Comparator.comparingDouble(p -> p.second));

        attributes.remove(res.first);
        return res.first;
    }

    /**
     * Calculates the average information gain on all the tuples in the data set.
     */
    private double calculateAverageInformationGain(DataTuple[] set) {
        double informationGain = 0.0;
        if (set.length == 0) {
            return informationGain;
        }
        //For every value of the class atrribute, 4 directions.
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            long nbrOfThisDirection = Arrays.stream(set).filter(a -> a.DirectionChosen.ordinal() == finalI).count();
            double result = ((double) nbrOfThisDirection / (double) set.length);
            informationGain += (-result * log2(result));
        }
        return informationGain;
    }

    /**
     * Calculates the attribute gain for a single attribute.
     */
    private double calculateAttributeGain(DataTuple[] set, Attribute attribute) {
        double informationGain = 0.0;
        if (set.length == 0) {
            return informationGain;
        }
        //For all attribute values, i e true, false, up, right, down etc.
        for (int i = 0; i < attribute.getNbrOfAttributeValues(); i++) {
            List<DataTuple> subSetAttributeValue = getSubset(set, attribute, i);
            double attributeClassValue = 0.0;
            //For every value of the class atrribute, 4 directions.
            for (int j = 0; j < 4; j++) {
                int finalJ = j;
                long nbrOfThisDirection = subSetAttributeValue.stream().filter(a -> a.DirectionChosen.ordinal() == finalJ).count();
                double result = ((double) nbrOfThisDirection / (double) subSetAttributeValue.size());
                if (subSetAttributeValue.size() != 0 && result != 0) {
                    attributeClassValue += (-result * log2(result));
                }
            }
            informationGain += ((double) subSetAttributeValue.size() / (double) set.length) * attributeClassValue;
        }
        return informationGain;
    }

    /**
     * Selects and returns a subset from the given set for certain attribute value.
     */
    private List<DataTuple> getSubset(DataTuple[] set, Attribute attribute, int attributeValue) {
        return Arrays.stream(set).filter(a -> getAttributeValue(a, attribute) == attributeValue).collect(Collectors.toList());
    }

    /**
     * Builds and calculates the confusion matrix.
     */
    private void calculateConfusionMatrix(DecisionTree tree, DataTuple[] set) {

        //Build the confusion matrix.
        int[][] confusion = new int[5][5];
        for (DataTuple dataTuple : set) {
            int testTupleClassValue = dataTuple.DirectionChosen.ordinal();
            int classifierClassValue = tree.findMove(dataTuple).ordinal();
            if (testTupleClassValue == classifierClassValue) {
                confusion[testTupleClassValue][testTupleClassValue] += 1;
            } else {
                confusion[classifierClassValue][testTupleClassValue] += 1;
            }
        }

        //Compute the totals:
        int tpFpSum = 0;
        int fnFpSum = 0;
        int colSum = 0;
        for (int i = 0; i < confusion.length; i++) {
            int rowSum = 0;
            for (int j = 0; j < confusion[i].length; j++) {
                rowSum += confusion[i][j];
                colSum += confusion[j][i];
            }
            confusion[i][4] = rowSum;
            confusion[4][i] = colSum;
            colSum = 0;

            if (confusion.length != i + 1) {
                tpFpSum += confusion[i][i];
                fnFpSum += confusion[i][3 - i];
            }
        }
        confusion[4][4] = set.length;

        printConfusionMatrix(confusion, tpFpSum, fnFpSum);
    }

    private void printConfusionMatrix(int[][] confusionMatrix, double accuracySum, double errorSum) {
        String[] leftLabels = new String[]{"Up  ", "Right  ", "Down  ", "Left  ", "  Totals  "};
        String[] leftClassLabel = new String[]{"Pr    ", "re ", "di  ", "ct  ", ""};

        System.out.println("\nActual       Up  Right Down Left | Total");
        System.out.println("___________________________________|______");
        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i == 4) {
                System.out.println("_____________________________________|______");
            }
            System.out.print(leftClassLabel[i] + " " + leftLabels[i] + "|");

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

        double accuracyRate = accuracySum / (double) confusionMatrix[4][4];
        double errorRate = errorSum / (double) confusionMatrix[4][4];
        System.out.println("Accuracy: " + String.format("%.1f", accuracyRate * 100) + "%");
        System.out.println("Error   : " + String.format("%.1f", errorRate * 100) + "% \n");

    }

    /**
     * Returns the log2 of a double.
     */
    private double log2(double n) {
        return (Math.log(n) / Math.log(2));
    }

    /**
     * Generic pair/wrapper class that contains two values.
     */
    private class Pair<T, U> {
        final T first;
        final U second;

        private Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * Checks wherever a array contains a value.
     */
    private static <T> boolean contains(final T[] array, final T v) {
        if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {
            for (final T e : array)
                if (e == v || v.equals(e))
                    return true;
        }
        return false;
    }
}
