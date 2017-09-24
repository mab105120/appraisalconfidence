package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.db.dao.QuestionnaireDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.UserConfidence;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import edu.grenoble.em.bourji.db.pojo.UserExperience;
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
 * Created by Moe on 9/9/2017.
 */
@Path("/questionnaire")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuestionnaireResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QuestionnaireResource.class);
    private final QuestionnaireDAO dao;
    private final StatusDAO statusDAO;
    private final JwtTokenHelper tokenHelper;

    public QuestionnaireResource(QuestionnaireDAO dao, JwtTokenHelper tokenHelper, StatusDAO statusDAO) {
        this.dao = dao;
        this.tokenHelper = tokenHelper;
        this.statusDAO = statusDAO;
    }

    @POST
    @Path("/user-demographic")
    @UnitOfWork
    public Response addUserDemographic(UserDemographic userDemographic,
                                       @Context HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            LOGGER.info("User id: " + userId);
            userDemographic.setUser(userId);
            dao.getUserDemographicDAO().add(userDemographic);
            statusDAO.add(new Status(userId, "USER_DEMOGRAPHIC"));
        } catch (HibernateException | JwkException e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/user-experience")
    @UnitOfWork
    public Response addUserExperience(UserExperience userExperience,
                                      @Context HttpHeaders httpHeaders) {

        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            LOGGER.info("User id: " + userId);
            userExperience.setUser(userId);
            dao.getUserExperienceDAO().add(userExperience);
            statusDAO.add(new Status(userId, "USER_EXPERIENCE"));
        } catch (HibernateException | JwkException e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/user-confidence")
    @UnitOfWork
    public Response addUserConfidence(List<UserConfidence> userConfidenceResponse,
                                      @Context HttpHeaders httpHeaders) {

        String authorizationHeader = httpHeaders.getHeaderString("Authorization");
        if (authorizationHeader == null)
            return Respond.respondWithUnauthorized();

        String accessToken = authorizationHeader.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(accessToken);
            LOGGER.info("User id: " + userId);
            userConfidenceResponse.stream().forEach(res -> res.setUser(userId));
            dao.getUserConfidenceDAO().addAll(userConfidenceResponse);
            statusDAO.add(new Status(userId, "USER_CONFIDENCE"));
        } catch (HibernateException | JwkException e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
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
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok(userDemographic).build();
    }

}