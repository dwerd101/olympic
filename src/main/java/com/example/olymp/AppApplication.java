package com.example.olymp;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileInputStream;
import java.util.Properties;

@SpringBootApplication
@ComponentScan("com.example")
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	@SneakyThrows
	public Properties myPropertyBean() {
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		Properties properties = new Properties();
		properties.load(fis);
		return properties;


	}
}
