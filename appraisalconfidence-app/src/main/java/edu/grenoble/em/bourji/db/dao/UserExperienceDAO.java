package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserExperience;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by Moe on 9/11/17.
 */
public class UserExperienceDAO extends AbstractDAO<UserExperience> {

    public UserExperienceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserExperience user) {
        persist(user);
    }

    public UserExperience getUserExperience(String userId) {
        return get(userId);
    }

}
