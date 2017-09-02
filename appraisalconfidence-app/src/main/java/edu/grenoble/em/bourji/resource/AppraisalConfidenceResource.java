package edu.grenoble.em.bourji.resource;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import edu.grenoble.em.bourji.db.AppraisalConfidenceDAO;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 8/16/2017.
 */
@Path("/appraisal")
@Produces(MediaType.APPLICATION_JSON)
public class AppraisalConfidenceResource {

    private final String authDomain;
    private final String kid;
    private final AppraisalConfidenceDAO dao;
    private final Map<String, Pair<String, String>> codeToProfilesMap;

    public AppraisalConfidenceResource(String authDomain, String kid, AppraisalConfidenceDAO dao) {
        this.authDomain = authDomain;
        this.kid = kid;
        this.dao = dao;
        this.codeToProfilesMap = new HashMap<>();
        codeToProfilesMap.put("1", new ImmutablePair<>("A", "B"));
    }

    @GET
    @Path("/test")
    public Response test() throws SQLException {
        dao.createTable();
        return Response.ok("Table is created").build();
    }

    @GET
    @Path("/create-table")
    public Response createTable() throws SQLException {
        dao.createTable();
        return Response.ok().build();
    }

    @GET
    @Path("/evaluations/{id}")
    public Response getEvaluationsById(@PathParam("id") String id) {
        Pair<String, String> codes = codeToProfilesMap.get(id);
        if(codes == null)
            return respondWithError("Invalid evaluation id: " + id);
        String code1 = codes.getLeft();
        String code2 = codes.getRight();
        TeacherDossiers teacherDossiers;
        try {
            teacherDossiers = dao.getTeacherDossiers(code1, code2);
        } catch(SQLException e) {
            return respondWithError("Unable to retrieve teacher profiles from database. Error details: " + e.getMessage());
        }
        return Response.ok(teacherDossiers).build();
    }

    @GET
    @Path("/whoami")
    public Response whoami(@Context HttpHeaders headers) throws JwkException {
        return Response.ok(getUserIdFromToken(headers.getHeaderString("Authorization").substring(7))).build();
    }

    private String getUserIdFromToken(String access_id) throws JwkException {
        JwkProvider provider = new UrlJwkProvider(String.format("https://%s/", authDomain));
        Jwk jwk = provider.get(kid);
        RSAPublicKey pk = (RSAPublicKey) jwk.getPublicKey();
        try {
            Algorithm algorithm = Algorithm.RSA256(pk, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(String.format("https://%s/", authDomain))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(access_id);
            return decodedJWT.getClaim("nickname").asString();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("An error occurred: " + e.getMessage());
        }
    }

    private Response respondWithError(String errorMessage) {
        return Response.status(500)
                .entity(errorMessage)
                .build();
    }

}