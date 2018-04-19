package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.ParticipantProfile;

/**
 * Created by Moe on 4/18/18.
 */
public enum ParticipantProfiles {

    EXPERT(new ParticipantProfile(false, 0, 5, null, 15)),
    HiExp_HiFed(new ParticipantProfile(false, 3, 4, "high", 15)),
    HiExp_LoFed(new ParticipantProfile(false, 3, 4, "low", 15)),
    LoExp_HiFed(new ParticipantProfile(false, 1, 4, "high", 15)),
    LoExp_LoFed(new ParticipantProfile(false, 1, 4, "low", 15)),
    RELATIVE(new ParticipantProfile(true, 0, 9, null, 30));

    private ParticipantProfile profile;
    ParticipantProfiles(ParticipantProfile profile) {
        this.profile = profile;
    }

    public ParticipantProfile getProfile() {
        return this.profile;
    }
}