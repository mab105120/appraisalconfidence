package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserConfidence;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Moe on 9/12/2017.
 */
public class UserConfidenceDAO extends AbstractDAO<UserConfidence> {
    public UserConfidenceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserConfidence userConfidence) {
        persist(userConfidence);
    }

    public void addAll(List<UserConfidence> userConfidenceList) {
        userConfidenceList.stream().forEach(this::add);
    }

    public UserConfidence getUserConfidence(String user) {
        return get(user);
    }
}
