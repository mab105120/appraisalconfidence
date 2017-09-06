package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.PerformanceReviewMap;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import edu.grenoble.em.bourji.db.dao.PerformanceReviewDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.HibernateException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/5/2017.
 */
@Path("/performance-review")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PerformanceReviewResource {

    private final PerformanceReviewDAO dao;

    public PerformanceReviewResource(PerformanceReviewDAO dao) {
        this.dao = dao;
    }

    @GET
    @Path("/{evaluationCode}")
    @UnitOfWork
    public Response getPerformanceReviews(@PathParam("evaluationCode") String evaluationCode) {
        Pair<String, String> evaluationPair = PerformanceReviewMap.getTeachersPerEvaluationCode(evaluationCode);
        if (evaluationPair == null)
            return respondWithError("Unknown evaluation code: " + evaluationCode);
        String teacher1 = evaluationPair.getLeft();
        String teacher2 = evaluationPair.getRight();
        TeacherDossiers dossiers;
        try {
            dossiers = dao.getTeacherDossiers(teacher1, teacher2);
            if (dossiers == null) throw new NullPointerException();
        } catch (HibernateException | NullPointerException e) {
            e.printStackTrace();
            return respondWithError("Unable to retrieve evaluation reviews from database. Error details: " + e.getMessage());
        }
        return Response.ok(dossiers).build();
    }

    private Response respondWithError(String errorMessage) {
        return Response.status(500)
                .entity(errorMessage)
                .build();
    }
}