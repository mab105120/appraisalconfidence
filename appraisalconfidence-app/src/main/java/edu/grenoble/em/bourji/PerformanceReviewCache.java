package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.TeacherDossier;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import edu.grenoble.em.bourji.db.dao.PerformanceReviewDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 9/5/2017.
 */
public class PerformanceReviewCache {

    private final PerformanceReviewDAO dao;
    private final Map<String, Pair<String, String>> codeToProfilesMap;
    private Map<String, TeacherDossier> teacherDossiers = new HashMap<>();

    public PerformanceReviewCache(PerformanceReviewDAO dao) {
        this.dao = dao;
        codeToProfilesMap = new HashMap<>();
        codeToProfilesMap.put("1", new ImmutablePair<>("A", "B"));
        codeToProfilesMap.put("2", new ImmutablePair<>("B", "C"));
    }

    @UnitOfWork
    void instantiateCache() {
        this.teacherDossiers = dao.getTeacherDossiers();
    }

    public TeacherDossiers getTeacherDossiers(String evaluationCode) {
        Pair<String, String> teachers = codeToProfilesMap.get(evaluationCode);
        if (teachers == null) throw new NullPointerException("Invalid evaluation code: " + evaluationCode);
        return new TeacherDossiers(teacherDossiers.get(teachers.getLeft()), teacherDossiers.get(teachers.getRight()));
    }

    public boolean isValid(String evaluationCode) {
        return codeToProfilesMap.get(evaluationCode) != null;
    }
}