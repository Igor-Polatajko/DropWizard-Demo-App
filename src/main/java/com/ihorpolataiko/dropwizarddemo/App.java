package com.ihorpolataiko.dropwizarddemo;

import com.ihorpolataiko.dropwizarddemo.configuration.db.MongoProperties;
import com.ihorpolataiko.dropwizarddemo.dao.ItemDao;
import com.ihorpolataiko.dropwizarddemo.resource.GreetingResource;
import com.ihorpolataiko.dropwizarddemo.resource.ItemResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class App extends Application<AppConfiguration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "demo-app";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<AppConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                    final AppConfiguration appConfiguration) {
                return appConfiguration.getSwaggerBundleConfiguration();
            }
        });
    }

    public void run(AppConfiguration appConfiguration, Environment environment) throws Exception {
        environment.jersey().register(GreetingResource.class);
        environment.jersey().register(ItemResource.class);
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ItemDao.class).to(ItemDao.class).in(Singleton.class);
                bind(appConfiguration.getMongoProperties()).to(MongoProperties.class).named("mongo-properties");
            }
        });
    }
}
