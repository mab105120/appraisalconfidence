package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Moe on 9/18/17.
 */
@Entity
@Table(name = "EVALUATION")
@IdClass(TeacherRecommendation.UniqueIdentifier.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherRecommendation {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Id
    @Column(name = "EVAL_CODE", nullable = false, length = 5)
    private String evaluationCode;
    @Column(name = "RECOMMENDATION_PICK", nullable = false, length = 5)
    private String recommendationPick;
    @Column(name = "ABS_CONFIDENCE", nullable = false)
    private int absConfidence;
    @Column(name = "REL_CONFIDENCE", nullable = false)
    private int relConfidence;
    @Column(name = "COMMENT", nullable = false, length = 250)
    private String comment;

    public TeacherRecommendation() {
        // no-arg default constructor for hibernate
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getEvaluationCode() {
        return evaluationCode;
    }

    public String getRecommendationPick() {
        return recommendationPick;
    }

    public int getAbsConfidence() {
        return absConfidence;
    }

    public int getRelConfidence() {
        return relConfidence;
    }

    public String getComment() {
        return comment;
    }

    public static class UniqueIdentifier implements Serializable {

        private String user;
        private String evaluationCode;

        public UniqueIdentifier() {
            // no-arg constructor for hibernate
        }

        public String getUser() {
            return user;
        }

        public String getEvalulationCode() {
            return evaluationCode;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(!(o instanceof UniqueIdentifier))
                return false;
            UniqueIdentifier that = (UniqueIdentifier) o;
            return this.user.equals(that.user) && this.evaluationCode.equals(that.evaluationCode);
        }

        @Override
        public int hashCode() {
            int hashCode = 5;
            return 37 * hashCode + (
                    this.user.hashCode() + this.evaluationCode.hashCode()
            );
        }
    }

}