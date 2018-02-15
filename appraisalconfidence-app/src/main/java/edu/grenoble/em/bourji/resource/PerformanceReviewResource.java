package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.PerformanceReviewCache;
import edu.grenoble.em.bourji.api.ExperimentSettings;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import io.dropwizard.hibernate.UnitOfWork;
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

    private final PerformanceReviewCache cache;
    private final ExperimentSettings settings;

    public PerformanceReviewResource(PerformanceReviewCache cache, ExperimentSettings settings) {
        this.cache = cache;
        this.settings = settings;
    }

    @GET
    @Path("/{evaluationCode}")
    @UnitOfWork
    public Response getPerformanceReviews(@PathParam("evaluationCode") String evaluationCode) {
        TeacherDossiers dossiers;
        try {
            dossiers = cache.getTeacherDossiers(evaluationCode);
        } catch (HibernateException | NullPointerException e) {
            return Respond.respondWithError("Unable to retrieve evaluation reviews from database. Error details: " + e.getMessage());
        }
        return Response.ok(dossiers).build();
    }

    @GET
    @Path("/settings")
    public Response getExperimentSettings() {
        return Response.ok(settings).build();
    }

}