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
 * Created by Moe on 8/22/17.
 */
public class PerformanceReviewResourcetest {

    private final String URL = "http://localhost:%d";

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));


    @Test
    public void getTeacherDossierWithValidId() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/performance-review/1")
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        TeacherDossiers teacherDossiers = response.readEntity(TeacherDossiers.class);
        assertNotNull(teacherDossiers);
        TeacherDossier teacher1 = teacherDossiers.getTeacher1();
        assertNotNull(teacher1);
        JobFunctionReview studentLearningSkills = teacher1.getStudentLearning();
        assertNotNull(studentLearningSkills);
        assertEquals("A-SL-SP1", studentLearningSkills.getSupervisor1Review());
        assertEquals("A-SL-SP3", studentLearningSkills.getSupervisor3Review());
        TeacherDossier teacher2 = teacherDossiers.getTeacher2();
        assertNotNull(teacher2);
        JobFunctionReview professionalism = teacher2.getProfessionalism();
        assertNotNull(professionalism);
        assertEquals("B-PF-SP2", professionalism.getSupervisor2Review());
    }

    @Test
    public void getTeacherDossierWithInvalidId() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/performance-review/100")
                .request()
                .get();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        assertEquals("Unable to retrieve evaluation reviews from database. Error details: Invalid evaluation code: 100", response.readEntity(String.class));
    }
}