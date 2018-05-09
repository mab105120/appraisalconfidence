package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.api.ExpertEvaluationPayload;
import edu.grenoble.em.bourji.db.dao.ExpertEvaluationDAO;
import edu.grenoble.em.bourji.db.pojo.ExpertEvaluation;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 5/7/18.
 */
@Path("/evaluation/expert")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticate
public class ExpertEvaluationResource {

    private ExpertEvaluationDAO dao;
    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExpertEvaluationResource.class);

    public ExpertEvaluationResource(ExpertEvaluationDAO dao) {
        this.dao = dao;
    }

    @POST
    @UnitOfWork
    public Response getExpertPerformanceEvaluation(ExpertEvaluationPayload payload,
                                                   @Context ContainerRequestContext requestContext) {
        String userId = requestContext.getProperty("user").toString();
        LOGGER.info("Getting expert evaluation for: " + userId);
        String evaluationCode = payload.getEvaluationCode();
        try {
            ExpertEvaluation expert = dao.get(userId, evaluationCode);
            if(expert != null)
                return Response.ok(new ExpertEvaluationPayload(
                        evaluationCode, expert.getStudentLearning(), expert.getInstructionalPractice(), expert.getProfessionalism(), expert.getOverall()
                )).build();
            else {
                LOGGER.info("No expert evaluation exist. Generating new evaluation with random setting for: " + userId);
                ExpertEvaluationPayload responsePayload = ExpertEvaluationPayload.getRandomResponse(
                        evaluationCode, payload.getStudentLearning(), payload.getInstructionalPractice(), payload.getProfessionalism()
                );
                LOGGER.info("Saving generated expert evaluation for: " + userId);
                dao.add(new ExpertEvaluation(
                        userId, evaluationCode, responsePayload.getStudentLearning(), responsePayload.getInstructionalPractice(),
                        responsePayload.getProfessionalism(), responsePayload.getOverall()
                ));
                return Response.ok(responsePayload).build();
            }
        } catch(Throwable e) {
            LOGGER.error("Unable to get expert evaluation for " + userId + ". Details: " + e.getMessage());
            return Respond.respondWithError(e.getMessage());
        }
    }
}