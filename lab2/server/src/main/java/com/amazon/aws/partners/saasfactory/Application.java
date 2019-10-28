package com.amazon.aws.partners.saasfactory;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplicationBuilder app = new SpringApplicationBuilder(Application.class)
				.web(WebApplicationType.SERVLET)
				.bannerMode(Banner.Mode.OFF);

		// We'll use the PID file to stop the app via CodeDeploy
		// See scripts/stop_app.sh
		app.build().addListeners(new ApplicationPidFileWriter("application.pid"));

		app.run();
	}

}