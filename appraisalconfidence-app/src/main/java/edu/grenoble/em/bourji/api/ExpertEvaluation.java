package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 5/1/18.
 */
public class ExpertEvaluation {
    private String evaluationCode;
    private double studentLearning;
    private double instructionalPractice;
    private double professionalism;
    private double overall;

    public ExpertEvaluation() {
        // default no-arg constructor for Jackson
    }

    public ExpertEvaluation(String evaluationCode, double studentLearning, double instructionalPractice, double professionalism, double overall) {
        this.evaluationCode = evaluationCode;
        this.studentLearning = studentLearning;
        this.instructionalPractice = instructionalPractice;
        this.professionalism = professionalism;
        this.overall = overall;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public double getStudentLearning() {
        return studentLearning;
    }

    public double getInstructionalPractice() {
        return instructionalPractice;
    }

    public double getProfessionalism() {
        return professionalism;
    }

    public double getOverall() {
        return overall;
    }
}
