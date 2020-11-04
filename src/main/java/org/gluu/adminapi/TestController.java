package org.gluu.adminapi;

import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestControllerInterface {

    @Override
    public String ping() {
        System.out.println("Ping controller");

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        // stop plugins
        PluginManager pluginManager = applicationContext.getBean(PluginManager.class);

        pluginManager.stopPlugins();

        return "pong";
    }

}
