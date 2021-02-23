package org.gluu.adminui.app.util;

import com.google.common.base.Splitter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class TestUtil {

    public static Map<String, String> getParamsMapFromRedirectUrl(String redirectUrl) {
        List<String> queryParams = Arrays.asList(redirectUrl.toString().split("&"));

        String query = redirectUrl.split("\\?")[1];
        final Map<String, String> paramsMap = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);

        return paramsMap;
    }
}
