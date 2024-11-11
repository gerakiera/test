package com.example.demo;

import com.example.demo.component.RsaKeyProperties;
import com.example.demo.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class LibraryApplication {

    public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}
}
