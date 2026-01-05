package com.bajaj.tradingsdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Trading SDK API",
        version = "1.0.0",
        description = "Wrapper SDK for Trading APIs - Bajaj Broking Campus Hiring Assignment",
        contact = @Contact(name = "Developer", email = "developer@example.com")
    )
)
public class TradingSdkApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingSdkApplication.class, args);
    }
}
