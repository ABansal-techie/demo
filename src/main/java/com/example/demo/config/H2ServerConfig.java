package com.example.demo.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2ServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        // Start TCP server on port 9092 accessible only locally
        return Server.createTcpServer("-tcp", "-tcpPort", "9092", "-tcpAllowOthers");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        // Start web console on port 8082
        return Server.createWebServer("-web", "-webPort", "8082", "-webAllowOthers");
    }
}
