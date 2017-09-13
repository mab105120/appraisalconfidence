package edu.grenoble.em.bourji.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import edu.grenoble.em.bourji.db.pojo.UserExperience;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Moe on 9/9/2017.
 */
public class QuestionnaireResourceTest {

    private final String URL = "http://localhost:%d";
    private final String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik5URkRPRVF3TlRVNE9UZzFRVE5DTXpZeE9FUkNRVFU1UWpSRVJEbERNRGRCTnpnNE5VWkRRdyJ9.eyJuYW1lIjoibW9oZC5ib3VyamlAZ21haWwuY29tIiwibmlja25hbWUiOiJtb2hkLmJvdXJqaSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8yMmQ2ZTM3ZWE4ZjgxNWNlOTRhYTU0ZGIyZTJjZGQwMT9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRm1vLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDE3LTA5LTEyVDA0OjQ5OjU3LjM1OFoiLCJpc3MiOiJodHRwczovL2FwcHJhaXNhbC1ncmVub2JsZS1ib3VyamkuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU5OTUxN2JlM2M4ZTcyNjRjMDA4YzMyOCIsImF1ZCI6ImtqbFhnNm1ZU040NEQ1WFNoQlNEM2M2UDBlcVdWRDR6IiwiZXhwIjoxNTA1MjI3Nzk3LCJpYXQiOjE1MDUxOTE3OTcsIm5vbmNlIjoiRHBjZ3l5RHVJUkdoUEU1UnlnUURtSVZ3OUVVaHo1SGQiLCJhdF9oYXNoIjoiXzVJRDFiemt0X1FWMUgzQnpTdXEyUSJ9.QOX2XdodtHcjNJEJoHlQCxdm52gqM3A92uQeVs18PlfcoOwgyv56gRj-KnsvXF7OdcANCEwT4jzptas8eQADFaj_w6qxI9kLfe68gJkKBPHSANI610ek-yohfMmwPTqQM5gnjB7aYJrGmZ7jy0TMlfpCkvchAWucj3G_iwfU2fGLC2b72OiePl9i4Et3k2PvR4uF9s9C_O4_7gmP9Y-bG-_MF-MU6vjn_mxMa86zDpyudPcAIVP8aUKSX2d0Ho2zwOeP2-lkY0kVOH7VFXvhx_ZL1zFllN8OJw1onHXk4LNcjcUcXtElDB5R4jCizMFUjNG0llghe8M8hD97sSDWkg";

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    @Test
    @Ignore("Generate a token that doesn't expire")
    public void addUserDemographic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UserDemographic user = mapper.readValue(fixture("json/user-demographic.json"), UserDemographic.class);
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/questionnaire/user-demographic")
                .request()
                .header("Authorization", "Bearer " + access_token)
                .post(Entity.json(user));
        assertEquals(HttpStatus.OK_200, response.getStatus());
        // Query user to make sure it was added correctly
        response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/questionnaire/user-demographic/mohd.bourji")
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        UserDemographic queriedUser = response.readEntity(UserDemographic.class);
        assertNotNull(queriedUser);
        assertEquals("mohd.bourji", queriedUser.getUser());
        assertEquals("[20-30]", queriedUser.getAge());
        assertEquals("masters", queriedUser.getEducation());
    }

    @Test
    public void addUserExperience() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UserExperience user = mapper.readValue(fixture("json/user-experience.json"), UserExperience.class);
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/questionnaire/user-experience")
                .request()
                .header("Authorization", "Bearer " + access_token)
                .post(Entity.json(user));
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }
}