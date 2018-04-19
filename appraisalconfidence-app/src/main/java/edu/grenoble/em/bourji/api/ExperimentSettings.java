package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 2/14/2018.
 */
public class ExperimentSettings {

    private String mode;
    private String assignment;

    public ExperimentSettings() {
        // default no-arg constructor
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAssignment() {
        return assignment;
    }
}