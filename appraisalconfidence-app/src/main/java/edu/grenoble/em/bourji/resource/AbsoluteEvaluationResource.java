package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.api.AbsoluteEvaluationPayload;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.EvaluationActivityDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.dao.AbsoluteEvaluationDao;
import edu.grenoble.em.bourji.db.pojo.AbsoluteEvaluation;
import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 4/11/18.
 */
@Path("/evaluation/absolute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AbsoluteEvaluationResource {

    private AbsoluteEvaluationDao dao;
    private EvaluationActivityDAO activityDao;
    private StatusDAO statusDao;

    public AbsoluteEvaluationResource(AbsoluteEvaluationDao dao, EvaluationActivityDAO activityDao, StatusDAO statusDAO) {
        this.dao = dao;
        this.activityDao = activityDao;
        this.statusDao = statusDAO;
    }

    /**
     * Adds absolute teacher evaluation to database.
     *
     * @param payload        evaluation payload includes: teacher evaluation, user activity, and evaluation start\end time
     * @param requestContext caller metadata
     * @return response: 500 if error, 200 otherwise
     */
    @POST
    @UnitOfWork
    public Response addEvaluation(AbsoluteEvaluationPayload payload,
                                  @Context ContainerRequestContext requestContext) {
        // Add teacher evaluation
        AbsoluteEvaluation evaluation = payload.getTeacherEvaluation();
        evaluation.setTimeIn(payload.getDatetimeIn());
        evaluation.setTimeOut(payload.getDatetimeOut());

        try {
            String user = requestContext.getProperty("user").toString();
            int nextSubmissionId = dao.getNextSubmissionId(user, payload.getTeacherEvaluation().getEvaluationCode());
            evaluation.setUser(user);
            evaluation.setSubmissionId(nextSubmissionId);
            dao.add(evaluation);
            // Add elvaluation activity
            payload.getActivities().forEach(a -> {
                a.setUser(user);
                a.setSubmissionId(nextSubmissionId);
            });
            payload.getActivities().forEach(activityDao::add);
            // Update status
            ProgressStatus status = ProgressStatus.valueOf("EVALUATION_" + evaluation.getEvaluationCode());
            statusDao.add(new Status(user, status.name(), nextSubmissionId));
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }
}