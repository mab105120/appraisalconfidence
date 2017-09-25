package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 8/29/17.
 */
public class TeacherDossiers {

    private TeacherDossier teacher1;
    private TeacherDossier teacher2;

    public TeacherDossiers() {
        // default no-arg constructor for jackson
    }

    public TeacherDossiers(TeacherDossier teacher1, TeacherDossier teacher2) {
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
    }

    public TeacherDossier getTeacher1() {
        return teacher1;
    }

    public TeacherDossier getTeacher2() {
        return teacher2;
    }

    public void setTeacher1(TeacherDossier teacher1) {
        this.teacher1 = teacher1;
    }

    public void setTeacher2(TeacherDossier teacher2) {
        this.teacher2 = teacher2;
    }
}