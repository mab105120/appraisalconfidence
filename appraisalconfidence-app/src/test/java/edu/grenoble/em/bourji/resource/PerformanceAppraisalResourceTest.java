package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.api.JobFunctionReview;
import edu.grenoble.em.bourji.api.TeacherDossier;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Moe on 9/5/2017.
 */
public class PerformanceAppraisalResourceTest {

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE = new DropwizardAppRule<>(AppraisalConfidenceApp.class,
            ResourceHelpers.resourceFilePath("test.yml"));

    @Test
    public void getReviewsByEvaluationCode() {
        Response response = getPerformanceReview("1");
        assertEquals(HttpStatus.OK_200, response.getStatus());
        TeacherDossiers dossiers = response.readEntity(TeacherDossiers.class);
        assertNotNull(dossiers);
        TeacherDossier teacher1 = dossiers.getTeacher1();
        assertNotNull(teacher1);
        JobFunctionReview studentLearningReview = teacher1.getStudentLearning();
        assertNotNull(studentLearningReview.getSupervisor1Review());
        assertEquals("A-SL-SP1", studentLearningReview.getSupervisor1Review());
        assertEquals("A-SL-SP3", studentLearningReview.getSupervisor3Review());
        JobFunctionReview professionalism = teacher1.getProfessionalism();
        assertNotNull(professionalism);
        assertEquals("A-PF-SP2", professionalism.getSupervisor2Review());
        TeacherDossier teacher2 = dossiers.getTeacher2();
        assertNotNull(teacher2);
        assertNotNull(teacher2.getStudentLearning().getSupervisor1Review());
        assertNotNull(teacher2.getInstructionalPractice().getSupervisor2Review());
        assertNotNull(teacher2.getProfessionalism().getSupervisor3Review());
    }

    @Test
    public void getPerformanceEvaluationsForBadId() {
        Response response = getPerformanceReview("INVALID_ID");
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        assertEquals("Unable to retrieve evaluation reviews from database. Error details: Invalid evaluation code: INVALID_ID", response.readEntity(String.class));
    }

    private Response getPerformanceReview(String evaluationId) {
        Client client = ClientBuilder.newClient();
        return client.target(String.format("http://localhost:%d", RULE.getLocalPort()))
                .path("/api/performance-review/" + evaluationId)
                .request()
                .get();
    }
}