package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;

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
        cr.add(Restrictions.eq("user", userId))
                .add(Restrictions.eq("evaluationCode", evalCode))
                .add(Restrictions.eq("submissionId", getLastSubmissionId(userId, evalCode)));
        return (TeacherRecommendation) cr.list().get(0);
    }

    public List<TeacherRecommendation> getAllRecommendations(String userId) {
        Criteria cr = currentSession().createCriteria(TeacherRecommendation.class);
        cr.add(Restrictions.eq("user", userId));
        return cr.list();
    }

    private int getLastSubmissionId(String user, String evaluationCode) {
        Criteria cr = currentSession().createCriteria(TeacherRecommendation.class);
        cr.add(Restrictions.eq("user", user));
        cr.add(Restrictions.eq("evaluationCode", evaluationCode));
        cr.setProjection(Projections.distinct(Projections.property("submissionId")));
        List<Integer> submissionIds = cr.list();
        submissionIds.sort(Comparator.naturalOrder());
        return submissionIds.isEmpty() ? 0 : submissionIds.get(submissionIds.size() - 1);
    }

    public int getNextSubmissionId(String user, String evaluationCode) {
        return getLastSubmissionId(user, evaluationCode) + 1;
    }
}
