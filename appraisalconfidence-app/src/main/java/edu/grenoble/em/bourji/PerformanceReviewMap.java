package edu.grenoble.em.bourji;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 9/5/2017.
 */
public class PerformanceReviewMap {

    private static final Map<String, Pair<String, String>> codeToProfilesMap = new HashMap<>();

    static {
        codeToProfilesMap.put("1", new ImmutablePair<>("A", "B"));
        codeToProfilesMap.put("2", new ImmutablePair<>("B", "C"));
    }

    public static Pair<String, String> getTeachersPerEvaluationCode(String evaluationCode) {
        return codeToProfilesMap.get(evaluationCode);
    }
}
