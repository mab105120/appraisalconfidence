package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.testing.junit.DAOTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by Moe on 9/23/2017.
 */
public class StatusDAOTest {

    private StatusDAO statusDAO;

    @Rule
    public DAOTestRule database = DAOTestRule
            .newBuilder()
            .addEntityClass(Status.class)
            .build();

    @Before
    public void setUp() {
        statusDAO = new StatusDAO(database.getSessionFactory());
        statusDAO.add(new Status("mohd.bourji", "USER_EXPERIENCE"));
    }

    @Test
    public void addUserConfidence() {
        String s = statusDAO.getCurrentStatus("mohd.bourji");
        System.out.println(s);
    }
}