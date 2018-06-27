package edu.grenoble.em.bourji;

/**
 * Created by Moe on 4/18/18.
 */
public enum ProfileAssignment {
    RELATIVE, RANDOM, RANDOM_WITH_TRAINING, EXPERT;

    public static ProfileAssignment getAssignmentMethod(String mode) {
        switch(mode.toLowerCase()) {
            case "relative":
                return ProfileAssignment.RELATIVE;
            case "expert":
                return ProfileAssignment.EXPERT;
            case "random":
                return ProfileAssignment.RANDOM;
            case "training":
                return ProfileAssignment.RANDOM_WITH_TRAINING;
        }
        return null;
    }
}
