package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.db.AppraisalConfidenceDAO;
import edu.grenoble.em.bourji.resource.AppraisalConfidenceResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * Created by Moe on 8/16/2017.
 */
public class AppraisalConfidenceApp extends Application<AppraisalConfidenceConfig> {

    public static void main(String[] args) throws Exception {
        new AppraisalConfidenceApp().run(args);
    }

    @Override
    public void run(AppraisalConfidenceConfig appraisalConfidenceConfig, Environment environment) throws Exception {
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