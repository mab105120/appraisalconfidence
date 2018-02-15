package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 10/20/17.
 */
public class EvaluationCode {

    private String evaluationCode;
    private String teacher1;
    private String teacher2;

    public EvaluationCode(String evaluationCode, String teacher1, String teacher2) {
        this.evaluationCode = evaluationCode;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public String getTeacher1() {
        return teacher1;
    }

    public String getTeacher2() {
        return teacher2;
    }
}
