package com.example.microserviciosdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.microserviciosdemo.repository")
public class DatabaseConfig {
}
