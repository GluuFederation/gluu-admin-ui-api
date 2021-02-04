package org.gluu.jansadminuiapi.util;

import com.google.common.base.Splitter;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
