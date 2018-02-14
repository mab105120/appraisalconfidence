package edu.grenoble.em.bourji.api;

import java.util.List;

/**
 * Created by Moe on 11/5/2017.
 */
public class Progress {

    private List<ProgressStatus> completed;
    private ProgressStatus next;

    public Progress(List<ProgressStatus> completed) {
        this.completed = completed;
        if (completed.size() != 0)
            this.next = ProgressStatus.getNextStatus(completed.get(completed.size() - 1));
        else next = ProgressStatus.QUEST_DEMO;
    }

    public Progress() {
        // default no-arg constructor for jackson
    }

    public List<ProgressStatus> getCompleted() {
        return completed;
    }

    public ProgressStatus getNext() {
        return next;
    }
}