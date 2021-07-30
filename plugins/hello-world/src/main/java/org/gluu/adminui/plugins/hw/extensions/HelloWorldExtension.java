package org.gluu.adminui.plugins.hw.extensions;

import lombok.Data;
import org.gluu.adminui.api.plugins.IAdminUIPlugin;
import org.gluu.adminui.plugins.hw.HelloWorldPlugin;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

@Data
@Extension
public class HelloWorldExtension implements IAdminUIPlugin {

    private String title;
    private String key;
    private String path;

    public HelloWorldExtension() {
        PluginWrapper wrapper = HelloWorldPlugin.INSTANCE.getWrapper();
        this.title = "Hello World";
        this.key = wrapper.getPluginId();
        this.path = "/hello-world";
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String path() {
        return this.path;
    }
}
