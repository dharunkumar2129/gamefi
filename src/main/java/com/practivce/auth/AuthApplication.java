package com.practivce.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan(basePackages = "com.practivce.auth.entity")
@EnableJpaRepositories(basePackages = "com.practivce.auth.repository")
@EnableAsync
//@EnableScheduling
public class AuthApplication {

    public static void main(String[] args) {
    	
    	System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(AuthApplication.class, args);
    }
}
