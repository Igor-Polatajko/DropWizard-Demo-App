package com.ihorpolataiko.dropwizarddemo;

import com.ihorpolataiko.dropwizarddemo.resource.GreetingResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class App extends Application<AppConfiguration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "demo-app";
    }

    public void run(AppConfiguration appConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new GreetingResource());
    }
}
