package ru.Ildar.AstonRest.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class DataSourceConfigurationTest {
    private static final Map<String, Object> props;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = ru.Ildar.AstonREST.db.DataSourceConfiguration.class.getClassLoader()
                .getResourceAsStream("application-test.yaml");
        Map<String, Object> data = yaml.load(inputStream);
        props = (Map<String, Object>) data.get("datasource");

    }

    public static Map<String, Object> getProps() {
        return props;
    }

}
