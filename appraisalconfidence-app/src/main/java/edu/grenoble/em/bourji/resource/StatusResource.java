package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.api.Progress;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/23/2017.
 */
@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StatusResource.class);
    private StatusDAO statusDAO;
    private JwtTokenHelper tokenHelper;

    public StatusResource(StatusDAO statusDAO, JwtTokenHelper tokenHelper) {
        this.statusDAO = statusDAO;
        this.tokenHelper = tokenHelper;
    }

//    @POST
//    @Path("/{status}")
//    @UnitOfWork
//    public Response postStatus(@PathParam("status") String status,
//                               @Context HttpHeaders httpHeaders) {
//        String access_token = httpHeaders.getHeaderString("Authorization");
//        if (access_token == null)
//            return Respond.respondWithUnauthorized();
//        access_token = access_token.substring(7);
//        try {
//            String user = tokenHelper.getUserIdFromToken(access_token);
//            LOGGER.info(String.format("Setting status of user (%s) to %s", user, status));
//            statusDAO.add(new Status(user, ProgressStatus.valueOf(status).name()));
//        } catch (Throwable e) {
//            String errorMessage = "Oops we had trouble updating your progress: " + e.getMessage();
//            LOGGER.error(errorMessage);
//            return Respond.respondWithError(errorMessage);
//        }
//        return Response.ok().build();
//    }

    @GET
    @UnitOfWork
    public Response getStatus(@Context HttpHeaders httpHeaders) {
        String access_token = httpHeaders.getHeaderString("Authorization");
        if (access_token == null)
            return Respond.respondWithUnauthorized();
        access_token = access_token.substring(7);
        try {
            String user = tokenHelper.getUserIdFromToken(access_token);
            LOGGER.info(String.format("Getting status for user (%s)", user));
            ProgressStatus status = statusDAO.getCurrentStatus(user);
            return Response.ok(status).build();
        } catch (Throwable e) {
            String errorMessage = String.format("Unable to get user status: %s", e.getMessage());
            LOGGER.error(errorMessage);
            return Respond.respondWithError(errorMessage);
        }
    }

    @GET
    @Path("/progress")
    @UnitOfWork
    public Response getProgressStatus(@Context HttpHeaders httpHeaders) {
        String access_token = httpHeaders.getHeaderString("Authorization");
        if (access_token == null)
            return Respond.respondWithUnauthorized();
        access_token = access_token.substring(7);
        try {
            String user = tokenHelper.getUserIdFromToken(access_token);
            LOGGER.info(String.format("Getting progress for user (%s)", user));
            return Response.ok(new Progress(statusDAO.getProgress(user))).build();
        } catch (Throwable e) {
            String message = "Unable to get progress. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @GET
    @Path("/step-is-completed/{step}")
    @UnitOfWork
    public Response isQuestionnaireCompleted(@PathParam("step") String step,
                                             @Context HttpHeaders httpHeaders) {
        LOGGER.info("Checking if step (" + step + ") is completed");
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            boolean isCompleted = statusDAO.stepCompleted(userId, ProgressStatus.valueOf(step));
            LOGGER.info(String.format("step %s completed for %s is %s", step, userId, isCompleted));
            return Response.ok(isCompleted).build();
        } catch (Throwable e) {
            String message = "Failed to retrieve whether user completed step (" + step + "). Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }
}