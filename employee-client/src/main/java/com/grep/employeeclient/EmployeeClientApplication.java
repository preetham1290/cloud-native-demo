package com.grep.employeeclient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@EnableFeignClients
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class EmployeeClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeClientApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

//@FeignClient("employee-service")
//@Component
//interface EmployeeReader {
//	@GetMapping("employees")
//	Resources<Employee> getNames();
//}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Employee {
	String name;
}

@RestController
@RequestMapping("employees")
class EmployeeClient {
//	@Autowired
//	private EmployeeReader employeeReader;

	@Autowired
	private RestTemplate restTemplate;

	List<Employee> fallBack() {
		return new ArrayList<>();
	}

//	@HystrixCommand(fallbackMethod = "fallBack")
//	@GetMapping("names")
//	List<String> getNames() {
//		return employeeReader.getNames().getContent().stream().map(Employee::getName).collect(Collectors.toList());
//	}

//	@HystrixCommand(fallbackMethod = "fallBack")
	@GetMapping("restnames")
	List<Employee> getNamesFromRest() {
		ResponseEntity < Resources < Employee >> response = restTemplate.exchange("http://employee-service/employees",
			    HttpMethod.GET, null, new ParameterizedTypeReference < Resources < Employee >> () {});
		System.err.println(response.getBody().getContent().size());
		return response.getBody().getContent().stream().collect(Collectors.toList());
	}

}