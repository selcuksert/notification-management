package com.corp.concepts.notification.services.mdm.service;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.corp.concepts.notification.models.Consent;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@Service
@Slf4j(topic = "MDM Message Generator Logger")
public class MdmMessageGenerator {

	private final EmitterProcessor<Message<?>> processor = EmitterProcessor.create();

	public void sendConsentMessage(Consent consent) {
		Message<Consent> message = MessageBuilder.withPayload(consent)
				.setHeader(KafkaHeaders.MESSAGE_KEY, consent.getRecipient()).build();

		processor.onNext(message);

		log.info("Message sent: {}", message.toString());
	}

	@Bean
	public Supplier<Flux<?>> output() {
		return () -> processor;
	}

}
