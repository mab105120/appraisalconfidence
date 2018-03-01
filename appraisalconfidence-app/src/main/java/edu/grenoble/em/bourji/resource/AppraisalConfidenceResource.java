package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.PerformanceReviewCache;
import edu.grenoble.em.bourji.api.EvaluationPayload;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.db.dao.EvaluationActivityDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
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
@Path("/appraisal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AppraisalConfidenceResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AppraisalConfidenceResource.class);
    private final PerformanceReviewCache performanceReviewCache;
    private final AppraisalConfidenceDAO dao;
    private final EvaluationActivityDAO evaluationActivityDAO;
    private final StatusDAO statusDAO;

    public AppraisalConfidenceResource(AppraisalConfidenceDAO dao, EvaluationActivityDAO evaluationActivityDAO,
                                       StatusDAO statusDAO, PerformanceReviewCache performanceReviewCache) {
        this.dao = dao;
        this.statusDAO = statusDAO;
        this.evaluationActivityDAO = evaluationActivityDAO;
        this.performanceReviewCache = performanceReviewCache;
    }

    @POST
    @UnitOfWork
    public Response postTeacherEvaluation(EvaluationPayload payload,
                                          @Context ContainerRequestContext requestContext) {

        TeacherRecommendation teacherRecommendation = payload.getRecommendation();
        List<EvaluationActivity> activities = payload.getActivities();

        if (!performanceReviewCache.isValid(teacherRecommendation.getEvaluationCode()))
            return Respond.respondWithError(String.format("Evaluation code (%s) is invalid!", teacherRecommendation.getEvaluationCode()));

        try {
            String userId = requestContext.getProperty("user").toString();
            int nextSubmissionId = dao.getNextSubmissionId(userId, payload.getRecommendation().getEvaluationCode());
            LOGGER.info("Saving user teacher recommendation for user id: " + userId + " and submission id: " + nextSubmissionId);
            teacherRecommendation.setUser(userId);
            teacherRecommendation.setSubmissionId(nextSubmissionId);
            dao.add(teacherRecommendation);
            activities.forEach(e -> {
                e.setUser(userId);
                e.setSubmissionId(nextSubmissionId);
            });
            activities.forEach(evaluationActivityDAO::add);
            ProgressStatus status = ProgressStatus.valueOf("EVALUATION_" + teacherRecommendation.getEvaluationCode());
            LOGGER.info(String.format("Setting user (%s) status to %s", userId, status));
            statusDAO.add(new Status(userId, status.name(), nextSubmissionId)); // TODO change this
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
            TeacherRecommendation recommendation = dao.getEvaluation(userId, evalCode);
            return Response.ok(recommendation).build();
        } catch (Throwable e) {
            String message = "Unable to retrieve user performance appraisal report. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }
}