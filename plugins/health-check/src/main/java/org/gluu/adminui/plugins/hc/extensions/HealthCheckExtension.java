package org.gluu.adminui.plugins.hc.extensions;

import lombok.Data;
import org.gluu.adminui.api.plugins.IAdminUIPlugin;
import org.gluu.adminui.plugins.hc.HealthCheckPlugin;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

@Data
@Extension
public class HealthCheckExtension implements IAdminUIPlugin {

    private String title;
    private String key;
    private String path;

    public HealthCheckExtension() {
        PluginWrapper wrapper = HealthCheckPlugin.INSTANCE.getWrapper();
        this.title = "Health Check";
        this.key = wrapper.getPluginId();
        this.path = "/health-check";
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
