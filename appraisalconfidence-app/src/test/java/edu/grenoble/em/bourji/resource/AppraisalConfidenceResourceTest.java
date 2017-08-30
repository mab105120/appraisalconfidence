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
public class AppraisalConfidenceResourceTest {

    private final String URL = "http://localhost:%d";

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<AppraisalConfidenceConfig>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    //@Test
    public void createTable() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/appraisal/create-table")
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

    @Test
    public void getTeacherDossierWithValidId() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/appraisal/evaluations/1")
                .request()
                .get();
        assertEquals(HttpStatus.OK_200, response.getStatus());
        TeacherDossiers teacherDossiers = response.readEntity(TeacherDossiers.class);
        assertNotNull(teacherDossiers);
        TeacherDossier teacher1 = teacherDossiers.getTeacher1();
        assertNotNull(teacher1);
        JobFunctionReview studentLearningSkills = teacher1.getStudentLearning();
        assertNotNull(studentLearningSkills);
        assertEquals(studentLearningSkills.getSupervisor1Review(), "Teacher 1 has proven, over the past four years, capable of enhancing the students' learning experience and promoting participation in the classroom. During the times I attended her classes, the majority of her students were actively participting in the class discussion. I believe Teacher 1's teaching methods are innovative and effective.");
        assertEquals(studentLearningSkills.getSupervisor3Review(), "I think Teacher 1 has succeeded in improving her class's academic performance against difficult conditions. While her class doesn't rank very high on standard exams, we should focus on the relative improvement of the class's performance. This puts teacher 1's class in a higher rank. I believe Teacher 1 still have a lot of room to grow. I have no good reason to believe that Teacher 1 won't continue learning new teaching methods and developing a stronger academic cohort.");
        TeacherDossier teacher2 = teacherDossiers.getTeacher2();
        assertNotNull(teacher2);
        JobFunctionReview professionalism = teacher2.getProfessionalism();
        assertNotNull(professionalism);
        assertEquals(professionalism.getSupervisor2Review(), "Teacher 2 has maintained a good relation with students' parents through close involvement with teacher\\parent conferences and closer communication with the parents. Such behavior is welcome and expected of tenured faculty. I think Teacher 2 should spend more time developing a career plan. During my time as a supervisor I we had only few of meetings for discussing career development. A tenured teacher is one committed to growing a career in education and that is something I expect of Teacher 2.");
    }

    @Test
    public void getTeacherDossierWithInvalidId() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format(URL, RULE.getLocalPort()))
                .path("/api/appraisal/evaluations/100")
                .request()
                .get();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        assertEquals("Invalid evaluation id: 100", response.readEntity(String.class));
    }
}