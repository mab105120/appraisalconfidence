package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.PerformanceReviewCache;
import edu.grenoble.em.bourji.db.dao.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    private final StatusDAO statusDAO;

    public AppraisalConfidenceResource(JwtTokenHelper tokenHelper, AppraisalConfidenceDAO dao,
                                       StatusDAO statusDAO, PerformanceReviewCache performanceReviewCache) {
        this.tokenHelper = tokenHelper;
        this.dao = dao;
        this.statusDAO = statusDAO;
        this.performanceReviewCache = performanceReviewCache;
    }

    @POST
    @UnitOfWork
    public Response postTeacherEvaluation(TeacherRecommendation teacherRecommendation,
                                          @Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        if(!performanceReviewCache.isValid(teacherRecommendation.getEvaluationCode()))
            return Respond.respondWithError(String.format("Evaluation code (%s) is invalid!", teacherRecommendation.getEvaluationCode()));

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            LOGGER.info("User id: " + userId);
            teacherRecommendation.setUser(userId);
            dao.add(teacherRecommendation);
            String status = "EVALUATION_" + teacherRecommendation.getEvaluationCode();
            LOGGER.info(String.format("Setting user (%s) status to %s", userId, status));
            statusDAO.add(new Status(userId, status));
        } catch (HibernateException | JwkException e) {
            LOGGER.error("Error: " + e.getMessage());
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }
}