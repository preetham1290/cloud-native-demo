package com.grep.employeeservice;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@EnableDiscoveryClient
@SpringBootApplication
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}
}

@Component
class DataCLR implements CommandLineRunner {

	@Autowired
	EmpRepo empRepo;

	@Override
	public void run(String... args) throws Exception {
		Stream.of("A", "B").forEach(n -> empRepo.save(new Employee(n)));
		empRepo.findAll().forEach(System.err::println);
	}

}

@RestController
@RefreshScope
class MessageController {

	@Value("${message}")
	private String value;

	@GetMapping("message")
	public String getString() {
		return value;
	}
}

@RepositoryRestResource
interface EmpRepo extends JpaRepository<Employee, String> {

}

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
class Employee {

	public Employee(String name) {
		super();
		Name = name;
	}

	@Id
	@GeneratedValue
	private String id;
	private String Name;
}
