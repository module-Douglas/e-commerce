package io.github.douglas.ms_orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrchestratorApplication.class, args);
	}

}
