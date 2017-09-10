package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Moe on 9/9/2017.
 */
public class QuestionnaireResourceTest {

    private final String URL = "http://localhost:%d";

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    @Test
    @Ignore("Send access token in header")
    public void addUserDemographic() {
        UserDemographic user = new UserDemographic(
                "user123", 30, "male", "masters", "technology"
        );
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/questionnaire/user-demographic")
                .request()
                .post(Entity.json(user));
        assertEquals(HttpStatus.OK_200, response.getStatus());
        // Query user to make sure it was added correctly
        response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/questionnaire/user-demographic/user123")
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        UserDemographic queriedUser = response.readEntity(UserDemographic.class);
        assertNotNull(queriedUser);
        assertEquals("user123", queriedUser.getUser());
        assertEquals(30, queriedUser.getAge());
        assertEquals("masters", queriedUser.getEducation());
    }
}