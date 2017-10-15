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
        codeToProfilesMap.put("2", new ImmutablePair<>("D", "E"));
        codeToProfilesMap.put("3", new ImmutablePair<>("G", "H"));
        codeToProfilesMap.put("4", new ImmutablePair<>("A", "D"));
        codeToProfilesMap.put("5", new ImmutablePair<>("B", "E"));
        codeToProfilesMap.put("6", new ImmutablePair<>("B", "C"));
        codeToProfilesMap.put("7", new ImmutablePair<>("E", "F"));
        codeToProfilesMap.put("8", new ImmutablePair<>("H", "I"));
        codeToProfilesMap.put("9", new ImmutablePair<>("D", "G"));
        codeToProfilesMap.put("10", new ImmutablePair<>("E", "F"));
        codeToProfilesMap.put("11", new ImmutablePair<>("A", "C"));
        codeToProfilesMap.put("12", new ImmutablePair<>("D", "F"));
        codeToProfilesMap.put("13", new ImmutablePair<>("G", "I"));
        codeToProfilesMap.put("14", new ImmutablePair<>("A", "G"));
        codeToProfilesMap.put("15", new ImmutablePair<>("B", "F"));
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