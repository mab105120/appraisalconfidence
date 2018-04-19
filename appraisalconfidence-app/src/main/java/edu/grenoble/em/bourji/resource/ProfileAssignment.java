package edu.grenoble.em.bourji.resource;

/**
 * Created by Moe on 4/18/18.
 */
public enum ProfileAssignment {
    RANDOM, EXPERT;

    public static ProfileAssignment getAssignmentMethod(String mode) {
        switch(mode.toLowerCase()) {
            case "expert":
                return ProfileAssignment.EXPERT;
            case "random":
                return ProfileAssignment.RANDOM;
        }
        return null;
    }
}
