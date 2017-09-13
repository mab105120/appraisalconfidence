package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by Moe on 9/9/2017.
 */
public class UserDemographicDAO extends AbstractDAO<UserDemographic> {

    public UserDemographicDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserDemographic user) {
        persist(user);
    }

    public UserDemographic getUserDemographics(String userId) {
        return get(userId);
    }
}
