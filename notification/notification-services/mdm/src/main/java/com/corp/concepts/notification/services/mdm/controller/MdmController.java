package com.corp.concepts.notification.services.mdm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.corp.concepts.notification.models.Consent;
import com.corp.concepts.notification.services.mdm.service.MdmDataGenerator;
import com.corp.concepts.notification.services.mdm.source.MdmMessageGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/mdm")
public class MdmController {

	private MdmDataGenerator mdmDataGenerator;
	private MdmMessageGenerator mdmMessageGenerator;
	private ObjectMapper objectMapper;

	public MdmController(MdmDataGenerator mdmDataGenerator, MdmMessageGenerator mdmMessageGenerator) {
		this.mdmDataGenerator = mdmDataGenerator;
		this.mdmMessageGenerator = mdmMessageGenerator;
		this.objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@PostMapping("/send")
	@ResponseBody
	public String generateAndSendConsentData(@RequestParam(value = "count") int count) {
		try {
			List<Consent> consentData = mdmDataGenerator.generateConsentData(count);
			consentData.stream().forEach(consent -> mdmMessageGenerator.sendConsentMessage(consent));
			return "Data sent";
		} catch (Exception e) {
			log.error("Error during sending consent data:", e);
			return e.getMessage();
		}
	}
}
