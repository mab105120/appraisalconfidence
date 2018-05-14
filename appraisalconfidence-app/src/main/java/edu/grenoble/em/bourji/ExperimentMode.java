package edu.grenoble.em.bourji;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 2/13/2018.
 */
public enum ExperimentMode {

    // Triads: (1, 4, 7), (2, 5, 8), (3, 6, 9)
    RELATIVE(new HashMap<String, Pair<String, String>>() {{
        put("1", new ImmutablePair<>("A", "B"));
        put("2", new ImmutablePair<>("D", "E"));
        put("3", new ImmutablePair<>("G", "H"));
        put("4", new ImmutablePair<>("B", "C"));
        put("5", new ImmutablePair<>("E", "F"));
        put("6", new ImmutablePair<>("H", "I"));
        put("7", new ImmutablePair<>("C", "A"));
        put("8", new ImmutablePair<>("F", "D"));
        put("9", new ImmutablePair<>("I", "G"));
    }}),
    EXPERT(new HashMap<String, Pair<String, String>>() {{
        put("1", new ImmutablePair<>("A", null));
        put("2", new ImmutablePair<>("B", null));
        put("3", new ImmutablePair<>("C", null));
        put("4", new ImmutablePair<>("D", null));
        put("5", new ImmutablePair<>("E", null));
        put("6", new ImmutablePair<>("F", null));
    }}),
    ABSOLUTE_HIGH(new HashMap<String, Pair<String, String>>() {{
        put("p1", new ImmutablePair<>("A", null));
        put("p2", new ImmutablePair<>("B", null));
        put("p3", new ImmutablePair<>("C", null));
        put("1", new ImmutablePair<>("D", null));
        put("2", new ImmutablePair<>("E", null));
        put("3", new ImmutablePair<>("F", null));
        put("4", new ImmutablePair<>("G", null));
    }}),
    ABSOLUTE_LOW(new HashMap<String, Pair<String, String>>() {{
        put("p1", new ImmutablePair<>("A", null));
        put("1", new ImmutablePair<>("D", null));
        put("2", new ImmutablePair<>("E", null));
        put("3", new ImmutablePair<>("F", null));
        put("4", new ImmutablePair<>("G", null));

    }});

    private Map<String, Pair<String, String>> evaluationCodes;

    ExperimentMode(Map<String, Pair<String, String>> codes) {
        evaluationCodes = codes;
    }

    Map<String, Pair<String, String>> getEvaluationCodes() {
        return this.evaluationCodes;
    }

    public static Map<String, Pair<String, String>> getEvaluationCodes(String mode) {
        for(ExperimentMode m: ExperimentMode.values())
            if(m.name().equalsIgnoreCase(mode))
                return m.getEvaluationCodes();
        throw new IllegalArgumentException("Unknown experiment mode: " + mode);
    }
}