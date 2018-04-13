package edu.grenoble.em.bourji;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.grenoble.em.bourji.db.dao.*;
import edu.grenoble.em.bourji.db.pojo.*;
import edu.grenoble.em.bourji.resource.*;
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
            PerformanceReview.class, UserDemographic.class, UserExperience.class,
            UserConfidence.class, RelativeEvaluation.class, Status.class, Activity.class,
            EvaluationActivity.class, AbsoluteEvaluation.class) {
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

        environment.getObjectMapper().registerModule(new JavaTimeModule());
        environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        PerformanceReviewDAO dao = new PerformanceReviewDAO(hibernate.getSessionFactory());
        PerformanceReviewCache performanceReviewCache = new UnitOfWorkAwareProxyFactory(hibernate).create(PerformanceReviewCache.class,
                new Class[]{PerformanceReviewDAO.class, ExperimentMode.class}, new Object[]{dao,
                        ExperimentMode.valueOf(config.getSettings().getMode().toUpperCase())});
        performanceReviewCache.instantiateCache();

        QuestionnaireDAO questionnaireDAO = new QuestionnaireDAO()
                .withUserDemographicDao(new UserDemographicDAO(hibernate.getSessionFactory()))
                .withUserExperienceDao(new UserExperienceDAO(hibernate.getSessionFactory()))
                .withUserConfidenceDao(new UserConfidenceDAO(hibernate.getSessionFactory()));

        JwtTokenHelper tokenHelper = new JwtTokenHelper(config.getAuth0Domain(), config.getKid());
        environment.jersey().register(new AuthFilter(tokenHelper));
        StatusDAO statusDAO = new StatusDAO(hibernate.getSessionFactory());
        ActivityDAO activityDAO = new ActivityDAO(hibernate.getSessionFactory());
        AppraisalConfidenceDAO confidenceDAO = new AppraisalConfidenceDAO(hibernate.getSessionFactory());
        EvaluationActivityDAO evaluationActivityDAO = new EvaluationActivityDAO(hibernate.getSessionFactory());
        AbsoluteEvaluationDao absEvalDao = new AbsoluteEvaluationDao(hibernate.getSessionFactory());
        // register resources
        environment.jersey().register(new PerformanceReviewResource(performanceReviewCache, config.getSettings()));
        environment.jersey().register(new RelativeEvaluationResource(confidenceDAO, evaluationActivityDAO, statusDAO, performanceReviewCache));
        environment.jersey().register(new QuestionnaireResource(questionnaireDAO, statusDAO));
        environment.jersey().register(new StatusResource(new StatusDAO(hibernate.getSessionFactory()), config.getSettings()));
        environment.jersey().register(new ActivityResource(activityDAO));
        environment.jersey().register(new CommunicationResource(config.getEmailConfiguration().getUsername(), config.getEmailConfiguration().getPassword()));
        environment.jersey().register(new ValidationResource(confidenceDAO, questionnaireDAO));
        environment.jersey().register(new AbsoluteEvaluationResource(absEvalDao, evaluationActivityDAO, statusDAO));
    }

    @Override
    public void initialize(Bootstrap<AppraisalConfidenceConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(hibernate);
    }
}