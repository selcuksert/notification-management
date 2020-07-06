package com.corp.concepts.notification.services.processor.beans;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.corp.concepts.notification.models.Consent;
import com.corp.concepts.notification.services.processor.service.ProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "Processor Beans Logger")
public class ProcessorBeans {

	private ProcessorService processorService;
	private ObjectMapper mapper;

	public ProcessorBeans(ProcessorService processorService) {
		this.processorService = processorService;
		this.mapper = new ObjectMapper();
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Bean
	public Consumer<KStream<String, Consent>> sinkMdmData() {
		return mdmStream -> mdmStream.foreach((key, mdmData) -> {
			log.info("mdmData: {}", mdmData);
			// Commits are handled by Kafka Streams internally and fully automatically.
			// In case of any error stream processing stops and the
			// consumer resumes processing from the latest committed offset after
			// restarting the application.
			processorService.sendDataToAuthority(mdmData);
		});
	}

	@Bean
	public Function<KStream<String, String>, KStream<String, Consent>> processConsentData() {
		return consentStream -> consentStream.map((key, value) -> {
			Consent consent = new Consent();
			try {
				consent = mapper.readValue(value, Consent.class);
				log.info("\n| input: {}\n| output: {}", value, consent.toString());
			} catch (JsonProcessingException e) {
				log.error("Error: ", e);
			}
			return new KeyValue<>(key, consent);
		});
	}
	
}
