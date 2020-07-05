package com.corp.concepts.notification.services.mdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;

@SpringBootApplication
@EnableSchemaRegistryClient
public class MdmApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdmApplication.class, args);
	}

}
