package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.PerformanceReviewCache;
import edu.grenoble.em.bourji.api.EvaluationPayload;
import edu.grenoble.em.bourji.db.dao.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.db.dao.EvaluationActivityDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
    private final JwtTokenHelper tokenHelper;
    private final PerformanceReviewCache performanceReviewCache;
    private final AppraisalConfidenceDAO dao;
    private final EvaluationActivityDAO evaluationActivityDAO;
    private final StatusDAO statusDAO;

    public AppraisalConfidenceResource(JwtTokenHelper tokenHelper, AppraisalConfidenceDAO dao, EvaluationActivityDAO evaluationActivityDAO,
                                       StatusDAO statusDAO, PerformanceReviewCache performanceReviewCache) {
        this.tokenHelper = tokenHelper;
        this.dao = dao;
        this.statusDAO = statusDAO;
        this.evaluationActivityDAO = evaluationActivityDAO;
        this.performanceReviewCache = performanceReviewCache;
    }

    @POST
    @UnitOfWork
    public Response postTeacherEvaluation(EvaluationPayload payload,
                                          @Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        TeacherRecommendation teacherRecommendation = payload.getRecommendation();
        List<EvaluationActivity> activities = payload.getActivities();

        if(!performanceReviewCache.isValid(teacherRecommendation.getEvaluationCode()))
            return Respond.respondWithError(String.format("Evaluation code (%s) is invalid!", teacherRecommendation.getEvaluationCode()));

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            LOGGER.info("Saving user teacher recommendation for user id: " + userId);
            teacherRecommendation.setUser(userId);
            dao.add(teacherRecommendation);
            activities.forEach(e -> e.setUser(userId));
            activities.forEach(evaluationActivityDAO::add);
            String status = "EVALUATION_" + teacherRecommendation.getEvaluationCode();
            LOGGER.info(String.format("Setting user (%s) status to %s", userId, status));
            statusDAO.add(new Status(userId, status));
        } catch (HibernateException | JwkException e) {
            LOGGER.error("Error: " + e.getMessage());
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{evalCode}")
    @UnitOfWork
    public Response getAppraisal(@PathParam("evalCode") String evalCode, @Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
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