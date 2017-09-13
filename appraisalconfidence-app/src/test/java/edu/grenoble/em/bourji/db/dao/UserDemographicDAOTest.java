package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Moe on 9/9/2017.
 */
public class UserDemographicDAOTest {

    private UserDemographicDAO userDemographicDAO;

    @Rule
    public DAOTestRule database = DAOTestRule
            .newBuilder()
            .addEntityClass(UserDemographic.class)
            .build();

    @Before
    public void setUp() {
        userDemographicDAO = new UserDemographicDAO(database.getSessionFactory());
    }

    @Test
    public void addUserDemographic() {
        UserDemographic userDemographic = new UserDemographic(
                "user123", "[20-30]", "male", "masters", "other"
        );
        userDemographicDAO.add(userDemographic);
        UserDemographic queriedUser = userDemographicDAO.getUserDemographics("user123");
        assertNotNull(queriedUser);
        assertEquals("[20-30]", queriedUser.getAge());
        assertEquals("masters", queriedUser.getEducation());
        assertEquals("other", queriedUser.getDivision());
        UserDemographic invalidUser = userDemographicDAO.getUserDemographics("invalid");
        assertNull(invalidUser);
    }
}