package com.corp.concepts.notification.services.processor.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.corp.concepts.notification.models.Consent;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProcessorService {

	public String sendDataToAuthority(Consent consent) {
		try {
			WebClient client = WebClient.create("http://localhost:8080/consent");

			String response = client.post().contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(consent.toString()), String.class).accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(String.class).block();

			return response;
		} catch (Exception e) {
			log.error("Error sending data to authority:", e);
			return e.getMessage();
		}
	}
}
