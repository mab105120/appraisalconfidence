package edu.grenoble.em.bourji.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 8/16/2017.
 */
@Path("/appraisal")
public class AppraisalConfidenceResource {

    @GET
    @Path("/test")
    public Response test() {
        return Response.ok("Application is up!").build();
    }
}