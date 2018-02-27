package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.api.ExperimentSettings;
import edu.grenoble.em.bourji.api.Progress;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
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
    private ExperimentSettings settings;

    public StatusResource(StatusDAO statusDAO, ExperimentSettings settings) {
        this.statusDAO = statusDAO;
        this.settings = settings;
    }

    @GET
    @Path("/progress")
    @UnitOfWork
    public Response getProgressStatus(@Context ContainerRequestContext requestContext) {
        try {
            String user = requestContext.getProperty("user").toString();
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
                                             @Context ContainerRequestContext requestContext) {
        LOGGER.info("Checking if step (" + step + ") is completed");

        try {
            String userId = requestContext.getProperty("user").toString();
            boolean isCompleted = statusDAO.stepCompleted(userId, ProgressStatus.valueOf(step));
            LOGGER.info(String.format("step %s completed for %s is %s", step, userId, isCompleted));
            return Response.ok(isCompleted).build();
        } catch (Throwable e) {
            String message = "Failed to retrieve whether user completed step (" + step + "). Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @GET
    @Path("/completion-code")
    @Produces(MediaType.TEXT_PLAIN)
    @UnitOfWork
    public Response getCompletionCode(@Context ContainerRequestContext requestContext) {
        String user = requestContext.getProperty("user").toString();
        Progress progress = new Progress(statusDAO.getProgress(user));
        if (settings.getTotalEvaluations() == progress.getNext().getPriority() - 4)
            return Response.ok(settings.getCompletionCode()).build();
        else return Response.serverError().entity("Experiment not completed.").build();
    }

}