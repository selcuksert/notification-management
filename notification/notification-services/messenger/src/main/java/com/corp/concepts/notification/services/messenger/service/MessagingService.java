package com.corp.concepts.notification.services.messenger.service;

import org.springframework.stereotype.Service;

import com.corp.concepts.notification.models.Consent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "Messaging Service Logger")
public class MessagingService {

	public void processConsentData(Consent consent) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(consent.getType().name()).append(" to ").append(consent.getRecipient());

		switch (consent.getStatus()) {
		case Onay:
			stringBuilder.append(" -> ALLOWED");
			break;
		case Red:
			stringBuilder.append(" -> NOT ALLOWED");
			break;
		default:
			break;
		}

		log.info(stringBuilder.toString());
	}

}
