package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.PerformanceReviewCache;
import edu.grenoble.em.bourji.api.RelativeEvaluationPayload;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.db.dao.EvaluationActivityDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.RelativeEvaluation;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Moe on 8/16/2017.
 */
@Path("/evaluation/relative")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticate
public class RelativeEvaluationResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RelativeEvaluationResource.class);
    private final AppraisalConfidenceDAO dao;
    private final EvaluationActivityDAO evaluationActivityDAO;
    private final StatusDAO statusDAO;

    public RelativeEvaluationResource(AppraisalConfidenceDAO dao, EvaluationActivityDAO evaluationActivityDAO, StatusDAO statusDAO) {
        this.dao = dao;
        this.statusDAO = statusDAO;
        this.evaluationActivityDAO = evaluationActivityDAO;
    }

    @POST
    @UnitOfWork
    public Response postTeacherEvaluation(RelativeEvaluationPayload payload,
                                          @Context ContainerRequestContext requestContext) {

        RelativeEvaluation relativeEvaluation = payload.getRecommendation();
        relativeEvaluation.setOpenTime(payload.getDatetimeIn());
        relativeEvaluation.setCloseTime(payload.getDatetimeOut());
        List<EvaluationActivity> activities = payload.getActivities();

        if (!PerformanceReviewCache.isValid(relativeEvaluation.getEvaluationCode(), payload.getMode()))
            return Respond.respondWithError(String.format("Evaluation code (%s) is invalid!", relativeEvaluation.getEvaluationCode()));

        try {
            String userId = requestContext.getProperty("user").toString();
            int nextSubmissionId = dao.getNextSubmissionId(userId, payload.getRecommendation().getEvaluationCode());
            LOGGER.info("Saving user teacher recommendation for user id: " + userId + " and submission id: " + nextSubmissionId);
            relativeEvaluation.setUser(userId);
            relativeEvaluation.setSubmissionId(nextSubmissionId);
            dao.add(relativeEvaluation);
            activities.forEach(e -> {
                e.setUser(userId);
                e.setSubmissionId(nextSubmissionId);
            });
            activities.forEach(evaluationActivityDAO::add);
            ProgressStatus status = ProgressStatus.valueOf("EVALUATION_" + relativeEvaluation.getEvaluationCode());
            LOGGER.info(String.format("Setting user (%s) status to %s", userId, status));
            statusDAO.add(new Status(userId, status.name(), nextSubmissionId));
        } catch (Throwable e) {
            LOGGER.error("Error: " + e.getMessage());
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{evalCode}")
    @UnitOfWork
    public Response getAppraisal(@PathParam("evalCode") String evalCode, @Context ContainerRequestContext requestContext) {

        try {
            String userId = requestContext.getProperty("user").toString();
            LOGGER.info("Getting performance appraisal recommendation for " + userId);
            RelativeEvaluation recommendation = dao.getEvaluation(userId, evalCode);
            return Response.ok(recommendation).build();
        } catch (Throwable e) {
            String message = "Unable to retrieve user performance appraisal report. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }
}