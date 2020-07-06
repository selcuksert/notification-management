package com.corp.concepts.notification.services.messenger.beans;

import java.util.function.Consumer;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.corp.concepts.notification.models.Consent;
import com.corp.concepts.notification.services.messenger.service.MessagingService;

@Component
public class MessengerBeans {

	@Value("${spring.cloud.stream.kafka.streams.binder.configuration.consent-materialized-as}")
	private String consentTable;

	private MessagingService messagingService;

	public MessengerBeans(MessagingService messagingService) {
		this.messagingService = messagingService;
	}

	@Bean
	public Consumer<KStream<String, Consent>> sinkConsent() {
		return input -> input.peek((key, consent) -> messagingService.processConsentData(consent))
				.toTable(Materialized.as(consentTable)).toStream();
	}

}
