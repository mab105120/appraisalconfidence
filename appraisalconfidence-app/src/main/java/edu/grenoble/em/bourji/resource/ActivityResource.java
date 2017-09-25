package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.db.dao.ActivityDAO;
import edu.grenoble.em.bourji.db.pojo.Activity;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.slf4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/24/17.
 */
@Path("/activity")
public class ActivityResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ActivityResource.class);
    private final JwtTokenHelper tokenHelper;
    private final ActivityDAO dao;

    public ActivityResource(JwtTokenHelper tokenHelper, ActivityDAO dao) {
        this.tokenHelper = tokenHelper;
        this.dao = dao;
    }

    @POST
    @Path("/login")
    @UnitOfWork
    public Response recordLogin(@Context HttpHeaders httpHeaders) {
        String access_token = httpHeaders.getHeaderString("Authorization");
        if(access_token ==null)
            return Respond.respondWithUnauthorized();

        access_token = access_token.substring(7);

        try {
            String userId = tokenHelper.getUserIdFromToken(access_token);
            LOGGER.info(String.format("Recording user %s login", userId));
            dao.add(new Activity(userId, "IN"));
            return Response.ok().build();
        } catch (JwkException | HibernateException e) {
            e.printStackTrace();
            return Respond.respondWithError("Unable to record login. Error: " + e.getMessage());
        }
    }

    @POST
    @Path("/logout")
    @UnitOfWork
    public Response recordLogout(@Context HttpHeaders httpHeaders) {
        String access_token = httpHeaders.getHeaderString("Authorization");
        if(access_token == null)
            return Respond.respondWithUnauthorized();

        access_token = access_token.substring(7);
        try {
            String userId = tokenHelper.getUserIdFromToken(access_token);
            LOGGER.info(String.format("Recording user %s logout", userId));
            dao.add(new Activity(userId, "OUT"));
            return Response.ok().build();
        } catch (JwkException | HibernateException e) {
            String errorMessage = String.format("Unable to record logout status. Reason: %s", e.getMessage());
            LOGGER.error(errorMessage);
            return Respond.respondWithError(errorMessage);
        }
    }
}