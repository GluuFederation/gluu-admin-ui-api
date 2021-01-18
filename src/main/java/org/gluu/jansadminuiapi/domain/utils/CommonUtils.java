package org.gluu.jansadminuiapi.domain.utils;

import com.google.common.base.Joiner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommonUtils {

    public static String joinAndUrlEncode(Collection<String> list) throws UnsupportedEncodingException {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return encode(Joiner.on("+").join(list));
    }

    public static String encode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }

    public static String decode(String str) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, "UTF-8");
    }
}
