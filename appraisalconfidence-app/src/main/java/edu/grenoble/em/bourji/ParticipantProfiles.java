package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.ParticipantProfile;

/**
 * Created by Moe on 4/18/18.
 */
public enum ParticipantProfiles {

    EXPERT(new ParticipantProfile(false, false, 0, 6, null, 15, ExperimentMode.EXPERT)),
    HiExp_HiFed(new ParticipantProfile(false, false, 4, 2, "high", 20, ExperimentMode.ABSOLUTE_HIGH)),
    HiExp_LoFed(new ParticipantProfile(false, false, 4, 2, "low", 20, ExperimentMode.ABSOLUTE_HIGH)),
    LoExp_HiFed(new ParticipantProfile(false, false, 2, 2, "high", 15, ExperimentMode.ABSOLUTE_LOW)),
    LoExp_LoFed(new ParticipantProfile(false, false, 2, 2, "low", 15, ExperimentMode.ABSOLUTE_LOW)),
    RELATIVE(new ParticipantProfile(true, false, 0, 9, null, 30, ExperimentMode.RELATIVE));

    private ParticipantProfile profile;
    ParticipantProfiles(ParticipantProfile profile) {
        this.profile = profile;
    }

    public ParticipantProfile getProfile() {
        return this.profile;
    }
}