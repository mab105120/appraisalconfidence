package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 11/5/2017.
 */
public enum ProgressStatus {

    NOT_STARTED(0),
    QUEST_DEMO(1),
    QUEST_EXP(2),
    QUEST_CON(3),
    EVALUATION_P1(4),
    EVALUATION_P2(5),
    EVALUATION_P3(6),
    EVALUATION_1(7),
    EVALUATION_2(8),
    EVALUATION_3(9),
    EVALUATION_4(10),
    EVALUATION_5(11),
    EVALUATION_6(12),
    EVALUATION_7(13),
    EVALUATION_8(14),
    EVALUATION_9(15),
    EVALUATION_10(16),
    EVALUATION_11(17),
    EVALUATION_12(18),
    EVALUATION_13(19),
    EVALUATION_14(20),
    EVALUATION_15(21),
    COMPLETE(22);

    private int priority;

    ProgressStatus(int priority) {
        this.priority = priority;
    }

    public static ProgressStatus getNextStatus(ProgressStatus current) {
        return ProgressStatus.values()[current.priority + 1];
    }

    public int getPriority() {
        return priority;
    }
}