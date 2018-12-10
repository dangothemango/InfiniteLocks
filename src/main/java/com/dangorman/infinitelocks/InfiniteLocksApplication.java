package com.dangorman.infinitelocks;

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
        // TODO: implement application
    }

}
