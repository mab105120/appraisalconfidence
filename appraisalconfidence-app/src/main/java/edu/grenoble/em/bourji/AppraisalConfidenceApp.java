package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.resource.AppraisalConfidenceResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by Moe on 8/16/2017.
 */
public class AppraisalConfidenceApp extends Application<AppraisalConfidenceConfig> {

    public static void main(String[] args) throws Exception {
        new AppraisalConfidenceApp().run(args);
    }

    @Override
    public void run(AppraisalConfidenceConfig appraisalConfidenceConfig, Environment environment) throws Exception {
        environment.jersey().register(new AppraisalConfidenceResource());
    }

    @Override
    public void initialize(Bootstrap<AppraisalConfidenceConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }
}