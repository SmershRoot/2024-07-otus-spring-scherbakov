package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Серверная часть: %n%s%n", "http://localhost:8080/library-api/swagger-ui/index.html");
		System.out.printf("Actuator: %n%s%n", "http://localhost:8080/library-api/actuator");
		System.out.printf("Prometheus: %n%s%n", "http://localhost:8080/library-api/actuator/prometheus");
		System.out.printf("HAL explorer: %n%s%n", "http://localhost:8080/library-api/explorer");
		System.out.printf("Data REST: %n%s%n", "http://localhost:8080/library-api/data-rest/book ({entity-name} book for example)");
	}

}
