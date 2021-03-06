package org.gluu.adminui.api.plugins;

import org.pf4j.ExtensionPoint;

public interface IAdminUIPlugin extends ExtensionPoint {

    String icon();

    String title();

    String key();

    String path();
}
