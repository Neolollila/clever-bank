package com.devmandrik.util;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Properties;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PropertiesUtil {

    Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    @SneakyThrows
    private void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        }
    }
}
