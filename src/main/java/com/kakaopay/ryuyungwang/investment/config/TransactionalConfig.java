package com.kakaopay.ryuyungwang.investment.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TransactionalConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        // TODO yml 파일 바라보게 하기
        hikariDataSource.setJdbcUrl("jdbc:mysql://mysql:3306/investment?characterEncoding=UTF-8&serverTimezone=UTC&useUnicode=true");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("root");
        hikariDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return hikariDataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
