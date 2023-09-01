package com.devmandrik.util;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.io.IOException;
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

    @SneakyThrows(IOException.class)
    private void loadProperties() {
        var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.yml");
        PROPERTIES.load(inputStream);
    }
}
