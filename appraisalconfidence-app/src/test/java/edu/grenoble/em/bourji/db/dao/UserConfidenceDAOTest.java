package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserConfidence;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;

/**
 * Created by Moe on 9/12/2017.
 */
public class UserConfidenceDAOTest {

    private UserConfidenceDAO userConfidenceDAO;

    @Rule
    public DAOTestRule database = DAOTestRule
            .newBuilder()
            .addEntityClass(UserConfidence.class)
            .build();

    @Before
    public void setUp() {
        userConfidenceDAO = new UserConfidenceDAO(database.getSessionFactory());
    }

//    @Test
//    public void addUserConfidence() {
//        UserConfidence userConfidence = new UserConfidence(
//                "user123", "SPJ1", -2
//        );
//        userConfidenceDAO.add(userConfidence);
//        UserConfidence queriedUser = userConfidenceDAO.getUserConfidence("user123");
//        assertNotNull(queriedUser);
//        assertEquals("user123", queriedUser.getUser());
//        assertEquals("SPJ1", queriedUser.getItemCode());
//        assertEquals(-2, queriedUser.getResponse());
//        UserConfidence invalidUser = userConfidenceDAO.getUserConfidence("invalid");
//        assertNull(invalidUser);
//    }
}