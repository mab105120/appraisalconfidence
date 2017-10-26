package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 10/20/17.
 */
public enum EvaluationCode {

    a("1","A","B"),
    b("2","C","I"),
    c("3","G","H"),
    d("4","A","D"),
    e("5","C","F"),
    f("6","G","I"),
    g("7","F","E"),
    h("8","B","C"),
    i("9","D","G"),
    j("10","F","I"),
    k("11","A","C"),
    l("12","D","E"),
    m("13","I","H"),
    n("14","A","G"),
    o("15","D","F");

    private String evaluationCode;
    private String teacher1;
    private String teacher2;

    EvaluationCode(String evaluationCode, String teacher1, String teacher2) {
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
