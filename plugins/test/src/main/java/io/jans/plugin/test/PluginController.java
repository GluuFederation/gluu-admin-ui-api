package io.jans.plugin.test;

import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Extension(ordinal = 1)
@RestController
public class PluginController implements ExtensionPoint {

    @GetMapping("/plugin")
    public Object plugin() {
        return String.format("%s : %d", "Test plugin working 3333333333 ... : ", System.currentTimeMillis());
    }

}
