package edu.grenoble.em.bourji.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.api.RelativeEvaluationPayload;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;

/**
 * Created by Moe on 9/18/17.
 */
public class RelativeEvaluationResourceTest {

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    @Test
    public void postUserRecommendation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RelativeEvaluationPayload payload = mapper.readValue(fixture("json/evaluation-payload.json"), RelativeEvaluationPayload.class);
        Client client = ClientBuilder.newClient();
        String URL = "http://localhost:%d";
        String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik5URkRPRVF3TlRVNE9UZzFRVE5DTXpZeE9FUkNRVFU1UWpSRVJEbERNRGRCTnpnNE5VWkRRdyJ9.eyJuYW1lIjoibW9oZC5ib3VyamlAZ21haWwuY29tIiwibmlja25hbWUiOiJtb2hkLmJvdXJqaSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8yMmQ2ZTM3ZWE4ZjgxNWNlOTRhYTU0ZGIyZTJjZGQwMT9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRm1vLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDE3LTA5LTI5VDA0OjIzOjE5Ljg5MVoiLCJpc3MiOiJodHRwczovL2FwcHJhaXNhbC1ncmVub2JsZS1ib3VyamkuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU5OTUxN2JlM2M4ZTcyNjRjMDA4YzMyOCIsImF1ZCI6ImtqbFhnNm1ZU040NEQ1WFNoQlNEM2M2UDBlcVdWRDR6IiwiZXhwIjoxNTA2Njk0OTk5LCJpYXQiOjE1MDY2NTg5OTksIm5vbmNlIjoiZFhydGcyTnNUUC5PNnVsb1BPMWpqeFB6UDRpWmFKTTEiLCJhdF9oYXNoIjoiN2syXzZidDJubFhvaC1VRGM5N3Y5USJ9.FZatPwuB0kN_PfiiHL6yKuMF6ihPuQv6vJMQ6B1CAKS1xRN4y5qiKjjhg6UDUzdkMetNo9VL-pLcLEDXQzJ0VmrDEs_K50CqyrFgC608tM5b8DaOjSQlFUbHMjvACR65OcGG9HUjS-RthcYWeD-VV4RKkz3Na1OPnLUkoHsYOxSOFtv9hTc9xrNk9M2rMjJiX2N1gymbAt2ENJWQvgA-bBkbmqXxBKb_-l1CpS5NSaoEpj5ZBn0EgZTJcMHuAN5NesHpNFq9Z1XTrnuX5GaO0heLOyJGzBZk073V385azbA0OxekgywZz9vWxWIrizoDTy9y6fCPp3J2QbNmvyfK6A";
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/appraisal")
                .request()
                .header("Authorization", "Bearer " + access_token)
                .post(Entity.json(payload));
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }
}