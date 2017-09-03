package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.db.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.resource.AppraisalConfidenceResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Moe on 8/16/2017.
 */
public class AppraisalConfidenceApp extends Application<AppraisalConfidenceConfig> {

    public static void main(String[] args) throws Exception {
        new AppraisalConfidenceApp().run(args);
    }

    @Override
    public void run(AppraisalConfidenceConfig appraisalConfidenceConfig, Environment environment) throws Exception {
        // CORS configuration
        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/api/*");
        HashMap<String, String> params = new HashMap<>();
        params.put(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        params.put(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        params.put(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameters(params);

        DBIFactory dbiFactory = new DBIFactory();
        DBI dbi = dbiFactory.build(environment, appraisalConfidenceConfig.getDataSourceFactory(), "gemappcondb");
        AppraisalConfidenceDAO appDAO = new AppraisalConfidenceDAO(dbi);

        environment.jersey().register(new AppraisalConfidenceResource(appraisalConfidenceConfig.getAuth0Domain(),
                appraisalConfidenceConfig.getKid(), appDAO));
    }

    @Override
    public void initialize(Bootstrap<AppraisalConfidenceConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }
}