package com.springboot.blog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
				info = @Info(
								title = "Spring Boot Blog App REST APIs",
								description = "Spring Boot Blog App REST APIs Documentation",
								version = "v1.0",
								contact = @Contact(
												name = "Hazel",
												email = "hazel@gmail.com",
												url = "https://www.localhost/8080/api"
								),
								license = @License(
												name = "Apache 2.0",
												url = "https://www.apache.org/licenses/LICENSE-2.0.html"
								)
				),
				externalDocs = @ExternalDocumentation(
								description = "Spring Boot Blog App Documentation",
								url = "github link"
				)
)
public class SpringbootBlogRestApiApplication /* implements CommandLineRunner */ {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

//	@Autowired
//	private RoleRepository roleRepository;
//
//	// insert metadata to the database automatically when the application runs
//	@Override
//	public void run(String... args) throws Exception {
//
//		Role adminRole = new Role();
//		adminRole.setName("ROLE_ADMIN");
//		roleRepository.save(adminRole);
//
//		Role userRole = new Role();
//		userRole.setName("ROLE_USER");
//		roleRepository.save(userRole);
//	}
}
