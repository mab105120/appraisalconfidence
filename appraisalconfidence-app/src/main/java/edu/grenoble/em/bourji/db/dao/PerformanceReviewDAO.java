package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.api.JobFunction;
import edu.grenoble.em.bourji.api.JobFunctionReview;
import edu.grenoble.em.bourji.api.TeacherDossier;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import edu.grenoble.em.bourji.db.pojo.PerformanceReview;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Moe on 9/5/2017.
 */
public class PerformanceReviewDAO extends AbstractDAO<PerformanceReview> {

    public PerformanceReviewDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(PerformanceReview review) {
        currentSession().save(review);
    }

    private List<PerformanceReview> getPerformanceReviewsByTeacher(String teacherIdentifier) {
        return list(criteria().add(Restrictions.eq("id", teacherIdentifier)));
    }

    private TeacherDossier getTeacherDossier(String teacherIdentifier) {
        List<PerformanceReview> performanceReviews = getPerformanceReviewsByTeacher(teacherIdentifier);
        TeacherDossier teacherDossier = new TeacherDossier();
        teacherDossier.setStudentLearning(getJobFunctionReview(JobFunction.STUDENT_LEARNING, performanceReviews));
        teacherDossier.setInstructionalPractice(getJobFunctionReview(JobFunction.INSTRUCTIONAL_PRACTICE, performanceReviews));
        teacherDossier.setProfessionalism(getJobFunctionReview(JobFunction.PROFESSIONALISM, performanceReviews));
        return teacherDossier;
    }

    public TeacherDossiers getTeacherDossiers(String teacher1, String teacher2) {
        return new TeacherDossiers(getTeacherDossier(teacher1), getTeacherDossier(teacher2));
    }

    private JobFunctionReview getJobFunctionReview(JobFunction jobFunction, List<PerformanceReview> reviews) {
        List<PerformanceReview> reviewsPerJobFunction = reviews
                .stream()
                .filter(r -> r.getJobFunction().equals(jobFunction.getCode()))
                .collect(Collectors.toList());
        JobFunctionReview jobFunctionReview = new JobFunctionReview();
        for (PerformanceReview review : reviewsPerJobFunction) {
            switch (review.getSupervisor().toLowerCase()) {
                case "sp1":
                    jobFunctionReview.setSupervisor1Review(review.getReview());
                    break;
                case "sp2":
                    jobFunctionReview.setSupervisor2Review(review.getReview());
                    break;
                case "sp3":
                    jobFunctionReview.setSupervisor3Review(review.getReview());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown supervisor code: " + review.getSupervisor());
            }
        }
        return jobFunctionReview;
    }
}
