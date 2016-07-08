package com.hong;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author huanggh
 * @since 2016年7月7日
 *
 */
public class PropertiesUtil {
    static Properties prop = new Properties();

    static {
	InputStream in = Object.class.getResourceAsStream("/app.properties");
	try {
	    prop.load(in);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public static String get(String key) {
	return StringUtils.trim(prop.getProperty(key));
    }
}