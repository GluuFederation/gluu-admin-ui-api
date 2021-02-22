package org.gluu.adminui.plugins.hc;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginWrapper;

public class HealthCheckPlugin extends SpringBootPlugin {

    public HealthCheckPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("HealthCheckPlugin.start()");
        super.start();
    }

    @Override
    public void stop() {
        System.out.println("HealthCheckPlugin.stop()");
        super.stop();
    }

    @Override
    protected SpringBootstrap createSpringBootstrap() {
        return new SpringBootstrap(
                this, HealthCheckPluginStarter.class);
    }

}
