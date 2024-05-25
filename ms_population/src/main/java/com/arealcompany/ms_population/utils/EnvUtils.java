package com.arealcompany.ms_population.utils;

public class EnvUtils {

    public static String getEnvField(String ENV_KEY_NAME) {
        String value = System.getenv(ENV_KEY_NAME);
        if (value == null) {
            throw new RuntimeException(ENV_KEY_NAME + " environment variable not set.");
        }
        return value;
    }
}
