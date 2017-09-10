package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.db.dao.PerformanceReviewDAO;
import edu.grenoble.em.bourji.db.dao.QuestionnaireDAO;
import edu.grenoble.em.bourji.db.dao.UserDemographicDAO;
import edu.grenoble.em.bourji.db.pojo.PerformanceReview;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import edu.grenoble.em.bourji.resource.AppraisalConfidenceResource;
import edu.grenoble.em.bourji.resource.PerformanceReviewResource;
import edu.grenoble.em.bourji.resource.QuestionnaireResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Moe on 8/16/2017.
 */
public class AppraisalConfidenceApp extends Application<AppraisalConfidenceConfig> {

    protected final HibernateBundle<AppraisalConfidenceConfig> hibernate = new HibernateBundle<AppraisalConfidenceConfig>(
            PerformanceReview.class,
            UserDemographic.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(AppraisalConfidenceConfig configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new AppraisalConfidenceApp().run(args);
    }

    @Override
    public void run(AppraisalConfidenceConfig config, Environment environment) throws Exception {

        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/api/*");
        HashMap<String, String> params = new HashMap<>();
        params.put(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        params.put(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        params.put(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameters(params);
        PerformanceReviewDAO dao = new PerformanceReviewDAO(hibernate.getSessionFactory());
        PerformanceReviewCache performanceReviewCache = new UnitOfWorkAwareProxyFactory(hibernate).create(PerformanceReviewCache.class,
                PerformanceReviewDAO.class, dao);
        performanceReviewCache.instantiateCache();

        QuestionnaireDAO questionnaireDAO = new QuestionnaireDAO()
                .withUserDemographicDao(new UserDemographicDAO(hibernate.getSessionFactory()));

        JwtTokenHelper tokenHelper = new JwtTokenHelper(config.getAuth0Domain(), config.getKid());

        // register resources
        environment.jersey().register(new AppraisalConfidenceResource(config.getAuth0Domain(), config.getKid()));
        environment.jersey().register(new PerformanceReviewResource(performanceReviewCache));
        environment.jersey().register(new QuestionnaireResource(questionnaireDAO, tokenHelper));
    }

    @Override
    public void initialize(Bootstrap<AppraisalConfidenceConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(hibernate);
    }
}