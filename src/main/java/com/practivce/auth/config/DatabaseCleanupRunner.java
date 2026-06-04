package com.practivce.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanupRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Drop verification_code column from users table
            jdbcTemplate.execute("ALTER TABLE users DROP COLUMN verification_code");
            System.out.println("--- DatabaseCleanupRunner: Successfully dropped column 'verification_code' from 'users' table ---");
        } catch (Exception e) {
            // Gracefully catch if column doesn't exist, which happens in subsequent restarts
            System.out.println("--- DatabaseCleanupRunner: Column 'verification_code' already dropped or could not be dropped: " + e.getMessage() + " ---");
        }
    }
}
