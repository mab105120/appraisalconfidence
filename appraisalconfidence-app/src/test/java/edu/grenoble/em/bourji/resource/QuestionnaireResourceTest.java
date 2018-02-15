package edu.grenoble.em.bourji.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.db.pojo.UserConfidence;
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
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Moe on 9/9/2017.
 */
@Ignore("Generate a token that doesn't expire")
public class QuestionnaireResourceTest {

    private final String URL = "http://localhost:%d";
    private final String access_token = "";

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    @Test
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

    @Test
    public void addUserConfidence() throws IOException {
        List<UserConfidence> list = new ObjectMapper().readValue(
                fixture("json/user-confidence.json"), new TypeReference<List<UserConfidence>>(){}
        );
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/questionnaire/user-confidence")
                .request()
                .header("Authorization", "Bearer " + access_token)
                .post(Entity.json(list));
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }
}