package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.EvaluationCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moe on 2/13/2018.
 */
public enum ExperimentMode {

    // Triads: (1, 4, 7), (2, 5, 8), (3, 6, 9)
    TALL(new ArrayList<EvaluationCode>() {{
        add(new EvaluationCode("1", "A", "B"));
        add(new EvaluationCode("2", "D", "E"));
        add(new EvaluationCode("3", "G", "H"));
        add(new EvaluationCode("4", "B", "C"));
        add(new EvaluationCode("5", "E", "F"));
        add(new EvaluationCode("6", "H", "I"));
        add(new EvaluationCode("7", "C", "A"));
        add(new EvaluationCode("8", "F", "D"));
        add(new EvaluationCode("9", "I", "G"));
    }}),
    GRANDE(new ArrayList<EvaluationCode>() {{
        add(new EvaluationCode("1", "A", "B"));
        add(new EvaluationCode("2", "D", "E"));
        add(new EvaluationCode("3", "G", "H"));
        add(new EvaluationCode("4", "A", "D"));
        add(new EvaluationCode("5", "B", "C"));
        add(new EvaluationCode("6", "E", "F"));
        add(new EvaluationCode("7", "A", "G"));
        add(new EvaluationCode("8", "H", "I"));
        add(new EvaluationCode("9", "D", "G"));
        add(new EvaluationCode("10", "C", "A"));
        add(new EvaluationCode("11", "F", "D"));
        add(new EvaluationCode("12", "I", "G"));
    }}),
    VENTI(new ArrayList<EvaluationCode>() {{
        add(new EvaluationCode("1", "A", "B"));
        add(new EvaluationCode("2", "C", "I"));
        add(new EvaluationCode("3", "G", "H"));
        add(new EvaluationCode("4", "A", "D"));
        add(new EvaluationCode("5", "C", "F"));
        add(new EvaluationCode("6", "G", "I"));
        add(new EvaluationCode("7", "F", "E"));
        add(new EvaluationCode("8", "B", "C"));
        add(new EvaluationCode("9", "D", "G"));
        add(new EvaluationCode("10", "F", "I"));
        add(new EvaluationCode("11", "A", "C"));
        add(new EvaluationCode("12", "D", "E"));
        add(new EvaluationCode("13", "I", "H"));
        add(new EvaluationCode("14", "A", "G"));
        add(new EvaluationCode("15", "D", "F"));
    }});

    private List<EvaluationCode> evaluationCodes;

    ExperimentMode(List<EvaluationCode> codes) {
        evaluationCodes = codes;
    }

    public static List<EvaluationCode> getEvaluationCodes(ExperimentMode mode) {
        return ExperimentMode.valueOf(mode.toString().toUpperCase()).evaluationCodes;
    }
}