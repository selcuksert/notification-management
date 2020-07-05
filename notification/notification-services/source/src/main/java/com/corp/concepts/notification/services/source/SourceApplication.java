package com.corp.concepts.notification.services.source;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.corp.concepts.notification.services.source.entity.Consent;
import com.corp.concepts.notification.services.source.repository.ConsentRepository;
import com.corp.concepts.notification.services.source.service.EventGenerator;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SourceApplication.class, args);
	}

	@Bean
	public CommandLineRunner addDemoData(ConsentRepository repository, EventGenerator generator) {
		return (args) -> {
			log.info("Generating demo data");
			List<Consent> consentData = generator.generateConsentData(10);
			repository.deleteAll();
			repository.saveAll(consentData);
		};
	}

}
