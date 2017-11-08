package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.QuestionnaireDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.UserConfidence;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import edu.grenoble.em.bourji.db.pojo.UserExperience;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Moe on 9/9/2017.
 */
@Path("/questionnaire")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuestionnaireResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QuestionnaireResource.class);
    private final QuestionnaireDAO dao;
    private final StatusDAO statusDAO;
    private final JwtTokenHelper tokenHelper;

    public QuestionnaireResource(QuestionnaireDAO dao, JwtTokenHelper tokenHelper, StatusDAO statusDAO) {
        this.dao = dao;
        this.tokenHelper = tokenHelper;
        this.statusDAO = statusDAO;
    }

    @POST
    @Path("/user-demographic")
    @UnitOfWork
    public Response addUserDemographic(UserDemographic userDemographic,
                                       @Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            int submissionId = dao.getUserDemographicDAO().getNextSubmissionId(userId);
            LOGGER.info(String.format("Posting demographic questionnaire response for user %s submission id %s ", userId, submissionId));
            userDemographic.setUser(userId);
            userDemographic.setSubmissionId(submissionId);
            dao.getUserDemographicDAO().add(userDemographic);
            LOGGER.info(String.format("Setting user (%s) status to QUEST_DEMO", userId));
            statusDAO.add(new Status(userId, ProgressStatus.QUEST_DEMO.name(), submissionId));
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/user-experience")
    @UnitOfWork
    public Response addUserExperience(UserExperience userExperience,
                                      @Context HttpHeaders httpHeaders) {

        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            int submissionId = dao.getUserExperienceDAO().getNextSubmissionId(userId);
            LOGGER.info(String.format("Posting experience questionnaire response for user %s submission id %s ", userId, submissionId));
            userExperience.setUser(userId);
            userExperience.setSubmissionId(submissionId);
            dao.getUserExperienceDAO().add(userExperience);
            LOGGER.info(String.format("Setting user (%s) status to QUEST_EXP", userId));
            statusDAO.add(new Status(userId, ProgressStatus.QUEST_EXP.name(), submissionId));
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/user-confidence")
    @UnitOfWork
    public Response addUserConfidence(List<UserConfidence> userConfidenceResponse,
                                      @Context HttpHeaders httpHeaders) {

        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            int submissionId = dao.getUserConfidenceDAO().getNextSubmissionId(userId);
            LOGGER.info(String.format("Posting confidence questionnaire response for user %s submission id %s ", userId, submissionId));
            userConfidenceResponse.stream().forEach(res -> {
                res.setUser(userId);
                res.setSubmissionId(submissionId);
            });
            dao.getUserConfidenceDAO().addAll(userConfidenceResponse);
            LOGGER.info(String.format("Setting user (%s) status to QUEST_CON", userId));
            statusDAO.add(new Status(userId, ProgressStatus.QUEST_CON.name(), submissionId));
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/user-confidence")
    @UnitOfWork
    public Response getUserConfidence(@Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            LOGGER.info("User id: " + userId);
            List<UserConfidence> userConfidence = dao.getUserConfidenceDAO().getUserConfidence(userId);
            LOGGER.info("Retrieved user confidence response for user: " + userId);
            return Response.ok(userConfidence).build();
        } catch (Throwable e) {
            String message = "Error retrieving user confidence response. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @GET
    @Path("/user-demographic")
    @UnitOfWork
    public Response getUserDemographic(@Context HttpHeaders httpHeaders) {
        LOGGER.info("Getting user demographic input to populate form");
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            UserDemographic userDemographic = dao.getUserDemographicDAO().getUserDemographics(userId);
            LOGGER.info("Retrieved user demographics for " + userId);
            return Response.ok(userDemographic).build();
        } catch (Throwable e) {
            String message = "Unable to get user demographics. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @GET
    @Path("/user-experience")
    @UnitOfWork
    public Response getUserExperience(@Context HttpHeaders httpHeaders) {
        LOGGER.info("Getting user experience input to populate form");
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            UserExperience userExperience = dao.getUserExperienceDAO().getUserExperience(userId);
            LOGGER.info("Retrieved user experience details for " + userId);
            return Response.ok(userExperience).build();
        } catch (Throwable e) {
            String message = "Unable to get user demographics. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }
}