package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

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

    public TeacherRecommendation getEvaluation(String userId, String evalCode) {
        Criteria cr = currentSession().createCriteria(TeacherRecommendation.class);
        cr.add(Restrictions.eq("user", userId)).add(Restrictions.eq("evaluationCode", evalCode));
        return (TeacherRecommendation) cr.list().get(0);
    }
}
