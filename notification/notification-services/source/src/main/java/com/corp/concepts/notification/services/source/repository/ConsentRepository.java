package com.corp.concepts.notification.services.source.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.corp.concepts.notification.services.source.entity.Consent;

@Repository
public interface ConsentRepository extends CrudRepository<Consent, String> {
}
