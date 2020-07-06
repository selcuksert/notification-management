package com.corp.concepts.notification.services.messenger.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.corp.concepts.notification.models.Consent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/messages")
public class MessengerController {
	private InteractiveQueryService interactiveQueryService;

	@Value("${spring.cloud.stream.kafka.streams.binder.configuration.consent-materialized-as}")
	private String consentTable;

	public MessengerController(InteractiveQueryService interactiveQueryService) {
		this.interactiveQueryService = interactiveQueryService;
	}

	@GetMapping("/consent")
	@ResponseBody
	public String getConsentById(@RequestParam(value = "recipient") String recipient) {
		try {
			ReadOnlyKeyValueStore<String, Consent> keyValueStore = interactiveQueryService
					.getQueryableStore(consentTable, QueryableStoreTypes.<String, Consent>keyValueStore());

			Consent consent = keyValueStore.get(recipient);

			if (consent != null) {
				return consent.toString();
			}

		} catch (Exception e) {
			log.error("Error when getting data:", e);
			return "Error occured. Please try again later.";
		}

		return "Not found";
	}

	@GetMapping
	@ResponseBody
	public List<Consent> getConsentData() {
		List<Consent> consentList = new ArrayList<>();
		try {
			ReadOnlyKeyValueStore<String, Consent> keyValueStore = interactiveQueryService
					.getQueryableStore(consentTable, QueryableStoreTypes.<String, Consent>keyValueStore());

			KeyValueIterator<String, Consent> consentData = keyValueStore.all();

			if (consentData != null) {
				if (consentData != null) {
					while (consentData.hasNext()) {
						KeyValue<String, Consent> consent = consentData.next();
						consentList.add(consent.value);
					}
					consentList.sort(Comparator.comparing(Consent::getRecipient));
				}
			}

		} catch (Exception e) {
			log.error("Error when getting consent list:", e);
		}

		return consentList;
	}

}
