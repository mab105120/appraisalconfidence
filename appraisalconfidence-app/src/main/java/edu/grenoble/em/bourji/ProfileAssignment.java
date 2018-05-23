package edu.grenoble.em.bourji;

/**
 * Created by Moe on 4/18/18.
 */
public enum ProfileAssignment {
    RELATIVE, RANDOM, EXPERT;

    public static ProfileAssignment getAssignmentMethod(String mode) {
        switch(mode.toLowerCase()) {
            case "relative":
                return ProfileAssignment.RELATIVE;
            case "expert":
                return ProfileAssignment.EXPERT;
            case "random":
                return ProfileAssignment.RANDOM;
        }
        return null;
    }
}
