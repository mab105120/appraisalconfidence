package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.db.dao.QuestionnaireDAO;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/9/2017.
 */
@Path("/questionnaire")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuestionnaireResource {

    private final QuestionnaireDAO dao;
    private final JwtTokenHelper tokenHelper;

    public QuestionnaireResource(QuestionnaireDAO dao, JwtTokenHelper tokenHelper) {
        this.dao = dao;
        this.tokenHelper = tokenHelper;
    }

    @POST
    @Path("/user-demographic")
    @UnitOfWork
    public Response addUserDemographic(UserDemographic userDemographic,
                                       @Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Response
                    .status(403)
                    .entity("You are not authorized to perform this operation!")
                    .build();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            userDemographic.setUser(userId);
            dao.getUserDemographicDAO().add(userDemographic);
        } catch (HibernateException | JwkException e) {
            return respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/user-demographic/{userId}")
    @UnitOfWork
    public Response getUserDemographic(@PathParam("userId") String userId) {
        UserDemographic userDemographic;
        try {
            userDemographic = dao.getUserDemographicDAO().getUserDemographics(userId);
        } catch (HibernateException e) {
            return respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok(userDemographic).build();
    }

    private Response respondWithError(String errorMessage) {
        return Response
                .status(500)
                .entity(errorMessage)
                .build();
    }
}