package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 8/29/17.
 */
public class TeacherDossier {

    private JobFunctionReview studentLearning;
    private JobFunctionReview instructionalPractice;
    private JobFunctionReview professionalism;

    public TeacherDossier() {
        // default no-arg constructor for jackson
    }

    public TeacherDossier(JobFunctionReview studentLearning,
                          JobFunctionReview instructionalPractice,
                          JobFunctionReview professionalism) {
        this.studentLearning = studentLearning;
        this.instructionalPractice = instructionalPractice;
        this.professionalism = professionalism;
    }

    public JobFunctionReview getStudentLearning() {
        return studentLearning;
    }

    public JobFunctionReview getInstructionalPractice() {
        return instructionalPractice;
    }

    public JobFunctionReview getProfessionalism() {
        return professionalism;
    }

    public void setStudentLearning(JobFunctionReview studentLearning) {
        this.studentLearning = studentLearning;
    }

    public void setInstructionalPractice(JobFunctionReview instructionalPractice) {
        this.instructionalPractice = instructionalPractice;
    }

    public void setProfessionalism(JobFunctionReview professionalism) {
        this.professionalism = professionalism;
    }
}