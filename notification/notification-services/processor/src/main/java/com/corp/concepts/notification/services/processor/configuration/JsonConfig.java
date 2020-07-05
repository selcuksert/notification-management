package com.corp.concepts.notification.services.processor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.MapperFeature;

@Configuration
public class JsonConfig {

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
	    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	    builder.featuresToEnable(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS);
	    return builder;
	}

}
