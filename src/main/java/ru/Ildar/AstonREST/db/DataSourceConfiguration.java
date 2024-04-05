package ru.Ildar.AstonREST.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Класс конфигурации База Данных
 */
public class DataSourceConfiguration {

    private static  final Map<String, Object> dataSourceProperties;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = DataSourceConfiguration.class.getClassLoader()
                .getResourceAsStream("application.yaml");
        Map<String, Object> data = yaml.load(inputStream);
        dataSourceProperties = (Map<String, Object>) data.get("datasource");
    }

    public static Map<String, Object> getDataSourceProperties() {
        return dataSourceProperties;
    }
}
