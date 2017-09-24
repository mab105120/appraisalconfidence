package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.JwtTokenHelper;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/23/2017.
 */
@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StatusResource.class);
    private StatusDAO statusDAO;
    private JwtTokenHelper tokenHelper;

    public StatusResource(StatusDAO statusDAO, JwtTokenHelper tokenHelper) {
        this.statusDAO = statusDAO;
        this.tokenHelper = tokenHelper;
    }

    @POST
    @Path("/{status}")
    @UnitOfWork
    public Response postStatus(@PathParam("status") String status,
                               @Context HttpHeaders httpHeaders) {
        String access_token = httpHeaders.getHeaderString("Authorization");
        if (access_token == null)
            return Respond.respondWithUnauthorized();
        access_token = access_token.substring(7);
        try {
            String user = tokenHelper.getUserIdFromToken(access_token);
            LOGGER.info(String.format("Setting status of user (%s) to %s", user, status));
            statusDAO.add(new Status(user, status));
        } catch (JwkException e) {
            String errorMessage = "Unable to get user from token: " + e.getMessage();
            LOGGER.error(errorMessage);
            return Respond.respondWithError(errorMessage);
        }
        return Response.ok().build();
    }

    @GET
    @UnitOfWork
    public Response getStatus(@Context HttpHeaders httpHeaders) {
        String access_token = httpHeaders.getHeaderString("Authorization");
        if (access_token == null)
            return Respond.respondWithUnauthorized();
        access_token = access_token.substring(7);
        try {
            String user = tokenHelper.getUserIdFromToken(access_token);
            LOGGER.info(String.format("Getting status for user (%s)", user));
            String status = statusDAO.getCurrentStatus(user);
            return Response.ok(status).build();
        } catch (JwkException | HibernateException e) {
            String errorMessage = String.format("Unable to get user status: %s", e.getMessage());
            LOGGER.error(errorMessage);
            return Respond.respondWithError(errorMessage);
        }
    }
}