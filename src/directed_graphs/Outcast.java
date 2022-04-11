package directed_graphs;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Outcast {

    private final WordNet wn;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        ArrayList<String> n = (ArrayList<String>) Arrays.asList(nouns);

        return null;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wn = new WordNet(
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src\\directed_graphs" +
                        "\\synsets.txt",
                "C:\\Users\\ADMIN\\IdeaProjects\\Data Structures and Algorithms\\src" +
                        "\\directed_graphs\\hypernyms.txt"
        );

        ArrayList<String> nouns = new ArrayList<>();

        for (String n : wn.nouns()) nouns.add(n);

        Outcast oc = new Outcast(wn);
        StdOut.println(nouns.size());
        StdOut.println(oc.outcast(nouns.toArray(new String[0])));
    }
}
