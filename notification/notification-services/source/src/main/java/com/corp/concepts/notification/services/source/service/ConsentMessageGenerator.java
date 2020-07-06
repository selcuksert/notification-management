package com.corp.concepts.notification.services.source.service;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.corp.concepts.notification.services.source.entity.Consent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Service
@Slf4j(topic = "Consent Message Generator Logger")
public class ConsentMessageGenerator {

	private final EmitterProcessor<Message<?>> processor = EmitterProcessor.create();
	private ObjectMapper mapper;

	public ConsentMessageGenerator() {
		this.mapper = new ObjectMapper();
	}

	public void sendConsentMessage(Consent consent) throws JsonProcessingException {

		Message<String> message = MessageBuilder.withPayload(mapper.writeValueAsString(consent))
				.setHeader(KafkaHeaders.MESSAGE_KEY, consent.getRecipient()).build();

		processor.onNext(message);

		log.info("Message sent: {}", message.toString());
	}

	@Bean
	public Supplier<Flux<?>> output() {
		return () -> processor;
	}
}
