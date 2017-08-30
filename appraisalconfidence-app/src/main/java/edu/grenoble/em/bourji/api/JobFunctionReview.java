package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 8/29/17.
 */
public class JobFunctionReview {
    
    private String supervisor1Review;
    private String supervisor2Review;
    private String supervisor3Review;

    public JobFunctionReview() {
        // default no arg constructor for jackson
    }

    public String getSupervisor1Review() {
        return supervisor1Review;
    }

    public String getSupervisor2Review() {
        return supervisor2Review;
    }

    public String getSupervisor3Review() {
        return supervisor3Review;
    }

    public void setSupervisor1Review(String supervisor1Review) {
        this.supervisor1Review = supervisor1Review;
    }

    public void setSupervisor2Review(String supervisor2Review) {
        this.supervisor2Review = supervisor2Review;
    }

    public void setSupervisor3Review(String supervisor3Review) {
        this.supervisor3Review = supervisor3Review;
    }
}