package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserExperience;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Moe on 9/11/17.
 */
public class UserExperienceDAOTest {

    private UserExperienceDAO userExperienceDAO;

    @Rule
    public DAOTestRule database = DAOTestRule
            .newBuilder()
            .addEntityClass(UserExperience.class)
            .build();

    @Before
    public void setUp() {
        userExperienceDAO = new UserExperienceDAO(database.getSessionFactory());
    }

    @Test
    public void addUserExperience() {
        UserExperience userExperience = new UserExperience(
                "user123", "associate", "[0-5]", "[0-5]", "[0-5]", "[20-40]",
                "[20-40]", "Yes", "[0-20]"
        );
        userExperienceDAO.add(userExperience);
        UserExperience queriedUser = userExperienceDAO.getUserExperience("user123");
        assertNotNull(queriedUser);
        assertEquals("associate", queriedUser.getTitle());
        assertEquals("[0-20]", queriedUser.getTotalCandidates());
        assertEquals("Yes", queriedUser.getPersonnelSelection());
        UserExperience invalidUser = userExperienceDAO.getUserExperience("invalid");
        assertNull(invalidUser);
    }
}