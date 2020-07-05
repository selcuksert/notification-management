package com.corp.concepts.notification.services.source.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "consent")
public class Consent implements Comparable<Consent> {

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
	private Long timestamp;

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

	@Override
	public int compareTo(Consent consent) {
		if (consent.getTimestamp() < this.getTimestamp()) {
			return 1;
		} else if (consent.getTimestamp() > this.getTimestamp()) {
			return -1;
		}
		return 0;
	}

}
