package edu.grenoble.em.bourji.api;

import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;

import java.util.List;

/**
 * Created by Moe on 9/28/17.
 */
public class EvaluationPayload {

    private TeacherRecommendation recommendation;
    private List<EvaluationActivity> activities;

    public EvaluationPayload() {
        // no-arg default constructor for jackson
    }

    public TeacherRecommendation getRecommendation() {
        return recommendation;
    }

    public List<EvaluationActivity> getActivities() {
        return activities;
    }
}
