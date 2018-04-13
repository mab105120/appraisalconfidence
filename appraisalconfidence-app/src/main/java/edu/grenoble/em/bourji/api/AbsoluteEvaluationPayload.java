package edu.grenoble.em.bourji.api;

import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.AbsoluteEvaluation;

import java.util.List;

/**
 * Created by Moe on 4/11/18.
 */
public class AbsoluteEvaluationPayload {

    private AbsoluteEvaluation teacherEvaluation;
    private List<EvaluationActivity> activities;
    private String datetimeIn;
    private String datetimeOut;

    public AbsoluteEvaluationPayload() {
        // no-arg default constructor for jackson
    }

    public AbsoluteEvaluation getTeacherEvaluation() {
        return teacherEvaluation;
    }

    public List<EvaluationActivity> getActivities() {
        return activities;
    }

    public String getDatetimeIn() {
        return datetimeIn;
    }

    public String getDatetimeOut() {
        return datetimeOut;
    }
}
