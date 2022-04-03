package directed_graphs;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class WordNet {

    private final ArrayList<Bag<String>> synSets;
    private final ArrayList<Bag<Integer>> hypernyms;

    // constructor takes the name of the two input files
    public WordNet(String synSetsFileName, String hypernymFileName) {
        argumentCheck(synSetsFileName, hypernymFileName);

        synSets = extractSynSetsFromFileInput(synSetsFileName);
        hypernyms = extractHypernymsFromInputFile(hypernymFileName);

        if (!isRootDAG()) throw new IllegalArgumentException();
    }

    private void argumentCheck(String a, String b) {
        if (a == null || b == null) throw new IllegalArgumentException();
    }

    private boolean isRootDAG() {
        return synSets.size() == hypernyms.size() + 1;
    }

    private int pointOfExtraction(String line) {
        int counter = 0;
        char c = line.charAt(counter);
        while (c != ',') {
            if (++counter == line.length()) { return -1; }
            c = line.charAt(counter);
        }
        return counter + 1;
    }

    private Bag<String> getBagOfSynonyms(int index, String line) {
        char c = line.charAt(index);
        StringBuilder word = new StringBuilder();
        Bag<String> wordBag = new Bag<>();
        while (c != ',') {
            if (c != ' ') word.append(c);
            else {
                wordBag.add(word.toString());
                word = new StringBuilder();
            }
            if (++index == line.length()) break;
            c = line.charAt(index);
        }
        if (!word.isEmpty()) wordBag.add(word.toString());
        return wordBag;
    }

    private Bag<Integer> getBagOfSynSetReferences(int index, String line) {
        char c = line.charAt(index);
        Bag<Integer> synSets = new Bag<>();

        StringBuilder referenceToSynSet = new StringBuilder();
        while (true) {
            if (c != ',') referenceToSynSet.append(c);
            else {
                synSets.add(Integer.parseInt(referenceToSynSet.toString()));
                referenceToSynSet = new StringBuilder();
            }
            if (++index == line.length()) break;
            c = line.charAt(index);
        }
        if (!referenceToSynSet.isEmpty()) synSets.add(Integer.parseInt(referenceToSynSet.toString()));
        return synSets;
    }

    private ArrayList<Bag<String>> extractSynSetsFromFileInput(String fileName) {
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

    private ArrayList<Bag<Integer>> extractHypernymsFromInputFile(String fileName) {
        In hypernymFile = new In(fileName);
        ArrayList<Bag<Integer>> hypernyms = new ArrayList<>();

        String line;
        int indexOfExtraction;
        Bag<Integer> synSetReferences;

        while (hypernymFile.hasNextLine()) {
            line = hypernymFile.readLine();
            indexOfExtraction = pointOfExtraction(line);
            if (indexOfExtraction != -1) {
                synSetReferences = getBagOfSynSetReferences(indexOfExtraction, line);
                hypernyms.add(synSetReferences);
            }
        }

        return hypernyms;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        ArrayList<String> nouns = new ArrayList<>();
        for (Bag<String> synonyms : synSets) {
            for (String word : synonyms) nouns.add(word);
        }
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) { return 0; }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) { return null; }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src\\directed_graphs" +
                        "\\synsets.txt",
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src" +
                        "\\directed_graphs\\hypernyms.txt"
        );
    }

}