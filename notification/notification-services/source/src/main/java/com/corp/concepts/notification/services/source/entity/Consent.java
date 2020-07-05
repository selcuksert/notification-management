package com.corp.concepts.notification.services.source.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "consent")
public class Consent {

	@Id
	private String recipient;

	private int iysCode;
	private int brandCode;
	private long consentDate;
	@Enumerated(EnumType.STRING)
	private ConsentType type;
	@Enumerated(EnumType.STRING)
	private RecipientType recipientType;
	@Enumerated(EnumType.STRING)
	private Source source;
	@Enumerated(EnumType.STRING)
	private Status status;

	public Consent(int iysCode, int brandCode, long consentDate, String recipient, ConsentType type,
			RecipientType recipientType, Source source, Status status) {
		this.iysCode = iysCode;
		this.brandCode = brandCode;
		this.consentDate = consentDate;
		this.recipient = recipient;
		this.type = type;
		this.recipientType = recipientType;
		this.source = source;
		this.status = status;
	}

	public enum ConsentType {
		Mesaj, Arama, Eposta;
	}

	public enum RecipientType {
		Bireysel, Kurumsal;
	}

	public enum Source {
		Fiziksel, Elektronik;
	}

	public enum Status {
		Onay, Red;
	}
}
