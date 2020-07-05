package com.corp.concepts.notification.services.source.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.corp.concepts.notification.services.source.entity.Consent;
import com.corp.concepts.notification.services.source.entity.Consent.Status;
import com.corp.concepts.notification.services.source.repository.ConsentRepository;
import com.corp.concepts.notification.services.source.service.EventGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/consent")
public class ConsentController {

	private ConsentRepository consentRepository;
	private EventGenerator eventGenerator;
	private ObjectMapper objectMapper;

	public ConsentController(ConsentRepository consentRepository, EventGenerator eventGenerator) {
		this.consentRepository = consentRepository;
		this.eventGenerator = eventGenerator;
		this.objectMapper = new ObjectMapper();
	}

	@GetMapping("/all")
	@ResponseBody
	public String getConsentList() {
		try {
			List<Consent> consentList = (List<Consent>) consentRepository.findAll();
			return objectMapper.writeValueAsString(consentList);
		} catch (Exception e) {
			log.error("Error during getting consent data:", e);
			return e.getMessage();
		}
	}
	
	@PostMapping("/reset")
	@ResponseBody
	public String resetConsentData(@RequestParam(value = "count") int count) {
		try {
			List<Consent> consentData = eventGenerator.generateConsentData(count);
			consentRepository.deleteAll();
			consentRepository.saveAll(consentData);
			return objectMapper.writeValueAsString(consentData);
		} catch (Exception e) {
			log.error("Error during resetting consent data:", e);
			return e.getMessage();
		}
	}

	@PostMapping
	@ResponseBody
	public String updateConsent(@RequestParam(value = "recipient") String recipient,
			@RequestParam(value = "approved") boolean approved) {
		try {
			Optional<Consent> consentToUpdate = consentRepository.findById(recipient);

			if (consentToUpdate.isPresent()) {
				Consent consent = consentToUpdate.get();
				if (approved) {
					consent.setStatus(Status.Onay);
				} else {
					consent.setStatus(Status.Red);
				}
				consent.setConsentDate(Calendar.getInstance().getTimeInMillis());
				consentRepository.save(consent);
			}

			List<Consent> consentList = (List<Consent>) consentRepository.findAll();
			return objectMapper.writeValueAsString(consentList);
		} catch (Exception e) {
			log.error("Error during updating consent data:", e);
			return e.getMessage();
		}

	}

}
