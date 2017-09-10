package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.api.JobFunction;
import edu.grenoble.em.bourji.api.JobFunctionReview;
import edu.grenoble.em.bourji.api.TeacherDossier;
import edu.grenoble.em.bourji.db.pojo.PerformanceReview;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Moe on 9/5/2017.
 */
public class PerformanceReviewDAO extends AbstractDAO<PerformanceReview> {

    public PerformanceReviewDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private Map<String, List<PerformanceReview>> getAllPerformanceReviews() {
        Map<String, List<PerformanceReview>> teacherToReviewMap = new HashMap<>();
        List<PerformanceReview> reviews = list(criteria());
        for (PerformanceReview review : reviews) {
            if (teacherToReviewMap.get(review.getId()) == null) {
                List<PerformanceReview> teacherReviews = new ArrayList<>();
                teacherReviews.add(review);
                teacherToReviewMap.put(review.getId(), teacherReviews);
            } else {
                teacherToReviewMap.get(review.getId()).add(review);
            }
        }
        return teacherToReviewMap;
    }

    public Map<String, TeacherDossier> getTeacherDossiers() {
        Map<String, List<PerformanceReview>> reviewsByTeacherId = getAllPerformanceReviews();
        Map<String, TeacherDossier> reviewDossiersByTeacherId = new HashMap<>();
        for (Map.Entry<String, List<PerformanceReview>> entry : reviewsByTeacherId.entrySet())
            reviewDossiersByTeacherId.put(entry.getKey(), getTeacherDossier(entry.getValue()));
        return reviewDossiersByTeacherId;
    }

    private TeacherDossier getTeacherDossier(List<PerformanceReview> teacherReviews) {
        TeacherDossier teacherDossier = new TeacherDossier();
        teacherDossier.setStudentLearning(getJobFunctionReview(JobFunction.STUDENT_LEARNING, teacherReviews));
        teacherDossier.setInstructionalPractice(getJobFunctionReview(JobFunction.INSTRUCTIONAL_PRACTICE, teacherReviews));
        teacherDossier.setProfessionalism(getJobFunctionReview(JobFunction.PROFESSIONALISM, teacherReviews));
        return teacherDossier;
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
