package directed_graphs;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WordNet {

    private final ArrayList<Bag<String>> synsets;
    private final ArrayList<Bag<Integer>> hypernyms;

    private boolean[] markedSynsets;

    private int[] childCount;

    private final ArrayList<String> nouns;

    private Integer rootPosition = null;

    // constructor takes the name of the two input files
    public WordNet(String synSetsFileName, String hypernymFileName) {
        validateInput(synSetsFileName == null, hypernymFileName == null);

        nouns = new ArrayList<>();

        synsets = getSynSetsFromFileInput(synSetsFileName);
        hypernyms = getHypernymsFromInputFile(hypernymFileName);

        Collections.sort(nouns);

        if (!isRootDAG()) throw new IllegalArgumentException();

        resetChildCount();

        resetMarkedSynsets();

    }

    private boolean isRootDAG() {
        return rootPosition != null;
    }

    private int pointOfExtraction(String line) {
        int counter = 0;
        char c = line.charAt(counter);
        while (c != ',') {
            if (++counter == line.length()) {
                return -1;
            }
            c = line.charAt(counter);
        }
        return counter + 1;
    }

    private Bag<String> getBagOfSynonyms(int index, String line) {
        char c = line.charAt(index);
        StringBuilder word = new StringBuilder();
        String wordToString;
        Bag<String> wordBag = new Bag<>();
        while (c != ',') {
            if (c != ' ') word.append(c);
            else {
                wordToString = word.toString();
                wordBag.add(wordToString);
                nouns.add(wordToString);
                word = new StringBuilder();
            }
            if (++index == line.length()) break;
            c = line.charAt(index);
        }
        if (!word.isEmpty()) {
            wordToString = word.toString();
            wordBag.add(word.toString());
            nouns.add(wordToString);
        }
        return wordBag;
    }

    private Bag<Integer> getBagOfSynSetReferences(int index, String line) {
        Bag<Integer> bagOfSynsets = null;
        if (index != -1) {
            char c = line.charAt(index);
            bagOfSynsets = new Bag<>();

            StringBuilder referenceToSynSet = new StringBuilder();
            while (true) {
                if (c != ',') referenceToSynSet.append(c);
                else {
                    bagOfSynsets.add(Integer.parseInt(referenceToSynSet.toString()));
                    referenceToSynSet = new StringBuilder();
                }
                if (++index == line.length()) break;
                c = line.charAt(index);
            }
            if (!referenceToSynSet.isEmpty()) bagOfSynsets.add(Integer.parseInt(referenceToSynSet.toString()));
        }

        return bagOfSynsets;
    }

    private ArrayList<Bag<String>> getSynSetsFromFileInput(String fileName) {
        In synSetFile = new In(fileName);
        ArrayList<Bag<String>> synSets = new ArrayList<>();

        while (synSetFile.hasNextLine()) addWordsToSynSetsFromFile(synSetFile, synSets);

        return synSets;
    }

    private void addWordsToSynSetsFromFile(In synSetFile, ArrayList<Bag<String>> synSets) {
        int indexOfExtraction;
        Bag<String> words;
        String line;
        line = synSetFile.readLine();
        indexOfExtraction = pointOfExtraction(line);
        if (indexOfExtraction != -1) {
            words = getBagOfSynonyms(indexOfExtraction, line);
            synSets.add(words);
        }
    }

    private ArrayList<Bag<Integer>> getHypernymsFromInputFile(String fileName) {
        In hypernymFile = new In(fileName);
        ArrayList<Bag<Integer>> hypernyms = new ArrayList<>();

        String line;
        int indexOfExtraction;
        Bag<Integer> synsetReferences;

        while (hypernymFile.hasNextLine()) {
            line = hypernymFile.readLine();
            indexOfExtraction = pointOfExtraction(line);
            synsetReferences = getBagOfSynSetReferences(indexOfExtraction, line);
            hypernyms.add(synsetReferences);
            if (indexOfExtraction == -1) rootPosition = hypernyms.size() - 1;
        }

        return hypernyms;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        int isNoun = Collections.binarySearch(nouns, word);
        return isNoun >= 0;
    }

    private ArrayList<Integer> getNounPositions(String noun) {
        ArrayList<Integer> nounPositions = new ArrayList<>();
        for (int c = 0; c < synsets.size(); ++c) {
            for (String word : synsets.get(c)) {
                if (word.equals(noun)) nounPositions.add(c);
            }
        }
        return nounPositions;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateInput(!isNoun(nounA), !isNoun(nounB));

        ArrayList<Integer> setOfSynsetsOfA = getNounPositions(nounA), setOfSynsetsOfB = getNounPositions(nounB);

        ArrayList<Integer> ancestors = new ArrayList<>(), lengths = new ArrayList<>();
        int ancestor;

        for (Integer i : setOfSynsetsOfA) {
            for (Integer j : setOfSynsetsOfB) {
                resetChildCount();
                resetMarkedSynsets();
                ancestor = findCommonAncestor(i, j);
                if (Collections.binarySearch(ancestors, ancestor) < 0) {
                    ancestors.add(ancestor);
                    lengths.add(childCount[ancestor]);
                }
            }
        }
        Collections.sort(lengths);
        if (!lengths.isEmpty()) return lengths.get(0); else return -1;
    }

    private void resetMarkedSynsets() {
        markedSynsets = new boolean[hypernyms.size()];
    }

    private void validateInput(boolean b, boolean b2) {
        if (b || b2) throw new IllegalArgumentException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateInput(!isNoun(nounA), !isNoun(nounB));

        ArrayList<Integer> setOfSynsetsOfA = getNounPositions(nounA), setOfSynsetsOfB = getNounPositions(nounB);

        ArrayList<Integer> ancestors = new ArrayList<>(), lengths = new ArrayList<>();
        HashMap<Integer, Integer> lengthHashMap = new HashMap<>();

        int ancestor;

        for (Integer i : setOfSynsetsOfA) {
            for (Integer j : setOfSynsetsOfB) {
                resetChildCount();
                resetMarkedSynsets();
                ancestor = findCommonAncestor(i, j);
                if (Collections.binarySearch(ancestors, ancestor) < 0){
                    ancestors.add(ancestor);
                    lengths.add(childCount[ancestor]);
                    lengthHashMap.put(childCount[ancestor], ancestor);
                }
            }
        }

        Collections.sort(lengths);

        StringBuilder ancestorBuilder = new StringBuilder();
        for (String s : synsets.get(lengthHashMap.get(lengths.get(0)))) {
            ancestorBuilder.append(s);
            ancestorBuilder.append(" ");
        }
        ancestorBuilder.deleteCharAt(ancestorBuilder.length() - 1);

        return ancestorBuilder.toString();

    }

    private int findCommonAncestor(int currentPositionA, int currentPositionB) {
        boolean toggleToB = true;
        Queue<Integer> queueA = new Queue<>();
        Queue<Integer> queueB = new Queue<>();

        queueA.enqueue(currentPositionA);
        queueB.enqueue(currentPositionB);

        markPosition(currentPositionA);
        markPosition(currentPositionB);

        while (true) {
            int s;
            if (toggleToB) {
                if (!queueB.isEmpty()) {
                    s = findCommonAncestor(queueB);
                    if (s != -1) return s;
                }
                toggleToB = false;
            } else {
                if (!queueA.isEmpty()) {
                    s = findCommonAncestor(queueA);
                    if (s != -1) return s;
                }
                toggleToB = true;
            }
        }
    }

    private int findCommonAncestor(Queue<Integer> queueB) {
        int d;
        d = queueB.dequeue();
        if (hypernyms.get(d) != null) {
            return enqueueVerticesOfDAndReturnIfMarked(queueB, d);
        }
        return -1;
    }

    private int enqueueVerticesOfDAndReturnIfMarked(Queue<Integer> queueB, int d) {
        for (int s : hypernyms.get(d)) {
            childCount[s] += childCount[d] + 1;
            if (!isSynsetMarked(s)) {
                queueB.enqueue(s);
                markPosition(s);
            } else return s;
        }
        return -1;
    }

    private void markPosition(int currentPositionA) {
        markedSynsets[currentPositionA] = true;
    }

    private boolean isSynsetMarked(Integer currentPositionA) {
        return markedSynsets[currentPositionA];
    }

    private void resetChildCount() {
        childCount = new int[hypernyms.size()];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src\\directed_graphs" +
                        "\\synsets.txt",
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src" +
                        "\\directed_graphs\\hypernyms.txt"
        );

        StdOut.println(wn.sap("demotion", "sprint"));
        StdOut.println(wn.distance("demotion", "sprint"));
    }
}