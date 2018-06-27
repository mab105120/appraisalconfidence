package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.ParticipantProfiles;
import edu.grenoble.em.bourji.ProfileAssignment;
import edu.grenoble.em.bourji.db.dao.ParticipantProfileDAO;
import edu.grenoble.em.bourji.db.pojo.ParticipantProfile;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;

/**
 * Created by Moe on 4/18/18.
 */
@Path("/participant-profile")
@Produces(MediaType.APPLICATION_JSON)
@Authenticate
public class ParticipantProfileResource {

    private ParticipantProfileDAO dao;
    private ProfileAssignment assignment;

    public ParticipantProfileResource(ParticipantProfileDAO dao, String assignment) {
        this.dao = dao;
        this.assignment = ProfileAssignment.getAssignmentMethod(assignment);
    }

    /**
     * Assign a participant profile if one doesn't exist, return if profile exists
     *
     * @param requestContext http request context
     * @return Assign a participant profile if one doesn't exist, return if profile exists
     */
    @GET
    @UnitOfWork
    public Response getParticipantProfile(@Context ContainerRequestContext requestContext) {
        try {
            if (assignment == ProfileAssignment.RELATIVE)
                return Response.ok(ParticipantProfiles.RELATIVE.getProfile()).build();
            String user = requestContext.getProperty("user").toString();
            ParticipantProfile profile = dao.getParticipantProfile(user);
            if (profile == null) {
                ParticipantProfiles newProfile = assignProfile(assignment);
                dao.add(user, newProfile);
                return Response.ok(newProfile.getProfile()).build();
            } else
                return Response.ok(ParticipantProfiles.valueOf(profile.getProfile()).getProfile()).build();
        } catch(Throwable e) {
            return Respond.respondWithError(e.getMessage());
        }
    }

    /**
     * Assigns a profile to a participant based on an assignment method.
     * if method is teacher return expert profile, else randomly return one of four participant profiles
     *
     * @param assignment assignment method: expert or random
     * @return profile assignment
     */
    private ParticipantProfiles assignProfile(ProfileAssignment assignment) {
        if (assignment == ProfileAssignment.EXPERT)
            return ParticipantProfiles.EXPERT;
        else if (assignment == ProfileAssignment.RANDOM) {
            Random random = new Random();
            int randomNumber = random.nextInt(4) + 1;
            switch (randomNumber) {
                case 1:
                    return ParticipantProfiles.HiExp_HiFed;
                case 2:
                    return ParticipantProfiles.HiExp_LoFed;
                case 3:
                    return ParticipantProfiles.LoExp_HiFed;
                case 4:
                    return ParticipantProfiles.LoExp_LoFed;
            }
        } else if (assignment == ProfileAssignment.RANDOM_WITH_TRAINING) {
            Random random = new Random();
            int rn = random.nextInt(2) + 1;
            switch(rn) {
                case 1:
                    return ParticipantProfiles.LoExp_LoFed_T;
                case 2:
                    return ParticipantProfiles.HiExp_LoFed_T;
            }
        }
        throw new IllegalArgumentException(String.format("Application is set up with an unknown assignment (%s).", assignment));
    }
}