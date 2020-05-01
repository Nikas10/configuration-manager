package org.nikas.config.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"org.nikas.config.poc"}
				, excludeFilters = {
				@ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class)})
@EnableSwagger2
@EnableMongoRepositories(basePackages = "org.nikas.config.poc.repository")
@EntityScan(basePackages = "org.nikas.config.poc")
public class PocApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocApplication.class, args);
	}

	@Bean
	public Docket swaggerSettings() {
		return new Docket(DocumentationType.SWAGGER_2)
						.select()
						.apis(RequestHandlerSelectors.basePackage("org.nikas.config.poc"))
						.build()
						.forCodeGeneration(true);
	}


}
