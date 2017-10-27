package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Moe on 9/23/2017.
 */
public class StatusDAO extends AbstractDAO<Status> {

    public StatusDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public String getCurrentStatus(String user) {
        Criteria cr = currentSession().createCriteria(Status.class)
                .add(Restrictions.eq("user", user))
                .addOrder(Order.desc("time"));
        Status status = list(cr).get(0);
        return status.getStatus();
    }

    public void add(Status s) {
        persist(s);
    }

    public boolean stepCompleted(String user, String status) {
        Criteria cr = currentSession().createCriteria(Status.class)
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("status", status));
        return !list(cr).isEmpty();
    }
}
