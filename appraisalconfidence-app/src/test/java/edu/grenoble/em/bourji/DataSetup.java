package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.db.dao.PerformanceReviewDAO;
import edu.grenoble.em.bourji.db.pojo.PerformanceReview;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by Moe on 9/5/2017.
 */
public class DataSetup {

    @UnitOfWork
    public void setUpPerformanceAppraisalData(PerformanceReviewDAO performanceReviewDAO) {
        performanceReviewDAO.add(new PerformanceReview("A", "SL", "SP1", "Review"));
    }

}
