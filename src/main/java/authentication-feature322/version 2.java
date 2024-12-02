```java
// LoginAPI - Spring Boot application

// Application.java
package com.example.loginapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User.java
package com.example.loginapi.model;

public class User {
    private String username;
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username; // Security: Ensure this isn't vulnerable to injection attacks
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Security: Ensure this isn't logged or exposed directly
    }
}

// UserController.java
package com.example.loginapi.controller;

import com.example.loginapi.model.User;
import com.example.loginapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        if (userService.validateUser(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials"); // Security: Avoid generic error messages
        }
    }
}

// UserService.java
package com.example.loginapi.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    // This method should validate the user credentials against a data source (e.g., database)
    public boolean validateUser(String username, String password) {
        // For simplicity, a hardcoded user for demonstration. Security: Replace with data source validation
        return "user".equals(username) && "password".equals(password);
    }
}

// SecurityConfig.java
package com.example.loginapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Security: CSRF protection should be enabled for state-changing operations
            .authorizeRequests()
            .antMatchers("/api/login").permitAll() // Security: Specify which endpoints are public
            .anyRequest().authenticated();
    }
}
```