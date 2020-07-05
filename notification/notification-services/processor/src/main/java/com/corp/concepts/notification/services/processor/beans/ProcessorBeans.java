package com.corp.concepts.notification.services.processor.beans;

import java.util.function.Consumer;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.corp.concepts.notification.models.Consent;
import com.corp.concepts.notification.services.processor.service.ProcessorService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "Processor Beans Logger")
public class ProcessorBeans {

	private ProcessorService processorService;

	public ProcessorBeans(ProcessorService processorService) {
		this.processorService = processorService;
	}

	@Bean
	public Consumer<KStream<String, Consent>> processMdmData() {
		return mdmStream -> mdmStream.foreach((key, mdmData) -> {
			log.info("mdmData: {}", mdmData);
			processorService.sendDataToAuthority(mdmData);
		});
	}
}
