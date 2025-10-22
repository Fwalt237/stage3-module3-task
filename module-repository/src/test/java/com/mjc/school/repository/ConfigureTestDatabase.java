package com.mjc.school.repository;


import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@DataJpaTest
@ComponentScan(basePackages = "com.mjc.school.repository")
@EnableJpaRepositories(basePackages = "com.mjc.school.repository")
public class ConfigureTestDatabase {
}
