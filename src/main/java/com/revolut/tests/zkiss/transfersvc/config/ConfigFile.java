package com.revolut.tests.zkiss.transfersvc.config;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@UtilityClass
public class ConfigFile {

    public static final String CONFIG_FILE = "config.properties";

    private static final Properties PROPERTIES = getProperties();

    public synchronized Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream is = ClassLoader.getSystemResourceAsStream(CONFIG_FILE)) {
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    public static final String get(String key) {
        Objects.requireNonNull(key);
        return PROPERTIES.getProperty(key);
    }

    public static final String get(String prefix, String key) {
        Objects.requireNonNull(prefix);
        Objects.requireNonNull(key);
        return get(prefix + "." + key);
    }
}