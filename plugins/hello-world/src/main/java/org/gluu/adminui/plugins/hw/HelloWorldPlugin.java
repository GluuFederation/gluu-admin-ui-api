package org.gluu.adminui.plugins.hw;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginWrapper;

public class HelloWorldPlugin extends SpringBootPlugin {

    public static HelloWorldPlugin INSTANCE;

    public HelloWorldPlugin(PluginWrapper wrapper) {
        super(wrapper);
        INSTANCE = this;
    }

    @Override
    public void start() {
        System.out.println("HelloWorldPlugin.start()");
        super.start();
    }

    @Override
    public void stop() {
        System.out.println("HelloWorldPlugin.stop()");
        super.stop();
    }

    @Override
    protected SpringBootstrap createSpringBootstrap() {
        return new SpringBootstrap(
                this, HelloWorldPluginStarter.class);
    }

}
