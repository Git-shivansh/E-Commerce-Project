// src/main/java/com/example/demo/config/MailConfig.java

package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {

    // Spring Boot automatically configures JavaMailSender
    // using application.properties values.

    // Add custom mail-related beans here if needed.
}