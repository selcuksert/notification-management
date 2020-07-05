package com.corp.concepts.notification.services.mdm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.corp.concepts.notification.models.Consent;
import com.corp.concepts.notification.models.ConsentType;
import com.corp.concepts.notification.models.RecipientType;
import com.corp.concepts.notification.models.Source;
import com.corp.concepts.notification.models.Status;
import com.github.javafaker.Faker;

@Service
public class MdmDataGenerator {
	private final Faker faker = Faker.instance();

	public List<Consent> generateConsentData(int count) {
		List<Consent> consentData = new ArrayList<>();

		for (int i = 0; i < count; i++) {

			RecipientType recipientType = RecipientType.values()[faker.number().numberBetween(0,
					RecipientType.values().length)];

			ConsentType type = ConsentType.values()[faker.number().numberBetween(0, ConsentType.values().length)];

			Source source = Source.values()[faker.number().numberBetween(0, Source.values().length)];

			Status status = Status.values()[faker.number().numberBetween(0, Status.values().length)];

			int brandCode = faker.number().numberBetween(1, 11);

			int iysCode = faker.number().numberBetween(1, 4);

			long consentDate = faker.date().past(365, TimeUnit.DAYS).getTime();

			String recipient;

			switch (type) {
			case Arama:
				recipient = faker.phoneNumber().cellPhone();
				break;
			case Eposta:
				recipient = faker.internet().emailAddress();
				break;
			case Mesaj:
				recipient = faker.phoneNumber().cellPhone();
				break;
			default:
				recipient = type.name();
				break;
			}

			Consent consent = new Consent(iysCode, brandCode, consentDate, type, recipientType, recipient, source,
					status);

			consentData.add(consent);
		}

		return consentData;
	}
}
