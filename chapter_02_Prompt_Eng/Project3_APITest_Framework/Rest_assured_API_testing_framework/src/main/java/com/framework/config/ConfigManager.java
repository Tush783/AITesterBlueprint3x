package com.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Resolution order: environment variable → system property → config.properties.
     * Env variable key is the property key uppercased with dots replaced by underscores,
     * e.g. "base.url" → BASE_URL.
     */
    public String get(String key) {
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public String getAuthToken() {
        return get("auth.token");
    }

    public String getAuthType() {
        return get("auth.type");
    }
}
