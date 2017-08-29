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
import edu.grenoble.em.bourji.db.AppraisalConfidenceDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;

/**
 * Created by Moe on 8/16/2017.
 */
@Path("/appraisal")
public class AppraisalConfidenceResource {

    private final String authDomain;
    private final String kid;
    private final AppraisalConfidenceDAO dao;

    public AppraisalConfidenceResource(String authDomain, String kid, AppraisalConfidenceDAO dao) {
        this.authDomain = authDomain;
        this.kid = kid;
        this.dao = dao;
    }

    @GET
    @Path("/test")
    public Response test() throws SQLException {
        dao.createTable();
        return Response.ok("Table is created").build();
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

}