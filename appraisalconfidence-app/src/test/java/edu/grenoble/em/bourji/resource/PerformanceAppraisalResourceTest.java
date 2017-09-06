package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import edu.grenoble.em.bourji.AppraisalConfidenceTestApp;
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
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE = new DropwizardAppRule<>(AppraisalConfidenceTestApp.class,
            ResourceHelpers.resourceFilePath("test.yml"));

    @Test
    public void getReviewsByEvaluationCode() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format("http://localhost:%d", RULE.getLocalPort()))
                .path("/api/performance-review/1")
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        TeacherDossiers dossiers = response.readEntity(TeacherDossiers.class);
        assertNotNull(dossiers);
        TeacherDossier teacher1 = dossiers.getTeacher1();
        assertNotNull(teacher1);
        JobFunctionReview studentLearningReview = teacher1.getStudentLearning();
//        assertNotNull(studentLearningReview.getSupervisor1Review());
    }
}