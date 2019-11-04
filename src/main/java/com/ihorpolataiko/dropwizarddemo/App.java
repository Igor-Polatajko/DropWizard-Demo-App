package com.ihorpolataiko.dropwizarddemo;

import com.ihorpolataiko.dropwizarddemo.resource.GreetingResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

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
        environment.jersey().register(new GreetingResource());
    }
}
