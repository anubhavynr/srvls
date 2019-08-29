package com.amazon.aws.partners.saasfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class ServerlessSaaSWorkshop {

	public static void main(String[] args) {
		SpringApplicationBuilder app = new SpringApplicationBuilder(ServerlessSaaSWorkshop.class)
				.web(WebApplicationType.SERVLET);
		app.build().addListeners(new ApplicationPidFileWriter("monolith.pid"));
		app.run();
//		SpringApplication.run(ServerlessSaaSWorkshop.class, args);
	}

}