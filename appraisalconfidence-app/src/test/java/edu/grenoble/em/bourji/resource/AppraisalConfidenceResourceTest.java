package edu.grenoble.em.bourji.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.db.pojo.TeacherRecommendation;
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
public class AppraisalConfidenceResourceTest {

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    @Test
    public void postUserRecommendation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TeacherRecommendation user = mapper.readValue(fixture("json/user-evaluation.json"), TeacherRecommendation.class);
        Client client = ClientBuilder.newClient();
        String URL = "http://localhost:%d";
        String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik5URkRPRVF3TlRVNE9UZzFRVE5DTXpZeE9FUkNRVFU1UWpSRVJEbERNRGRCTnpnNE5VWkRRdyJ9.eyJuYW1lIjoibW9oZC5ib3VyamlAZ21haWwuY29tIiwibmlja25hbWUiOiJtb2hkLmJvdXJqaSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8yMmQ2ZTM3ZWE4ZjgxNWNlOTRhYTU0ZGIyZTJjZGQwMT9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRm1vLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDE3LTA5LTE5VDAxOjUwOjQ0Ljk0M1oiLCJpc3MiOiJodHRwczovL2FwcHJhaXNhbC1ncmVub2JsZS1ib3VyamkuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU5OTUxN2JlM2M4ZTcyNjRjMDA4YzMyOCIsImF1ZCI6ImtqbFhnNm1ZU040NEQ1WFNoQlNEM2M2UDBlcVdWRDR6IiwiZXhwIjoxNTA1ODIxODQ0LCJpYXQiOjE1MDU3ODU4NDQsIm5vbmNlIjoid2JOcS42RkkwN0pNYWJSaGJsaHM0Um5TRXpGNHphVWkiLCJhdF9oYXNoIjoiYXB2Y3p0SUZTN3NWcXdKdTRnRk02USJ9.Izm3rG0ajQfjBBAXJvvCY-AMvabsagv19GiGjkMYtdlhksFYnnbQbO1TNQA3IDnw5DgrG8N7RlFkPHpbOebjpfxOi9K37ikRBW_fe4B_vdW2DC3YyB2YkwRw3Fu8VnciOiWsT0TUKSPWLGm5WeaTF6VdOvEEqTGklHo4MMex9mUw1AvRsI1RpUx2Y2TRPR-7Cn0Ev3Vb8zRfGeIxZlpPaExNgI4w6hxsJETJtdpWG0v9R47zZ0R8jdJlMXi9bJaEZdw4VEFvit99TEaSUcJi61ErHVZ6nTk7MOZb37Sc48K888pr_5Gnh86-8MS_U8Nvh8h_lKSm2_ALmYkYGLnoTA";
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/appraisal")
                .request()
                .header("Authorization", "Bearer " + access_token)
                .post(Entity.json(user));
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }
}