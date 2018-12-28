package com.dangorman.infinitelocks;

import com.dangorman.infinitelocks.db.DatabaseModule;
import com.dangorman.infinitelocks.health.TemplateHealthcheck;
import com.dangorman.infinitelocks.resources.DevelopmentResource;
import com.dangorman.infinitelocks.resources.HelloWorldResource;
import com.dangorman.infinitelocks.resources.LockResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class InfiniteLocksApplication extends Application<InfiniteLocksConfiguration> {

    public static void main(final String[] args) throws Exception {
        new InfiniteLocksApplication().run(args);
    }

    @Override
    public String getName() {
        return "InfiniteLocks";
    }

    @Override
    public void initialize(final Bootstrap<InfiniteLocksConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final InfiniteLocksConfiguration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDatabaseUrl()
        );

        DatabaseModule.setDbUrl(configuration.getDatabaseUrl());
        final TemplateHealthcheck healthCheck =
                new TemplateHealthcheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(new DevelopmentResource());
        environment.jersey().register(new LockResource());
    }
}


