package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.db.dao.PerformanceReviewDAO;
import edu.grenoble.em.bourji.db.pojo.PerformanceReview;
import edu.grenoble.em.bourji.resource.AppraisalConfidenceResource;
import edu.grenoble.em.bourji.resource.PerformanceReviewResource;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Moe on 9/5/2017.
 */
public class AppraisalConfidenceTestApp extends AppraisalConfidenceApp {

    @Override
    public void run(AppraisalConfidenceConfig appraisalConfidenceConfig, Environment environment) throws Exception {

        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/api/*");
        HashMap<String, String> params = new HashMap<>();
        params.put(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        params.put(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        params.put(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameters(params);


        PerformanceReviewDAO performanceReviewDAO = new PerformanceReviewDAO(hibernate.getSessionFactory());


        setUpPerformanceAppraisalData(hibernate.getSessionFactory());
        environment.jersey().register(new AppraisalConfidenceResource(appraisalConfidenceConfig.getAuth0Domain(), appraisalConfidenceConfig.getKid()));
        environment.jersey().register(new PerformanceReviewResource(performanceReviewDAO));
    }


    private void setUpPerformanceAppraisalData(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new PerformanceReview("A", "SL","SP1","Test"));
        session.getTransaction().commit();
    }
}