package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by Moe on 9/18/17.
 */
public class AppraisalConfidenceDAO extends AbstractDAO<TeacherRecommendation> {

    public AppraisalConfidenceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(TeacherRecommendation teacherRecommendation) {
        persist(teacherRecommendation);
    }
}
