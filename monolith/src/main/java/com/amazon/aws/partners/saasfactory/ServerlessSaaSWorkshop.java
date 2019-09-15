package com.amazon.aws.partners.saasfactory;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

import java.io.File;

@SpringBootApplication
public class ServerlessSaaSWorkshop {

	public static void main(String[] args) {
		SpringApplicationBuilder app = new SpringApplicationBuilder(ServerlessSaaSWorkshop.class)
				.web(WebApplicationType.SERVLET);
		app.build().addListeners(new ApplicationPidFileWriter(new File("/home/ec2-user/monolith.pid")));
		app.run();
	}

}