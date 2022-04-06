package directed_graphs;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class WordNet {

    private final ArrayList<Bag<String>> synsets;
    private final ArrayList<Bag<Integer>> hypernyms;

    private boolean[] markedSynsets;

    private int[] v;

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

        v = new int[hypernyms.size()];

        markedSynsets = new boolean[hypernyms.size()];

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

    private int getNounPosition(String noun) {
        for (int c = 0; c < synsets.size(); ++c) {
            for (String word : synsets.get(c)) {
                if (word.equals(noun)) return c;
            }
        }
        return -1;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateInput(!isNoun(nounA), !isNoun(nounB));
        resetAncestorCount();
        markedSynsets = new boolean[hypernyms.size()];

        int positionA = getNounPosition(nounA), positionB = getNounPosition(nounB);
        return v[findCommonAncestor(positionA, positionB, true)];
    }

    private void validateInput(boolean b, boolean b2) {
        if (b || b2) throw new IllegalArgumentException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateInput(!isNoun(nounA), !isNoun(nounB));
        resetAncestorCount();
        markedSynsets = new boolean[hypernyms.size()];

        int positionA = getNounPosition(nounA), positionB = getNounPosition(nounB);

        Bag<String> ancestor = synsets.get(findCommonAncestor(positionA, positionB, true));

        StringBuilder ancestorBuilder = new StringBuilder();
        for (String s : ancestor) {
            ancestorBuilder.append(s);
            ancestorBuilder.append(" ");
        }
        ancestorBuilder.deleteCharAt(ancestorBuilder.length() - 1);

        return ancestorBuilder.toString();

    }

    private int findCommonAncestor(int currentPositionA, int currentPositionB, boolean switchToA) {
        int ancestor;
        if (switchToA) {
            if (isSynsetMarked(currentPositionA)) return currentPositionA;
            markedSynsets[currentPositionA] = true;

            if (hypernyms.get(currentPositionA) != null) {
                for (int p : hypernyms.get(currentPositionA)) {
                    ancestor = getAncestor(p, findCommonAncestor(p, currentPositionB, false));
                    v[ancestor] += v[currentPositionA] + 1;
                    return ancestor;
                }
            }
        } else {
            if (isSynsetMarked(currentPositionB)) return currentPositionB;
            markedSynsets[currentPositionB] = true;

            if (hypernyms.get(currentPositionB) != null) {
                for (int p : hypernyms.get(currentPositionB)) {
                    ancestor = getAncestor(p, findCommonAncestor(currentPositionA, p, true));
                    v[ancestor] += v[currentPositionB] + 1;
                    return ancestor;
                }
            }
        }
        return rootPosition;
    }

    private int getAncestor(int p, int commonAncestor) {
        if (!isSynsetMarked(p)) {
            return commonAncestor;
        } else return p;
    }

    private boolean isSynsetMarked(Integer currentPositionA) {
        return markedSynsets[currentPositionA];
    }

    private void resetAncestorCount() {
        v = new int[hypernyms.size()];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src\\directed_graphs" +
                        "\\synsets.txt",
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src" +
                        "\\directed_graphs\\hypernyms.txt"
        );

        StdOut.println(wn.sap("Abuja", "Accra"));
        StdOut.println(wn.distance("Abuja", "Accra"));
    }
}