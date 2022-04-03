package directed_graphs;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class WordNet {

    // constructor takes the name of the two input files
    public WordNet(String synSetsFileName, String hypernyms) {
        ArrayList<Bag<String>> synSets = extractSynSetsFromFileInput(synSetsFileName);
    }

    private int pointOfSynSetExtraction(String line) {
        int counter = 0;
        char c = line.charAt(counter);
        while (c != ',') {
            if (++counter == line.length()) { return -1; }
            c = line.charAt(counter);
        }
        return counter + 1;
    }

    private Bag<String> getBagOfStrings(int index, String line) {
        char c = line.charAt(index);
        StringBuilder word = new StringBuilder();
        Bag<String> wordBag = new Bag<>();
        while (c != ',') {
            if (c != ' ') {
                word.append(c);
            } else {
                wordBag.add(word.toString());
                word = new StringBuilder();
            }
            if (++index == line.length()) break;
            c = line.charAt(index);
        }
        if (!word.isEmpty()) wordBag.add(word.toString());
        return wordBag;
    }

    private ArrayList<Bag<String>> extractSynSetsFromFileInput(String fileName) {
        In synSetFile = new In(fileName);
        ArrayList<Bag<String>> synSets = new ArrayList<>();

        String line;
        int indexOfExtraction;
        Bag<String> words;

        while (synSetFile.hasNextLine()) {
            line = synSetFile.readLine();
            indexOfExtraction = pointOfSynSetExtraction(line);
            words = getBagOfStrings(indexOfExtraction, line);
            synSets.add(words);
        }

        return synSets;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() { return null; }

    // is the word a WordNet noun?
    public boolean isNoun(String word) { return false; }

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
                null);
    }

}