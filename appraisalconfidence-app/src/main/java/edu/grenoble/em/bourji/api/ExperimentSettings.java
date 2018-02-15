package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 2/14/2018.
 */
public class ExperimentSettings {

    private String mode;
    private int totalEvaluations;
    private String duration;

    public ExperimentSettings() {
        // default no-arg constructor
    }

    public ExperimentSettings(String mode, String duration, int totalEvaluations) {
        this.mode = mode;
        this.duration = duration;
        this.totalEvaluations = totalEvaluations;
    }

    public String getMode() {
        return mode;
    }

    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    public String getDuration() {
        return duration;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTotalEvaluations(int totalEvaluations) {
        this.totalEvaluations = totalEvaluations;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}