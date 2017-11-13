package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 11/5/2017.
 */
public enum ProgressStatus {

    NOT_STARTED(0),
    QUEST_DEMO(1),
    QUEST_EXP(2),
    QUEST_CON(3),
    EVALUATION_1(4),
    EVALUATION_2(5),
    EVALUATION_3(6),
    EVALUATION_4(7),
    EVALUATION_5(8),
    EVALUATION_6(9),
    EVALUATION_7(10),
    EVALUATION_8(11),
    EVALUATION_9(12),
    EVALUATION_10(13),
    EVALUATION_11(14),
    EVALUATION_12(15),
    EVALUATION_13(16),
    EVALUATION_14(17),
    EVALUATION_15(18);

    private int priority;

    ProgressStatus(int priority) {
        this.priority = priority;
    }

    public static ProgressStatus getNextStatus(ProgressStatus current) {
        return ProgressStatus.values()[current.priority];
    }
}